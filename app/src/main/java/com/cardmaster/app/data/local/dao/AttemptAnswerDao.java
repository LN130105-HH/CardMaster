package com.cardmaster.app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cardmaster.app.data.local.entity.AttemptAnswerEntity;

import java.util.List;

@Dao
public interface AttemptAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AttemptAnswerEntity answer);

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId ORDER BY answeredAt ASC")
    List<AttemptAnswerEntity> getAnswersForAttempt(long attemptId);

    @Query("SELECT * FROM attempt_answers WHERE attemptId = :attemptId AND cardId = :cardId LIMIT 1")
    AttemptAnswerEntity getAnswer(long attemptId, long cardId);
}
