package com.collisionmapdumper.cache.region;

import lombok.Value;

@Value
public class Location {
    private final int id;
    private final int type;
    private final int orientation;
    private final Position position;
}
