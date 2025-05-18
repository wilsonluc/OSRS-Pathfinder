package com.pathfinder;

import com.pathfinder.enums.Diary;
import com.pathfinder.pathfinding.*;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import java.util.*;

public class Main {
    // TODO: Implement as SpringBoot application to allow for local testing
    public static void main(String[] args) {
        WorldPoint startWP;
        WorldPoint endWP;

        // Sample config, start & end points
        startWP = new WorldPoint(3143, 3514, 0);
        endWP = new WorldPoint(3137, 3515, 0);

        // Spirit tree testing coords
        startWP = new WorldPoint(3179, 3506, 0);
        endWP = new WorldPoint(2771, 3215, 0);

        // Fairy ring testing coords
        startWP = new WorldPoint(3133, 3509, 0);
        endWP = new WorldPoint(2801, 3003, 0);

        // Config player props
        Map<Skill, Integer> skillLevels = new HashMap<>();
        Set<Quest> questsCompleted = new HashSet<>();
        Set<Diary> diariesCompleted = new HashSet<>();
        boolean isFairyRingsUnlocked = true;
        boolean isSpiritTreesUnlocked = true;
        PlayerProperties playerProperties = new PlayerProperties(
                skillLevels,
                questsCompleted,
                diariesCompleted,
                isFairyRingsUnlocked,
                isSpiritTreesUnlocked
        );

        System.out.println(Pathfinder.generatePath(startWP, endWP, playerProperties));
    }
}