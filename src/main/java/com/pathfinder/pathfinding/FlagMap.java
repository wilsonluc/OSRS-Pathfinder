package com.pathfinder.pathfinding;

import lombok.Getter;

import java.util.BitSet;
import java.util.Locale;

import static net.runelite.api.Constants.REGION_SIZE;

/**
 * A region-based data structure representing collision or pathfinding flags
 * in a 3D grid (X, Y, Z), where each tile can store multiple boolean flags.
 *
 * <p>The flags are stored compactly using a {@link BitSet}, and accessed via
 * (x, y, z, flagIndex) coordinates within a bounded region of size {@code REGION_SIZE}.
 */
public class FlagMap {
    /**
     * Number of flag slots available per tile.
     * Each tile can have up to 2 different flags (e.g., walkable, blocked).
     */
    private static final byte FLAG_COUNT = 2;

    /**
     * Compact bit storage for all flags across the 3D region
     * This BitSet holds all the flag data for the region, with each bit representing a flag.
     */
    private final BitSet flags;

    /**
     * Number of vertical levels (planes) represented in the flag map.
     */
    @Getter
    private final byte planeCount;

    /**
     * The minimum X coordinate of this region.
     */
    private final int minX;

    /**
     * The minimum Y coordinate of this region.
     */
    private final int minY;

    /**
     * Constructs a {@code FlagMap} from the given bit-packed byte array.
     *
     * @param minX  The minimum X coordinate of this region
     * @param minY  The minimum Y coordinate of this region
     * @param bytes A byte array representing a {@link BitSet} of packed flag data
     */
    public FlagMap(int minX, int minY, byte[] bytes) {
        this.minX = minX;
        this.minY = minY;
        flags = BitSet.valueOf(bytes);
        int scale = REGION_SIZE * REGION_SIZE * FLAG_COUNT;
        this.planeCount = (byte) ((flags.size() + scale - 1) / scale);
    }

    /**
     * Returns the boolean value of a flag at a specific tile and plane.
     *
     * @param x    X-coordinate
     * @param y    Y-coordinate
     * @param z    Z-plane
     * @param flag Index of the flag (0-based, must be < {@code FLAG_COUNT})
     * @return {@code true} if the flag is set, {@code false} otherwise or if out of bounds
     */
    public boolean get(int x, int y, int z, int flag) {
        if (x < minX || x >= (minX + REGION_SIZE) || y < minY || y >= (minY + REGION_SIZE) || z < 0 || z >= planeCount) {
            return false;
        }

        return flags.get(index(x, y, z, flag));
    }

    /**
     * Sets or clears a flag at a specific tile and plane.
     *
     * @param x     X-coordinate
     * @param y     Y-coordinate
     * @param z     Z-plane
     * @param flag  Index of the flag (0-based, must be < {@code FLAG_COUNT})
     * @param value {@code true} to set the flag, {@code false} to clear it
     */
    public void set(int x, int y, int z, int flag, boolean value) {
        flags.set(index(x, y, z, flag), value);
    }

    /**
     * Calculates the internal bit index in the {@link BitSet} for a given tile, plane, and flag index.
     *
     * @param x    X-coordinate of the tile
     * @param y    Y-coordinate of the tile
     * @param z    Z-plane (vertical layer)
     * @param flag Index of the flag (0-based)
     * @return The index in the BitSet corresponding to the tile and flag
     * @throws IndexOutOfBoundsException if any coordinate or flag index is outside valid bounds
     */
    private int index(int x, int y, int z, int flag) {
        if (x < minX || x >= (minX + REGION_SIZE) || y < minY || y >= (minY + REGION_SIZE) || z < 0 || z >= planeCount || flag < 0 || flag >= FLAG_COUNT) {
            throw new IndexOutOfBoundsException(
                    String.format(Locale.ENGLISH, "[%d,%d,%d,%d] when extents are [>=%d,>=%d,>=%d,>=%d] - [<=%d,<=%d,<%d,<%d]",
                            x, y, z, flag,
                            minX, minY, 0, 0,
                            minX + REGION_SIZE - 1, minY + REGION_SIZE - 1, planeCount, FLAG_COUNT
                    )
            );
        }

        return (z * REGION_SIZE * REGION_SIZE + (y - minY) * REGION_SIZE + (x - minX)) * FLAG_COUNT + flag;
    }
}
