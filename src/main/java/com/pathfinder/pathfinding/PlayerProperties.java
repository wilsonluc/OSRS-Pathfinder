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
    private Map<Skill, Integer> skillLevels;
    private Set<Quest> questsCompleted;
    private Set<Diary> diariesCompleted;

    private boolean isFairyRingsUnlocked;
    private boolean isSpiritTreesUnlocked;

    /**
     * Get the level of a skill.
     *
     * @param skill The skill to get the level of.
     * @return The level of the skill, or 1 if not found.
     */
    public int getSkillLevel(Skill skill) {
        if (!skillLevels.containsKey(skill)) {
            return 1;
        }

        return skillLevels.get(skill);
    }

    /**
     * Get the completion state of a quest.
     *
     * @param quest The quest to get the completion state of.
     * @return {@code true} if the quest is completed, {@code false} otherwise.
     */
    public boolean getQuestCompleted(Quest quest) {
        return questsCompleted.contains(quest);
    }

    /**
     * Get the completion state of a diary.
     *
     * @param diary The diary to get the completion state of.
     * @return {@code true} if the diary is completed, {@code false} otherwise.
     */
    public boolean getDiaryCompleted(Diary diary) {
        return diariesCompleted.contains(diary);
    }

    /**
     * Check if the player has unlocked fairy rings.
     *
     * @return {@code true} if fairy rings are unlocked, {@code false} otherwise.
     */
    public boolean isFairyRingsUnlocked() {
        return isFairyRingsUnlocked;
    }

    /**
     * Check if the player has unlocked spirit trees.
     *
     * @return {@code true} if spirit trees are unlocked, {@code false} otherwise.
     */
    public boolean isSpiritTreesUnlocked() {
        return isSpiritTreesUnlocked;
    }
}
