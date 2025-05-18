package com.pathfinder.pathfinding;

import com.pathfinder.pathfinding.node.Node;
import com.pathfinder.pathfinding.node.TransportNode;
import com.pathfinder.pathfinding.transports.Transport;
import com.pathfinder.pathfinding.transports.TransportHandler;
import com.pathfinder.util.WorldPointUtil;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.*;

/**
 * Implements a pathfinding algorithm capable of traversing a grid-based map using collision logic.
 * <p>
 * The pathfinding process is executed via the {@link #run()} method, which performs a
 * best-first search using a combination of frontier and priority queues.
 */
public class Pathfinder implements Runnable {
    /**
     * Starting point of the pathfinding search.
     */
    @Getter
    private final WorldPoint start;

    /**
     * List of target destinationWP points.
     */
    @Getter
    private final List<WorldPoint> targets;

    /**
     * Packed 32-bit integer representations of the target destinationWP points for quick lookup.
     */
    private final Set<Integer> targetsPacked;

    /**
     * Configuration used to control pathfinding behavior.
     */
    private final PathfinderConfig config;

    /**
     * The map providing collision data.
     */
    private final CollisionMap map;

    /**
     * Whether the target is located in wilderness.
     */
    private final boolean targetInWilderness;

    /**
     * Queue of walkable nodes prioritized by insertion order (frontier).
     */
    private final Deque<Node> boundary = new ArrayDeque<>(4096);

    /**
     * Priority queue of transport nodes, ordered by their cumulative cost.
     */
    private final Queue<Node> pending = new PriorityQueue<>(256);

    /**
     * Tracks visited nodes to prevent cycles and redundant work.
     */
    private final VisitedTiles visited;

    /**
     * The computed path as a list of {@link WorldPoint} objects.
     */
    private List<WorldPoint> path = new ArrayList<>();

    /**
     * Indicates whether the path needs to be regenerated.
     */
    private boolean pathNeedsUpdate = false;

    /**
     * The best node found so far during the search, either a target or closest approximation.
     */
    private Node bestLastNode;

    /**
     * Constructs a new {@code Pathfinder} with the given configuration, start, and targets.
     *
     * @param config  Pathfinding configuration
     * @param start   The starting world point
     * @param targets One or more target world points
     */
    public Pathfinder(PathfinderConfig config, WorldPoint start, List<WorldPoint> targets) {
        this.config = config;
        this.map = config.getMap();
        this.start = start;
        this.targets = targets;
        visited = new VisitedTiles(map);
        Set<Integer> targetsPacked = new HashSet<>();
        for (WorldPoint target : targets) {
            targetsPacked.add(WorldPointUtil.packWorldPoint(target));
        }
        this.targetsPacked = targetsPacked;
        targetInWilderness = PathfinderConfig.isInWilderness(targets.get(0));
    }

    /**
     * Returns the computed path to the best target (or closest node if incomplete).
     *
     * @return List of {@link WorldPoint} representing the current best path
     */
    public List<WorldPoint> getPath() {
        Node lastNode = bestLastNode; // For thread safety, read bestLastNode once
        if (lastNode == null) {
            return path;
        }

        if (pathNeedsUpdate) {
            path = lastNode.getPath();
            pathNeedsUpdate = false;
        }

        return path;
    }

    /**
     * Expands the neighbors of a given node and queues them for further evaluation.
     *
     * @param node The current node
     * @return A node if it directly reaches a target, otherwise null
     */
    private Node addNeighbors(Node node) {
        List<Node> nodes = map.getNeighbors(node, visited, config);
        for (Node neighbor : nodes) {
            if (config.isDisableWilderness() && config.disableWilderness(targetInWilderness)) {
                continue;
            }

            if (targetsPacked.contains(neighbor.packedWP)) {
                return neighbor;
            }

            if (config.isAvoidWilderness() && config.avoidWilderness(node.packedWP, neighbor.packedWP, targetInWilderness)) {
                continue;
            }

            visited.set(neighbor.packedWP);
            if (neighbor instanceof TransportNode) {
                pending.add(neighbor);
            } else {
                boundary.addLast(neighbor);
            }
        }

        return null;
    }

    /**
     * Executes the pathfinding search.
     * <p>
     * The search terminates early if:
     * - A target is reached.
     * - The best candidate (by heuristic) is found before a timeout.
     */
    @Override
    public void run() {
        boundary.addFirst(new Node(start, null));

        int bestDistance = Integer.MAX_VALUE;
        long bestHeuristic = Integer.MAX_VALUE;
        long cutoffDurationMillis = 30000;
        long cutoffTimeMillis = System.currentTimeMillis() + cutoffDurationMillis;

        while ((!boundary.isEmpty() || !pending.isEmpty())) {
            Node node = boundary.peekFirst();
            Node p = pending.peek();

            if (p != null && (node == null || p.cost < node.cost)) {
                boundary.addFirst(p);
                pending.poll();
            }

            node = boundary.removeFirst();

            if (targetsPacked.contains(node.packedWP)) {
                bestLastNode = node;
                pathNeedsUpdate = true;
                break;
            }

            int distance = Integer.MAX_VALUE;
            long heuristic = Integer.MAX_VALUE;
            for (WorldPoint target : targets) {
                int d = WorldPointUtil.distanceBetween(node.packedWP, WorldPointUtil.packWorldPoint(target));
                if (d < distance) {
                    distance = d;
                }

                long h = WorldPointUtil.distanceBetween(node.packedWP, WorldPointUtil.packWorldPoint(target), 2);
                if (h < heuristic) {
                    heuristic = h;
                }
            }

            if (heuristic < bestHeuristic || (heuristic <= bestHeuristic && distance < bestDistance)) {
                bestLastNode = node;
                pathNeedsUpdate = true;
                bestDistance = distance;
                bestHeuristic = heuristic;
                cutoffTimeMillis = System.currentTimeMillis() + cutoffDurationMillis;
            }

            if (System.currentTimeMillis() > cutoffTimeMillis) {
                break;
            }

            // Check if target was found without processing the queue to find it
            if ((p = addNeighbors(node)) != null) {
                bestLastNode = p;
                pathNeedsUpdate = true;
                break;
            }
        }

        boundary.clear();
        visited.clear();
        pending.clear();
    }

    /**
     * Generates a path from the start to the end {@link WorldPoint} using the provided player properties.
     *
     * @param startWP          The starting {@link WorldPoint}
     * @param endWP            The target {@link WorldPoint}
     * @param playerProperties The player's properties for pathfinding such as quests and skills
     * @return A list of {@link WorldPoint} representing the path
     */
    public static List<WorldPoint> generatePath(WorldPoint startWP, WorldPoint endWP, PlayerProperties playerProperties) {
        TransportHandler.setPlayerProperties(playerProperties);
        SplitFlagMap map = SplitFlagMap.fromResources();
        Map<WorldPoint, List<Transport>> transports = TransportHandler.loadAllFromResources();
        PathfinderConfig pathfinderConfig = new PathfinderConfig(map, transports);

        System.out.println("Start WP: " + startWP);
        System.out.println("End WP: " + endWP);
        System.out.println("FAIRY RINGS: " + playerProperties.isFairyRingsUnlocked());
        System.out.println("SPIRIT TREES: " + playerProperties.isSpiritTreesUnlocked());
        Pathfinder pathfinder = new Pathfinder(pathfinderConfig, startWP, List.of(endWP));
        pathfinderConfig.refreshTransportData();
        pathfinder.run();
        return pathfinder.getPath();
    }
}
