package com.pathfinder.enums;

import lombok.Getter;

@Getter
public enum TransportType {
    WALK(1),
    TRANSPORT(5),
    SPIRIT_TREE(6),
    FAIRY_RING(15),
    ;

    private final int additionalCost;

    TransportType(int additionalCost) {
        this.additionalCost = additionalCost;
    }
}
