package com.pathfinder.pathfinding;

import com.pathfinder.Main;
import com.pathfinder.util.Util;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static net.runelite.api.Constants.REGION_SIZE;

/**
 * A spatially partitioned structure for storing and retrieving flag-based pathfinding data
 * (e.g. collision flags) for a grid-based map.
 * <p>
 * Each map region is stored separately in a {@link FlagMap}, and accessed using a packed
 * (x, y) coordinate space to enable efficient lookup and memory usage.
 */
public class SplitFlagMap {
    /**
     * Extents of all loaded regions. Used to calculate index offsets.
     */
    @Getter
    private static RegionExtent regionExtents;

    /**
     * Stores the number of vertical planes available per region, used during pathfinding.
     */
    @Getter
    private final byte[] regionMapPlaneCounts;

    /**
     * Holds the actual flag maps for each region, flattened into a 1D array using region coordinates.
     */
    private final FlagMap[] regionMaps;

    /**
     * Width (in regions) of the collision grid, including both min and max X boundaries.
     */
    private final int widthInclusive;

    /**
     * Constructs a SplitFlagMap from compressed region flag data.
     *
     * @param compressedRegions A map of packed region positions to their compressed flag bytes
     */
    public SplitFlagMap(Map<Integer, byte[]> compressedRegions) {
        widthInclusive = regionExtents.getWidth() + 1;
        final int heightInclusive = regionExtents.getHeight() + 1;
        regionMaps = new FlagMap[widthInclusive * heightInclusive];
        regionMapPlaneCounts = new byte[regionMaps.length];

        for (Map.Entry<Integer, byte[]> entry : compressedRegions.entrySet()) {
            final int pos = entry.getKey();
            final int x = unpackX(pos);
            final int y = unpackY(pos);
            final int index = getIndex(x, y);
            FlagMap flagMap = new FlagMap(x * REGION_SIZE, y * REGION_SIZE, entry.getValue());
            regionMaps[index] = flagMap;
            regionMapPlaneCounts[index] = flagMap.getPlaneCount();
        }
    }

    /**
     * Checks if a given flag is set at the specified world coordinates.
     *
     * @param x    The X-coordinate of a {@link WorldPoint}
     * @param y    The Y-coordinate of a {@link WorldPoint}
     * @param z    The vertical plane of a {@link WorldPoint}
     * @param flag The flag index to check
     * @return {@code true} if the flag is set, {@code false} otherwise
     */
    public boolean get(int x, int y, int z, int flag) {
        final int index = getIndex(x / REGION_SIZE, y / REGION_SIZE);
        if (index < 0 || index >= regionMaps.length || regionMaps[index] == null) {
            return false;
        }

        return regionMaps[index].get(x, y, z, flag);
    }

    /**
     * Computes the array index for the region at the given coordinates.
     *
     * @param regionX Region X index
     * @param regionY Region Y index
     * @return Flattened index in the internal array
     */
    private int getIndex(int regionX, int regionY) {
        return (regionX - regionExtents.minX) + (regionY - regionExtents.minY) * widthInclusive;
    }

    /**
     * Unpacks the X coordinate from a 32-bit integer representation of a {@link WorldPoint}.
     *
     * @param packedWorldPoint The packed (x, y) packedWorldPoint
     * @return The X coordinate
     */
    public static int unpackX(int packedWorldPoint) {
        return packedWorldPoint & 0xFFFF;
    }

    /**
     * Unpacks the Y coordinate from a 32-bit integer representation of a {@link WorldPoint}.
     *
     * @param packedWorldPoint The packed (x, y) packedWorldPoint
     * @return The Y coordinate
     */
    public static int unpackY(int packedWorldPoint) {
        return (packedWorldPoint >> 16) & 0xFFFF;
    }

    /**
     * Packs an (x, y) pair into a 32-bit integer representation.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return 32-bit integer representation of the position
     */
    public static int packPosition(int x, int y) {
        return (x & 0xFFFF) | ((y & 0xFFFF) << 16);
    }

    /**
     * Loads and decompresses flag map data from a zip resource file.
     *
     * @return A new {@link SplitFlagMap} containing all regions in the archive
     */
    public static SplitFlagMap fromResources() {
        Map<Integer, byte[]> compressedRegions = new HashMap<>();
        try (ZipInputStream in = new ZipInputStream(Objects.requireNonNull(Main.class.getResourceAsStream("/collision-map.zip")))) {
            int minX = 0;
            int minY = 0;
            int maxX = 0;
            int maxY = 0;

            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                String[] n = entry.getName().split("_");
                final int x = Integer.parseInt(n[0]);
                final int y = Integer.parseInt(n[1]);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);

                compressedRegions.put(SplitFlagMap.packPosition(x, y), Util.readAllBytes(in));
            }

            regionExtents = new RegionExtent(minX, minY, maxX, maxY);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return new SplitFlagMap(compressedRegions);
    }

    /**
     * Class holding the spatial bounds of all regions stored in this map.
     * Provides utilities to calculate grid dimensions.
     */
    @Getter
    public static class RegionExtent {
        private final int minX;
        private final int minY;
        private final int maxX;
        private final int maxY;

        public RegionExtent(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public int getWidth() {
            return maxX - minX;
        }

        public int getHeight() {
            return maxY - minY;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RegionExtent)) return false;
            RegionExtent other = (RegionExtent) obj;
            return minX == other.minX &&
                    minY == other.minY &&
                    maxX == other.maxX &&
                    maxY == other.maxY;
        }

        @Override
        public int hashCode() {
            int result = Integer.hashCode(minX);
            result = 31 * result + Integer.hashCode(minY);
            result = 31 * result + Integer.hashCode(maxX);
            result = 31 * result + Integer.hashCode(maxY);
            return result;
        }

        @Override
        public String toString() {
            return "RegionExtent{" +
                    "minX=" + minX +
                    ", minY=" + minY +
                    ", maxX=" + maxX +
                    ", maxY=" + maxY +
                    '}';
        }
    }
}
