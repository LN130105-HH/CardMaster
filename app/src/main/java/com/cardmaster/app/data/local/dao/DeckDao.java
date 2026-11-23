package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.DeckEntity;

import java.util.List;

@Dao
public interface DeckDao {
    @Insert
    long insert(DeckEntity deck);

    @Update
    void update(DeckEntity deck);

    @Delete
    void delete(DeckEntity deck);

    @Query("SELECT * FROM decks WHERE userId = :userId ORDER BY updatedAt DESC")
    LiveData<List<DeckEntity>> getDecksLive(long userId);

    @Query("SELECT * FROM decks WHERE userId = :userId ORDER BY updatedAt DESC")
    List<DeckEntity> getDecks(long userId);

    @Query("SELECT * FROM decks WHERE userId = :userId AND subjectId = :subjectId ORDER BY updatedAt DESC")
    LiveData<List<DeckEntity>> getDecksBySubjectLive(long userId, long subjectId);

    @Query("SELECT * FROM decks WHERE userId = :userId AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%') ORDER BY updatedAt DESC")
    LiveData<List<DeckEntity>> searchDecksLive(long userId, String query);

    @Query("SELECT * FROM decks WHERE id = :id LIMIT 1")
    DeckEntity getById(long id);

    @Query("SELECT * FROM decks WHERE id = :id LIMIT 1")
    LiveData<DeckEntity> getLiveById(long id);

    @Query("SELECT * FROM decks WHERE userId = :userId AND title = :title LIMIT 1")
    DeckEntity findByTitle(long userId, String title);

    @Query("SELECT COUNT(*) FROM decks WHERE userId = :userId")
    int getDeckCount(long userId);
}
