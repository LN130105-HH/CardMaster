package com.cardmaster.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cardmaster.app.data.local.entity.CardEntity;

import java.util.List;

@Dao
public interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CardEntity card);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<CardEntity> cards);

    @Update
    void update(CardEntity card);

    @Delete
    void delete(CardEntity card);

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY id ASC")
    LiveData<List<CardEntity>> getCardsByDeckLive(long deckId);

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY id ASC")
    List<CardEntity> getCardsByDeck(long deckId);

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND type = 'FLASHCARD' ORDER BY id ASC")
    List<CardEntity> getFlashcardsByDeck(long deckId);

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND type = 'FLASHCARD' ORDER BY id ASC")
    LiveData<List<CardEntity>> getFlashcardsByDeckLive(long deckId);

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND type = 'QUIZ' ORDER BY id ASC")
    List<CardEntity> getQuizCardsByDeck(long deckId);

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND type = 'QUIZ' ORDER BY id ASC")
    LiveData<List<CardEntity>> getQuizCardsByDeckLive(long deckId);

    @Query("SELECT * FROM cards WHERE id = :id LIMIT 1")
    CardEntity getById(long id);

    @Query("SELECT COUNT(*) FROM cards WHERE deckId = :deckId")
    int countTotalInDeck(long deckId);

    @Query("SELECT COUNT(*) FROM cards WHERE deckId = :deckId AND type = 'FLASHCARD'")
    int countFlashcardsInDeck(long deckId);

    @Query("SELECT COUNT(*) FROM cards WHERE deckId = :deckId AND type = 'QUIZ'")
    int countQuizCardsInDeck(long deckId);

    @Query("SELECT COUNT(*) FROM cards WHERE deckId = :deckId AND type = 'FLASHCARD' AND status = 'MEMORIZED'")
    int countMemorizedInDeck(long deckId);

    @Query("SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = :userId")
    LiveData<Integer> getTotalCardsForUserLive(long userId);

    @Query("SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = :userId AND cards.type = 'FLASHCARD' AND cards.status = 'MEMORIZED'")
    LiveData<Integer> getMemorizedCardsForUserLive(long userId);

    @Query("SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = :userId AND cards.type = 'FLASHCARD' AND cards.status = 'NEED_REVIEW'")
    LiveData<Integer> getNeedReviewCardsForUserLive(long userId);

    @Query("UPDATE cards SET status = :status, updatedAt = :updatedAt WHERE id = :cardId")
    void updateCardStatus(long cardId, String status, long updatedAt);
}
