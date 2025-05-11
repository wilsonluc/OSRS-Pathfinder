package com.pathfinder.pathfinding;

import com.pathfinder.util.WorldPointUtil;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration & settings for pathfinder.
 */
public class PathfinderConfig {
    /**
     * Map data used for collision checking.
     */
    private final SplitFlagMap mapData;

    /**
     * Thread-local cache of {@link CollisionMap} for thread-safe access.
     */
    private final ThreadLocal<CollisionMap> map;

    /**
     * The maximum time (in milliseconds) the pathfinder will search before aborting.
     */
    @Getter
    private final long calculationCutoffMillis = 15000;

    /**
     * Whether the pathfinder should prefer avoiding the wilderness.
     */
    @Getter
    private boolean avoidWilderness;

    /**
     * Whether the pathfinder should completely disable routing into the wilderness.
     */
    @Getter
    private boolean disableWilderness;

    /**
     * Constructs a {@code PathfinderConfig} using the specified collision map.
     *
     * @param mapData    The shared collision data for the world
     */
    public PathfinderConfig(SplitFlagMap mapData) {
        this.mapData = mapData;
        this.map = ThreadLocal.withInitial(() -> new CollisionMap(this.mapData));
        // TODO: Add transports
    }

    /**
     * Gets the thread-local collision map instance.
     *
     * @return The current thread's {@link CollisionMap}
     */
    public CollisionMap getMap() {
        return map.get();
    }

    /**
     * Determines whether a {@link WorldPoint} lies within the Wilderness.
     *
     * @param worldPoint The {@link WorldPoint} to be checked
     * @return {@code true} if the point is in the wilderness, {@code false} otherwise
     */
    public static boolean isInWilderness(WorldPoint worldPoint) {
        if (Transport.FEROX.contains(worldPoint)) {
            return false;
        }
        return Transport.WILDY.distanceTo(worldPoint) == 0 || Transport.UNDERGROUND_WILDY.distanceTo(worldPoint) == 0;
    }

    /**
     * Determines whether a 32-bit integer representation of a {@link WorldPoint} lies within the Wilderness.
     *
     * @param packedPoint 32-bit integer representation of a {@link WorldPoint} to be checked
     * @return {@code true} if the point is in the wilderness, {@code false} otherwise
     */
    public static boolean isInWilderness(int packedPoint) {
        if (WorldPointUtil.distanceToArea(packedPoint, Transport.FEROX) == 0) {
            return false;
        }
        return WorldPointUtil.distanceToArea(packedPoint, Transport.WILDY) == 0 || WorldPointUtil.distanceToArea(packedPoint, Transport.UNDERGROUND_WILDY) == 0;
    }

    /**
     * Determines whether the pathfinder should avoid entering the wilderness.
     *
     * @param packedPosition         The current 32-bit integer representation of a {@link WorldPoint}
     * @param packedNeighborPosition The 32-bit integer representation of a neighboring {@link WorldPoint}
     * @param targetInWilderness     Whether the target is in the wilderness
     * @return {@code true} if wilderness should be avoided
     */
    public boolean avoidWilderness(int packedPosition, int packedNeighborPosition, boolean targetInWilderness) {
        return avoidWilderness && !isInWilderness(packedPosition) && isInWilderness(packedNeighborPosition) && !targetInWilderness;
    }

    /**
     * Determines whether the pathfinder should completely disable wilderness paths when the target is inside it.
     *
     * @param targetInWilderness Whether the current path target is in the Wilderness
     * @return {@code true} if wilderness routing should be disabled
     */
    public boolean disableWilderness(boolean targetInWilderness) {
        return disableWilderness && targetInWilderness;
    }
}
