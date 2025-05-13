package com.pathfinder;

import com.pathfinder.pathfinding.Pathfinder;
import com.pathfinder.pathfinding.PathfinderConfig;
import com.pathfinder.pathfinding.SplitFlagMap;
import com.pathfinder.pathfinding.Transport;
import net.runelite.api.coords.WorldPoint;

import java.util.List;
import java.util.Map;

public class Main {
    // TODO: Implement as SpringBoot application to allow for local testing
    public static void main(String[] args) {
        // Sample test case:

        // Sample config, start & end points
        WorldPoint startWP = new WorldPoint(3143, 3514, 0);
        WorldPoint endWP = new WorldPoint(3137, 3515, 0);

        // Run pathfinder
        System.out.println("Start WP: " + startWP);
        System.out.println("End WP: " + endWP);
        SplitFlagMap map = SplitFlagMap.fromResources();
        Map<WorldPoint, List<Transport>> transports = Transport.loadAllFromResources();
        PathfinderConfig pathfinderConfig = new PathfinderConfig(map, transports);
        Pathfinder pathfinder = new Pathfinder(pathfinderConfig, startWP, List.of(endWP));
        pathfinderConfig.refreshTransportData();
        pathfinder.run();
        System.out.println(pathfinder.getPath());
    }
}