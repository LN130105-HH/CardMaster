package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.StudyPlanEntity;

import java.util.List;

@Dao
public interface StudyPlanDao {
    @Insert
    long insert(StudyPlanEntity plan);

    @Update
    void update(StudyPlanEntity plan);

    @Delete
    void delete(StudyPlanEntity plan);

    @Query("SELECT * FROM study_plans WHERE userId = :userId AND syncStatus != 'DELETED' ORDER BY scheduledTime ASC")
    LiveData<List<StudyPlanEntity>> getPlansLive(long userId);

    @Query("SELECT * FROM study_plans WHERE userId = :userId AND scheduledTime >= :now AND syncStatus != 'DELETED' ORDER BY scheduledTime ASC LIMIT 5")
    LiveData<List<StudyPlanEntity>> getUpcomingPlansLive(long userId, long now);

    @Query("SELECT * FROM study_plans WHERE userId = :userId AND syncStatus = 'PENDING_SYNC'")
    List<StudyPlanEntity> getPendingSyncPlans(long userId);

    @Query("SELECT * FROM study_plans WHERE calendarEventId = :calendarEventId LIMIT 1")
    StudyPlanEntity getByCalendarEventId(String calendarEventId);

    @Query("SELECT * FROM study_plans WHERE id = :id LIMIT 1")
    StudyPlanEntity getById(long id);

    @Query("SELECT * FROM study_plans WHERE userId = :userId AND syncStatus = 'DELETED'")
    List<StudyPlanEntity> getDeletedSyncPlans(long userId);
}
