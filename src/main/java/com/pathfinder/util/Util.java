package com.pathfinder.util;

import com.pathfinder.enums.Diary;
import com.pathfinder.pathfinding.requirement.DiaryReq;
import com.pathfinder.pathfinding.requirement.QuestReq;
import com.pathfinder.pathfinding.requirement.SkillReq;
import net.runelite.api.Quest;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

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
     * Convert to {@link SkillReq} from String.
     *
     * @param skillReqString String version of {@link SkillReq}
     * @return {@link SkillReq} representation of skillReqString
     */
    public static SkillReq getSkillReq(String skillReqString) {
        try {
            String[] sourceSplit = skillReqString.split(" ");
            if (sourceSplit.length == 2) {
                String skillString = sourceSplit[1];
                for (Skill skill : Skill.values()) {
                    if (skill.getName().equalsIgnoreCase(skillString)) {
                        return new SkillReq(skill, Integer.parseInt(sourceSplit[0]));
                    }
                }
            }
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
}