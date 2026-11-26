package com.cardmaster.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.cardmaster.app.data.local.dao.AttemptAnswerDao;
import com.cardmaster.app.data.local.dao.AttemptAnswerDao_Impl;
import com.cardmaster.app.data.local.dao.CardDao;
import com.cardmaster.app.data.local.dao.CardDao_Impl;
import com.cardmaster.app.data.local.dao.DeckDao;
import com.cardmaster.app.data.local.dao.DeckDao_Impl;
import com.cardmaster.app.data.local.dao.NotificationDao;
import com.cardmaster.app.data.local.dao.NotificationDao_Impl;
import com.cardmaster.app.data.local.dao.QuizAttemptDao;
import com.cardmaster.app.data.local.dao.QuizAttemptDao_Impl;
import com.cardmaster.app.data.local.dao.StudyPlanDao;
import com.cardmaster.app.data.local.dao.StudyPlanDao_Impl;
import com.cardmaster.app.data.local.dao.StudySessionDao;
import com.cardmaster.app.data.local.dao.StudySessionDao_Impl;
import com.cardmaster.app.data.local.dao.SubjectDao;
import com.cardmaster.app.data.local.dao.SubjectDao_Impl;
import com.cardmaster.app.data.local.dao.UserDao;
import com.cardmaster.app.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile SubjectDao _subjectDao;

  private volatile DeckDao _deckDao;

  private volatile CardDao _cardDao;

  private volatile StudySessionDao _studySessionDao;

  private volatile QuizAttemptDao _quizAttemptDao;

  private volatile AttemptAnswerDao _attemptAnswerDao;

  private volatile StudyPlanDao _studyPlanDao;

  private volatile NotificationDao _notificationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `firebaseUid` TEXT, `fullName` TEXT, `email` TEXT, `passwordHash` TEXT, `avatarUri` TEXT, `gender` TEXT, `dateOfBirth` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `subjects` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT, `description` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `decks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `subjectId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT, `description` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `type` TEXT, `front` TEXT, `back` TEXT, `status` TEXT, `question` TEXT, `optionA` TEXT, `optionB` TEXT, `optionC` TEXT, `optionD` TEXT, `correctIndex` INTEGER NOT NULL, `explanation` TEXT, `sourceFile` TEXT, `sourcePage` TEXT, `sourceExcerpt` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `study_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `totalCards` INTEGER NOT NULL, `memorizedCount` INTEGER NOT NULL, `needReviewCount` INTEGER NOT NULL, `durationSeconds` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_attempts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `totalQuestions` INTEGER NOT NULL, `correctCount` INTEGER NOT NULL, `wrongCount` INTEGER NOT NULL, `skippedCount` INTEGER NOT NULL, `scorePercentage` REAL NOT NULL, `bestScore` REAL NOT NULL, `status` TEXT, `resumedFromQuestionIndex` INTEGER NOT NULL, `durationSeconds` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attempt_answers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `attemptId` INTEGER NOT NULL, `cardId` INTEGER NOT NULL, `selectedIndex` INTEGER NOT NULL, `answerStatus` TEXT, `answeredAt` INTEGER NOT NULL, `timeSpentMs` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `study_plans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `scheduledTime` INTEGER NOT NULL, `repeatMode` TEXT, `isRemind` INTEGER NOT NULL, `calendarEventId` TEXT, `lastSyncedAt` INTEGER NOT NULL, `syncStatus` TEXT, `lastModifiedAt` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notifications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `title` TEXT, `message` TEXT, `type` TEXT, `isRead` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '5c6e3e86d63f18ff9ed414d6e4393b33')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `subjects`");
        db.execSQL("DROP TABLE IF EXISTS `decks`");
        db.execSQL("DROP TABLE IF EXISTS `cards`");
        db.execSQL("DROP TABLE IF EXISTS `study_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `quiz_attempts`");
        db.execSQL("DROP TABLE IF EXISTS `attempt_answers`");
        db.execSQL("DROP TABLE IF EXISTS `study_plans`");
        db.execSQL("DROP TABLE IF EXISTS `notifications`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(9);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("firebaseUid", new TableInfo.Column("firebaseUid", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("passwordHash", new TableInfo.Column("passwordHash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("avatarUri", new TableInfo.Column("avatarUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("gender", new TableInfo.Column("gender", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("dateOfBirth", new TableInfo.Column("dateOfBirth", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.cardmaster.app.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsSubjects = new HashMap<String, TableInfo.Column>(5);
        _columnsSubjects.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSubjects.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSubjects = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSubjects = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSubjects = new TableInfo("subjects", _columnsSubjects, _foreignKeysSubjects, _indicesSubjects);
        final TableInfo _existingSubjects = TableInfo.read(db, "subjects");
        if (!_infoSubjects.equals(_existingSubjects)) {
          return new RoomOpenHelper.ValidationResult(false, "subjects(com.cardmaster.app.data.local.entity.SubjectEntity).\n"
                  + " Expected:\n" + _infoSubjects + "\n"
                  + " Found:\n" + _existingSubjects);
        }
        final HashMap<String, TableInfo.Column> _columnsDecks = new HashMap<String, TableInfo.Column>(7);
        _columnsDecks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("subjectId", new TableInfo.Column("subjectId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDecks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDecks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDecks = new TableInfo("decks", _columnsDecks, _foreignKeysDecks, _indicesDecks);
        final TableInfo _existingDecks = TableInfo.read(db, "decks");
        if (!_infoDecks.equals(_existingDecks)) {
          return new RoomOpenHelper.ValidationResult(false, "decks(com.cardmaster.app.data.local.entity.DeckEntity).\n"
                  + " Expected:\n" + _infoDecks + "\n"
                  + " Found:\n" + _existingDecks);
        }
        final HashMap<String, TableInfo.Column> _columnsCards = new HashMap<String, TableInfo.Column>(18);
        _columnsCards.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("deckId", new TableInfo.Column("deckId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("front", new TableInfo.Column("front", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("back", new TableInfo.Column("back", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("question", new TableInfo.Column("question", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("optionA", new TableInfo.Column("optionA", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("optionB", new TableInfo.Column("optionB", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("optionC", new TableInfo.Column("optionC", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("optionD", new TableInfo.Column("optionD", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("correctIndex", new TableInfo.Column("correctIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("explanation", new TableInfo.Column("explanation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("sourceFile", new TableInfo.Column("sourceFile", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("sourcePage", new TableInfo.Column("sourcePage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("sourceExcerpt", new TableInfo.Column("sourceExcerpt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCards = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCards = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCards = new TableInfo("cards", _columnsCards, _foreignKeysCards, _indicesCards);
        final TableInfo _existingCards = TableInfo.read(db, "cards");
        if (!_infoCards.equals(_existingCards)) {
          return new RoomOpenHelper.ValidationResult(false, "cards(com.cardmaster.app.data.local.entity.CardEntity).\n"
                  + " Expected:\n" + _infoCards + "\n"
                  + " Found:\n" + _existingCards);
        }
        final HashMap<String, TableInfo.Column> _columnsStudySessions = new HashMap<String, TableInfo.Column>(9);
        _columnsStudySessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("deckId", new TableInfo.Column("deckId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("totalCards", new TableInfo.Column("totalCards", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("memorizedCount", new TableInfo.Column("memorizedCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("needReviewCount", new TableInfo.Column("needReviewCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudySessions.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudySessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudySessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudySessions = new TableInfo("study_sessions", _columnsStudySessions, _foreignKeysStudySessions, _indicesStudySessions);
        final TableInfo _existingStudySessions = TableInfo.read(db, "study_sessions");
        if (!_infoStudySessions.equals(_existingStudySessions)) {
          return new RoomOpenHelper.ValidationResult(false, "study_sessions(com.cardmaster.app.data.local.entity.StudySessionEntity).\n"
                  + " Expected:\n" + _infoStudySessions + "\n"
                  + " Found:\n" + _existingStudySessions);
        }
        final HashMap<String, TableInfo.Column> _columnsQuizAttempts = new HashMap<String, TableInfo.Column>(14);
        _columnsQuizAttempts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("deckId", new TableInfo.Column("deckId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("totalQuestions", new TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("correctCount", new TableInfo.Column("correctCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("wrongCount", new TableInfo.Column("wrongCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("skippedCount", new TableInfo.Column("skippedCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("scorePercentage", new TableInfo.Column("scorePercentage", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("bestScore", new TableInfo.Column("bestScore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("resumedFromQuestionIndex", new TableInfo.Column("resumedFromQuestionIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizAttempts.put("durationSeconds", new TableInfo.Column("durationSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizAttempts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizAttempts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuizAttempts = new TableInfo("quiz_attempts", _columnsQuizAttempts, _foreignKeysQuizAttempts, _indicesQuizAttempts);
        final TableInfo _existingQuizAttempts = TableInfo.read(db, "quiz_attempts");
        if (!_infoQuizAttempts.equals(_existingQuizAttempts)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_attempts(com.cardmaster.app.data.local.entity.QuizAttemptEntity).\n"
                  + " Expected:\n" + _infoQuizAttempts + "\n"
                  + " Found:\n" + _existingQuizAttempts);
        }
        final HashMap<String, TableInfo.Column> _columnsAttemptAnswers = new HashMap<String, TableInfo.Column>(7);
        _columnsAttemptAnswers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("attemptId", new TableInfo.Column("attemptId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("selectedIndex", new TableInfo.Column("selectedIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("answerStatus", new TableInfo.Column("answerStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("answeredAt", new TableInfo.Column("answeredAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttemptAnswers.put("timeSpentMs", new TableInfo.Column("timeSpentMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttemptAnswers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttemptAnswers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttemptAnswers = new TableInfo("attempt_answers", _columnsAttemptAnswers, _foreignKeysAttemptAnswers, _indicesAttemptAnswers);
        final TableInfo _existingAttemptAnswers = TableInfo.read(db, "attempt_answers");
        if (!_infoAttemptAnswers.equals(_existingAttemptAnswers)) {
          return new RoomOpenHelper.ValidationResult(false, "attempt_answers(com.cardmaster.app.data.local.entity.AttemptAnswerEntity).\n"
                  + " Expected:\n" + _infoAttemptAnswers + "\n"
                  + " Found:\n" + _existingAttemptAnswers);
        }
        final HashMap<String, TableInfo.Column> _columnsStudyPlans = new HashMap<String, TableInfo.Column>(11);
        _columnsStudyPlans.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("deckId", new TableInfo.Column("deckId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("scheduledTime", new TableInfo.Column("scheduledTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("repeatMode", new TableInfo.Column("repeatMode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("isRemind", new TableInfo.Column("isRemind", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("calendarEventId", new TableInfo.Column("calendarEventId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("lastSyncedAt", new TableInfo.Column("lastSyncedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("lastModifiedAt", new TableInfo.Column("lastModifiedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudyPlans.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudyPlans = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudyPlans = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudyPlans = new TableInfo("study_plans", _columnsStudyPlans, _foreignKeysStudyPlans, _indicesStudyPlans);
        final TableInfo _existingStudyPlans = TableInfo.read(db, "study_plans");
        if (!_infoStudyPlans.equals(_existingStudyPlans)) {
          return new RoomOpenHelper.ValidationResult(false, "study_plans(com.cardmaster.app.data.local.entity.StudyPlanEntity).\n"
                  + " Expected:\n" + _infoStudyPlans + "\n"
                  + " Found:\n" + _existingStudyPlans);
        }
        final HashMap<String, TableInfo.Column> _columnsNotifications = new HashMap<String, TableInfo.Column>(7);
        _columnsNotifications.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotifications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotifications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotifications = new TableInfo("notifications", _columnsNotifications, _foreignKeysNotifications, _indicesNotifications);
        final TableInfo _existingNotifications = TableInfo.read(db, "notifications");
        if (!_infoNotifications.equals(_existingNotifications)) {
          return new RoomOpenHelper.ValidationResult(false, "notifications(com.cardmaster.app.data.local.entity.NotificationEntity).\n"
                  + " Expected:\n" + _infoNotifications + "\n"
                  + " Found:\n" + _existingNotifications);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "5c6e3e86d63f18ff9ed414d6e4393b33", "87532a86f4fcae34919867d94af37704");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","subjects","decks","cards","study_sessions","quiz_attempts","attempt_answers","study_plans","notifications");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `subjects`");
      _db.execSQL("DELETE FROM `decks`");
      _db.execSQL("DELETE FROM `cards`");
      _db.execSQL("DELETE FROM `study_sessions`");
      _db.execSQL("DELETE FROM `quiz_attempts`");
      _db.execSQL("DELETE FROM `attempt_answers`");
      _db.execSQL("DELETE FROM `study_plans`");
      _db.execSQL("DELETE FROM `notifications`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SubjectDao.class, SubjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeckDao.class, DeckDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CardDao.class, CardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudySessionDao.class, StudySessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuizAttemptDao.class, QuizAttemptDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AttemptAnswerDao.class, AttemptAnswerDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudyPlanDao.class, StudyPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationDao.class, NotificationDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public SubjectDao subjectDao() {
    if (_subjectDao != null) {
      return _subjectDao;
    } else {
      synchronized(this) {
        if(_subjectDao == null) {
          _subjectDao = new SubjectDao_Impl(this);
        }
        return _subjectDao;
      }
    }
  }

  @Override
  public DeckDao deckDao() {
    if (_deckDao != null) {
      return _deckDao;
    } else {
      synchronized(this) {
        if(_deckDao == null) {
          _deckDao = new DeckDao_Impl(this);
        }
        return _deckDao;
      }
    }
  }

  @Override
  public CardDao cardDao() {
    if (_cardDao != null) {
      return _cardDao;
    } else {
      synchronized(this) {
        if(_cardDao == null) {
          _cardDao = new CardDao_Impl(this);
        }
        return _cardDao;
      }
    }
  }

  @Override
  public StudySessionDao studySessionDao() {
    if (_studySessionDao != null) {
      return _studySessionDao;
    } else {
      synchronized(this) {
        if(_studySessionDao == null) {
          _studySessionDao = new StudySessionDao_Impl(this);
        }
        return _studySessionDao;
      }
    }
  }

  @Override
  public QuizAttemptDao quizAttemptDao() {
    if (_quizAttemptDao != null) {
      return _quizAttemptDao;
    } else {
      synchronized(this) {
        if(_quizAttemptDao == null) {
          _quizAttemptDao = new QuizAttemptDao_Impl(this);
        }
        return _quizAttemptDao;
      }
    }
  }

  @Override
  public AttemptAnswerDao attemptAnswerDao() {
    if (_attemptAnswerDao != null) {
      return _attemptAnswerDao;
    } else {
      synchronized(this) {
        if(_attemptAnswerDao == null) {
          _attemptAnswerDao = new AttemptAnswerDao_Impl(this);
        }
        return _attemptAnswerDao;
      }
    }
  }

  @Override
  public StudyPlanDao studyPlanDao() {
    if (_studyPlanDao != null) {
      return _studyPlanDao;
    } else {
      synchronized(this) {
        if(_studyPlanDao == null) {
          _studyPlanDao = new StudyPlanDao_Impl(this);
        }
        return _studyPlanDao;
      }
    }
  }

  @Override
  public NotificationDao notificationDao() {
    if (_notificationDao != null) {
      return _notificationDao;
    } else {
      synchronized(this) {
        if(_notificationDao == null) {
          _notificationDao = new NotificationDao_Impl(this);
        }
        return _notificationDao;
      }
    }
  }
}
