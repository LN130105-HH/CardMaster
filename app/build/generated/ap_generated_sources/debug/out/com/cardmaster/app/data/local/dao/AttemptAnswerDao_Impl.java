package com.cardmaster.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cardmaster.app.data.local.entity.AttemptAnswerEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AttemptAnswerDao_Impl implements AttemptAnswerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AttemptAnswerEntity> __insertionAdapterOfAttemptAnswerEntity;

  public AttemptAnswerDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAttemptAnswerEntity = new EntityInsertionAdapter<AttemptAnswerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attempt_answers` (`id`,`attemptId`,`cardId`,`selectedIndex`,`answerStatus`,`answeredAt`,`timeSpentMs`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final AttemptAnswerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getAttemptId());
        statement.bindLong(3, entity.getCardId());
        statement.bindLong(4, entity.getSelectedIndex());
        if (entity.getAnswerStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAnswerStatus());
        }
        statement.bindLong(6, entity.getAnsweredAt());
        statement.bindLong(7, entity.getTimeSpentMs());
      }
    };
  }

  @Override
  public long insert(final AttemptAnswerEntity answer) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfAttemptAnswerEntity.insertAndReturnId(answer);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<AttemptAnswerEntity> getAnswersForAttempt(final long attemptId) {
    final String _sql = "SELECT * FROM attempt_answers WHERE attemptId = ? ORDER BY answeredAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, attemptId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAttemptId = CursorUtil.getColumnIndexOrThrow(_cursor, "attemptId");
      final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
      final int _cursorIndexOfSelectedIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedIndex");
      final int _cursorIndexOfAnswerStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "answerStatus");
      final int _cursorIndexOfAnsweredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "answeredAt");
      final int _cursorIndexOfTimeSpentMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpentMs");
      final List<AttemptAnswerEntity> _result = new ArrayList<AttemptAnswerEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final AttemptAnswerEntity _item;
        final long _tmpAttemptId;
        _tmpAttemptId = _cursor.getLong(_cursorIndexOfAttemptId);
        final long _tmpCardId;
        _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
        final int _tmpSelectedIndex;
        _tmpSelectedIndex = _cursor.getInt(_cursorIndexOfSelectedIndex);
        final String _tmpAnswerStatus;
        if (_cursor.isNull(_cursorIndexOfAnswerStatus)) {
          _tmpAnswerStatus = null;
        } else {
          _tmpAnswerStatus = _cursor.getString(_cursorIndexOfAnswerStatus);
        }
        final long _tmpAnsweredAt;
        _tmpAnsweredAt = _cursor.getLong(_cursorIndexOfAnsweredAt);
        final long _tmpTimeSpentMs;
        _tmpTimeSpentMs = _cursor.getLong(_cursorIndexOfTimeSpentMs);
        _item = new AttemptAnswerEntity(_tmpAttemptId,_tmpCardId,_tmpSelectedIndex,_tmpAnswerStatus,_tmpAnsweredAt,_tmpTimeSpentMs);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public AttemptAnswerEntity getAnswer(final long attemptId, final long cardId) {
    final String _sql = "SELECT * FROM attempt_answers WHERE attemptId = ? AND cardId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, attemptId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, cardId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAttemptId = CursorUtil.getColumnIndexOrThrow(_cursor, "attemptId");
      final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
      final int _cursorIndexOfSelectedIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedIndex");
      final int _cursorIndexOfAnswerStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "answerStatus");
      final int _cursorIndexOfAnsweredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "answeredAt");
      final int _cursorIndexOfTimeSpentMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpentMs");
      final AttemptAnswerEntity _result;
      if (_cursor.moveToFirst()) {
        final long _tmpAttemptId;
        _tmpAttemptId = _cursor.getLong(_cursorIndexOfAttemptId);
        final long _tmpCardId;
        _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
        final int _tmpSelectedIndex;
        _tmpSelectedIndex = _cursor.getInt(_cursorIndexOfSelectedIndex);
        final String _tmpAnswerStatus;
        if (_cursor.isNull(_cursorIndexOfAnswerStatus)) {
          _tmpAnswerStatus = null;
        } else {
          _tmpAnswerStatus = _cursor.getString(_cursorIndexOfAnswerStatus);
        }
        final long _tmpAnsweredAt;
        _tmpAnsweredAt = _cursor.getLong(_cursorIndexOfAnsweredAt);
        final long _tmpTimeSpentMs;
        _tmpTimeSpentMs = _cursor.getLong(_cursorIndexOfTimeSpentMs);
        _result = new AttemptAnswerEntity(_tmpAttemptId,_tmpCardId,_tmpSelectedIndex,_tmpAnswerStatus,_tmpAnsweredAt,_tmpTimeSpentMs);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
