package com.pathfinder.util;

import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

/**
 * Utility class for working with {@link WorldPoint} and converting it to a compact
 * 32-bit integer for efficient storage and comparison.
 *
 * <p>Packing uses a 32-bit integer with the following format:
 * <ul>
 *     <li>Bits 0–14: X coordinate (15 bits)</li>
 *     <li>Bits 15–29: Y coordinate (15 bits)</li>
 *     <li>Bits 30–31: Plane (2 bits)</li>
 * </ul>
 */
public class WorldPointUtil {
    /**
     * Packs a {@link WorldPoint} into a 32-bit integer.
     *
     * @param worldPoint World point to be packed
     * @return 32-bit integer representation of world point
     */
    public static int packWorldPoint(WorldPoint worldPoint) {
        return packWorldPoint(worldPoint.getX(), worldPoint.getY(), worldPoint.getPlane());
    }

    /**
     * Packs x, y, and plane values into a 32-bit integer.
     *
     * @param x     The X-coordinate (15 bits)
     * @param y     The Y-coordinate (15 bits)
     * @param plane The plane (2 bits)
     * @return 32-bit integer representation of world point coords
     */
    public static int packWorldPoint(int x, int y, int plane) {
        return (x & 0x7FFF) | ((y & 0x7FFF) << 15) | ((plane & 0x3) << 30);
    }

    /**
     * Unpacks a packed 32-bit integer representation of a {@link WorldPoint} into a {@link WorldPoint} object.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @return Unpacked world point
     */
    public static WorldPoint unpackWorldPoint(int packedWorldPoint) {
        final int x = unpackWorldX(packedWorldPoint);
        final int y = unpackWorldY(packedWorldPoint);
        final int plane = unpackWorldPlane(packedWorldPoint);
        return new WorldPoint(x, y, plane);
    }

    /**
     * Extracts the X coordinate from a packed 32-bit integer representation of a {@link WorldPoint}.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @return X coordinate
     */
    public static int unpackWorldX(int packedWorldPoint) {
        return packedWorldPoint & 0x7FFF;
    }

    /**
     * Extracts the Y coordinate from a packed 32-bit integer representation of a {@link WorldPoint}.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @return Y coordinate
     */
    public static int unpackWorldY(int packedWorldPoint) {
        return (packedWorldPoint >> 15) & 0x7FFF;
    }

    /**
     * Extracts the plane from a packed 32-bit integer representation of a {@link WorldPoint}.
     *
     * @param packedWorldPoint 32-bit integer representation of world point
     * @return Plane (0-3)
     */
    public static int unpackWorldPlane(int packedWorldPoint) {
        return (packedWorldPoint >> 30) & 0x3;
    }

    /**
     * Calculates distance between two packed points.
     *
     * @param previousPackedWorldPoint 32-bit integer representation of previous world point
     * @param currentPackedWorldPoint  32-bit integer representation of current world point
     * @return Chebyshev distance between two world points
     */
    public static int distanceBetween(int previousPackedWorldPoint, int currentPackedWorldPoint) {
        return distanceBetween(previousPackedWorldPoint, currentPackedWorldPoint, 1);
    }

    /**
     * Calculates distance between two packed points.
     *
     * @param previousPackedWorldPoint 32-bit integer representation of previous world point
     * @param currentPackedWorldPoint  32-bit integer representation of current world point
     * @param diagonal                 Movement style: 1 for diagonal (Chebyshev), 2 for Manhattan
     * @return Chebyshev/Manhattan distance between two world points
     */
    public static int distanceBetween(int previousPackedWorldPoint, int currentPackedWorldPoint, int diagonal) {
        final int previousX = WorldPointUtil.unpackWorldX(previousPackedWorldPoint);
        final int previousY = WorldPointUtil.unpackWorldY(previousPackedWorldPoint);
        final int currentX = WorldPointUtil.unpackWorldX(currentPackedWorldPoint);
        final int currentY = WorldPointUtil.unpackWorldY(currentPackedWorldPoint);
        final int dx = Math.abs(previousX - currentX);
        final int dy = Math.abs(previousY - currentY);

        if (diagonal == 1) {
            return Math.max(dx, dy);
        } else if (diagonal == 2) {
            return dx + dy;
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Calculates Chebyshev distance between two {@link WorldPoint}s.
     *
     * @param previousWorldPoint Previous world point
     * @param currentWorldPoint  Current world point
     * @return Chebyshev distance between two world points
     */
    public static int distanceBetween(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint) {
        return distanceBetween(previousWorldPoint, currentWorldPoint, 1);
    }

    /**
     * Calculates Chebyshev distance between two {@link WorldPoint}s.
     *
     * @param previousWorldPoint Previous world point
     * @param currentWorldPoint  Current world point
     * @param diagonal           Movement style: 1 for diagonal (Chebyshev), 2 for Manhattan
     * @return Chebyshev/Manhattan distance between two world points
     */
    public static int distanceBetween(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint, int diagonal) {
        final int dx = Math.abs(previousWorldPoint.getX() - currentWorldPoint.getX());
        final int dy = Math.abs(previousWorldPoint.getY() - currentWorldPoint.getY());

        if (diagonal == 1) {
            return Math.max(dx, dy);
        } else if (diagonal == 2) {
            return dx + dy;
        }

        return Integer.MAX_VALUE;
    }

    /**
     * Calculates the Chebyshev distance from a packed point to a {@link WorldArea}.
     * Returns {@code Integer.MAX_VALUE} if the plane does not match.
     *
     * @param packedWorldPoint Packed world point
     * @param worldArea        World area containing one or many world points
     * @return Distance to the area, or {@code Integer.MAX_VALUE} if not on same plane
     */
    public static int distanceToArea(int packedWorldPoint, WorldArea worldArea) {
        final int plane = unpackWorldPlane(packedWorldPoint);
        if (worldArea.getPlane() != plane) {
            return Integer.MAX_VALUE;
        }

        final int y = unpackWorldY(packedWorldPoint);
        final int x = unpackWorldX(packedWorldPoint);
        final int areaMaxX = worldArea.getX() + worldArea.getWidth() - 1;
        final int areaMaxY = worldArea.getY() + worldArea.getHeight() - 1;
        final int dx = Math.max(Math.max(worldArea.getX() - x, 0), x - areaMaxX);
        final int dy = Math.max(Math.max(worldArea.getY() - y, 0), y - areaMaxY);

        return Math.max(dx, dy);
    }
}
