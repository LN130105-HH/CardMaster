package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.SubjectEntity;

import java.util.List;

@Dao
public interface SubjectDao {
    @Insert
    long insert(SubjectEntity subject);

    @Update
    void update(SubjectEntity subject);

    @Delete
    void delete(SubjectEntity subject);

    @Query("SELECT * FROM subjects WHERE userId = :userId ORDER BY name ASC")
    LiveData<List<SubjectEntity>> getSubjectsLive(long userId);

    @Query("SELECT * FROM subjects WHERE userId = :userId ORDER BY name ASC")
    List<SubjectEntity> getSubjects(long userId);

    @Query("SELECT * FROM subjects WHERE id = :id LIMIT 1")
    SubjectEntity getById(long id);

    @Query("SELECT * FROM subjects WHERE userId = :userId AND name = :name LIMIT 1")
    SubjectEntity findByName(long userId, String name);
}
