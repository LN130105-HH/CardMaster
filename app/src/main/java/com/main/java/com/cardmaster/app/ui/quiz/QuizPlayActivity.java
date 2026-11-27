package com.main.java.com.cardmaster.app.ui.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.cardmaster.app.ui.quiz.QuizResultActivity;
import com.cardmaster.app.ui.study.QuizItem;
import com.cardmaster.app.ui.study.ShuffleUtils;
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

    // Danh sách câu hỏi của phiên làm bài (đã trộn nếu người dùng chọn).
    // Đây là bản sao trong bộ nhớ, dữ liệu gốc trong Database không bị thay đổi.
    private List<QuizItem> quizItems = new ArrayList<>();
    private boolean shuffleQuestions = false;
    private boolean shuffleOptions = false;

    // 4 ô đáp án A, B, C, D trên màn hình (vị trí 0..3)
    private LinearLayout[] optionCards;
    private TextView[] optionPrefixes;
    private TextView[] optionTexts;

    private OnBackPressedCallback backCallback;
    private AlertDialog exitDialog;
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

        optionCards = new LinearLayout[]{binding.cardOptionA, binding.cardOptionB, binding.cardOptionC, binding.cardOptionD};
        optionPrefixes = new TextView[]{binding.tvOptionPrefixA, binding.tvOptionPrefixB, binding.tvOptionPrefixC, binding.tvOptionPrefixD};
        optionTexts = new TextView[]{binding.tvOptionTextA, binding.tvOptionTextB, binding.tvOptionTextC, binding.tvOptionTextD};

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        deckId = getIntent().getLongExtra("DECK_ID", -1);
        attemptId = getIntent().getLongExtra("ATTEMPT_ID", -1);
        int resumeIndex = getIntent().getIntExtra("RESUME_INDEX", 0);
        targetCount = getIntent().getIntExtra("TARGET_COUNT", 10);
        timeLimitSec = getIntent().getIntExtra("TIME_LIMIT_SEC", 0);
        shuffleQuestions = getIntent().getBooleanExtra("SHUFFLE_QUESTIONS", false);
        shuffleOptions = getIntent().getBooleanExtra("SHUFFLE_OPTIONS", false);

        quizStartTime = System.currentTimeMillis();
        currentIndex = resumeIndex;

        setupBackHandling();
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

            // Làm tiếp bài cũ (ATTEMPT_ID != -1): giữ nguyên thứ tự gốc để RESUME_INDEX còn đúng.
            boolean resuming = attemptId != -1;
            // Trộn câu hỏi TRƯỚC rồi mới cắt lấy N câu => mỗi lần làm là một bộ N câu ngẫu nhiên.
            // buildQuizSession làm việc trên bản sao, không đổi list/Entity lấy từ DB.
            List<QuizItem> session = ShuffleUtils.buildQuizSession(
                    cards, !resuming && shuffleQuestions, !resuming && shuffleOptions);
            int count = Math.min(targetCount, session.size());
            quizItems = new ArrayList<>(session.subList(0, count));

            if (attemptId != -1) {
                currentAttempt = db.quizAttemptDao().getById(attemptId);
            }
            if (currentAttempt == null) {
                currentAttempt = new QuizAttemptEntity(deckId, sessionManager.getUserId(), quizStartTime, quizItems.size());
                long newId = db.quizAttemptDao().insert(currentAttempt);
                currentAttempt.setId(newId);
            }

            runOnUiThread(this::displayCurrentQuestion);
        });
    }

    private void displayCurrentQuestion() {
        if (currentIndex >= quizItems.size()) {
            finishQuiz();
            return;
        }

        isAnswered = false;
        questionStartTime = System.currentTimeMillis();
        QuizItem item = quizItems.get(currentIndex);
        CardEntity card = item.getSource();

        binding.tvQuizProgressCounter.setText("Câu " + (currentIndex + 1) + " / " + quizItems.size());
        int progress = (int) (((currentIndex + 1) / (float) quizItems.size()) * 100);
        binding.progressBar.setProgress(progress);

        binding.tvQuestionText.setText(card.getQuestion());

        // Hiển thị đáp án THEO THỨ TỰ ĐÃ TRỘN: vị trí i (0..3) = A..D trên màn hình.
        // Ẩn các ô thừa (câu chỉ có 2-3 đáp án) và reset lại màu của từng ô.
        for (int i = 0; i < optionCards.length; i++) {
            if (i < item.getOptionCount()) {
                optionCards[i].setVisibility(View.VISIBLE);
                optionTexts[i].setText(item.getOption(i));
            } else {
                optionCards[i].setVisibility(View.GONE);
            }
            resetOptionStyle(optionCards[i], optionPrefixes[i], optionTexts[i]);
        }

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
        binding.btnBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

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

        QuizItem item = quizItems.get(currentIndex);
        CardEntity card = item.getSource();
        // selectedIdx và correctIdx đều là vị trí TRÊN MÀN HÌNH (sau khi trộn).
        // Chấm điểm bằng item.isCorrect(), KHÔNG so với card.getCorrectIndex() gốc.
        int correctIdx = item.getCorrectIndex();
        boolean isCorrect = item.isCorrect(selectedIdx);
        // Lưu vào DB theo thứ tự GỐC (0=A..3=D) để dữ liệu thống kê không phụ thuộc vào lần trộn.
        int selectedOriginal = item.getOriginalIndex(selectedIdx);

        long timeSpent = System.currentTimeMillis() - questionStartTime;

        if (isCorrect) {
            correctCount++;
            setOptionColor(selectedIdx, true);
            binding.tvResultStatus.setText("Chính xác ✓");
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.color_success));
            recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), selectedOriginal, AttemptAnswerEntity.STATUS_CORRECT, System.currentTimeMillis(), timeSpent));
        } else {
            wrongCount++;
            setOptionColor(selectedIdx, false); // Wrong option in red
            if (correctIdx >= 0) setOptionColor(correctIdx, true);   // Correct option highlighted in green
            binding.tvResultStatus.setText("Chưa chính xác ✗");
            binding.tvResultStatus.setTextColor(ContextCompat.getColor(this, R.color.color_error));
            recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), selectedOriginal, AttemptAnswerEntity.STATUS_WRONG, System.currentTimeMillis(), timeSpent));
        }

        String prefix = getPrefix(selectedIdx);
        String correctPrefix = correctIdx >= 0 ? getPrefix(correctIdx) : "-";
        binding.tvUserChoiceSummary.setText("Bạn chọn: " + prefix + " • Đáp án đúng: " + correctPrefix);

        // Explanation text and source
        // LƯU Ý: giải thích lấy từ câu hỏi gốc. Nếu nội dung giải thích có ghi sẵn chữ cái
        // (vd "Đáp án B...") thì khi trộn đáp án nhãn đó sẽ lệch với màn hình.
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
        LinearLayout card = optionCards[index];
        TextView prefix = optionPrefixes[index];
        TextView text = optionTexts[index];

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
        CardEntity card = quizItems.get(currentIndex).getSource();
        skippedCount++;
        recordedAnswers.add(new AttemptAnswerEntity(currentAttempt.getId(), card.getId(), -1, AttemptAnswerEntity.STATUS_SKIPPED, System.currentTimeMillis(), 0));

        Toast.makeText(this, "Đã bỏ qua câu " + (currentIndex + 1), Toast.LENGTH_SHORT).show();
        currentIndex++;
        displayCurrentQuestion();
    }

    private void finishQuiz() {
        if (timer != null) timer.cancel();
        backCallback.setEnabled(false);   // đã nộp bài: Back không cần hỏi xác nhận nữa

        long endTime = System.currentTimeMillis();
        long durationSec = Math.max(1, (endTime - quizStartTime) / 1000);

        int totalQuestions = quizItems.size();
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

    // ===== MỤC 1: Xác nhận khi thoát Quiz đang làm dở =====
    // Dùng OnBackPressedCallback thay cho onBackPressed() (đã lỗi thời) -> bắt được cả nút Back
    // của hệ thống, cử chỉ vuốt quay lại và nút mũi tên btnBack trên màn hình.
    private void setupBackHandling() {
        backCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backCallback);
    }

    private void showExitConfirmDialog() {
        if (exitDialog != null && exitDialog.isShowing()) return;
        exitDialog = new AlertDialog.Builder(this)
                .setTitle("Thoát bài Quiz?")
                .setMessage("Tiến trình làm bài sẽ bị mất nếu bạn thoát.")
                .setCancelable(false)   // bắt buộc chọn 1 trong 2 nút
                .setNegativeButton("Ở lại", (dialog, which) -> dialog.dismiss())   // tiếp tục làm bài
                .setPositiveButton("Thoát", (dialog, which) -> exitQuizAndDiscardProgress())   // xác nhận thoát
                .show();
    }

    private void exitQuizAndDiscardProgress() {
        if (timer != null) timer.cancel();
        // Huỷ phiên đang làm dở để màn hình chuẩn bị Quiz không còn hiện banner "Làm tiếp"
        if (currentAttempt != null) {
            final QuizAttemptEntity attempt = currentAttempt;
            Executors.newSingleThreadExecutor().execute(() -> {
                attempt.setStatus(QuizAttemptEntity.STATUS_ABANDONED);
                db.quizAttemptDao().update(attempt);
            });
        }
        backCallback.setEnabled(false);   // tắt chặn để thoát được
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
        if (exitDialog != null && exitDialog.isShowing()) exitDialog.dismiss();
    }
}
