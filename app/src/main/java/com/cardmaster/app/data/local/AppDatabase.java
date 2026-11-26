package com.cardmaster.app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cardmaster.app.data.local.dao.*;
import com.cardmaster.app.data.local.entity.*;

import java.util.concurrent.Executors;

@Database(entities = {
        UserEntity.class,
        SubjectEntity.class,
        DeckEntity.class,
        CardEntity.class,
        StudySessionEntity.class,
        QuizAttemptEntity.class,
        AttemptAnswerEntity.class,
        StudyPlanEntity.class,
        NotificationEntity.class
}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract SubjectDao subjectDao();
    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();
    public abstract StudySessionDao studySessionDao();
    public abstract QuizAttemptDao quizAttemptDao();
    public abstract AttemptAnswerDao attemptAnswerDao();
    public abstract StudyPlanDao studyPlanDao();
    public abstract NotificationDao notificationDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cardmaster_database"
                    ).fallbackToDestructiveMigration()
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(() -> {
                                populateInitialData(getInstance(context));
                            });
                        }
                    }).build();
                }
            }
        }
        return INSTANCE;
    }

    private static void populateInitialData(AppDatabase db) {
        long now = System.currentTimeMillis();

        // 1. Default demo user
        UserEntity user = new UserEntity("Hoàng Vũ Lê Huy", "user@cardmaster.app", "123456", null, now);
        user.setGender("Nam");
        user.setDateOfBirth("15/08/2003");
        user.setFirebaseUid("firebase_user_demo_01");
        long userId = db.userDao().insert(user);

        // 2. Default Subjects
        SubjectEntity sub1 = new SubjectEntity(userId, "Lập trình di động", "Các kiến thức Android, Activity, Room, UI và Kotlin DSL", now);
        SubjectEntity sub2 = new SubjectEntity(userId, "Cơ sở dữ liệu", "Kiến thức SQLite, SQL và NoSQL", now);
        SubjectEntity sub3 = new SubjectEntity(userId, "Trí tuệ nhân tạo", "Mô hình ngôn ngữ lớn, Prompt Engineering và Gemini API", now);

        long sub1Id = db.subjectDao().insert(sub1);
        long sub2Id = db.subjectDao().insert(sub2);
        long sub3Id = db.subjectDao().insert(sub3);

        // 3. Default Decks
        DeckEntity deck1 = new DeckEntity(sub1Id, userId, "Room & SQLite trong Android", "Ôn tập kiến thức Room persistence library và SQLite", now, now);
        DeckEntity deck2 = new DeckEntity(sub1Id, userId, "Activity Lifecycle & Navigation", "Vòng đời Activity, Fragment và điều hướng", now, now);
        DeckEntity deck3 = new DeckEntity(sub3Id, userId, "Gemini API & AI Models", "Kiến thức về Gemini API và sinh nội dung học tập", now, now);

        long deck1Id = db.deckDao().insert(deck1);
        long deck2Id = db.deckDao().insert(deck2);
        long deck3Id = db.deckDao().insert(deck3);

        // 4. Cards for Deck 1 (Mix of Flashcard and Quiz Card)
        // Flashcard 1
        db.cardDao().insert(CardEntity.createFlashcard(deck1Id,
                "Room Persistence Library là gì?",
                "Room là một thư viện cung cấp một lớp trừu tượng (abstraction layer) trên SQLite, giúp truy cập cơ sở dữ liệu mạnh mẽ hơn trên Android."));

        // Flashcard 2
        db.cardDao().insert(CardEntity.createFlashcard(deck1Id,
                "Ba thành phần chính của Room gồm những gì?",
                "1. Database Class: điểm truy cập chính.\n2. Data Access Objects (DAO): chứa các phương thức truy vấn.\n3. Entities: biểu diễn các bảng trong database."));

        // Quiz Card 1
        db.cardDao().insert(CardEntity.createQuizCard(deck1Id,
                "Trong Room Database, annotation nào được dùng để đánh dấu một lớp là thực thể ánh xạ vào bảng SQLite?",
                "@Database", "@Entity", "@Table", "@Dao",
                1, // @Entity is index 1
                "Annotation @Entity được sử dụng để đánh dấu một Java/Kotlin class là một bảng trong SQLite Database.",
                "Android_Room_Guide.pdf", "Trang 12", "Mục 2.1 - Khởi tạo Entity"));

        // Quiz Card 2
        db.cardDao().insert(CardEntity.createQuizCard(deck1Id,
                "Room có hỗ trợ thực hiện truy vấn trực tiếp trên Main Thread theo mặc định không?",
                "Có, luôn luôn cho phép",
                "Không, Room cấm truy vấn trên Main Thread để tránh ANR",
                "Chỉ cho phép với lệnh SELECT",
                "Tùy thuộc vào phiên bản Android",
                1, // index 1
                "Mặc định Room không cho phép chạy query trên Main UI Thread để ngăn chặn tình trạng lag ứng dụng hoặc lỗi Application Not Responding (ANR).",
                "Android_Room_Guide.pdf", "Trang 25", "Mục 4 - Threading"));

        // 5. Initial Notifications
        db.notificationDao().insert(new NotificationEntity(userId,
                "Chào mừng đến với CardMaster!",
                "Hãy bắt đầu tạo môn học, bộ thẻ và học tập ngay hôm nay.",
                NotificationEntity.TYPE_SYSTEM, false, now));

        db.notificationDao().insert(new NotificationEntity(userId,
                "Lập lịch ôn tập",
                "Bạn có thể thiết lập lịch ôn tập bộ thẻ và đồng bộ với Google Calendar trong chi tiết bộ thẻ.",
                NotificationEntity.TYPE_REMINDER, false, now));

        // 6. Initial Study Plan
        db.studyPlanDao().insert(new StudyPlanEntity(
                deck1Id, userId, now + (3600 * 1000 * 24),
                StudyPlanEntity.REPEAT_DAILY, true, null, 0,
                StudyPlanEntity.SYNC_PENDING, now
        ));
    }
}
