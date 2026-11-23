package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cardmaster.app.data.local.entity.StudySessionEntity;

import java.util.List;

@Dao
public interface StudySessionDao {
    @Insert
    long insert(StudySessionEntity session);

    @Query("SELECT * FROM study_sessions WHERE userId = :userId ORDER BY endTime DESC")
    LiveData<List<StudySessionEntity>> getAllForUserLive(long userId);

    @Query("SELECT * FROM study_sessions WHERE userId = :userId AND startTime >= :startTime ORDER BY startTime ASC")
    List<StudySessionEntity> getSessionsSince(long userId, long startTime);

    @Query("SELECT * FROM study_sessions WHERE userId = :userId ORDER BY startTime ASC")
    List<StudySessionEntity> getAllSessions(long userId);

    @Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM study_sessions WHERE userId = :userId")
    LiveData<Long> getTotalDurationLive(long userId);

    @Query("SELECT COUNT(*) FROM study_sessions WHERE userId = :userId")
    LiveData<Integer> getSessionCountLive(long userId);
}
