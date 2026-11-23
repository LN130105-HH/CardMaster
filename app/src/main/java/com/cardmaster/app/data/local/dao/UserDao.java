package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.UserEntity;

@Dao
public interface UserDao {
    @Insert
    long insert(UserEntity user);

    @Update
    void update(UserEntity user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    UserEntity findByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    UserEntity getById(long userId);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    LiveData<UserEntity> getLiveById(long userId);
}
