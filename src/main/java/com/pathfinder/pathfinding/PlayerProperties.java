package com.pathfinder.pathfinding;

import com.pathfinder.enums.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
public class PlayerProperties {
    private boolean isFairyRingsUnlocked;
    private boolean isSpiritTreesUnlocked;
    private Map<Skill, Integer> skillLevels;
    private Set<Quest> questsCompleted;
    private Set<Diary> diariesCompleted;

    public int getSkillLevel(Skill skill) {
        // TODO:
        return -1;
    }

    public boolean getQuestCompleted(Quest quest) {
        // TODO:
        return true;
    }

    public boolean getDiaryCompleted(Diary diary) {
        // TODO:
        return true;
    }

    public boolean isFairyRingsUnlocked() {
        // TODO:
        return true;
    }

    public boolean isSpiritTreesUnlocked() {
        // TODO:
        return true;
    }
}
