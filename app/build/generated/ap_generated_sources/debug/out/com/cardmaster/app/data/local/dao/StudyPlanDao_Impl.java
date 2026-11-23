package com.cardmaster.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StudyPlanDao_Impl implements StudyPlanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StudyPlanEntity> __insertionAdapterOfStudyPlanEntity;

  private final EntityDeletionOrUpdateAdapter<StudyPlanEntity> __deletionAdapterOfStudyPlanEntity;

  private final EntityDeletionOrUpdateAdapter<StudyPlanEntity> __updateAdapterOfStudyPlanEntity;

  public StudyPlanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudyPlanEntity = new EntityInsertionAdapter<StudyPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `study_plans` (`id`,`deckId`,`userId`,`scheduledTime`,`repeatMode`,`isRemind`,`calendarEventId`,`lastSyncedAt`,`syncStatus`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudyPlanEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        statement.bindLong(3, entity.getUserId());
        statement.bindLong(4, entity.getScheduledTime());
        if (entity.getRepeatMode() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRepeatMode());
        }
        final int _tmp = entity.isRemind() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getCalendarEventId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCalendarEventId());
        }
        statement.bindLong(8, entity.getLastSyncedAt());
        if (entity.getSyncStatus() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncStatus());
        }
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfStudyPlanEntity = new EntityDeletionOrUpdateAdapter<StudyPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `study_plans` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudyPlanEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfStudyPlanEntity = new EntityDeletionOrUpdateAdapter<StudyPlanEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `study_plans` SET `id` = ?,`deckId` = ?,`userId` = ?,`scheduledTime` = ?,`repeatMode` = ?,`isRemind` = ?,`calendarEventId` = ?,`lastSyncedAt` = ?,`syncStatus` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudyPlanEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        statement.bindLong(3, entity.getUserId());
        statement.bindLong(4, entity.getScheduledTime());
        if (entity.getRepeatMode() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRepeatMode());
        }
        final int _tmp = entity.isRemind() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getCalendarEventId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCalendarEventId());
        }
        statement.bindLong(8, entity.getLastSyncedAt());
        if (entity.getSyncStatus() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncStatus());
        }
        statement.bindLong(10, entity.getCreatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public long insert(final StudyPlanEntity plan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfStudyPlanEntity.insertAndReturnId(plan);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final StudyPlanEntity plan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStudyPlanEntity.handle(plan);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final StudyPlanEntity plan) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStudyPlanEntity.handle(plan);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<StudyPlanEntity>> getPlansLive(final long userId) {
    final String _sql = "SELECT * FROM study_plans WHERE userId = ? AND syncStatus != 'DELETED' ORDER BY scheduledTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"study_plans"}, false, new Callable<List<StudyPlanEntity>>() {
      @Override
      @Nullable
      public List<StudyPlanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
          final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
          final int _cursorIndexOfCalendarEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarEventId");
          final int _cursorIndexOfLastSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<StudyPlanEntity> _result = new ArrayList<StudyPlanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudyPlanEntity _item;
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final String _tmpRepeatMode;
            if (_cursor.isNull(_cursorIndexOfRepeatMode)) {
              _tmpRepeatMode = null;
            } else {
              _tmpRepeatMode = _cursor.getString(_cursorIndexOfRepeatMode);
            }
            final boolean _tmpIsRemind;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRemind);
            _tmpIsRemind = _tmp != 0;
            final String _tmpCalendarEventId;
            if (_cursor.isNull(_cursorIndexOfCalendarEventId)) {
              _tmpCalendarEventId = null;
            } else {
              _tmpCalendarEventId = _cursor.getString(_cursorIndexOfCalendarEventId);
            }
            final long _tmpLastSyncedAt;
            _tmpLastSyncedAt = _cursor.getLong(_cursorIndexOfLastSyncedAt);
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new StudyPlanEntity(_tmpDeckId,_tmpUserId,_tmpScheduledTime,_tmpRepeatMode,_tmpIsRemind,_tmpCalendarEventId,_tmpLastSyncedAt,_tmpSyncStatus,_tmpCreatedAt);
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
  public LiveData<List<StudyPlanEntity>> getUpcomingPlansLive(final long userId, final long now) {
    final String _sql = "SELECT * FROM study_plans WHERE userId = ? AND scheduledTime >= ? AND syncStatus != 'DELETED' ORDER BY scheduledTime ASC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, now);
    return __db.getInvalidationTracker().createLiveData(new String[] {"study_plans"}, false, new Callable<List<StudyPlanEntity>>() {
      @Override
      @Nullable
      public List<StudyPlanEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
          final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
          final int _cursorIndexOfCalendarEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarEventId");
          final int _cursorIndexOfLastSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<StudyPlanEntity> _result = new ArrayList<StudyPlanEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudyPlanEntity _item;
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final String _tmpRepeatMode;
            if (_cursor.isNull(_cursorIndexOfRepeatMode)) {
              _tmpRepeatMode = null;
            } else {
              _tmpRepeatMode = _cursor.getString(_cursorIndexOfRepeatMode);
            }
            final boolean _tmpIsRemind;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsRemind);
            _tmpIsRemind = _tmp != 0;
            final String _tmpCalendarEventId;
            if (_cursor.isNull(_cursorIndexOfCalendarEventId)) {
              _tmpCalendarEventId = null;
            } else {
              _tmpCalendarEventId = _cursor.getString(_cursorIndexOfCalendarEventId);
            }
            final long _tmpLastSyncedAt;
            _tmpLastSyncedAt = _cursor.getLong(_cursorIndexOfLastSyncedAt);
            final String _tmpSyncStatus;
            if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
              _tmpSyncStatus = null;
            } else {
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new StudyPlanEntity(_tmpDeckId,_tmpUserId,_tmpScheduledTime,_tmpRepeatMode,_tmpIsRemind,_tmpCalendarEventId,_tmpLastSyncedAt,_tmpSyncStatus,_tmpCreatedAt);
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
  public List<StudyPlanEntity> getPendingSyncPlans(final long userId) {
    final String _sql = "SELECT * FROM study_plans WHERE userId = ? AND syncStatus = 'PENDING_SYNC'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
      final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
      final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
      final int _cursorIndexOfCalendarEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarEventId");
      final int _cursorIndexOfLastSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAt");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final List<StudyPlanEntity> _result = new ArrayList<StudyPlanEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final StudyPlanEntity _item;
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpScheduledTime;
        _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
        final String _tmpRepeatMode;
        if (_cursor.isNull(_cursorIndexOfRepeatMode)) {
          _tmpRepeatMode = null;
        } else {
          _tmpRepeatMode = _cursor.getString(_cursorIndexOfRepeatMode);
        }
        final boolean _tmpIsRemind;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsRemind);
        _tmpIsRemind = _tmp != 0;
        final String _tmpCalendarEventId;
        if (_cursor.isNull(_cursorIndexOfCalendarEventId)) {
          _tmpCalendarEventId = null;
        } else {
          _tmpCalendarEventId = _cursor.getString(_cursorIndexOfCalendarEventId);
        }
        final long _tmpLastSyncedAt;
        _tmpLastSyncedAt = _cursor.getLong(_cursorIndexOfLastSyncedAt);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _item = new StudyPlanEntity(_tmpDeckId,_tmpUserId,_tmpScheduledTime,_tmpRepeatMode,_tmpIsRemind,_tmpCalendarEventId,_tmpLastSyncedAt,_tmpSyncStatus,_tmpCreatedAt);
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
  public StudyPlanEntity getByCalendarEventId(final String calendarEventId) {
    final String _sql = "SELECT * FROM study_plans WHERE calendarEventId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (calendarEventId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, calendarEventId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
      final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
      final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
      final int _cursorIndexOfCalendarEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarEventId");
      final int _cursorIndexOfLastSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAt");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final StudyPlanEntity _result;
      if (_cursor.moveToFirst()) {
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpScheduledTime;
        _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
        final String _tmpRepeatMode;
        if (_cursor.isNull(_cursorIndexOfRepeatMode)) {
          _tmpRepeatMode = null;
        } else {
          _tmpRepeatMode = _cursor.getString(_cursorIndexOfRepeatMode);
        }
        final boolean _tmpIsRemind;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsRemind);
        _tmpIsRemind = _tmp != 0;
        final String _tmpCalendarEventId;
        if (_cursor.isNull(_cursorIndexOfCalendarEventId)) {
          _tmpCalendarEventId = null;
        } else {
          _tmpCalendarEventId = _cursor.getString(_cursorIndexOfCalendarEventId);
        }
        final long _tmpLastSyncedAt;
        _tmpLastSyncedAt = _cursor.getLong(_cursorIndexOfLastSyncedAt);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result = new StudyPlanEntity(_tmpDeckId,_tmpUserId,_tmpScheduledTime,_tmpRepeatMode,_tmpIsRemind,_tmpCalendarEventId,_tmpLastSyncedAt,_tmpSyncStatus,_tmpCreatedAt);
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

  @Override
  public StudyPlanEntity getById(final long id) {
    final String _sql = "SELECT * FROM study_plans WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
      final int _cursorIndexOfRepeatMode = CursorUtil.getColumnIndexOrThrow(_cursor, "repeatMode");
      final int _cursorIndexOfIsRemind = CursorUtil.getColumnIndexOrThrow(_cursor, "isRemind");
      final int _cursorIndexOfCalendarEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "calendarEventId");
      final int _cursorIndexOfLastSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSyncedAt");
      final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final StudyPlanEntity _result;
      if (_cursor.moveToFirst()) {
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpScheduledTime;
        _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
        final String _tmpRepeatMode;
        if (_cursor.isNull(_cursorIndexOfRepeatMode)) {
          _tmpRepeatMode = null;
        } else {
          _tmpRepeatMode = _cursor.getString(_cursorIndexOfRepeatMode);
        }
        final boolean _tmpIsRemind;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsRemind);
        _tmpIsRemind = _tmp != 0;
        final String _tmpCalendarEventId;
        if (_cursor.isNull(_cursorIndexOfCalendarEventId)) {
          _tmpCalendarEventId = null;
        } else {
          _tmpCalendarEventId = _cursor.getString(_cursorIndexOfCalendarEventId);
        }
        final long _tmpLastSyncedAt;
        _tmpLastSyncedAt = _cursor.getLong(_cursorIndexOfLastSyncedAt);
        final String _tmpSyncStatus;
        if (_cursor.isNull(_cursorIndexOfSyncStatus)) {
          _tmpSyncStatus = null;
        } else {
          _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
        }
        final long _tmpCreatedAt;
        _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
        _result = new StudyPlanEntity(_tmpDeckId,_tmpUserId,_tmpScheduledTime,_tmpRepeatMode,_tmpIsRemind,_tmpCalendarEventId,_tmpLastSyncedAt,_tmpSyncStatus,_tmpCreatedAt);
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
