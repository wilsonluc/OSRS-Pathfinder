package com.pathfinder.pathfinding;

import net.runelite.api.coords.WorldPoint;

/**
 * Represents a pair of {@link WorldPoint}s used to uniquely identify a directional transport or segment of path.
 * <p>
 * This record is primarily used as a key in data structures where a unique source-to-destination
 * relationship needs to be tracked.
 *
 * @param sourceWP      The origin {@link WorldPoint} of the pair
 * @param destinationWP The destination {@link WorldPoint} of the pair
 */
public record WorldPointPair(WorldPoint sourceWP, WorldPoint destinationWP) {
    /**
     * Compares this {@code WorldPointPair} to another object for equality.
     *
     * @param obj The object to compare to
     * @return {@code true} if the object is a {@code WorldPointPair} with the same source and destination, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldPointPair other)) {
            return false;
        }
        return sourceWP.equals(other.sourceWP) && destinationWP.equals(other.destinationWP);
    }
}