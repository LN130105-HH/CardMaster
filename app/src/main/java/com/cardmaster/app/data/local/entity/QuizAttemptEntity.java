package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_attempts")
public class QuizAttemptEntity {
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_ABANDONED = "ABANDONED";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long deckId;
    private long userId;
    private long startTime;
    private long endTime;
    private int totalQuestions;
    private int correctCount;
    private int wrongCount;
    private int skippedCount;
    private double scorePercentage;
    private double bestScore;
    private String status;
    private int resumedFromQuestionIndex;
    private long durationSeconds;

    public QuizAttemptEntity(long deckId, long userId, long startTime, int totalQuestions) {
        this.deckId = deckId;
        this.userId = userId;
        this.startTime = startTime;
        this.totalQuestions = totalQuestions;
        this.status = STATUS_IN_PROGRESS;
        this.resumedFromQuestionIndex = 0;
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

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectCount() { return correctCount; }
    public void setCorrectCount(int correctCount) { this.correctCount = correctCount; }

    public int getWrongCount() { return wrongCount; }
    public void setWrongCount(int wrongCount) { this.wrongCount = wrongCount; }

    public int getSkippedCount() { return skippedCount; }
    public void setSkippedCount(int skippedCount) { this.skippedCount = skippedCount; }

    public double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(double scorePercentage) { this.scorePercentage = scorePercentage; }

    public double getBestScore() { return bestScore; }
    public void setBestScore(double bestScore) { this.bestScore = bestScore; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getResumedFromQuestionIndex() { return resumedFromQuestionIndex; }
    public void setResumedFromQuestionIndex(int resumedFromQuestionIndex) { this.resumedFromQuestionIndex = resumedFromQuestionIndex; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }
}
