package com.pathfinder.pathfinding.node;

import com.pathfinder.enums.TransportType;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

/**
 * Defines a node within a path
 */
@Getter
public class NodeEdge {
    private final WorldPoint sourceWP;
    private final WorldPoint destinationWP;
    private final TransportType transportType;

    /**
     * Constructs a CustomWorldPoint with specific source, destination, and type of transport
     *
     * @param sourceWP      Starting WorldPoint
     * @param destinationWP Destination WorldPoint
     * @param transportType          Travel type (e.g., walk, transport)
     */
    public NodeEdge(WorldPoint sourceWP, WorldPoint destinationWP, TransportType transportType) {
        this.sourceWP = sourceWP;
        this.destinationWP = destinationWP;
        this.transportType = transportType;
    }

    /**
     * Constructor for CustomWorldPoint
     *
     * @param sourceWP      Current WorldPoint of node
     * @param destinationWP WorldPoint node is connected to
     */
    public NodeEdge(WorldPoint sourceWP, WorldPoint destinationWP) {
        this(sourceWP, destinationWP, TransportType.WALK);
    }
}
