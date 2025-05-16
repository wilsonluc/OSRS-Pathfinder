package com.pathfinder.pathfinding;

import com.pathfinder.enums.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Quest;
import net.runelite.api.Skill;

@AllArgsConstructor
@Getter
public class PlayerProperties {
    private boolean isFairyRingsUnlocked;
    private boolean isSpiritTreesUnlocked;

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
}
