package com.pathfinder.pathfinding;


import com.pathfinder.pathfinding.node.Node;
import com.pathfinder.pathfinding.node.TransportNode;
import com.pathfinder.util.WorldPointUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a collision map used for pathfinding, allowing checks on tile accessibility
 * and determining valid movement directions from a given point.
 */
public class CollisionMap {
    /**
     * Cached enum values for traversal directions to avoid repeated allocation
     */
    private static final OrdinalDirection[] ORDINAL_VALUES = OrdinalDirection.values();

    /**
     * Backing data sourceWP containing collision flags
     */
    private final SplitFlagMap collisionData;

    /**
     * Reusable list of neighboring nodes for performance
     */
    private final List<Node> neighbors = new ArrayList<>(16);

    /**
     * Reusable direction mask to avoid rechecking every frame
     */
    private final boolean[] traversable = new boolean[8];

    /**
     * Constructs a CollisionMap using the provided collision flag data.
     *
     * @param collisionData The sourceWP of regional collision flag data.
     */
    public CollisionMap(SplitFlagMap collisionData) {
        this.collisionData = collisionData;
    }

    /**
     * @return An array indicating how many planes are stored for each region.
     */
    public byte[] getPlanes() {
        return collisionData.getRegionMapPlaneCounts();
    }

    /**
     * Checks if the specified flag is set at the given world coordinate.
     *
     * @param x    The world X coordinate.
     * @param y    The world Y coordinate.
     * @param z    The plane.
     * @param flag The collision flag to check.
     * @return True if the flag is set, false otherwise.
     */
    public boolean get(int x, int y, int z, int flag) {
        return collisionData.get(x, y, z, flag);
    }

    // Directional access helpers using Runelite collision flag conventions

    public boolean n(int x, int y, int z) {
        return get(x, y, z, 0);
    }

    public boolean s(int x, int y, int z) {
        return get(x, y - 1, z, 0);
    }

    public boolean e(int x, int y, int z) {
        return get(x, y, z, 1);
    }

    public boolean w(int x, int y, int z) {
        return get(x - 1, y, z, 1);
    }

    /**
     * Checks whether diagonal movement in the given direction is valid.
     *
     * @param x   Starting X coordinate.
     * @param y   Starting Y coordinate.
     * @param z   Plane.
     * @param dir Diagonal direction to check.
     * @return True if movement in the given diagonal direction is possible.
     */
    private boolean diagonal(int x, int y, int z, OrdinalDirection dir) {
        int dx = dir.x, dy = dir.y;
        return get(x, y, z, 0) &&
                get(x + dx, y, z, 0) &&
                get(x, y + dy, z, 1) &&
                get(x + dx, y + dy, z, 1);
    }

    /**
     * Determines if the tile is fully blocked in all cardinal directions.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Plane.
     * @return True if movement is not possible in any direction.
     */
    public boolean isBlocked(int x, int y, int z) {
        return !(n(x, y, z) || s(x, y, z) || e(x, y, z) || w(x, y, z));
    }

    /**
     * Computes a new packed coordinate by applying a direction offset.
     *
     * @param packed The original packed coordinate.
     * @param d      The direction offset.
     * @return A new packed coordinate offset by the given direction.
     */
    private static int packedPointFromOrdinal(int packed, OrdinalDirection d) {
        int x = WorldPointUtil.unpackWorldX(packed);
        int y = WorldPointUtil.unpackWorldY(packed);
        int z = WorldPointUtil.unpackWorldPlane(packed);
        return WorldPointUtil.packWorldPoint(x + d.x, y + d.y, z);
    }

    /**
     * Returns a list of walkable neighbor nodes from the current position,
     * filtered by visited state and collision checks.
     *
     * @param node    The current node.
     * @param visited Tracker of visited tiles.
     * @param config  Pathfinding configuration.
     * @return A list of valid neighboring nodes.
     */
    public List<Node> getNeighbors(Node node, VisitedTiles visited, PathfinderConfig config) {
        final int x = WorldPointUtil.unpackWorldX(node.packedWP);
        final int y = WorldPointUtil.unpackWorldY(node.packedWP);
        final int z = WorldPointUtil.unpackWorldPlane(node.packedWP);

        neighbors.clear();

        List<Transport> transports = config.getTransportsPacked().getOrDefault(node.packedWP, List.of());

        for (Transport transport : transports) {
            if (visited.get(transport.getDestination())) {
                continue;
            }
            neighbors.add(new TransportNode(transport.getDestination(), node, transport.additionalCost));
        }

        if (isBlocked(x, y, z)) {
            // Region is fully blocked; allow movement to adjacent non-blocked tiles.
            for (int i = 0; i < ORDINAL_VALUES.length; i++) {
                OrdinalDirection d = ORDINAL_VALUES[i];
                int nx = x + d.x, ny = y + d.y;

                traversable[i] = !isBlocked(nx, ny, z);
            }
        } else {
            traversable[0] = w(x, y, z);
            traversable[1] = e(x, y, z);
            traversable[2] = s(x, y, z);
            traversable[3] = n(x, y, z);
            traversable[4] = diagonal(x, y, z, OrdinalDirection.SOUTH_WEST);
            traversable[5] = diagonal(x, y, z, OrdinalDirection.SOUTH_EAST);
            traversable[6] = diagonal(x, y, z, OrdinalDirection.NORTH_WEST);
            traversable[7] = diagonal(x, y, z, OrdinalDirection.NORTH_EAST);
        }

        for (int i = 0; i < traversable.length; i++) {
            OrdinalDirection d = ORDINAL_VALUES[i];
            int neighborPacked = packedPointFromOrdinal(node.packedWP, d);
            if (visited.get(neighborPacked)) continue;

            if (traversable[i]) {
                neighbors.add(new Node(neighborPacked, node));
            } else if (Math.abs(d.x + d.y) == 1 && isBlocked(x + d.x, y + d.y, z)) {
                List<Transport> neighborTransports = config.getTransportsPacked().getOrDefault(neighborPacked, List.of());
                for (Transport transport : neighborTransports) {
                    if (visited.get(transport.getOrigin())) {
                        continue;
                    }
                    neighbors.add(new Node(transport.getOrigin(), node));
                }
            }
        }

        return neighbors;
    }
}