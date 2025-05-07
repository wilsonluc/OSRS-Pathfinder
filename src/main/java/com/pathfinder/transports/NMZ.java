package com.pathfinder.transports;

import net.runelite.api.Quest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NMZ {
    private static final List<Quest> nmzQuests = List.of(
            Quest.THE_ASCENT_OF_ARCEUUS,
            Quest.CONTACT,
            Quest.THE_CORSAIR_CURSE,
            Quest.THE_DEPTHS_OF_DESPAIR,
            Quest.DESERT_TREASURE_I,
            Quest.DRAGON_SLAYER_I,
            Quest.DREAM_MENTOR,
            Quest.FAIRYTALE_I__GROWING_PAINS,
            Quest.FAMILY_CREST,
            Quest.FIGHT_ARENA,
            Quest.THE_FREMENNIK_ISLES,
            Quest.GETTING_AHEAD,
            Quest.THE_GRAND_TREE,
            Quest.THE_GREAT_BRAIN_ROBBERY,
            Quest.GRIM_TALES,
            Quest.HAUNTED_MINE,
            Quest.HOLY_GRAIL,
            Quest.HORROR_FROM_THE_DEEP,
            Quest.IN_SEARCH_OF_THE_MYREQUE,
            Quest.LEGENDS_QUEST,
            Quest.LOST_CITY,
            Quest.LUNAR_DIPLOMACY,
            Quest.MONKEY_MADNESS_I,
            Quest.MOUNTAIN_DAUGHTER,
            Quest.MY_ARMS_BIG_ADVENTURE,
            Quest.ONE_SMALL_FAVOUR,
            Quest.RECIPE_FOR_DISASTER,
            Quest.ROVING_ELVES,
            Quest.SHADOW_OF_THE_STORM,
            Quest.SHILO_VILLAGE,
            Quest.SONG_OF_THE_ELVES,
            Quest.TALE_OF_THE_RIGHTEOUS,
            Quest.TREE_GNOME_VILLAGE,
            Quest.TROLL_ROMANCE,
            Quest.TROLL_STRONGHOLD,
            Quest.VAMPYRE_SLAYER,
            Quest.WHAT_LIES_BELOW,
            Quest.WITCHS_HOUSE
    );

    /**
     * Whether NMZ is accessible for user
     *
     * @param questsCompleted Quests completed by user
     * @return True if number of intersecting quests is >= 5
     */
    public static boolean canAccessNMZ(Set<Quest> questsCompleted) {
        Set<Quest> intersectingQuests = new HashSet<>(questsCompleted);
        intersectingQuests.retainAll(nmzQuests);
        return intersectingQuests.size() >= 5;
    }
}
