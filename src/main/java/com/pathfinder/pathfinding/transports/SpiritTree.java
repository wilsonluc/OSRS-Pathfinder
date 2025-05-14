package com.pathfinder.pathfinding.transports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@AllArgsConstructor
@Getter
public enum SpiritTree {
    TREE_GNOME_VILLAGE(new WorldPoint(2542, 3170, 0), 1293, "1"),
    GNOME_STRONGHOLD(new WorldPoint(2461, 3444, 0), 1294, "2"),
    BATTLEFIELD_OF_KHAZARD(new WorldPoint(2555, 3259, 0), 1295, "3"),
    GRAND_EXCHANGE(new WorldPoint(3185, 3508, 0), 1295, "4"),
    FELDIP_HILLS(new WorldPoint(2488, 2850, 0), 1295, "5"),
    PRIFDDINAS(new WorldPoint(3274, 6123, 0), 37329, "6"),

    PORT_SARIM(new WorldPoint(3058, 3257, 0), 8338, "7"),
    ETCETERIA(new WorldPoint(2613, 3855, 0), 8382, "8"),
    BRIMHAVEN(new WorldPoint(2800, 3203, 0), 8383, "9"),
    HOSIDIUS(new WorldPoint(1693, 3540, 0), 27116, "A"),
    FARMING_GUILD(new WorldPoint(1251, 3750, 0), 33733, "B"),
    POH(new WorldPoint(0, 0, 0), 29227, "C"),
    POISON_WASTE(new WorldPoint(2339, 3109, 0), 49598, "D"),
    ;

    private final WorldPoint worldPoint;
    private final int id;
    private final String menuOption;
}
