package com.pathfinder;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

/**
 * Defines a node within a path
 */
@Getter
public class CustomWorldPoint {
    private final WorldPoint source;
    private final WorldPoint destination;

    /**
     * Constructor for CustomWorldPoint
     *
     * @param source      Current WorldPoint of node
     * @param destination WorldPoint node is connected to
     */
    public CustomWorldPoint(WorldPoint source, WorldPoint destination) {
        this.source = source;
        this.destination = destination;
    }
}
