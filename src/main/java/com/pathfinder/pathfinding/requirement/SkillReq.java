package com.pathfinder.pathfinding.requirement;

import lombok.Getter;
import net.runelite.api.Skill;

import java.util.Objects;

/**
 * Represents a skill requirement, including the skill and the required level.
 */
@Getter
public class SkillReq {
    private final Skill skill;
    private final int level;

    public SkillReq(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SkillReq)) return false;
        SkillReq other = (SkillReq) obj;
        return level == other.level &&
                (Objects.equals(skill, other.skill));
    }

    @Override
    public int hashCode() {
        int result = (skill != null ? skill.hashCode() : 0);
        result = 31 * result + level;
        return result;
    }

    @Override
    public String toString() {
        return "SkillReq{skill=" + skill + ", level=" + level + '}';
    }
}
