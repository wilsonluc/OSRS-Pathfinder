package com.pathfinder.enums;

import lombok.Getter;
import net.runelite.api.coords.WorldArea;

@Getter
public enum WorldAreas {
    // Ferox's Enclave
    FEROX(new WorldArea(3125, 3622, 27, 18, 0)),
    // Above ground wilderness
    WILDY(new WorldArea(2944, 3522, 446, 446, 0)),
    // Underground wilderness
    UNDERGROUND_WILDY(new WorldArea(2944, 9918, 320, 442, 0))
    ;

    private final WorldArea worldArea;

    WorldAreas(net.runelite.api.coords.WorldArea worldArea) {
        this.worldArea = worldArea;
    }
}
