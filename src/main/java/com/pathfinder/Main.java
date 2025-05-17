package com.pathfinder;

import com.pathfinder.enums.Diary;
import com.pathfinder.pathfinding.*;
import com.pathfinder.pathfinding.transports.Transport;
import com.pathfinder.pathfinding.transports.TransportHandler;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import java.util.*;

public class Main {
    // TODO: Implement as SpringBoot application to allow for local testing
    public static void main(String[] args) {
        // Sample config, start & end points
        WorldPoint startWP = new WorldPoint(3143, 3514, 0);
        WorldPoint endWP = new WorldPoint(3137, 3515, 0);

        // Spirit tree testing coords
        startWP = new WorldPoint(3179, 3506, 0);
        endWP = new WorldPoint(2771, 3215, 0);

        // Fairy ring testing coords
        startWP = new WorldPoint(3133, 3509, 0);
        endWP = new WorldPoint(2801, 3003, 0);

        // Set up player props
        setPlayerProperties();

        // Set up config & generate path
        generatePath(startWP, endWP);
    }

    /**
     * Sets up player properties for testing.
     */
    private static void setPlayerProperties() {
        boolean isFairyRingsUnlocked = true;
        boolean isSpiritTreesUnlocked = true;
        Map<Skill, Integer> skillLevels = new HashMap<>();
        Set<Quest> questsCompleted = new HashSet<>();
        Set<Diary> diariesCompleted = new HashSet<>();

        PlayerProperties playerProperties = new PlayerProperties(
                isFairyRingsUnlocked,
                isSpiritTreesUnlocked,
                skillLevels,
                questsCompleted,
                diariesCompleted
        );
        TransportHandler.setPlayerProperties(playerProperties);
    }

    /**
     * Generates a path from start to end using the Pathfinder.
     *
     * @param startWP The starting {@link WorldPoint}.
     * @param endWP   The ending {@link WorldPoint}.
     */
    private static void generatePath(WorldPoint startWP, WorldPoint endWP) {
        SplitFlagMap map = SplitFlagMap.fromResources();
        Map<WorldPoint, List<Transport>> transports = TransportHandler.loadAllFromResources();
        PathfinderConfig pathfinderConfig = new PathfinderConfig(map, transports);

        // Run pathfinder and get path
        System.out.println("Start WP: " + startWP);
        System.out.println("End WP: " + endWP);
        Pathfinder pathfinder = new Pathfinder(pathfinderConfig, startWP, List.of(endWP));
        pathfinderConfig.refreshTransportData();
        pathfinder.run();
        System.out.println(pathfinder.getPath());
    }
}