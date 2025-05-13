package com.pathfinder.enums;

import lombok.Getter;

@Getter
public enum Type {
    WALK(1),
    TRANSPORT(5);

    private final int additionalCost;

    Type(int additionalCost) {
        this.additionalCost = additionalCost;
    }
}
