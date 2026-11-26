package com.cardmaster.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cardmaster.app.data.local.entity.StudySessionEntity;
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
public final class StudySessionDao_Impl implements StudySessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StudySessionEntity> __insertionAdapterOfStudySessionEntity;

  public StudySessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudySessionEntity = new EntityInsertionAdapter<StudySessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `study_sessions` (`id`,`deckId`,`userId`,`startTime`,`endTime`,`totalCards`,`memorizedCount`,`needReviewCount`,`durationSeconds`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudySessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        statement.bindLong(3, entity.getUserId());
        statement.bindLong(4, entity.getStartTime());
        statement.bindLong(5, entity.getEndTime());
        statement.bindLong(6, entity.getTotalCards());
        statement.bindLong(7, entity.getMemorizedCount());
        statement.bindLong(8, entity.getNeedReviewCount());
        statement.bindLong(9, entity.getDurationSeconds());
      }
    };
  }

  @Override
  public long insert(final StudySessionEntity session) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfStudySessionEntity.insertAndReturnId(session);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<StudySessionEntity>> getAllForUserLive(final long userId) {
    final String _sql = "SELECT * FROM study_sessions WHERE userId = ? ORDER BY endTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"study_sessions"}, false, new Callable<List<StudySessionEntity>>() {
      @Override
      @Nullable
      public List<StudySessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfTotalCards = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCards");
          final int _cursorIndexOfMemorizedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memorizedCount");
          final int _cursorIndexOfNeedReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "needReviewCount");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final List<StudySessionEntity> _result = new ArrayList<StudySessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudySessionEntity _item;
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            final int _tmpTotalCards;
            _tmpTotalCards = _cursor.getInt(_cursorIndexOfTotalCards);
            final int _tmpMemorizedCount;
            _tmpMemorizedCount = _cursor.getInt(_cursorIndexOfMemorizedCount);
            final int _tmpNeedReviewCount;
            _tmpNeedReviewCount = _cursor.getInt(_cursorIndexOfNeedReviewCount);
            final long _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
            _item = new StudySessionEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpTotalCards,_tmpMemorizedCount,_tmpNeedReviewCount,_tmpDurationSeconds);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
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
  public List<StudySessionEntity> getSessionsSince(final long userId, final long startTime) {
    final String _sql = "SELECT * FROM study_sessions WHERE userId = ? AND startTime >= ? ORDER BY startTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, startTime);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfTotalCards = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCards");
      final int _cursorIndexOfMemorizedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memorizedCount");
      final int _cursorIndexOfNeedReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "needReviewCount");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final List<StudySessionEntity> _result = new ArrayList<StudySessionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StudySessionEntity _item;
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        final int _tmpTotalCards;
        _tmpTotalCards = _cursor.getInt(_cursorIndexOfTotalCards);
        final int _tmpMemorizedCount;
        _tmpMemorizedCount = _cursor.getInt(_cursorIndexOfMemorizedCount);
        final int _tmpNeedReviewCount;
        _tmpNeedReviewCount = _cursor.getInt(_cursorIndexOfNeedReviewCount);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _item = new StudySessionEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpTotalCards,_tmpMemorizedCount,_tmpNeedReviewCount,_tmpDurationSeconds);
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
  public List<StudySessionEntity> getAllSessions(final long userId) {
    final String _sql = "SELECT * FROM study_sessions WHERE userId = ? ORDER BY startTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfTotalCards = CursorUtil.getColumnIndexOrThrow(_cursor, "totalCards");
      final int _cursorIndexOfMemorizedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "memorizedCount");
      final int _cursorIndexOfNeedReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "needReviewCount");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final List<StudySessionEntity> _result = new ArrayList<StudySessionEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StudySessionEntity _item;
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        final int _tmpTotalCards;
        _tmpTotalCards = _cursor.getInt(_cursorIndexOfTotalCards);
        final int _tmpMemorizedCount;
        _tmpMemorizedCount = _cursor.getInt(_cursorIndexOfMemorizedCount);
        final int _tmpNeedReviewCount;
        _tmpNeedReviewCount = _cursor.getInt(_cursorIndexOfNeedReviewCount);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _item = new StudySessionEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpTotalCards,_tmpMemorizedCount,_tmpNeedReviewCount,_tmpDurationSeconds);
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
  public LiveData<Long> getTotalDurationLive(final long userId) {
    final String _sql = "SELECT COALESCE(SUM(durationSeconds), 0) FROM study_sessions WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"study_sessions"}, false, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
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
  public LiveData<Integer> getSessionCountLive(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM study_sessions WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"study_sessions"}, false, new Callable<Integer>() {
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
