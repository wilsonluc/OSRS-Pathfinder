package com.pathfinder.pathfinding.node;

import com.pathfinder.enums.Type;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

/**
 * Defines a node within a path
 */
@Getter
public class NodeEdge {
    private final WorldPoint sourceWP;
    private final WorldPoint destinationWP;
    private final Type type;

    /**
     * Constructs a CustomWorldPoint with specific source, destination, and type of transport
     *
     * @param sourceWP      Starting WorldPoint
     * @param destinationWP Destination WorldPoint
     * @param type          Travel type (e.g., walk, transport)
     */
    public NodeEdge(WorldPoint sourceWP, WorldPoint destinationWP, Type type) {
        this.sourceWP = sourceWP;
        this.destinationWP = destinationWP;
        this.type = type;
    }

    /**
     * Constructor for CustomWorldPoint
     *
     * @param sourceWP      Current WorldPoint of node
     * @param destinationWP WorldPoint node is connected to
     */
    public NodeEdge(WorldPoint sourceWP, WorldPoint destinationWP) {
        this(sourceWP, destinationWP, Type.WALK);
    }
}
