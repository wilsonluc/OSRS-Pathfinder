package com.pathfinder.pathfinding;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import java.util.Objects;

/**
 * Represents a pair of {@link WorldPoint}s used to uniquely identify a directional transport or segment of path.
 * <p>
 * This class is primarily used as a key in data structures where a unique source-to-destination
 * relationship needs to be tracked.
 */
@Getter
public class WorldPointPair {
    private final WorldPoint sourceWP;
    private final WorldPoint destinationWP;

    public WorldPointPair(WorldPoint sourceWP, WorldPoint destinationWP) {
        this.sourceWP = sourceWP;
        this.destinationWP = destinationWP;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WorldPointPair)) return false;
        WorldPointPair other = (WorldPointPair) obj;
        return Objects.equals(sourceWP, other.sourceWP) &&
                Objects.equals(destinationWP, other.destinationWP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceWP, destinationWP);
    }

    @Override
    public String toString() {
        return "WorldPointPair{" +
                "sourceWP=" + sourceWP +
                ", destinationWP=" + destinationWP +
                '}';
    }
}