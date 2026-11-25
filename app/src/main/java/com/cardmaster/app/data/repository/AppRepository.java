package com.cardmaster.app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cardmaster.app.util.StudyReminderScheduler;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.dao.*;
import com.cardmaster.app.data.local.entity.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private final Application application;
    private final UserDao userDao;
    private final SubjectDao subjectDao;
    private final DeckDao deckDao;
    private final CardDao cardDao;
    private final StudySessionDao studySessionDao;
    private final QuizAttemptDao quizAttemptDao;
    private final AttemptAnswerDao attemptAnswerDao;
    private final StudyPlanDao studyPlanDao;
    private final NotificationDao notificationDao;

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public AppRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        subjectDao = db.subjectDao();
        deckDao = db.deckDao();
        cardDao = db.cardDao();
        studySessionDao = db.studySessionDao();
        quizAttemptDao = db.quizAttemptDao();
        attemptAnswerDao = db.attemptAnswerDao();
        studyPlanDao = db.studyPlanDao();
        notificationDao = db.notificationDao();
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    // --- User ---
    public LiveData<UserEntity> getUserLive(long userId) {
        return userDao.getLiveById(userId);
    }

    public void updateUser(UserEntity user, Runnable onDone) {
        executor.execute(() -> {
            userDao.update(user);
            if (onDone != null) onDone.run();
        });
    }

    // --- Subjects ---
    public LiveData<List<SubjectEntity>> getSubjectsLive(long userId) {
        return subjectDao.getSubjectsLive(userId);
    }

    public void insertSubject(SubjectEntity subject, Runnable onDone) {
        executor.execute(() -> {
            subjectDao.insert(subject);
            if (onDone != null) onDone.run();
        });
    }

    public void updateSubject(SubjectEntity subject, Runnable onDone) {
        executor.execute(() -> {
            subjectDao.update(subject);
            if (onDone != null) onDone.run();
        });
    }

    public void deleteSubject(SubjectEntity subject, Runnable onDone) {
        executor.execute(() -> {
            subjectDao.delete(subject);
            if (onDone != null) onDone.run();
        });
    }

    // --- Decks ---
    public LiveData<List<DeckEntity>> getDecksLive(long userId) {
        return deckDao.getDecksLive(userId);
    }

    public LiveData<List<DeckEntity>> getDecksBySubjectLive(long userId, long subjectId) {
        return deckDao.getDecksBySubjectLive(userId, subjectId);
    }

    public LiveData<List<DeckEntity>> searchDecksLive(long userId, String query) {
        return deckDao.searchDecksLive(userId, query);
    }

    public LiveData<DeckEntity> getDeckLive(long deckId) {
        return deckDao.getLiveById(deckId);
    }

    public void insertDeck(DeckEntity deck, Runnable onDone) {
        executor.execute(() -> {
            deckDao.insert(deck);
            if (onDone != null) onDone.run();
        });
    }

    public void updateDeck(DeckEntity deck, Runnable onDone) {
        executor.execute(() -> {
            deckDao.update(deck);
            if (onDone != null) onDone.run();
        });
    }

    public void deleteDeck(DeckEntity deck, Runnable onDone) {
        executor.execute(() -> {
            deckDao.delete(deck);
            if (onDone != null) onDone.run();
        });
    }

    // --- Cards ---
    public LiveData<List<CardEntity>> getCardsByDeckLive(long deckId) {
        return cardDao.getCardsByDeckLive(deckId);
    }

    public LiveData<List<CardEntity>> getFlashcardsByDeckLive(long deckId) {
        return cardDao.getFlashcardsByDeckLive(deckId);
    }

    public LiveData<List<CardEntity>> getQuizCardsByDeckLive(long deckId) {
        return cardDao.getQuizCardsByDeckLive(deckId);
    }

    public void insertCard(CardEntity card, Runnable onDone) {
        executor.execute(() -> {
            cardDao.insert(card);
            if (onDone != null) onDone.run();
        });
    }

    public void insertCards(List<CardEntity> cards, Runnable onDone) {
        executor.execute(() -> {
            cardDao.insertAll(cards);
            if (onDone != null) onDone.run();
        });
    }

    public void updateCard(CardEntity card, Runnable onDone) {
        executor.execute(() -> {
            cardDao.update(card);
            if (onDone != null) onDone.run();
        });
    }

    public void deleteCard(CardEntity card, Runnable onDone) {
        executor.execute(() -> {
            cardDao.delete(card);
            if (onDone != null) onDone.run();
        });
    }

    public void updateCardStatus(long cardId, String status) {
        executor.execute(() -> cardDao.updateCardStatus(cardId, status, System.currentTimeMillis()));
    }

    public LiveData<Integer> getTotalCardsLive(long userId) {
        return cardDao.getTotalCardsForUserLive(userId);
    }

    public LiveData<Integer> getMemorizedCardsLive(long userId) {
        return cardDao.getMemorizedCardsForUserLive(userId);
    }

    public LiveData<Integer> getNeedReviewCardsLive(long userId) {
        return cardDao.getNeedReviewCardsForUserLive(userId);
    }

    // --- Study Sessions ---
    public void recordStudySession(StudySessionEntity session) {
        executor.execute(() -> studySessionDao.insert(session));
    }

    public LiveData<List<StudySessionEntity>> getStudySessionsLive(long userId) {
        return studySessionDao.getAllForUserLive(userId);
    }

    public LiveData<Long> getTotalStudyDurationLive(long userId) {
        return studySessionDao.getTotalDurationLive(userId);
    }

    public LiveData<Integer> getStudySessionCountLive(long userId) {
        return studySessionDao.getSessionCountLive(userId);
    }

    // --- Quiz Attempts ---
    public void recordQuizAttempt(QuizAttemptEntity attempt, List<AttemptAnswerEntity> answers, Runnable onDone) {
        executor.execute(() -> {
            if (attempt.getId() == 0) {
                long id = quizAttemptDao.insert(attempt);
                attempt.setId(id);
            } else {
                quizAttemptDao.update(attempt);
            }
            if (answers != null) {
                for (AttemptAnswerEntity a : answers) {
                    a.setAttemptId(attempt.getId());
                    attemptAnswerDao.insert(a);
                }
            }
            if (onDone != null) onDone.run();
        });
    }

    public LiveData<List<QuizAttemptEntity>> getCompletedAttemptsLive(long userId) {
        return quizAttemptDao.getCompletedAttemptsLive(userId);
    }

    public LiveData<Integer> getCompletedQuizCountLive(long userId) {
        return quizAttemptDao.getCompletedQuizCountLive(userId);
    }

    public LiveData<Double> getAverageScoreLive(long userId) {
        return quizAttemptDao.getAverageScoreLive(userId);
    }

    // --- Study Plans ---
    public LiveData<List<StudyPlanEntity>> getPlansLive(long userId) {
        return studyPlanDao.getPlansLive(userId);
    }

    public LiveData<List<StudyPlanEntity>> getUpcomingPlansLive(long userId) {
        return studyPlanDao.getUpcomingPlansLive(userId, System.currentTimeMillis());
    }

    public void insertPlan(StudyPlanEntity plan, Runnable onDone) {
        executor.execute(() -> {
            long newId = studyPlanDao.insert(plan);
            plan.setId(newId);
            StudyReminderScheduler.scheduleOrUpdate(application, plan);
            if (onDone != null) onDone.run();
        });
    }

    public void updatePlan(StudyPlanEntity plan, Runnable onDone) {
        executor.execute(() -> {
            studyPlanDao.update(plan);
            StudyReminderScheduler.scheduleOrUpdate(application, plan);
            if (onDone != null) onDone.run();
        });
    }

    public void deletePlan(StudyPlanEntity plan, Runnable onDone) {
        executor.execute(() -> {
            // Hủy thông báo nhắc nhở trên thiết bị ngay lập tức
            StudyReminderScheduler.cancel(application, plan.getId());

            if (plan.getCalendarEventId() != null && !plan.getCalendarEventId().trim().isEmpty()) {
                // Đánh dấu DELETED để lần đồng bộ Google Calendar tiếp theo xóa sự kiện trên cloud
                plan.setSyncStatus(StudyPlanEntity.SYNC_DELETED);
                studyPlanDao.update(plan);
            } else {
                studyPlanDao.delete(plan);
            }
            if (onDone != null) onDone.run();
        });
    }

    // --- Notifications ---
    public LiveData<List<NotificationEntity>> getNotificationsLive(long userId) {
        return notificationDao.getNotificationsLive(userId);
    }

    public LiveData<Integer> getUnreadNotificationCountLive(long userId) {
        return notificationDao.getUnreadCountLive(userId);
    }

    public void markNotificationRead(long notifId) {
        executor.execute(() -> notificationDao.markAsRead(notifId));
    }

    public void markAllNotificationsRead(long userId) {
        executor.execute(() -> notificationDao.markAllAsRead(userId));
    }

    public void deleteNotification(NotificationEntity notif) {
        executor.execute(() -> notificationDao.delete(notif));
    }

    public void deleteAllNotifications(long userId, Runnable onDone) {
        executor.execute(() -> {
            notificationDao.deleteAllForUser(userId);
            if (onDone != null) onDone.run();
        });
    }
}
