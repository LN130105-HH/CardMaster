package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.QuizAttemptEntity;

import java.util.List;

@Dao
public interface QuizAttemptDao {
    @Insert
    long insert(QuizAttemptEntity attempt);

    @Update
    void update(QuizAttemptEntity attempt);

    @Query("SELECT * FROM quiz_attempts WHERE id = :id LIMIT 1")
    QuizAttemptEntity getById(long id);

    @Query("SELECT * FROM quiz_attempts WHERE deckId = :deckId AND status = 'IN_PROGRESS' ORDER BY startTime DESC LIMIT 1")
    QuizAttemptEntity getInProgressAttemptForDeck(long deckId);

    @Query("SELECT * FROM quiz_attempts WHERE userId = :userId AND status = 'COMPLETED' ORDER BY endTime DESC")
    LiveData<List<QuizAttemptEntity>> getCompletedAttemptsLive(long userId);

    @Query("SELECT * FROM quiz_attempts WHERE userId = :userId AND status = 'COMPLETED' AND startTime >= :startTime ORDER BY startTime ASC")
    List<QuizAttemptEntity> getCompletedAttemptsSince(long userId, long startTime);

    @Query("SELECT * FROM quiz_attempts WHERE userId = :userId AND status = 'COMPLETED' ORDER BY startTime ASC")
    List<QuizAttemptEntity> getAllCompletedAttempts(long userId);

    @Query("SELECT MAX(scorePercentage) FROM quiz_attempts WHERE deckId = :deckId AND status = 'COMPLETED'")
    Double getBestScoreForDeck(long deckId);

    @Query("SELECT COUNT(*) FROM quiz_attempts WHERE userId = :userId AND status = 'COMPLETED'")
    LiveData<Integer> getCompletedQuizCountLive(long userId);

    @Query("SELECT AVG(scorePercentage) FROM quiz_attempts WHERE userId = :userId AND status = 'COMPLETED'")
    LiveData<Double> getAverageScoreLive(long userId);
}
