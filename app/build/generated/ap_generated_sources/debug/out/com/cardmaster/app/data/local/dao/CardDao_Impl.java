package com.cardmaster.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cardmaster.app.data.local.entity.CardEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CardDao_Impl implements CardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CardEntity> __insertionAdapterOfCardEntity;

  private final EntityDeletionOrUpdateAdapter<CardEntity> __deletionAdapterOfCardEntity;

  private final EntityDeletionOrUpdateAdapter<CardEntity> __updateAdapterOfCardEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCardStatus;

  public CardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCardEntity = new EntityInsertionAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cards` (`id`,`deckId`,`type`,`front`,`back`,`status`,`question`,`optionA`,`optionB`,`optionC`,`optionD`,`correctIndex`,`explanation`,`sourceFile`,`sourcePage`,`sourceExcerpt`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getFront() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFront());
        }
        if (entity.getBack() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBack());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        if (entity.getQuestion() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getQuestion());
        }
        if (entity.getOptionA() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOptionA());
        }
        if (entity.getOptionB() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getOptionB());
        }
        if (entity.getOptionC() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getOptionC());
        }
        if (entity.getOptionD() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getOptionD());
        }
        statement.bindLong(12, entity.getCorrectIndex());
        if (entity.getExplanation() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExplanation());
        }
        if (entity.getSourceFile() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getSourceFile());
        }
        if (entity.getSourcePage() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSourcePage());
        }
        if (entity.getSourceExcerpt() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getSourceExcerpt());
        }
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfCardEntity = new EntityDeletionOrUpdateAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CardEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCardEntity = new EntityDeletionOrUpdateAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cards` SET `id` = ?,`deckId` = ?,`type` = ?,`front` = ?,`back` = ?,`status` = ?,`question` = ?,`optionA` = ?,`optionB` = ?,`optionC` = ?,`optionD` = ?,`correctIndex` = ?,`explanation` = ?,`sourceFile` = ?,`sourcePage` = ?,`sourceExcerpt` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        if (entity.getType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getType());
        }
        if (entity.getFront() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getFront());
        }
        if (entity.getBack() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBack());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        if (entity.getQuestion() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getQuestion());
        }
        if (entity.getOptionA() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getOptionA());
        }
        if (entity.getOptionB() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getOptionB());
        }
        if (entity.getOptionC() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getOptionC());
        }
        if (entity.getOptionD() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getOptionD());
        }
        statement.bindLong(12, entity.getCorrectIndex());
        if (entity.getExplanation() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExplanation());
        }
        if (entity.getSourceFile() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getSourceFile());
        }
        if (entity.getSourcePage() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getSourcePage());
        }
        if (entity.getSourceExcerpt() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getSourceExcerpt());
        }
        statement.bindLong(17, entity.getCreatedAt());
        statement.bindLong(18, entity.getUpdatedAt());
        statement.bindLong(19, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateCardStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE cards SET status = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final CardEntity card) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfCardEntity.insertAndReturnId(card);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Long> insertAll(final List<CardEntity> cards) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final List<Long> _result = __insertionAdapterOfCardEntity.insertAndReturnIdsList(cards);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final CardEntity card) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCardEntity.handle(card);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final CardEntity card) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCardEntity.handle(card);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateCardStatus(final long cardId, final String status, final long updatedAt) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCardStatus.acquire();
    int _argIndex = 1;
    if (status == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, status);
    }
    _argIndex = 2;
    _stmt.bindLong(_argIndex, updatedAt);
    _argIndex = 3;
    _stmt.bindLong(_argIndex, cardId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateCardStatus.release(_stmt);
    }
  }

  @Override
  public LiveData<List<CardEntity>> getCardsByDeckLive(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards"}, false, new Callable<List<CardEntity>>() {
      @Override
      @Nullable
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
          final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            _item = new CardEntity();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            _item.setDeckId(_tmpDeckId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            _item.setType(_tmpType);
            final String _tmpFront;
            if (_cursor.isNull(_cursorIndexOfFront)) {
              _tmpFront = null;
            } else {
              _tmpFront = _cursor.getString(_cursorIndexOfFront);
            }
            _item.setFront(_tmpFront);
            final String _tmpBack;
            if (_cursor.isNull(_cursorIndexOfBack)) {
              _tmpBack = null;
            } else {
              _tmpBack = _cursor.getString(_cursorIndexOfBack);
            }
            _item.setBack(_tmpBack);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpQuestion;
            if (_cursor.isNull(_cursorIndexOfQuestion)) {
              _tmpQuestion = null;
            } else {
              _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            }
            _item.setQuestion(_tmpQuestion);
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            _item.setOptionA(_tmpOptionA);
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            _item.setOptionB(_tmpOptionB);
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            _item.setOptionC(_tmpOptionC);
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            _item.setOptionD(_tmpOptionD);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            _item.setCorrectIndex(_tmpCorrectIndex);
            final String _tmpExplanation;
            if (_cursor.isNull(_cursorIndexOfExplanation)) {
              _tmpExplanation = null;
            } else {
              _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            }
            _item.setExplanation(_tmpExplanation);
            final String _tmpSourceFile;
            if (_cursor.isNull(_cursorIndexOfSourceFile)) {
              _tmpSourceFile = null;
            } else {
              _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            }
            _item.setSourceFile(_tmpSourceFile);
            final String _tmpSourcePage;
            if (_cursor.isNull(_cursorIndexOfSourcePage)) {
              _tmpSourcePage = null;
            } else {
              _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
            }
            _item.setSourcePage(_tmpSourcePage);
            final String _tmpSourceExcerpt;
            if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
              _tmpSourceExcerpt = null;
            } else {
              _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
            }
            _item.setSourceExcerpt(_tmpSourceExcerpt);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<CardEntity> getCardsByDeck(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
      final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
      final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
      final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
      final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
      final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
      final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
      final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
      final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
      final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final CardEntity _item;
        _item = new CardEntity();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        _item.setDeckId(_tmpDeckId);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final String _tmpFront;
        if (_cursor.isNull(_cursorIndexOfFront)) {
          _tmpFront = null;
        } else {
          _tmpFront = _cursor.getString(_cursorIndexOfFront);
        }
        _item.setFront(_tmpFront);
        final String _tmpBack;
        if (_cursor.isNull(_cursorIndexOfBack)) {
          _tmpBack = null;
        } else {
          _tmpBack = _cursor.getString(_cursorIndexOfBack);
        }
        _item.setBack(_tmpBack);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _item.setQuestion(_tmpQuestion);
        final String _tmpOptionA;
        if (_cursor.isNull(_cursorIndexOfOptionA)) {
          _tmpOptionA = null;
        } else {
          _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
        }
        _item.setOptionA(_tmpOptionA);
        final String _tmpOptionB;
        if (_cursor.isNull(_cursorIndexOfOptionB)) {
          _tmpOptionB = null;
        } else {
          _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
        }
        _item.setOptionB(_tmpOptionB);
        final String _tmpOptionC;
        if (_cursor.isNull(_cursorIndexOfOptionC)) {
          _tmpOptionC = null;
        } else {
          _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
        }
        _item.setOptionC(_tmpOptionC);
        final String _tmpOptionD;
        if (_cursor.isNull(_cursorIndexOfOptionD)) {
          _tmpOptionD = null;
        } else {
          _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
        }
        _item.setOptionD(_tmpOptionD);
        final int _tmpCorrectIndex;
        _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
        _item.setCorrectIndex(_tmpCorrectIndex);
        final String _tmpExplanation;
        if (_cursor.isNull(_cursorIndexOfExplanation)) {
          _tmpExplanation = null;
        } else {
          _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
        }
        _item.setExplanation(_tmpExplanation);
        final String _tmpSourceFile;
        if (_cursor.isNull(_cursorIndexOfSourceFile)) {
          _tmpSourceFile = null;
        } else {
          _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
        }
        _item.setSourceFile(_tmpSourceFile);
        final String _tmpSourcePage;
        if (_cursor.isNull(_cursorIndexOfSourcePage)) {
          _tmpSourcePage = null;
        } else {
          _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
        }
        _item.setSourcePage(_tmpSourcePage);
        final String _tmpSourceExcerpt;
        if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
          _tmpSourceExcerpt = null;
        } else {
          _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
        }
        _item.setSourceExcerpt(_tmpSourceExcerpt);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CardEntity> getFlashcardsByDeck(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND type = 'FLASHCARD' ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
      final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
      final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
      final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
      final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
      final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
      final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
      final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
      final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
      final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final CardEntity _item;
        _item = new CardEntity();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        _item.setDeckId(_tmpDeckId);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final String _tmpFront;
        if (_cursor.isNull(_cursorIndexOfFront)) {
          _tmpFront = null;
        } else {
          _tmpFront = _cursor.getString(_cursorIndexOfFront);
        }
        _item.setFront(_tmpFront);
        final String _tmpBack;
        if (_cursor.isNull(_cursorIndexOfBack)) {
          _tmpBack = null;
        } else {
          _tmpBack = _cursor.getString(_cursorIndexOfBack);
        }
        _item.setBack(_tmpBack);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _item.setQuestion(_tmpQuestion);
        final String _tmpOptionA;
        if (_cursor.isNull(_cursorIndexOfOptionA)) {
          _tmpOptionA = null;
        } else {
          _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
        }
        _item.setOptionA(_tmpOptionA);
        final String _tmpOptionB;
        if (_cursor.isNull(_cursorIndexOfOptionB)) {
          _tmpOptionB = null;
        } else {
          _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
        }
        _item.setOptionB(_tmpOptionB);
        final String _tmpOptionC;
        if (_cursor.isNull(_cursorIndexOfOptionC)) {
          _tmpOptionC = null;
        } else {
          _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
        }
        _item.setOptionC(_tmpOptionC);
        final String _tmpOptionD;
        if (_cursor.isNull(_cursorIndexOfOptionD)) {
          _tmpOptionD = null;
        } else {
          _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
        }
        _item.setOptionD(_tmpOptionD);
        final int _tmpCorrectIndex;
        _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
        _item.setCorrectIndex(_tmpCorrectIndex);
        final String _tmpExplanation;
        if (_cursor.isNull(_cursorIndexOfExplanation)) {
          _tmpExplanation = null;
        } else {
          _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
        }
        _item.setExplanation(_tmpExplanation);
        final String _tmpSourceFile;
        if (_cursor.isNull(_cursorIndexOfSourceFile)) {
          _tmpSourceFile = null;
        } else {
          _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
        }
        _item.setSourceFile(_tmpSourceFile);
        final String _tmpSourcePage;
        if (_cursor.isNull(_cursorIndexOfSourcePage)) {
          _tmpSourcePage = null;
        } else {
          _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
        }
        _item.setSourcePage(_tmpSourcePage);
        final String _tmpSourceExcerpt;
        if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
          _tmpSourceExcerpt = null;
        } else {
          _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
        }
        _item.setSourceExcerpt(_tmpSourceExcerpt);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<CardEntity>> getFlashcardsByDeckLive(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND type = 'FLASHCARD' ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards"}, false, new Callable<List<CardEntity>>() {
      @Override
      @Nullable
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
          final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            _item = new CardEntity();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            _item.setDeckId(_tmpDeckId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            _item.setType(_tmpType);
            final String _tmpFront;
            if (_cursor.isNull(_cursorIndexOfFront)) {
              _tmpFront = null;
            } else {
              _tmpFront = _cursor.getString(_cursorIndexOfFront);
            }
            _item.setFront(_tmpFront);
            final String _tmpBack;
            if (_cursor.isNull(_cursorIndexOfBack)) {
              _tmpBack = null;
            } else {
              _tmpBack = _cursor.getString(_cursorIndexOfBack);
            }
            _item.setBack(_tmpBack);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpQuestion;
            if (_cursor.isNull(_cursorIndexOfQuestion)) {
              _tmpQuestion = null;
            } else {
              _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            }
            _item.setQuestion(_tmpQuestion);
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            _item.setOptionA(_tmpOptionA);
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            _item.setOptionB(_tmpOptionB);
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            _item.setOptionC(_tmpOptionC);
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            _item.setOptionD(_tmpOptionD);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            _item.setCorrectIndex(_tmpCorrectIndex);
            final String _tmpExplanation;
            if (_cursor.isNull(_cursorIndexOfExplanation)) {
              _tmpExplanation = null;
            } else {
              _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            }
            _item.setExplanation(_tmpExplanation);
            final String _tmpSourceFile;
            if (_cursor.isNull(_cursorIndexOfSourceFile)) {
              _tmpSourceFile = null;
            } else {
              _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            }
            _item.setSourceFile(_tmpSourceFile);
            final String _tmpSourcePage;
            if (_cursor.isNull(_cursorIndexOfSourcePage)) {
              _tmpSourcePage = null;
            } else {
              _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
            }
            _item.setSourcePage(_tmpSourcePage);
            final String _tmpSourceExcerpt;
            if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
              _tmpSourceExcerpt = null;
            } else {
              _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
            }
            _item.setSourceExcerpt(_tmpSourceExcerpt);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<CardEntity> getQuizCardsByDeck(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND type = 'QUIZ' ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
      final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
      final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
      final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
      final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
      final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
      final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
      final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
      final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
      final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final CardEntity _item;
        _item = new CardEntity();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        _item.setDeckId(_tmpDeckId);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _item.setType(_tmpType);
        final String _tmpFront;
        if (_cursor.isNull(_cursorIndexOfFront)) {
          _tmpFront = null;
        } else {
          _tmpFront = _cursor.getString(_cursorIndexOfFront);
        }
        _item.setFront(_tmpFront);
        final String _tmpBack;
        if (_cursor.isNull(_cursorIndexOfBack)) {
          _tmpBack = null;
        } else {
          _tmpBack = _cursor.getString(_cursorIndexOfBack);
        }
        _item.setBack(_tmpBack);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _item.setQuestion(_tmpQuestion);
        final String _tmpOptionA;
        if (_cursor.isNull(_cursorIndexOfOptionA)) {
          _tmpOptionA = null;
        } else {
          _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
        }
        _item.setOptionA(_tmpOptionA);
        final String _tmpOptionB;
        if (_cursor.isNull(_cursorIndexOfOptionB)) {
          _tmpOptionB = null;
        } else {
          _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
        }
        _item.setOptionB(_tmpOptionB);
        final String _tmpOptionC;
        if (_cursor.isNull(_cursorIndexOfOptionC)) {
          _tmpOptionC = null;
        } else {
          _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
        }
        _item.setOptionC(_tmpOptionC);
        final String _tmpOptionD;
        if (_cursor.isNull(_cursorIndexOfOptionD)) {
          _tmpOptionD = null;
        } else {
          _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
        }
        _item.setOptionD(_tmpOptionD);
        final int _tmpCorrectIndex;
        _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
        _item.setCorrectIndex(_tmpCorrectIndex);
        final String _tmpExplanation;
        if (_cursor.isNull(_cursorIndexOfExplanation)) {
          _tmpExplanation = null;
        } else {
          _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
        }
        _item.setExplanation(_tmpExplanation);
        final String _tmpSourceFile;
        if (_cursor.isNull(_cursorIndexOfSourceFile)) {
          _tmpSourceFile = null;
        } else {
          _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
        }
        _item.setSourceFile(_tmpSourceFile);
        final String _tmpSourcePage;
        if (_cursor.isNull(_cursorIndexOfSourcePage)) {
          _tmpSourcePage = null;
        } else {
          _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
        }
        _item.setSourcePage(_tmpSourcePage);
        final String _tmpSourceExcerpt;
        if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
          _tmpSourceExcerpt = null;
        } else {
          _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
        }
        _item.setSourceExcerpt(_tmpSourceExcerpt);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _item.setUpdatedAt(_tmpUpdatedAt);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<CardEntity>> getQuizCardsByDeckLive(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND type = 'QUIZ' ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards"}, false, new Callable<List<CardEntity>>() {
      @Override
      @Nullable
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
          final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
          final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
          final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
          final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
          final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
          final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
          final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
          final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            _item = new CardEntity();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            _item.setDeckId(_tmpDeckId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            _item.setType(_tmpType);
            final String _tmpFront;
            if (_cursor.isNull(_cursorIndexOfFront)) {
              _tmpFront = null;
            } else {
              _tmpFront = _cursor.getString(_cursorIndexOfFront);
            }
            _item.setFront(_tmpFront);
            final String _tmpBack;
            if (_cursor.isNull(_cursorIndexOfBack)) {
              _tmpBack = null;
            } else {
              _tmpBack = _cursor.getString(_cursorIndexOfBack);
            }
            _item.setBack(_tmpBack);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final String _tmpQuestion;
            if (_cursor.isNull(_cursorIndexOfQuestion)) {
              _tmpQuestion = null;
            } else {
              _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
            }
            _item.setQuestion(_tmpQuestion);
            final String _tmpOptionA;
            if (_cursor.isNull(_cursorIndexOfOptionA)) {
              _tmpOptionA = null;
            } else {
              _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
            }
            _item.setOptionA(_tmpOptionA);
            final String _tmpOptionB;
            if (_cursor.isNull(_cursorIndexOfOptionB)) {
              _tmpOptionB = null;
            } else {
              _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
            }
            _item.setOptionB(_tmpOptionB);
            final String _tmpOptionC;
            if (_cursor.isNull(_cursorIndexOfOptionC)) {
              _tmpOptionC = null;
            } else {
              _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
            }
            _item.setOptionC(_tmpOptionC);
            final String _tmpOptionD;
            if (_cursor.isNull(_cursorIndexOfOptionD)) {
              _tmpOptionD = null;
            } else {
              _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
            }
            _item.setOptionD(_tmpOptionD);
            final int _tmpCorrectIndex;
            _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
            _item.setCorrectIndex(_tmpCorrectIndex);
            final String _tmpExplanation;
            if (_cursor.isNull(_cursorIndexOfExplanation)) {
              _tmpExplanation = null;
            } else {
              _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            }
            _item.setExplanation(_tmpExplanation);
            final String _tmpSourceFile;
            if (_cursor.isNull(_cursorIndexOfSourceFile)) {
              _tmpSourceFile = null;
            } else {
              _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
            }
            _item.setSourceFile(_tmpSourceFile);
            final String _tmpSourcePage;
            if (_cursor.isNull(_cursorIndexOfSourcePage)) {
              _tmpSourcePage = null;
            } else {
              _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
            }
            _item.setSourcePage(_tmpSourcePage);
            final String _tmpSourceExcerpt;
            if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
              _tmpSourceExcerpt = null;
            } else {
              _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
            }
            _item.setSourceExcerpt(_tmpSourceExcerpt);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item.setCreatedAt(_tmpCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item.setUpdatedAt(_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public CardEntity getById(final long id) {
    final String _sql = "SELECT * FROM cards WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfFront = CursorUtil.getColumnIndexOrThrow(_cursor, "front");
      final int _cursorIndexOfBack = CursorUtil.getColumnIndexOrThrow(_cursor, "back");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfQuestion = CursorUtil.getColumnIndexOrThrow(_cursor, "question");
      final int _cursorIndexOfOptionA = CursorUtil.getColumnIndexOrThrow(_cursor, "optionA");
      final int _cursorIndexOfOptionB = CursorUtil.getColumnIndexOrThrow(_cursor, "optionB");
      final int _cursorIndexOfOptionC = CursorUtil.getColumnIndexOrThrow(_cursor, "optionC");
      final int _cursorIndexOfOptionD = CursorUtil.getColumnIndexOrThrow(_cursor, "optionD");
      final int _cursorIndexOfCorrectIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "correctIndex");
      final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
      final int _cursorIndexOfSourceFile = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceFile");
      final int _cursorIndexOfSourcePage = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcePage");
      final int _cursorIndexOfSourceExcerpt = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceExcerpt");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final CardEntity _result;
      if (_cursor.moveToFirst()) {
        _result = new CardEntity();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        _result.setDeckId(_tmpDeckId);
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        _result.setType(_tmpType);
        final String _tmpFront;
        if (_cursor.isNull(_cursorIndexOfFront)) {
          _tmpFront = null;
        } else {
          _tmpFront = _cursor.getString(_cursorIndexOfFront);
        }
        _result.setFront(_tmpFront);
        final String _tmpBack;
        if (_cursor.isNull(_cursorIndexOfBack)) {
          _tmpBack = null;
        } else {
          _tmpBack = _cursor.getString(_cursorIndexOfBack);
        }
        _result.setBack(_tmpBack);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final String _tmpQuestion;
        if (_cursor.isNull(_cursorIndexOfQuestion)) {
          _tmpQuestion = null;
        } else {
          _tmpQuestion = _cursor.getString(_cursorIndexOfQuestion);
        }
        _result.setQuestion(_tmpQuestion);
        final String _tmpOptionA;
        if (_cursor.isNull(_cursorIndexOfOptionA)) {
          _tmpOptionA = null;
        } else {
          _tmpOptionA = _cursor.getString(_cursorIndexOfOptionA);
        }
        _result.setOptionA(_tmpOptionA);
        final String _tmpOptionB;
        if (_cursor.isNull(_cursorIndexOfOptionB)) {
          _tmpOptionB = null;
        } else {
          _tmpOptionB = _cursor.getString(_cursorIndexOfOptionB);
        }
        _result.setOptionB(_tmpOptionB);
        final String _tmpOptionC;
        if (_cursor.isNull(_cursorIndexOfOptionC)) {
          _tmpOptionC = null;
        } else {
          _tmpOptionC = _cursor.getString(_cursorIndexOfOptionC);
        }
        _result.setOptionC(_tmpOptionC);
        final String _tmpOptionD;
        if (_cursor.isNull(_cursorIndexOfOptionD)) {
          _tmpOptionD = null;
        } else {
          _tmpOptionD = _cursor.getString(_cursorIndexOfOptionD);
        }
        _result.setOptionD(_tmpOptionD);
        final int _tmpCorrectIndex;
        _tmpCorrectIndex = _cursor.getInt(_cursorIndexOfCorrectIndex);
        _result.setCorrectIndex(_tmpCorrectIndex);
        final String _tmpExplanation;
        if (_cursor.isNull(_cursorIndexOfExplanation)) {
          _tmpExplanation = null;
        } else {
          _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
        }
        _result.setExplanation(_tmpExplanation);
        final String _tmpSourceFile;
        if (_cursor.isNull(_cursorIndexOfSourceFile)) {
          _tmpSourceFile = null;
        } else {
          _tmpSourceFile = _cursor.getString(_cursorIndexOfSourceFile);
        }
        _result.setSourceFile(_tmpSourceFile);
        final String _tmpSourcePage;
        if (_cursor.isNull(_cursorIndexOfSourcePage)) {
          _tmpSourcePage = null;
        } else {
          _tmpSourcePage = _cursor.getString(_cursorIndexOfSourcePage);
        }
        _result.setSourcePage(_tmpSourcePage);
        final String _tmpSourceExcerpt;
        if (_cursor.isNull(_cursorIndexOfSourceExcerpt)) {
          _tmpSourceExcerpt = null;
        } else {
          _tmpSourceExcerpt = _cursor.getString(_cursorIndexOfSourceExcerpt);
        }
        _result.setSourceExcerpt(_tmpSourceExcerpt);
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result.setCreatedAt(_tmpCreatedAt);
        final long _tmpUpdatedAt;
        _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
        _result.setUpdatedAt(_tmpUpdatedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countTotalInDeck(final long deckId) {
    final String _sql = "SELECT COUNT(*) FROM cards WHERE deckId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countFlashcardsInDeck(final long deckId) {
    final String _sql = "SELECT COUNT(*) FROM cards WHERE deckId = ? AND type = 'FLASHCARD'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countQuizCardsInDeck(final long deckId) {
    final String _sql = "SELECT COUNT(*) FROM cards WHERE deckId = ? AND type = 'QUIZ'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countMemorizedInDeck(final long deckId) {
    final String _sql = "SELECT COUNT(*) FROM cards WHERE deckId = ? AND type = 'FLASHCARD' AND status = 'MEMORIZED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<Integer> getTotalCardsForUserLive(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards",
        "decks"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getMemorizedCardsForUserLive(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = ? AND cards.type = 'FLASHCARD' AND cards.status = 'MEMORIZED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards",
        "decks"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Integer> getNeedReviewCardsForUserLive(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM cards INNER JOIN decks ON cards.deckId = decks.id WHERE decks.userId = ? AND cards.type = 'FLASHCARD' AND cards.status = 'NEED_REVIEW'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards",
        "decks"}, false, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
