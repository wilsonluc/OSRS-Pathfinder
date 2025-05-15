package com.pathfinder.enums;

import net.runelite.api.Client;
import net.runelite.api.QuestState;

public enum Diary {
    // Ardougne
    ARDOUGNE_EASY("Ardougne Easy Diary", 4499),
    ARDOUGNE_MEDIUM(ARDOUGNE_EASY, "Ardougne Medium Diary", 4500),
    ARDOUGNE_HARD(ARDOUGNE_MEDIUM, "Ardougne Hard Diary", 4501),
    ARDOUGNE_ELITE(ARDOUGNE_HARD, "Ardougne Elite Diary", 4502),

    // Desert
    DESERT_EASY("Desert Easy Diary", 4523),
    DESERT_MEDIUM(DESERT_EASY, "Desert Medium Diary", 4524),
    DESERT_HARD(DESERT_MEDIUM, "Desert Hard Diary", 4525),
    DESERT_ELITE(DESERT_HARD, "Desert Elite Diary", 4526),

    // Falador
    FALADOR_EASY("Falador Easy Diary", 4503),
    FALADOR_MEDIUM(FALADOR_EASY, "Falador Medium Diary", 4504),
    FALADOR_HARD(FALADOR_MEDIUM, "Falador Hard Diary", 4505),
    FALADOR_ELITE(FALADOR_HARD, "Falador Elite Diary", 4506),

    // Fremennik
    FREMENNIK_EASY("Fremennik Easy Diary", 4531),
    FREMENNIK_MEDIUM(FREMENNIK_EASY, "Fremennik Medium Diary", 4532),
    FREMENNIK_HARD(FREMENNIK_MEDIUM, "Fremennik Hard Diary", 4533),
    FREMENNIK_ELITE(FREMENNIK_HARD, "Fremennik Elite Diary", 4534),

    // Kandarin
    KANDARIN_EASY("Kandarin Easy Diary", 4515),
    KANDARIN_MEDIUM(KANDARIN_EASY, "Kandarin Medium Diary", 4516),
    KANDARIN_HARD(KANDARIN_MEDIUM, "Kandarin Hard Diary", 4517),
    KANDARIN_ELITE(KANDARIN_HARD, "Kandarin Elite Diary", 4518),

    // Karamja
    KARAMJA_EASY("Karamja Easy Diary", 3577),
    KARAMJA_MEDIUM(KARAMJA_EASY, "Karamja Medium Diary", 3598),
    KARAMJA_HARD(KARAMJA_MEDIUM, "Karamja Hard Diary", 3610),
    KARAMJA_ELITE(KARAMJA_HARD, "Karamja Elite Diary", 4567),

    // Kourend & Kebos
    KOUREND_EASY("Kourend & Kebos Easy Diary", 7929),
    KOUREND_MEDIUM(KOUREND_EASY, "Kourend & Kebos Medium Diary", 7930),
    KOUREND_HARD(KOUREND_MEDIUM, "Kourend & Kebos Hard Diary", 7931),
    KOUREND_ELITE(KOUREND_HARD, "Kourend & Kebos Elite Diary", 7932),

    // Lumbridge & Draynor
    LUMBRIDGE_EASY("Lumbridge & Draynor Easy Diary", 4535),
    LUMBRIDGE_MEDIUM(LUMBRIDGE_EASY, "Lumbridge & Draynor Medium Diary", 4536),
    LUMBRIDGE_HARD(LUMBRIDGE_MEDIUM, "Lumbridge & Draynor Hard Diary", 4537),
    LUMBRIDGE_ELITE(LUMBRIDGE_HARD, "Lumbridge & Draynor Elite Diary", 4538),

    // Morytania
    MORYTANIA_EASY("Morytania Easy Diary", 4527),
    MORYTANIA_MEDIUM(MORYTANIA_EASY, "Morytania Medium Diary", 4528),
    MORYTANIA_HARD(MORYTANIA_MEDIUM, "Morytania Medium Diary", 4529),
    MORYTANIA_ELITE(MORYTANIA_HARD, "Morytania Medium Diary", 4530),

    // Varrock
    VARROCK_EASY("Varrock Easy Diary", 4519),
    VARROCK_MEDIUM(VARROCK_EASY, "Varrock Medium Diary", 4520),
    VARROCK_HARD(VARROCK_MEDIUM, "Varrock Hard Diary", 4521),
    VARROCK_ELITE(VARROCK_HARD, "Varrock Elite Diary", 4522),

    // Western Provinces
    WESTERN_EASY("Western Provinces Easy Diary", 4511),
    WESTERN_MEDIUM(WESTERN_EASY, "Western Provinces Medium Diary", 4512),
    WESTERN_HARD(WESTERN_MEDIUM, "Western Provinces Hard Diary", 4513),
    WESTERN_ELITE(WESTERN_HARD, "Western Provinces Elite Diary", 4514),

    // Wilderness
    WILDERNESS_EASY("Wilderness Easy Diary", 4507),
    WILDERNESS_MEDIUM(WILDERNESS_EASY, "Wilderness Medium Diary", 4508),
    WILDERNESS_HARD(WILDERNESS_MEDIUM, "Wilderness Hard Diary", 4509),
    WILDERNESS_ELITE(WILDERNESS_HARD, "Wilderness Elite Diary", 4510),

    // Combat Achievements
    COMBAT_ACHIEVEMENT_EASY("Combat Achievement Easy", 12863),
    COMBAT_ACHIEVEMENT_MEDIUM(COMBAT_ACHIEVEMENT_EASY, "Combat Achievement Medium", 12863),
    COMBAT_ACHIEVEMENT_HARD(COMBAT_ACHIEVEMENT_MEDIUM, "Combat Achievement Hard", 12863),
    COMBAT_ACHIEVEMENT_ELITE(COMBAT_ACHIEVEMENT_HARD, "Combat Achievement Elite", 12863),
    COMBAT_ACHIEVEMENT_MASTER(COMBAT_ACHIEVEMENT_ELITE, "Combat Achievement Master", 12863),
    COMBAT_ACHIEVEMENT_GRANDMASTER(COMBAT_ACHIEVEMENT_MASTER, "Combat Achievement Grandmaster", 12863),
    ;

    private final Diary diaryReq;
    private final String name;
    private final int varbit;

    Diary(Diary diaryReq, String name, int varbit) {
        this.diaryReq = diaryReq;
        this.name = name;
        this.varbit = varbit;
    }

    Diary(String name, int varbit) {
        this.diaryReq = null;
        this.name = name;
        this.varbit = varbit;
    }

    public Diary getDiaryReq() {
        return diaryReq;
    }

    public String getName() {
        return name;
    }
    
    public int getVarbit() {
        return varbit;
    }

    public QuestState getState(Client client) {
        return client.getVarbitValue(varbit) == 1 ? QuestState.FINISHED : QuestState.NOT_STARTED;
    }

    public boolean isCompleted(Client client) {
        return client.getVarbitValue(varbit) == 1;
    }
}
