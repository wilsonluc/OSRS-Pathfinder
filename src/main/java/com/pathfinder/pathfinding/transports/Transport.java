package com.pathfinder.pathfinding.transports;

import com.pathfinder.enums.TransportType;
import net.runelite.api.coords.WorldPoint;

import javax.annotation.Nullable;

/**
 * This class represents a travel point between two WorldPoints.
 *
 * @param transportType The type of this transport.
 * @param destination   The ending point of this transport.
 * @param origin        The starting point of this transport.
 * @param objectID      Object ID associated with this transport.
 * @param menuOption    Menu option to use this transport.
 */
public record Transport(TransportType transportType,
                        WorldPoint destination,
                        WorldPoint origin,
                        @Nullable Integer objectID,
                        @Nullable String menuOption) {
}
