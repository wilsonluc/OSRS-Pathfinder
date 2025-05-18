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

    private int destinationX;
    private int destinationY;
    private int destinationZ;

    // TODO: Make these dynamic
    private boolean fairyRingsUnlocked;
    private boolean spiritTreesUnlocked;
//    Map<Skill, Integer> skillLevels = new HashMap<>();
//    Set<Quest> questsCompleted = new HashSet<>();
//    Set<Diary> diariesCompleted = new HashSet<>();

    @Override
    public String toString() {
        return "Request{" +
                "sourceX=" + sourceX +
                ", sourceY=" + sourceY +
                ", sourceZ=" + sourceZ +
                ", destinationX=" + destinationX +
                ", destinationY=" + destinationY +
                ", destinationZ=" + destinationZ +
                ", fairyRingsUnlocked=" + fairyRingsUnlocked +
                ", spiritTreesUnlocked=" + spiritTreesUnlocked +
//                ", skillLevels=" + skillLevels +
//                ", questsCompleted=" + questsCompleted +
//                ", diariesCompleted=" + diariesCompleted +
                '}';
    }
}
