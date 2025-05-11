package com.pathfinder.pathfinding.node;

import com.pathfinder.util.WorldPointUtil;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Node} class represents a node within a path. Each node maintains a reference to its {@link WorldPoint},
 * the previous node in the path, and the cumulative cost to reach this node.
 *
 * <p>The WorldPoint of the node is converted to a 32-bit integer using bit manipulation to improve space efficiency
 * and performance. The path from start to current node can be reconstructed using the {@link #getPath()} method.</p>
 */
public class Node {
    /**
     * 32-bit integer representation of the {@link WorldPoint} associated with the node.
     */
    public final int packedWorldPoint;

    /**
     * Previous node in the path. {@code null} if this node is the start node.
     */
    public final Node previous;

    /**
     * The cumulative cost to reach this node from the start node.
     */
    public final int cost;

    /**
     * Constructs a new {@code Node} from a given {@link WorldPoint}, a reference to the previous node,
     * and an additional cost to reach this node.
     *
     * @param position       The position of the node as a {@link WorldPoint}
     * @param previous       The previous node in the path; {@code null} if this is the starting node
     * @param additionalCost The additional cost needed to reach this node
     */
    public Node(WorldPoint position, Node previous, int additionalCost) {
        this.packedWorldPoint = WorldPointUtil.packWorldPoint(position);
        this.previous = previous;
        this.cost = cost(previous, additionalCost);
    }


    /**
     * Constructs a new {@code Node} from a given {@link WorldPoint} and a reference to the previous node.
     *
     * @param position The position of the node as a {@link WorldPoint}
     * @param previous The previous node in the path; {@code null} if this is the starting node
     */
    public Node(WorldPoint position, Node previous) {
        this(position, previous, 0);
    }

    /**
     * Constructs a new {@code Node} from a 32-bit integer representation of a {@link WorldPoint}, a reference to
     * the previous node, and an additional cost to reach this node.
     *
     * @param packedWorldPoint 32-bit integer representation of a {@link WorldPoint}
     * @param previous         The previous node in the path; {@code null} if this is the starting node
     * @param additionalCost   The additional cost needed to reach this node
     */
    public Node(int packedWorldPoint, Node previous, int additionalCost) {
        this.packedWorldPoint = packedWorldPoint;
        this.previous = previous;
        this.cost = cost(previous, additionalCost);
    }

    /**
     * Constructs a new {@code Node} from a 32-bit integer representation of a {@link WorldPoint} and an additional
     * cost to reach this node.
     *
     * @param packedWorldPoint 32-bit integer representation of a {@link WorldPoint}
     * @param previous         The previous node in the path; {@code null} if this is the starting node
     */
    public Node(int packedWorldPoint, Node previous) {
        this(packedWorldPoint, previous, 0);
    }

    /**
     * Reconstructs and returns the full path from the start node to this node as a list of {@link WorldPoint} objects.
     *
     * @return A list of {@link WorldPoint} objects representing the path from start to current node
     */
    public List<WorldPoint> getPath() {
        List<WorldPoint> path = new LinkedList<>();
        Node node = this;

        while (node != null) {
            WorldPoint position = WorldPointUtil.unpackWorldPoint(node.packedWorldPoint);
            path.add(0, position);
            node = node.previous;
        }

        return new ArrayList<>(path);
    }

    /**
     * Calculates the cumulative cost to reach this node from the start node, currently using a constant
     * cost of 1 per step.
     *
     * @param previous       The previous node in the path
     * @param additionalCost The additional cost from the previous node to this node
     * @return The cumulative cost from the start node
     */
    private int cost(Node previous, int additionalCost) {
        // TODO: Make use of additionalCost heuristic
        return previous == null ? 0 : 1;
    }
}
