package com.pathfinder.pathfinding;

import com.pathfinder.pathfinding.transports.Transport;
import com.pathfinder.util.PrimitiveIntHashMap;
import com.pathfinder.util.WorldPointUtil;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pathfinder.enums.WorldAreas.*;

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
     * All {@link Transport}s originating from {@link WorldPoint} key.
     */
    private final Map<WorldPoint, List<Transport>> allTransports;

    /**
     * Shallow copy of {@link #allTransports}.
     */
    @Getter
    private final Map<WorldPoint, List<Transport>> transports;

    /**
     * Custom hash map that maps a 32-bit integer representation of a {@link WorldPoint} to a list of {@link Transport}s.
     */
    @Getter
    private final PrimitiveIntHashMap<List<Transport>> transportsPacked;

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
    public PathfinderConfig(SplitFlagMap mapData, Map<WorldPoint, List<Transport>> transports) {
        this.mapData = mapData;
        this.map = ThreadLocal.withInitial(() -> new CollisionMap(this.mapData));
        this.allTransports = transports;
        this.transports = new HashMap<>(allTransports.size());
        this.transportsPacked = new PrimitiveIntHashMap<>(allTransports.size());
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
     * Refreshes the internal transport caches from {@link #allTransports}.
     * <p>
     * This method creates fresh copies of transport lists for each origin {@link WorldPoint} and stores them in:
     * <ul>
     *     <li>{@link #transports} — a direct {@link WorldPoint}-keyed map</li>
     *     <li>{@link #transportsPacked} — an integer-keyed map using packed {@link WorldPoint}s via
     *     {@link WorldPointUtil#packWorldPoint(WorldPoint)}</li>
     * </ul>
     * <p>
     * Intended to be called when transport data is updated or initialised.
     */
    public void refreshTransportData() {
        for (Map.Entry<WorldPoint, List<Transport>> entry : allTransports.entrySet()) {
            List<Transport> usableTransports = new ArrayList<>(entry.getValue().size());
            usableTransports.addAll(entry.getValue());

            WorldPoint point = entry.getKey();
            transports.put(point, usableTransports);
            transportsPacked.put(WorldPointUtil.packWorldPoint(point), usableTransports);
        }
    }

    /**
     * Determines whether a {@link WorldPoint} lies within the Wilderness.
     *
     * @param worldPoint The {@link WorldPoint} to be checked
     * @return {@code true} if the point is in the wilderness, {@code false} otherwise
     */
    public static boolean isInWilderness(WorldPoint worldPoint) {
        if (FEROX.getWorldArea().contains(worldPoint)) {
            return false;
        }
        return WILDY.getWorldArea().distanceTo(worldPoint) == 0
                || UNDERGROUND_WILDY.getWorldArea().distanceTo(worldPoint) == 0;
    }

    /**
     * Determines whether a 32-bit integer representation of a {@link WorldPoint} lies within the Wilderness.
     *
     * @param packedPoint 32-bit integer representation of a {@link WorldPoint} to be checked
     * @return {@code true} if the point is in the wilderness, {@code false} otherwise
     */
    public static boolean isInWilderness(int packedPoint) {
        if (WorldPointUtil.distanceToArea(packedPoint, FEROX.getWorldArea()) == 0) {
            return false;
        }
        return WorldPointUtil.distanceToArea(packedPoint, WILDY.getWorldArea()) == 0
                || WorldPointUtil.distanceToArea(packedPoint, UNDERGROUND_WILDY.getWorldArea()) == 0;
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
