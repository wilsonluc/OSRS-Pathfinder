package com.pathfinder.pathfinding;

/**
 * Enum representing the eight possible ordinal directions on a 2D grid.
 * Each direction is defined by a change in the X and Y coordinates.
 */
public enum OrdinalDirection {
    WEST(-1, 0),
    EAST(1, 0),
    SOUTH(0, -1),
    NORTH(0, 1),
    SOUTH_WEST(-1, -1),
    SOUTH_EAST(1, -1),
    NORTH_WEST(-1, 1),
    NORTH_EAST(1, 1);

    final int x;
    final int y;

    /**
     * Constructor to set the X and Y changes for each direction.
     *
     * @param x The change in the X coordinate
     * @param y The change in the Y coordinate
     */
    OrdinalDirection(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
