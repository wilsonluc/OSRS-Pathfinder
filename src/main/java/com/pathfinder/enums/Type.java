package com.pathfinder.enums;

import lombok.Getter;

@Getter
public enum Type {
    WALK(1),
    TRANSPORT(5),
    SPIRIT_TREE(6),
    FAIRY_RING(15),
    ;

    private final int additionalCost;

    Type(int additionalCost) {
        this.additionalCost = additionalCost;
    }
}
