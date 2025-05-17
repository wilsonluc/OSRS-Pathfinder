package com.pathfinder.pathfinding.requirement;

import lombok.Getter;
import net.runelite.api.Quest;

import java.util.Objects;

/**
 * Represents a requirement for a specific Quest.
 */
@Getter
public class QuestReq {
    private final Quest quest;

    public QuestReq(Quest quest) {
        this.quest = quest;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuestReq)) return false;
        QuestReq other = (QuestReq) obj;
        return Objects.equals(quest, other.quest);
    }

    @Override
    public int hashCode() {
        return quest != null ? quest.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "QuestReq{quest=" + quest + '}';
    }
}
