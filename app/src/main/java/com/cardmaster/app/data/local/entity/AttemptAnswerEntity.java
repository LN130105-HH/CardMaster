package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attempt_answers")
public class AttemptAnswerEntity {
    public static final String STATUS_CORRECT = "CORRECT";
    public static final String STATUS_WRONG = "WRONG";
    public static final String STATUS_SKIPPED = "SKIPPED";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long attemptId;
    private long cardId;
    private int selectedIndex; // 0..3 or -1 if skipped
    private String answerStatus; // CORRECT, WRONG, SKIPPED
    private long answeredAt;
    private long timeSpentMs;

    public AttemptAnswerEntity(long attemptId, long cardId, int selectedIndex, String answerStatus, long answeredAt, long timeSpentMs) {
        this.attemptId = attemptId;
        this.cardId = cardId;
        this.selectedIndex = selectedIndex;
        this.answerStatus = answerStatus;
        this.answeredAt = answeredAt;
        this.timeSpentMs = timeSpentMs;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getAttemptId() { return attemptId; }
    public void setAttemptId(long attemptId) { this.attemptId = attemptId; }

    public long getCardId() { return cardId; }
    public void setCardId(long cardId) { this.cardId = cardId; }

    public int getSelectedIndex() { return selectedIndex; }
    public void setSelectedIndex(int selectedIndex) { this.selectedIndex = selectedIndex; }

    public String getAnswerStatus() { return answerStatus; }
    public void setAnswerStatus(String answerStatus) { this.answerStatus = answerStatus; }

    public long getAnsweredAt() { return answeredAt; }
    public void setAnsweredAt(long answeredAt) { this.answeredAt = answeredAt; }

    public long getTimeSpentMs() { return timeSpentMs; }
    public void setTimeSpentMs(long timeSpentMs) { this.timeSpentMs = timeSpentMs; }
}
