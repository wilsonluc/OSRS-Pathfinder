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

        // Spirit tree testing coords
        startWP = new WorldPoint(3179, 3506, 0);
        endWP = new WorldPoint(2771, 3215, 0);

        // Fairy ring testing coords
        startWP = new WorldPoint(3133, 3509, 0);
        endWP = new WorldPoint(2801, 3003, 0);

        // Set up config
        SplitFlagMap map = SplitFlagMap.fromResources();
        Map<WorldPoint, List<Transport>> transports = Transport.loadAllFromResources();
        PathfinderConfig pathfinderConfig = new PathfinderConfig(map, transports);
        // TODO: Add player properties

        // Run pathfinder and get path
        System.out.println("Start WP: " + startWP);
        System.out.println("End WP: " + endWP);
        Pathfinder pathfinder = new Pathfinder(pathfinderConfig, startWP, List.of(endWP));
        pathfinderConfig.refreshTransportData();
        pathfinder.run();
        System.out.println(pathfinder.getPath());
    }
}