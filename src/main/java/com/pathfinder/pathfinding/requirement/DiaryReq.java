package com.pathfinder.pathfinding.requirement;

import com.pathfinder.enums.Diary;
import lombok.Getter;

import java.util.Objects;

/**
 * Represents a requirement for a specific Diary.
 */
@Getter
public class DiaryReq {
    private final Diary diary;

    public DiaryReq(Diary diary) {
        this.diary = diary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DiaryReq)) return false;
        DiaryReq other = (DiaryReq) obj;
        return Objects.equals(diary, other.diary);
    }

    @Override
    public int hashCode() {
        return diary != null ? diary.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DiaryReq{diary=" + diary + '}';
    }
}
