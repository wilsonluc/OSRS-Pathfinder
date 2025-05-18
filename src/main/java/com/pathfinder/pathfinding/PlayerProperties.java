package com.pathfinder.pathfinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerProperties {
    private boolean fairyRingsUnlocked;
    private boolean spiritTreesUnlocked;

//    private Map<Skill, Integer> skillLevels;
//    private Set<Quest> questsCompleted;
//    private Set<Diary> diariesCompleted;

//    /**
//     * Get the level of a skill.
//     *
//     * @param skill The skill to get the level of.
//     * @return The level of the skill, or 1 if not found.
//     */
//    public int getSkillLevel(Skill skill) {
//        if (!skillLevels.containsKey(skill)) {
//            return 1;
//        }
//
//        return skillLevels.get(skill);
//    }
//
//    /**
//     * Get the completion state of a quest.
//     *
//     * @param quest The quest to get the completion state of.
//     * @return {@code true} if the quest is completed, {@code false} otherwise.
//     */
//    public boolean getQuestCompleted(Quest quest) {
//        return questsCompleted.contains(quest);
//    }
//
//    /**
//     * Get the completion state of a diary.
//     *
//     * @param diary The diary to get the completion state of.
//     * @return {@code true} if the diary is completed, {@code false} otherwise.
//     */
//    public boolean getDiaryCompleted(Diary diary) {
//        return diariesCompleted.contains(diary);
//    }
}
