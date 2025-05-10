package com.pathfinder.pathfinding;

import com.pathfinder.util.WorldPointUtil;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@code Node} class represents a node within a path. Each node maintains a reference to its world point, the
 * previous node in the path, and the cumulative cost to reach this node.
 *
 * <p>The WorldPoint of the node is converted to a 32-bit integer using bit manipulation to improve space efficiency
 * and performance. The path from start to current node can be reconstructed using the {@link #getPath()} method.</p>
 */
public class Node {
    /**
     * 32-bit integer representation of WorldPoint of node
     */
    public final int packedWorldPoint;

    /**
     * Previous node in the path. {@code null} if this node is the start node.
     */
    public final Node previousNode;

    /**
     * The cumulative cost to reach this node from the start node.
     */
    public final int cost;

    /**
     * Constructs a new {@code Node} from a {@link WorldPoint} worldPoint, a reference to the previous node,
     * and additional cost needed to go to the node.
     *
     * @param worldPoint     World point of current node
     * @param previousNode   The previous node
     * @param additionalCost Additional cost to reach current node
     */
    public Node(WorldPoint worldPoint, Node previousNode, int additionalCost) {
        this.packedWorldPoint = WorldPointUtil.packWorldPoint(worldPoint);
        this.previousNode = previousNode;
        this.cost = cost(previousNode, additionalCost);
    }

    /**
     * Constructs a new {@code Node} from a {@link WorldPoint} worldPoint and a reference to the previous node.
     * Operates under the assumption that the node is within walking distance, hence additional cost is 0.
     *
     * @param worldPoint   World point of current node
     * @param previousNode The previous node
     */
    public Node(WorldPoint worldPoint, Node previousNode) {
        this(worldPoint, previousNode, 0);
    }

    /**
     * Constructs a new {@code Node} from a {@link WorldPoint} and a reference to the previous node.
     * The node may not be within walking distance / may take more time than normal to travel to, in which
     * the additional cost will be added to the original cost.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @param previousNode     The previous node
     * @param additionalCost   Additional cost to reach current node
     */
    public Node(int packedWorldPoint, Node previousNode, int additionalCost) {
        this.packedWorldPoint = packedWorldPoint;
        this.previousNode = previousNode;
        this.cost = cost(previousNode, additionalCost);
    }

    /**
     * Constructs a new {@code Node} using the 32-bit integer representation of a {@link WorldPoint} and a
     * reference to the previous node.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @param previousNode     The previous node
     */
    public Node(int packedWorldPoint, Node previousNode) {
        this(packedWorldPoint, previousNode, 0);
    }

    /**
     * Reconstructs and returns the path from the start node to this node.
     *
     * @return a list of {@link WorldPoint} objects representing the path
     */
    public List<WorldPoint> getPath() {
        List<WorldPoint> path = new LinkedList<>();
        Node node = this;

        while (node != null) {
            WorldPoint worldPoint = WorldPointUtil.unpackWorldPoint(node.packedWorldPoint);
            path.add(0, worldPoint);
            node = node.previousNode;
        }

        return new ArrayList<>(path);
    }

    /**
     * Computes cost to reach this node
     *
     * @param previousNode   The previous node
     * @param additionalCost Additional cost to reach current node
     * @return Cumulative cost to reach current node from previous node
     */
    private int cost(Node previousNode, int additionalCost) {
        // TODO: Make use of wait heuristic
        return previousNode == null ? 0 : 1;
    }
}
