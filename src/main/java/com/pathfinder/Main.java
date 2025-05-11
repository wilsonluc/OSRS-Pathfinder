package com.pathfinder;

import com.pathfinder.pathfinding.Pathfinder;
import com.pathfinder.pathfinding.PathfinderConfig;
import com.pathfinder.pathfinding.SplitFlagMap;
import net.runelite.api.coords.WorldPoint;

import java.util.HashMap;
import java.util.List;

public class Main {
    // TODO: Implement as SpringBoot application to allow for local testing
    public static void main(String[] args) {
        // Sample test case
        SplitFlagMap splitFlagMap = SplitFlagMap.fromResources();
        PathfinderConfig pathfinderConfig = new PathfinderConfig(splitFlagMap);
        WorldPoint startWP = new WorldPoint(2963, 3386, 0);
        WorldPoint endWP = new WorldPoint(2965, 3376, 0);

        System.out.println(String.format("Start WP: %s", startWP));
        System.out.println(String.format("End WP: %s", endWP));
        Pathfinder pathfinder = new Pathfinder(pathfinderConfig, startWP, List.of(endWP));
        pathfinder.run();
        System.out.println(pathfinder.getPath());
    }
}