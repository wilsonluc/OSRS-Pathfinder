package com.pathfinder.pathfinding;

import com.pathfinder.util.WorldPointUtil;
import net.runelite.api.coords.WorldPoint;

import static net.runelite.api.Constants.REGION_SIZE;

/**
 * Tracks which tiles have been visited during pathfinding.
 * <p>
 * This class breaks the world into regions and tracks visited tiles using
 * compact bitsets to avoid revisiting the same tile during search.
 */
public class VisitedTiles {
    private final SplitFlagMap.RegionExtent regionExtents;
    private final int widthInclusive;

    /**
     * One {@link VisitedRegion} per map region, storing visited tiles.
     */
    private final VisitedRegion[] visitedRegions;

    /**
     * Stores the number of planes per region as retrieved from the {@link CollisionMap}.
     */
    private final byte[] visitedRegionPlanes;

    /**
     * Initialises a new visited tile tracker based on the provided collision map.
     *
     * @param map The collision map, used to get region plane counts.
     */
    public VisitedTiles(CollisionMap map) {
        regionExtents = SplitFlagMap.getRegionExtents();
        widthInclusive = regionExtents.getWidth() + 1;
        final int heightInclusive = regionExtents.getHeight() + 1;

        visitedRegions = new VisitedRegion[widthInclusive * heightInclusive];
        visitedRegionPlanes = map.getPlanes();
    }

    /**
     * Checks whether the given {@link WorldPoint} has been visited.
     *
     * @param point The point to check.
     * @return {@code true} if visited; otherwise {@code false}.
     */
    public boolean get(WorldPoint point) {
        return get(point.getX(), point.getY(), point.getPlane());
    }

    /**
     * Checks whether the given {@link WorldPoint} has been visited.
     *
     * @param packedPoint The 32-bit integer representation of a {@link WorldPoint}.
     * @return {@code true} if visited; otherwise {@code false}.
     */
    public boolean get(int packedPoint) {
        final int x = WorldPointUtil.unpackWorldX(packedPoint);
        final int y = WorldPointUtil.unpackWorldY(packedPoint);
        final int plane = WorldPointUtil.unpackWorldPlane(packedPoint);
        return get(x, y, plane);
    }

    /**
     * Checks whether a given tile coordinate has been visited.
     *
     * @param x     World X coordinate.
     * @param y     World Y coordinate.
     * @param plane Z-plane (height level).
     * @return {@code true} if visited; otherwise {@code false}.
     */
    public boolean get(int x, int y, int plane) {
        final int regionIndex = getRegionIndex(x / REGION_SIZE, y / REGION_SIZE);
        if (regionIndex < 0 || regionIndex >= visitedRegions.length) {
            return true; // Region is out of bounds; report that it's been visited to avoid exploring it further
        }

        final VisitedRegion region = visitedRegions[regionIndex];
        if (region == null) {
            return false;
        }

        return region.get(x % REGION_SIZE, y % REGION_SIZE, plane);
    }

    /**
     * Marks the given packed point as visited.
     *
     * @param packedPoint The packed point.
     */
    public void set(int packedPoint) {
        final int x = WorldPointUtil.unpackWorldX(packedPoint);
        final int y = WorldPointUtil.unpackWorldY(packedPoint);
        final int plane = WorldPointUtil.unpackWorldPlane(packedPoint);
        set(x, y, plane);
    }

    /**
     * Marks the given tile coordinate as visited.
     *
     * @param x     World X coordinate.
     * @param y     World Y coordinate.
     * @param plane Z-plane (height level).
     */
    public void set(int x, int y, int plane) {
        final int regionIndex = getRegionIndex(x / REGION_SIZE, y / REGION_SIZE);
        if (regionIndex < 0 || regionIndex >= visitedRegions.length) {
            return; // Region is out of bounds; report that it's been visited to avoid exploring it further
        }

        VisitedRegion region = visitedRegions[regionIndex];
        if (region == null) {
            region = new VisitedRegion(visitedRegionPlanes[regionIndex]);
            visitedRegions[regionIndex] = region;
        }

        region.set(x % REGION_SIZE, y % REGION_SIZE, plane);
    }

    /**
     * Resets the visited state of all tiles.
     */
    public void clear() {
        for (int i = 0; i < visitedRegions.length; ++i) {
            if (visitedRegions[i] != null) {
                visitedRegions[i] = null;
            }
        }
    }

    /**
     * Computes the linear array index for a given region coordinate.
     *
     * @param regionX Region X index.
     * @param regionY Region Y index.
     * @return The 1D array index.
     */
    private int getRegionIndex(int regionX, int regionY) {
        return (regionX - regionExtents.getMinX()) + (regionY - regionExtents.getMinY()) * widthInclusive;
    }

    /**
     * Represents a set of visited tiles within a single region and all its planes.
     */
    private static class VisitedRegion {
        // This assumes a row is at most 64 tiles and fits in a long
        private final long[] planes;
        private final byte planeCount;

        /**
         * Constructs a new visited region with the given number of planes.
         *
         * @param planeCount Number of planes supported.
         */
        VisitedRegion(byte planeCount) {
            this.planeCount = planeCount;
            this.planes = new long[planeCount * REGION_SIZE];
        }

        /**
         * Marks the tile at (x, y, plane) as visited in the tile bitset.
         *
         * @param x     Local X within region.
         * @param y     Local Y within region.
         * @param plane Z-plane.
         */
        public void set(int x, int y, int plane) {
            if (plane >= planeCount) {
                // Plane is out of bounds; report that it has been visited to avoid further exploration
                return;
            }
            final int index = y + plane * REGION_SIZE;
            planes[index] |= 1L << x;
        }

        /**
         * Returns whether the tile at (x, y, plane) has been visited via tile bitset.
         *
         * @param x     Local X within region.
         * @param y     Local Y within region.
         * @param plane Z-plane.
         * @return {@code true} if visited; otherwise {@code false}.
         */
        public boolean get(int x, int y, int plane) {
            if (plane >= planeCount) {
                // This check is necessary since we check visited tiles before checking the collision map, e.g. the node
                // at (2816, 3455, 1) will check its neighbour to the north which is in a new region with no plane = 1
                return true;
            }
            return (planes[y + plane * REGION_SIZE] & (1L << x)) != 0;
        }
    }
}
