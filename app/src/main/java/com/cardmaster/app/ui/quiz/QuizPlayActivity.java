package com.cardmaster.app.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.AttemptAnswerEntity;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.QuizAttemptEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityQuizPlayBinding;
import com.cardmaster.app.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class QuizPlayActivity extends AppCompatActivity {

    private ActivityQuizPlayBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;

    private long deckId = -1;
    private long attemptId = -1;
    private int targetCount = 10;
    private int timeLimitSec = 0;

    private List<CardEntity> quizCards = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isAnswered = false;

    private int correctCount = 0;
    private int wrongCount = 0;
    private int skippedCount = 0;
    private final List<AttemptAnswerEntity> recordedAnswers = new ArrayList<>();

    private QuizAttemptEntity currentAttempt;
    private CountDownTimer timer;
    private long quizStartTime = 0;
    private long questionStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        deckId = getIntent().getLongExtra("DECK_ID", -1);
        attemptId = getIntent().getLongExtra("ATTEMPT_ID", -1);
        int resumeIndex = getIntent().getIntExtra("RESUME_INDEX", 0);
        targetCount = getIntent().getIntExtra("TARGET_COUNT", 10);
        timeLimitSec = getIntent().getIntExtra("TIME_LIMIT_SEC", 0);

        quizStartTime = System.currentTimeMillis();
        currentIndex = resumeIndex;

        loadQuestions();
        setupListeners();
        setupTimer();
    }

    private void setupTimer() {
        if (timeLimitSec > 0) {
            binding.tvTimer.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(timeLimitSec * 1000L, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long sec = millisUntilFinished / 1000;
                    long m = sec / 60;
                    long s = sec % 60;
                    binding.tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", m, s));
                }

                @Override
                public void onFinish() {
                    Toast.makeText(QuizPlayActivity.this, "Đã hết thời gian làm bài!", Toast.LENGTH_SHORT).show();
                    finishQuiz();
                }
            }.start();
        }
    }

    private void loadQuestions() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<CardEntity> cards = db.cardDao().getQuizCardsByDeck(deckId);
            if (cards == null || cards.isEmpty()) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không tìm thấy câu hỏi trắc nghiệm", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            int count = Math.min(targetCount, cards.size());
            quizCards = new ArrayList<>(cards.subList(0, count));

            if (attemptId != -1) {
                currentAttempt = db.quizAttemptDao().getById(attemptId);
            }
            if (currentAttempt == null) {
                currentAttempt = new QuizAttemptEntity(deckId, sessionManager.getUserId(), quizStartTime, quizCards.size());
                long newId = db.quizAttemptDao().insert(currentAttempt);
                currentAttempt.setId(newId);
            }

            runOnUiThread(this::displayCurrentQuestion);
        });
    }

    private void displayCurrentQuestion() {
        if (currentIndex >= quizCards.size()) {
            finishQuiz();
            return;
        }

        isAnswered = false;
        questionStartTime = System.currentTimeMillis();
        CardEntity card = quizCards.get(currentIndex);

        binding.tvQuizProgressCounter.setText("Câu " + (currentIndex + 1) + " / " + quizCards.size());
        int progress = (int) (((currentIndex + 1) / (float) quizCards.size()) * 100);
        binding.progressBar.setProgress(progress);

        binding.tvQuestionText.setText(card.getQuestion());
        binding.tvOptionTextA.setText(card.getOptionA());
        binding.tvOptionTextB.setText(card.getOptionB());

        if (card.getOptionC() != null && !card.getOptionC().isEmpty()) {
            binding.cardOptionC.setVisibility(View.VISIBLE);
            binding.tvOptionTextC.setText(card.getOptionC());
        } else {
            binding.cardOptionC.setVisibility(View.GONE);
        }

        if (card.getOptionD() != null && !card.getOptionD().isEmpty()) {
            binding.cardOptionD.setVisibility(View.VISIBLE);
            binding.tvOptionTextD.setText(card.getOptionD());
        } else {
            binding.cardOptionD.setVisibility(View.GONE);
        }

        // Reset option styles
        resetOptionStyle(binding.cardOptionA, binding.tvOptionPrefixA, binding.tvOptionTextA);
        resetOptionStyle(binding.cardOptionB, binding.tvOptionPrefixB, binding.tvOptionTextB);
        resetOptionStyle(binding.cardOptionC, binding.tvOptionPrefixC, binding.tvOptionTextC);
        resetOptionStyle(binding.cardOptionD, binding.tvOptionPrefixD, binding.tvOptionTextD);

        // Hide explanation & disable next button
        binding.llExplanationSection.setVisibility(View.GONE);
        binding.flipExplanationView.resetToFront();
        binding.btnNextQuestion.setEnabled(false);
        binding.btnSkipQuestion.setEnabled(true);
    }

    private void resetOptionStyle(LinearLayout card, TextView prefix, TextView text) {
        card.setBackgroundResource(R.drawable.bg_quiz_option_default);
        prefix.setBackgroundResource(R.drawable.bg_badge_neutral);
        prefix.setTextColor(ContextCompat.getColor(this, R.color.color_text_primary));
        text.setTextColor(ContextCompat.getColor(this, R.color.color_text_primary));
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.cardOptionA.setOnClickListener(v -> selectOption(0));
        binding.cardOptionB.setOnClickListener(v -> selectOption(1));
        binding.cardOptionC.setOnClickListener(v -> selectOption(2));
        binding.cardOptionD.setOnClickListener(v -> selectOption(3));

        binding.btnFlipToSeeExplanation.setOnClickListener(v -> {
            binding.flipExplanationView.flip();
        });
        binding.cardDetailedExplanation.setOnClickListener(v -> {
            binding.flipExplanationView.flip();
        });

        binding.btnSkipQuestion.setOnClickListener(v -> skipCurrentQuestion());

        binding.btnNextQuestion.setOnClickListener(v -> {
            currentIndex++;
            if (currentAttempt != null) {
                currentAttempt.setResumedFromQuestionIndex(currentIndex);
                repository.recordQuizAttempt(currentAttempt, null, null);
            }
            displayCurrentQuestion();
        });
    }

    private void selectOption(int selectedIdx) {
        if (isAnswered) return; // Locked once answered
        isAnswered = true;

        binding.btnSkipQuestion.setEnabled(false);
        binding.btnNextQuestion.setEnabled(true);

        CardEntity card = quizCards.get(currentIndex);
        int correctIdx = card.getCorrectIndex();
        boolean isCorrect = (selectedIdx == correctIdx);

        long timeSpent = System.currentTimeMillis() - questionStartTime;

        if (isCorrect) {
            correctCount++;
            setOptionColor(selectedIdx, true);
            binding.tvResultStatus.setText("Chính xác ✓");
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.color_success));
            recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), selectedIdx, AttemptAnswerEntity.STATUS_CORRECT, System.currentTimeMillis(), timeSpent));
        } else {
            wrongCount++;
            setOptionColor(selectedIdx, false); // Wrong option in red
            setOptionColor(correctIdx, true);   // Correct option highlighted in green
            binding.tvResultStatus.setText("Chưa chính xác ✗");
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), selectedIdx, AttemptAnswerEntity.STATUS_WRONG, System.currentTimeMillis(), timeSpent));
        }

        String prefix = getPrefix(selectedIdx);
        String correctPrefix = getPrefix(correctIdx);
        binding.tvUserChoiceSummary.setText("Bạn chọn: " + prefix + " • Đáp án đúng: " + correctPrefix);

        // Explanation text and source
        String explanation = card.getExplanation();
        if (explanation == null || explanation.isEmpty()) {
            explanation = "Đáp án đúng là " + correctPrefix + ".";
        }
        binding.tvExplanationText.setText(explanation);

        if (card.getSourceFile() != null && !card.getSourceFile().isEmpty()) {
            binding.tvSourceInfo.setVisibility(View.VISIBLE);
            binding.tvSourceInfo.setText("Nguồn: " + card.getSourceFile() +
                    (card.getSourcePage() != null && !card.getSourcePage().isEmpty() ? " — " + card.getSourcePage() : ""));
        } else {
            binding.tvSourceInfo.setVisibility(View.GONE);
        }

        binding.llExplanationSection.setVisibility(View.VISIBLE);
    }

    private void setOptionColor(int index, boolean isCorrect) {
        LinearLayout card;
        TextView prefix;
        TextView text;

        if (index == 0) {
            card = binding.cardOptionA; prefix = binding.tvOptionPrefixA; text = binding.tvOptionTextA;
        } else if (index == 1) {
            card = binding.cardOptionB; prefix = binding.tvOptionPrefixB; text = binding.tvOptionTextB;
        } else if (index == 2) {
            card = binding.cardOptionC; prefix = binding.tvOptionPrefixC; text = binding.tvOptionTextC;
        } else {
            card = binding.cardOptionD; prefix = binding.tvOptionPrefixD; text = binding.tvOptionTextD;
        }

        if (isCorrect) {
            card.setBackgroundResource(R.drawable.bg_quiz_option_correct);
            prefix.setBackgroundResource(R.drawable.bg_badge_success);
            prefix.setTextColor(ContextCompat.getColor(this, R.color.color_on_success_container));
            text.setTextColor(ContextCompat.getColor(this, R.color.color_on_success_container));
        } else {
            card.setBackgroundResource(R.drawable.bg_quiz_option_wrong);
            prefix.setBackgroundResource(R.drawable.bg_badge_error);
            prefix.setTextColor(ContextCompat.getColor(this, R.color.color_on_error_container));
            text.setTextColor(ContextCompat.getColor(this, R.color.color_on_error_container));
        }
    }

    private String getPrefix(int idx) {
        if (idx == 0) return "A";
        if (idx == 1) return "B";
        if (idx == 2) return "C";
        return "D";
    }

    private void skipCurrentQuestion() {
        if (isAnswered) return;
        CardEntity card = quizCards.get(currentIndex);
        skippedCount++;
        recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), -1, AttemptAnswerEntity.STATUS_SKIPPED, System.currentTimeMillis(), 0));

        Toast.makeText(this, "Đã bỏ qua câu " + (currentIndex + 1), Toast.LENGTH_SHORT).show();
        currentIndex++;
        displayCurrentQuestion();
    }

    private void finishQuiz() {
        if (timer != null) timer.cancel();

        long endTime = System.currentTimeMillis();
        long durationSec = Math.max(1, (endTime - quizStartTime) / 1000);

        int totalQuestions = quizCards.size();
        int scoredQuestions = totalQuestions - skippedCount;
        double scorePercent = scoredQuestions > 0 ? ((correctCount / (double) scoredQuestions) * 100.0) : 0.0;

        Executors.newSingleThreadExecutor().execute(() -> {
            Double previousBest = db.quizAttemptDao().getBestScoreForDeck(deckId);
            double bestScore = (previousBest != null) ? Math.max(previousBest, scorePercent) : scorePercent;

            currentAttempt.setEndTime(endTime);
            currentAttempt.setCorrectCount(correctCount);
            currentAttempt.setWrongCount(wrongCount);
            currentAttempt.setSkippedCount(skippedCount);
            currentAttempt.setScorePercentage(scorePercent);
            currentAttempt.setBestScore(bestScore);
            currentAttempt.setStatus(QuizAttemptEntity.STATUS_COMPLETED);
            currentAttempt.setDurationSeconds(durationSec);

            repository.recordQuizAttempt(currentAttempt, recordedAnswers, () -> {
                runOnUiThread(() -> {
                    Intent intent = new Intent(QuizPlayActivity.this, QuizResultActivity.class);
                    intent.putExtra("DECK_ID", deckId);
                    intent.putExtra("ATTEMPT_ID", currentAttempt.getId());
                    intent.putExtra("TOTAL_QUESTIONS", totalQuestions);
                    intent.putExtra("CORRECT_COUNT", correctCount);
                    intent.putExtra("WRONG_COUNT", wrongCount);
                    intent.putExtra("SKIPPED_COUNT", skippedCount);
                    intent.putExtra("SCORE", scorePercent);
                    intent.putExtra("BEST_SCORE", bestScore);
                    intent.putExtra("DURATION_SEC", durationSec);
                    startActivity(intent);
                    finish();
                });
            });
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Tạm dừng bài trắc nghiệm?")
                .setMessage("Tiến độ hiện tại sẽ được lưu lại để bạn có thể tiếp tục sau.")
                .setPositiveButton("Tạm dừng & Thoát", (dialog, which) -> {
                    if (timer != null) timer.cancel();
                    if (currentAttempt != null) {
                        currentAttempt.setResumedFromQuestionIndex(currentIndex);
                        currentAttempt.setCorrectCount(correctCount);
                        currentAttempt.setWrongCount(wrongCount);
                        currentAttempt.setSkippedCount(skippedCount);
                        repository.recordQuizAttempt(currentAttempt, recordedAnswers, null);
                    }
                    finish();
                })
                .setNegativeButton("Làm tiếp", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
