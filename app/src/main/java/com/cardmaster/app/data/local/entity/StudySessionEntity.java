package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_sessions")
public class StudySessionEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long deckId;
    private long userId;
    private long startTime;
    private long endTime;
    private int totalCards;
    private int memorizedCount;
    private int needReviewCount;
    private long durationSeconds;

    public StudySessionEntity(long deckId, long userId, long startTime, long endTime, int totalCards, int memorizedCount, int needReviewCount, long durationSeconds) {
        this.deckId = deckId;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCards = totalCards;
        this.memorizedCount = memorizedCount;
        this.needReviewCount = needReviewCount;
        this.durationSeconds = durationSeconds;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getDeckId() { return deckId; }
    public void setDeckId(long deckId) { this.deckId = deckId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public int getTotalCards() { return totalCards; }
    public void setTotalCards(int totalCards) { this.totalCards = totalCards; }

    public int getMemorizedCount() { return memorizedCount; }
    public void setMemorizedCount(int memorizedCount) { this.memorizedCount = memorizedCount; }

    public int getNeedReviewCount() { return needReviewCount; }
    public void setNeedReviewCount(int needReviewCount) { this.needReviewCount = needReviewCount; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
}
