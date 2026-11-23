package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cardmaster.app.data.local.entity.NotificationEntity;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    long insert(NotificationEntity notification);

    @Delete
    void delete(NotificationEntity notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    LiveData<List<NotificationEntity>> getNotificationsLive(long userId);

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    LiveData<Integer> getUnreadCountLive(long userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    void markAllAsRead(long userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    void markAsRead(long id);

    @Query("DELETE FROM notifications WHERE userId = :userId")
    void deleteAllForUser(long userId);
}
