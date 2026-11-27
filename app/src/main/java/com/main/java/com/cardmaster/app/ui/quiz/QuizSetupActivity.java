package com.main.java.com.cardmaster.app.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.QuizAttemptEntity;
import com.cardmaster.app.databinding.ActivityQuizSetupBinding;
import com.cardmaster.app.ui.quiz.QuizPlayActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class QuizSetupActivity extends AppCompatActivity {

    private ActivityQuizSetupBinding binding;
    private AppDatabase db;
    private long deckId = -1;
    private QuizAttemptEntity inProgressAttempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(this);
        deckId = getIntent().getLongExtra("DECK_ID", -1);

        checkDeckAndInProgressAttempt();
        setupListeners();
    }

    private void checkDeckAndInProgressAttempt() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<CardEntity> quizCards = db.cardDao().getQuizCardsByDeck(deckId);
            if (quizCards == null || quizCards.isEmpty()) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Bộ thẻ này chưa có câu hỏi trắc nghiệm (Quiz Card) nào!", Toast.LENGTH_LONG).show();
                    finish();
                });
                return;
            }

            inProgressAttempt = db.quizAttemptDao().getInProgressAttemptForDeck(deckId);
            runOnUiThread(() -> {
                if (inProgressAttempt != null) {
                    binding.cardResumeBanner.setVisibility(View.VISIBLE);
                    binding.tvResumeDescription.setText("Bạn đang làm dở tại câu " + (inProgressAttempt.getResumedFromQuestionIndex() + 1) + "/" + inProgressAttempt.getTotalQuestions() + ".");
                } else {
                    binding.cardResumeBanner.setVisibility(View.GONE);
                }
            });
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnResumeQuiz.setOnClickListener(v -> {
            if (inProgressAttempt != null) {
                Intent intent = new Intent(this, com.cardmaster.app.ui.quiz.QuizPlayActivity.class);
                intent.putExtra("DECK_ID", deckId);
                intent.putExtra("ATTEMPT_ID", inProgressAttempt.getId());
                intent.putExtra("RESUME_INDEX", inProgressAttempt.getResumedFromQuestionIndex());
                startActivity(intent);
                finish();
            }
        });

        binding.btnRestartFromBeginning.setOnClickListener(v -> {
            if (inProgressAttempt != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    inProgressAttempt.setStatus(QuizAttemptEntity.STATUS_ABANDONED);
                    db.quizAttemptDao().update(inProgressAttempt);
                    runOnUiThread(() -> {
                        binding.cardResumeBanner.setVisibility(View.GONE);
                        inProgressAttempt = null;
                        startNewQuiz();
                    });
                });
            } else {
                startNewQuiz();
            }
        });

        binding.btnStartQuiz.setOnClickListener(v -> startNewQuiz());
    }

    private void startNewQuiz() {
        int count = 10;
        if (binding.rbCount5.isChecked()) count = 5;
        else if (binding.rbCount20.isChecked()) count = 20;
        else if (binding.rbCountAll.isChecked()) count = 9999;

        int timeLimitSeconds = 0;
        if (binding.rbTime5Min.isChecked()) timeLimitSeconds = 5 * 60;
        else if (binding.rbTime10Min.isChecked()) timeLimitSeconds = 10 * 60;

        // MỤC 4: hỏi người dùng có muốn trộn không trước khi bắt đầu làm bài
        final int finalCount = count;
        final int finalTimeLimit = timeLimitSeconds;
        final boolean[] shuffle = {true, true};   // [0] = trộn câu hỏi, [1] = trộn đáp án A, B, C, D
        new AlertDialog.Builder(this)
                .setTitle("Tuỳ chọn trộn")
                .setMultiChoiceItems(
                        new String[]{"Trộn thứ tự câu hỏi", "Trộn thứ tự đáp án A, B, C, D"},
                        shuffle,
                        (dialog, which, isChecked) -> shuffle[which] = isChecked)
                .setPositiveButton("Bắt đầu", (dialog, which) ->
                        launchQuiz(finalCount, finalTimeLimit, shuffle[0], shuffle[1]))
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void launchQuiz(int count, int timeLimitSeconds, boolean shuffleQuestions, boolean shuffleOptions) {
        Intent intent = new Intent(this, QuizPlayActivity.class);
        intent.putExtra("DECK_ID", deckId);
        intent.putExtra("TARGET_COUNT", count);
        intent.putExtra("TIME_LIMIT_SEC", timeLimitSeconds);
        intent.putExtra("SHUFFLE_QUESTIONS", shuffleQuestions);
        intent.putExtra("SHUFFLE_OPTIONS", shuffleOptions);
        startActivity(intent);
        finish();
    }
}
