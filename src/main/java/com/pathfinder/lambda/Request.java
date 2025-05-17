package com.pathfinder.lambda;

import com.pathfinder.enums.Diary;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents connected nodes in a path
 */
@Getter
@Setter
public class Request {
    private int sourceX;
    private int sourceY;
    private int sourceZ;

    private int destinationX;
    private int destinationY;
    private int destinationZ;

    // TODO: Make these dynamic
    boolean isFairyRingsUnlocked = true;
    boolean isSpiritTreesUnlocked = true;
    Map<Skill, Integer> skillLevels = new HashMap<>();
    Set<Quest> questsCompleted = new HashSet<>();
    Set<Diary> diariesCompleted = new HashSet<>();
}
