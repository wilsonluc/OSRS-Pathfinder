package com.pathfinder.pathfinding.node;

import net.runelite.api.coords.WorldPoint;

/**
 * A {@code TransportNode} represents a special type of {@link Node} that is used for transports or teleports,
 * typically involving an additional cost beyond just 1 per step.
 *
 * <p>This class extends {@link Node} and implements {@link Comparable} to allow sorting or prioritisation
 * based on the cumulative pathfinding cost.</p>
 */
public class TransportNode extends Node implements Comparable<TransportNode> {
    /**
     * Constructs a {@code TransportNode} with a {@link WorldPoint}, a reference to the previous {@link Node},
     * and an additional cost to travel to the node.
     *
     * @param worldPoint     The {@link WorldPoint} representing this node's position
     * @param previous       The previous node in the path; may be {@code null} if this is the starting node
     * @param additionalCost The additional cost associated with this node
     */
    public TransportNode(WorldPoint worldPoint, Node previous, int additionalCost) {
        super(worldPoint, previous, additionalCost);
    }

    /**
     * Compares this {@code TransportNode} to another based on cumulative pathfinding cost.
     *
     * @param other The other {@code TransportNode} to compare with
     * @return -1 if node is less than other, 0 if they are equal, otherwise 1
     */
    @Override
    public int compareTo(TransportNode other) {
        return Integer.compare(cost, other.cost);
    }
}
