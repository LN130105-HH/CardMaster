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
import com.cardmaster.app.data.local.entity.QuizAttemptEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class QuizAttemptDao_Impl implements QuizAttemptDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuizAttemptEntity> __insertionAdapterOfQuizAttemptEntity;

  private final EntityDeletionOrUpdateAdapter<QuizAttemptEntity> __updateAdapterOfQuizAttemptEntity;

  public QuizAttemptDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuizAttemptEntity = new EntityInsertionAdapter<QuizAttemptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `quiz_attempts` (`id`,`deckId`,`userId`,`startTime`,`endTime`,`totalQuestions`,`correctCount`,`wrongCount`,`skippedCount`,`scorePercentage`,`bestScore`,`status`,`resumedFromQuestionIndex`,`durationSeconds`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final QuizAttemptEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        statement.bindLong(3, entity.getUserId());
        statement.bindLong(4, entity.getStartTime());
        statement.bindLong(5, entity.getEndTime());
        statement.bindLong(6, entity.getTotalQuestions());
        statement.bindLong(7, entity.getCorrectCount());
        statement.bindLong(8, entity.getWrongCount());
        statement.bindLong(9, entity.getSkippedCount());
        statement.bindDouble(10, entity.getScorePercentage());
        statement.bindDouble(11, entity.getBestScore());
        if (entity.getStatus() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStatus());
        }
        statement.bindLong(13, entity.getResumedFromQuestionIndex());
        statement.bindLong(14, entity.getDurationSeconds());
      }
    };
    this.__updateAdapterOfQuizAttemptEntity = new EntityDeletionOrUpdateAdapter<QuizAttemptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `quiz_attempts` SET `id` = ?,`deckId` = ?,`userId` = ?,`startTime` = ?,`endTime` = ?,`totalQuestions` = ?,`correctCount` = ?,`wrongCount` = ?,`skippedCount` = ?,`scorePercentage` = ?,`bestScore` = ?,`status` = ?,`resumedFromQuestionIndex` = ?,`durationSeconds` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final QuizAttemptEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        statement.bindLong(3, entity.getUserId());
        statement.bindLong(4, entity.getStartTime());
        statement.bindLong(5, entity.getEndTime());
        statement.bindLong(6, entity.getTotalQuestions());
        statement.bindLong(7, entity.getCorrectCount());
        statement.bindLong(8, entity.getWrongCount());
        statement.bindLong(9, entity.getSkippedCount());
        statement.bindDouble(10, entity.getScorePercentage());
        statement.bindDouble(11, entity.getBestScore());
        if (entity.getStatus() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getStatus());
        }
        statement.bindLong(13, entity.getResumedFromQuestionIndex());
        statement.bindLong(14, entity.getDurationSeconds());
        statement.bindLong(15, entity.getId());
      }
    };
  }

  @Override
  public long insert(final QuizAttemptEntity attempt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfQuizAttemptEntity.insertAndReturnId(attempt);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final QuizAttemptEntity attempt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfQuizAttemptEntity.handle(attempt);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public QuizAttemptEntity getById(final long id) {
    final String _sql = "SELECT * FROM quiz_attempts WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfCorrectCount = CursorUtil.getColumnIndexOrThrow(_cursor, "correctCount");
      final int _cursorIndexOfWrongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongCount");
      final int _cursorIndexOfSkippedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skippedCount");
      final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
      final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfResumedFromQuestionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "resumedFromQuestionIndex");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final QuizAttemptEntity _result;
      if (_cursor.moveToFirst()) {
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        _result = new QuizAttemptEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpTotalQuestions);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _result.setEndTime(_tmpEndTime);
        final int _tmpCorrectCount;
        _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
        _result.setCorrectCount(_tmpCorrectCount);
        final int _tmpWrongCount;
        _tmpWrongCount = _cursor.getInt(_cursorIndexOfWrongCount);
        _result.setWrongCount(_tmpWrongCount);
        final int _tmpSkippedCount;
        _tmpSkippedCount = _cursor.getInt(_cursorIndexOfSkippedCount);
        _result.setSkippedCount(_tmpSkippedCount);
        final double _tmpScorePercentage;
        _tmpScorePercentage = _cursor.getDouble(_cursorIndexOfScorePercentage);
        _result.setScorePercentage(_tmpScorePercentage);
        final double _tmpBestScore;
        _tmpBestScore = _cursor.getDouble(_cursorIndexOfBestScore);
        _result.setBestScore(_tmpBestScore);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final int _tmpResumedFromQuestionIndex;
        _tmpResumedFromQuestionIndex = _cursor.getInt(_cursorIndexOfResumedFromQuestionIndex);
        _result.setResumedFromQuestionIndex(_tmpResumedFromQuestionIndex);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _result.setDurationSeconds(_tmpDurationSeconds);
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
  public QuizAttemptEntity getInProgressAttemptForDeck(final long deckId) {
    final String _sql = "SELECT * FROM quiz_attempts WHERE deckId = ? AND status = 'IN_PROGRESS' ORDER BY startTime DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfCorrectCount = CursorUtil.getColumnIndexOrThrow(_cursor, "correctCount");
      final int _cursorIndexOfWrongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongCount");
      final int _cursorIndexOfSkippedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skippedCount");
      final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
      final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfResumedFromQuestionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "resumedFromQuestionIndex");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final QuizAttemptEntity _result;
      if (_cursor.moveToFirst()) {
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        _result = new QuizAttemptEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpTotalQuestions);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _result.setEndTime(_tmpEndTime);
        final int _tmpCorrectCount;
        _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
        _result.setCorrectCount(_tmpCorrectCount);
        final int _tmpWrongCount;
        _tmpWrongCount = _cursor.getInt(_cursorIndexOfWrongCount);
        _result.setWrongCount(_tmpWrongCount);
        final int _tmpSkippedCount;
        _tmpSkippedCount = _cursor.getInt(_cursorIndexOfSkippedCount);
        _result.setSkippedCount(_tmpSkippedCount);
        final double _tmpScorePercentage;
        _tmpScorePercentage = _cursor.getDouble(_cursorIndexOfScorePercentage);
        _result.setScorePercentage(_tmpScorePercentage);
        final double _tmpBestScore;
        _tmpBestScore = _cursor.getDouble(_cursorIndexOfBestScore);
        _result.setBestScore(_tmpBestScore);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.setStatus(_tmpStatus);
        final int _tmpResumedFromQuestionIndex;
        _tmpResumedFromQuestionIndex = _cursor.getInt(_cursorIndexOfResumedFromQuestionIndex);
        _result.setResumedFromQuestionIndex(_tmpResumedFromQuestionIndex);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _result.setDurationSeconds(_tmpDurationSeconds);
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
  public LiveData<List<QuizAttemptEntity>> getCompletedAttemptsLive(final long userId) {
    final String _sql = "SELECT * FROM quiz_attempts WHERE userId = ? AND status = 'COMPLETED' ORDER BY endTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"quiz_attempts"}, false, new Callable<List<QuizAttemptEntity>>() {
      @Override
      @Nullable
      public List<QuizAttemptEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
          final int _cursorIndexOfCorrectCount = CursorUtil.getColumnIndexOrThrow(_cursor, "correctCount");
          final int _cursorIndexOfWrongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongCount");
          final int _cursorIndexOfSkippedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skippedCount");
          final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
          final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfResumedFromQuestionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "resumedFromQuestionIndex");
          final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
          final List<QuizAttemptEntity> _result = new ArrayList<QuizAttemptEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final QuizAttemptEntity _item;
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final int _tmpTotalQuestions;
            _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
            _item = new QuizAttemptEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpTotalQuestions);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpEndTime;
            _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            _item.setEndTime(_tmpEndTime);
            final int _tmpCorrectCount;
            _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
            _item.setCorrectCount(_tmpCorrectCount);
            final int _tmpWrongCount;
            _tmpWrongCount = _cursor.getInt(_cursorIndexOfWrongCount);
            _item.setWrongCount(_tmpWrongCount);
            final int _tmpSkippedCount;
            _tmpSkippedCount = _cursor.getInt(_cursorIndexOfSkippedCount);
            _item.setSkippedCount(_tmpSkippedCount);
            final double _tmpScorePercentage;
            _tmpScorePercentage = _cursor.getDouble(_cursorIndexOfScorePercentage);
            _item.setScorePercentage(_tmpScorePercentage);
            final double _tmpBestScore;
            _tmpBestScore = _cursor.getDouble(_cursorIndexOfBestScore);
            _item.setBestScore(_tmpBestScore);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            _item.setStatus(_tmpStatus);
            final int _tmpResumedFromQuestionIndex;
            _tmpResumedFromQuestionIndex = _cursor.getInt(_cursorIndexOfResumedFromQuestionIndex);
            _item.setResumedFromQuestionIndex(_tmpResumedFromQuestionIndex);
            final long _tmpDurationSeconds;
            _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
            _item.setDurationSeconds(_tmpDurationSeconds);
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
  public List<QuizAttemptEntity> getCompletedAttemptsSince(final long userId,
      final long startTime) {
    final String _sql = "SELECT * FROM quiz_attempts WHERE userId = ? AND status = 'COMPLETED' AND startTime >= ? ORDER BY startTime ASC";
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
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfCorrectCount = CursorUtil.getColumnIndexOrThrow(_cursor, "correctCount");
      final int _cursorIndexOfWrongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongCount");
      final int _cursorIndexOfSkippedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skippedCount");
      final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
      final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfResumedFromQuestionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "resumedFromQuestionIndex");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final List<QuizAttemptEntity> _result = new ArrayList<QuizAttemptEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final QuizAttemptEntity _item;
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        _item = new QuizAttemptEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpTotalQuestions);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final int _tmpCorrectCount;
        _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
        _item.setCorrectCount(_tmpCorrectCount);
        final int _tmpWrongCount;
        _tmpWrongCount = _cursor.getInt(_cursorIndexOfWrongCount);
        _item.setWrongCount(_tmpWrongCount);
        final int _tmpSkippedCount;
        _tmpSkippedCount = _cursor.getInt(_cursorIndexOfSkippedCount);
        _item.setSkippedCount(_tmpSkippedCount);
        final double _tmpScorePercentage;
        _tmpScorePercentage = _cursor.getDouble(_cursorIndexOfScorePercentage);
        _item.setScorePercentage(_tmpScorePercentage);
        final double _tmpBestScore;
        _tmpBestScore = _cursor.getDouble(_cursorIndexOfBestScore);
        _item.setBestScore(_tmpBestScore);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final int _tmpResumedFromQuestionIndex;
        _tmpResumedFromQuestionIndex = _cursor.getInt(_cursorIndexOfResumedFromQuestionIndex);
        _item.setResumedFromQuestionIndex(_tmpResumedFromQuestionIndex);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _item.setDurationSeconds(_tmpDurationSeconds);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<QuizAttemptEntity> getAllCompletedAttempts(final long userId) {
    final String _sql = "SELECT * FROM quiz_attempts WHERE userId = ? AND status = 'COMPLETED' ORDER BY startTime ASC";
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
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfCorrectCount = CursorUtil.getColumnIndexOrThrow(_cursor, "correctCount");
      final int _cursorIndexOfWrongCount = CursorUtil.getColumnIndexOrThrow(_cursor, "wrongCount");
      final int _cursorIndexOfSkippedCount = CursorUtil.getColumnIndexOrThrow(_cursor, "skippedCount");
      final int _cursorIndexOfScorePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "scorePercentage");
      final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfResumedFromQuestionIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "resumedFromQuestionIndex");
      final int _cursorIndexOfDurationSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSeconds");
      final List<QuizAttemptEntity> _result = new ArrayList<QuizAttemptEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final QuizAttemptEntity _item;
        final long _tmpDeckId;
        _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
        final long _tmpUserId;
        _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpStartTime;
        _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        _item = new QuizAttemptEntity(_tmpDeckId,_tmpUserId,_tmpStartTime,_tmpTotalQuestions);
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpEndTime;
        _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.setEndTime(_tmpEndTime);
        final int _tmpCorrectCount;
        _tmpCorrectCount = _cursor.getInt(_cursorIndexOfCorrectCount);
        _item.setCorrectCount(_tmpCorrectCount);
        final int _tmpWrongCount;
        _tmpWrongCount = _cursor.getInt(_cursorIndexOfWrongCount);
        _item.setWrongCount(_tmpWrongCount);
        final int _tmpSkippedCount;
        _tmpSkippedCount = _cursor.getInt(_cursorIndexOfSkippedCount);
        _item.setSkippedCount(_tmpSkippedCount);
        final double _tmpScorePercentage;
        _tmpScorePercentage = _cursor.getDouble(_cursorIndexOfScorePercentage);
        _item.setScorePercentage(_tmpScorePercentage);
        final double _tmpBestScore;
        _tmpBestScore = _cursor.getDouble(_cursorIndexOfBestScore);
        _item.setBestScore(_tmpBestScore);
        final String _tmpStatus;
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _tmpStatus = null;
        } else {
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
        }
        _item.setStatus(_tmpStatus);
        final int _tmpResumedFromQuestionIndex;
        _tmpResumedFromQuestionIndex = _cursor.getInt(_cursorIndexOfResumedFromQuestionIndex);
        _item.setResumedFromQuestionIndex(_tmpResumedFromQuestionIndex);
        final long _tmpDurationSeconds;
        _tmpDurationSeconds = _cursor.getLong(_cursorIndexOfDurationSeconds);
        _item.setDurationSeconds(_tmpDurationSeconds);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Double getBestScoreForDeck(final long deckId) {
    final String _sql = "SELECT MAX(scorePercentage) FROM quiz_attempts WHERE deckId = ? AND status = 'COMPLETED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final Double _result;
      if (_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
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
  public LiveData<Integer> getCompletedQuizCountLive(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM quiz_attempts WHERE userId = ? AND status = 'COMPLETED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"quiz_attempts"}, false, new Callable<Integer>() {
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
  public LiveData<Double> getAverageScoreLive(final long userId) {
    final String _sql = "SELECT AVG(scorePercentage) FROM quiz_attempts WHERE userId = ? AND status = 'COMPLETED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"quiz_attempts"}, false, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
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
