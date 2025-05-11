package com.pathfinder.pathfinding;

import lombok.Getter;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

/**
 * This class represents a travel point between two WorldPoints.
 */
public class Transport {
    /**
     * The starting point of this transport.
     */
    @Getter
    private final WorldPoint origin;

    /**
     * The ending point of this transport.
     */
    @Getter
    private final WorldPoint destination;

    /**
     * The additional cost to reach the destination.
     */
    @Getter
    public int additionalCost;

    /**
     * {@link WorldArea} representing Ferox's Enclave.
     */
    @Getter
    public static final WorldArea FEROX = new WorldArea(3125, 3622, 27, 18, 0);

    /**
     * {@link WorldArea} representing the wilderness.
     */
    @Getter
    public static final WorldArea WILDY = new WorldArea(2944, 3522, 446, 446, 0);

    /**
     * {@link WorldArea} representing the underground section of the wilderness.
     */
    @Getter
    public static final WorldArea UNDERGROUND_WILDY = new WorldArea(2944, 9918, 320, 442, 0);

    /**
     * Constructs a transport link between two {@link WorldPoint} objects.
     *
     * @param origin      The starting location of the transport
     * @param destination The end location of the transport
     */
    public Transport(WorldPoint origin, WorldPoint destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
