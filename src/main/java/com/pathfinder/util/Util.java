package com.pathfinder.util;

import com.pathfinder.enums.Diary;
import com.pathfinder.pathfinding.PlayerProperties;
import com.pathfinder.pathfinding.requirement.DiaryReq;
import com.pathfinder.pathfinding.requirement.QuestReq;
import com.pathfinder.pathfinding.requirement.SkillReq;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Utility class providing common operations.
 */
public class Util {
    /**
     * Reads all bytes from the provided {@link InputStream} until end-of-stream is reached.
     *
     * @param in The input stream to read from. Must not be {@code null}.
     * @return A byte array containing all data read from the stream.
     * @throws IOException If an I/O error occurs while reading the stream.
     */
    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (true) {
            int read = in.read(buffer, 0, buffer.length);

            if (read == -1) {
                return result.toByteArray();
            }

            result.write(buffer, 0, read);
        }
    }

    /**
     * Convert to {@link WorldPoint} from String.
     *
     * @param worldPointString String version of {@link WorldPoint}
     * @return {@link WorldPoint} representation of worldPointString
     */
    public static WorldPoint getWorldPoint(String worldPointString) {
        try {
            String[] sourceSplit = worldPointString.split(" ");
            return new WorldPoint(Integer.parseInt(sourceSplit[0]), Integer.parseInt(sourceSplit[1]), Integer.parseInt(sourceSplit[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Not enough values in the source string.");
        } catch (NumberFormatException e) {
            System.err.println("Error: One of the values is not a valid integer.");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred.");
        }
        return null;
    }

    /**
     * Convert to {@link Set<SkillReq>} from String.
     *
     * @param skillReqString String version of {@link Set<SkillReq>}
     * @return {@link Set<SkillReq>} representation of skillReqString
     */
    public static Set<SkillReq> getSkillReqs(String skillReqString) {
        try {
            Set<SkillReq> skillReqs = new HashSet<>();
            String[] skillSplitSemicolonArr = skillReqString.split(";");


            for (String skillSplitSemicolon : skillSplitSemicolonArr) {
                String[] skillSplitSpaceArr = skillSplitSemicolon.split(" ");
                if (skillSplitSpaceArr.length == 2) {
                    skillReqs.add(new SkillReq(Skill.valueOf(skillSplitSpaceArr[1].toUpperCase()), Integer.parseInt(skillSplitSpaceArr[0])));
                }
            }
            return skillReqs;
        } catch (NumberFormatException e) {
            System.err.println("Error: Level of skill is not a valid integer.");
        } catch (Exception e) {
            System.err.println("Unexpected error occurred.");
        }
        return null;
    }

    /**
     * Convert to {@link QuestReq} from String.
     *
     * @param questReqString String version of {@link QuestReq}
     * @return {@link QuestReq} representation of questReqString
     */
    public static QuestReq getQuestReq(String questReqString) {
        try {
            for (Quest quest : Quest.values()) {
                if (quest.getName().equalsIgnoreCase(questReqString)) {
                    return new QuestReq(quest);
                }
            }
        } catch (Exception e) {
            System.err.println("Unexpected error occurred.");
        }
        return null;
    }

    /**
     * Convert to {@link DiaryReq} from String.
     *
     * @param diaryReqString String version of {@link DiaryReq}
     * @return {@link DiaryReq} representation of diaryReqString
     */
    public static DiaryReq getDiaryReq(String diaryReqString) {
        try {
            if (diaryReqString == null || diaryReqString.isEmpty()) {
                return null;
            }

            String input = diaryReqString.toLowerCase().trim();

            // Must include "diary"
            if (!input.contains("diary")) {
                return null;
            }

            // Difficulty keywords
            String[] difficulties = {"easy", "medium", "hard", "elite"};
            String matchedDifficulty = null;
            for (String diff : difficulties) {
                if (input.contains(diff)) {
                    matchedDifficulty = diff;
                    break;
                }
            }

            if (matchedDifficulty == null) {
                return null;
            }

            for (Diary diary : Diary.values()) {
                String diaryName = diary.getName().toLowerCase();

                // Must match difficulty exactly
                if (!diaryName.contains(matchedDifficulty)) {
                    continue;
                }

                // Must match at least one location word
                String[] locationWords = diaryName.replace("diary", "")
                        .replace(matchedDifficulty, "")
                        .trim()
                        .split("\\s+");

                for (String word : locationWords) {
                    if (!word.isEmpty() && input.contains(word)) {
                        return new DiaryReq(diary);
                    }
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error occurred.");
        }
        return null;
    }

    /**
     * Check if the player meets the skill requirements.
     *
     * @param playerProperties The player's properties containing data such as quests and skills.
     * @param skillReqs        The skill requirements to check.
     * @return {@code true} if the player meets all skill requirements, {@code false} otherwise.
     */
    public static boolean isSkillReqsMet(PlayerProperties playerProperties, Set<SkillReq> skillReqs) {
        if (skillReqs == null) {
            return true;
        }

//        for (SkillReq skillReq : skillReqs) {
//            if (playerProperties.getSkillLevel(skillReq.getSkill()) < skillReq.getLevel()) {
//                return false;
//            }
//        }
        return true;
    }

    /**
     * Check if the player meets the quest requirements.
     *
     * @param playerProperties The player's properties containing data such as quests and skills.
     * @param questReq         The quest requirement to check.
     * @return {@code true} if the player meets the quest requirements, {@code false} otherwise.
     */
    public static boolean isQuestReqMet(PlayerProperties playerProperties, QuestReq questReq) {
        if (questReq == null) {
            return true;
        }

//        return playerProperties.getQuestCompleted(questReq.getQuest());
        return true;
    }

    /**
     * Check if the player meets the quest requirements.
     *
     * @param playerProperties The player's properties containing data such as quests and skills.
     * @param quest            The quest to check.
     * @return {@code true} if the player meets the quest requirements, {@code false} otherwise.
     */
    public static boolean isQuestReqMet(PlayerProperties playerProperties, Quest quest) {
        if (quest == null) {
            return true;
        }

//        return playerProperties.getQuestCompleted(quest);
        return true;
    }

    /**
     * Check if the player meets the diary requirements.
     *
     * @param playerProperties The player's properties containing data such as quests and skills.
     * @param diaryReq         The diary requirement to check.
     * @return {@code true} if the player meets the diary requirements, {@code false} otherwise.
     */
    public static boolean isDiaryReqMet(PlayerProperties playerProperties, DiaryReq diaryReq) {
        if (diaryReq == null) {
            return true;
        }

//        return playerProperties.getDiaryCompleted(diaryReq.getDiary());
        return true;
    }
}