package com.cardmaster.app.ui.study;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.StudySessionEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityFlashcardStudyBinding;
import com.cardmaster.app.util.DateUtils;
import com.cardmaster.app.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FlashcardStudyActivity extends AppCompatActivity {

    private ActivityFlashcardStudyBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;

    private long deckId = -1;
    private List<CardEntity> originalFlashcards = new ArrayList<>();   // dữ liệu gốc từ DB
    private List<CardEntity> flashcards = new ArrayList<>();           // thứ tự đang học
    private int currentIndex = 0;
    private int memorizedCount = 0;
    private int needReviewCount = 0;
    private long sessionStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFlashcardStudyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        deckId = getIntent().getLongExtra("DECK_ID", -1);
        sessionStartTime = System.currentTimeMillis();

        loadFlashcards();
        setupListeners();
    }

    private void loadFlashcards() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<CardEntity> cards = db.cardDao().getFlashcardsByDeck(deckId);
            runOnUiThread(() -> {
                if (cards == null || cards.isEmpty()) {
                    Toast.makeText(this, "Bộ thẻ này chưa có flashcard nào để học!", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                originalFlashcards = cards;   // giữ nguyên dữ liệu gốc từ DB
                showStartOrderDialog();       // hỏi trộn hay không rồi mới bắt đầu học
            });
        });
    }

    // Trộn thứ tự Flashcard trước khi bắt đầu học
    private void showStartOrderDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Bắt đầu học")
                .setMessage("Bạn muốn học các thẻ theo thứ tự nào?")
                .setCancelable(false)
                .setPositiveButton("Trộn thẻ", (dialog, which) -> startStudy(true))
                .setNegativeButton("Theo thứ tự", (dialog, which) -> startStudy(false))
                .setNeutralButton("Huỷ", (dialog, which) -> finish())
                .show();
    }

    private void startStudy(boolean shuffle) {
        // không sửa originalFlashcards và không ghi gì vào Database
        flashcards = shuffle ? ShuffleUtils.shuffled(originalFlashcards) : new ArrayList<>(originalFlashcards);
        currentIndex = 0;
        sessionStartTime = System.currentTimeMillis();   // không tính thời gian chờ chọn vào thời gian học
        displayCurrentCard();
    }

    private void displayCurrentCard() {
        if (currentIndex >= flashcards.size()) {
            finishSession();
            return;
        }

        CardEntity card = flashcards.get(currentIndex);
        binding.tvFrontText.setText(card.getFront());
        binding.tvBackText.setText(card.getBack());

        binding.flipCardView.resetToFront();
        binding.llFlipAction.setVisibility(View.VISIBLE);
        binding.llPostFlipActions.setVisibility(View.GONE);

        int progress = (int) (((currentIndex + 1) / (float) flashcards.size()) * 100);
        binding.progressBar.setProgress(progress);
        binding.tvProgressCounter.setText((currentIndex + 1) + " / " + flashcards.size());
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.flipCardView.setOnFlipListener(isFront -> {
            if (isFront) {
                binding.llFlipAction.setVisibility(View.VISIBLE);
                binding.llPostFlipActions.setVisibility(View.GONE);
            } else {
                binding.llFlipAction.setVisibility(View.GONE);
                binding.llPostFlipActions.setVisibility(View.VISIBLE);
            }
        });

        binding.btnFlipCard.setOnClickListener(v -> flipCard());
        binding.cardFront.setOnClickListener(v -> flipCard());
        binding.cardBack.setOnClickListener(v -> flipCard());

        binding.btnMemorized.setOnClickListener(v -> {
            CardEntity card = flashcards.get(currentIndex);
            memorizedCount++;
            repository.updateCardStatus(card.getId(), CardEntity.STATUS_MEMORIZED);
            currentIndex++;
            displayCurrentCard();
        });

        binding.btnNotMemorized.setOnClickListener(v -> {
            CardEntity card = flashcards.get(currentIndex);
            needReviewCount++;
            repository.updateCardStatus(card.getId(), CardEntity.STATUS_NEED_REVIEW);
            currentIndex++;
            displayCurrentCard();
        });
    }

    private void flipCard() {
        binding.flipCardView.flip();
    }

    private void finishSession() {
        long endTime = System.currentTimeMillis();
        long durationSec = Math.max(1, (endTime - sessionStartTime) / 1000);

        StudySessionEntity session = new StudySessionEntity(
                deckId,
                sessionManager.getUserId(),
                sessionStartTime,
                endTime,
                flashcards.size(),
                memorizedCount,
                needReviewCount,
                durationSec
        );
        repository.recordStudySession(session);

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành phiên học!")
                .setMessage("Bạn đã hoàn thành phiên ôn tập flashcard:\n\n" +
                        "• Tổng số thẻ: " + flashcards.size() + "\n" +
                        "• Đã nhớ: " + memorizedCount + " thẻ\n" +
                        "• Cần ôn lại: " + needReviewCount + " thẻ\n" +
                        "• Thời gian học: " + DateUtils.formatDuration(durationSec))
                .setPositiveButton("Xong", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        if (currentIndex < flashcards.size() && flashcards.size() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Dừng phiên học?")
                    .setMessage("Bạn có chắc chắn muốn rời phiên học? Tiến độ các thẻ đã hoàn thành sẽ được lưu.")
                    .setPositiveButton("Rời đi", (dialog, which) -> finish())
                    .setNegativeButton("Học tiếp", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
