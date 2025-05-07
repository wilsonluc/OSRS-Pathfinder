package com.pathfinder.lambda;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents connected nodes in a path
 */
@Getter
@Setter
public class Request {
    private int sourceX;
    private int sourceY;
    private int sourceZ;
}
