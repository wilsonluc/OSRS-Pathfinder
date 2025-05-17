package com.collisionmapdumper.cache.definitions;

import com.collisionmapdumper.cache.region.Location;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LocationsDefinition {
    private int regionX;
    private int regionY;
    private List<Location> locations = new ArrayList<>();
}
