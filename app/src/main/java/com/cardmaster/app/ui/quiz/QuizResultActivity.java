package com.cardmaster.app.ui.quiz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.R;
import com.cardmaster.app.databinding.ActivityQuizResultBinding;
import com.cardmaster.app.ui.main.MainActivity;
import com.cardmaster.app.util.DateUtils;

import java.util.Locale;

public class QuizResultActivity extends AppCompatActivity {

    private ActivityQuizResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        long deckId = getIntent().getLongExtra("DECK_ID", -1);
        int total = getIntent().getIntExtra("TOTAL_QUESTIONS", 0);
        int correct = getIntent().getIntExtra("CORRECT_COUNT", 0);
        int wrong = getIntent().getIntExtra("WRONG_COUNT", 0);
        int skipped = getIntent().getIntExtra("SKIPPED_COUNT", 0);
        double score = getIntent().getDoubleExtra("SCORE", 0.0);
        double bestScore = getIntent().getDoubleExtra("BEST_SCORE", 0.0);
        long durationSec = getIntent().getLongExtra("DURATION_SEC", 0);

        binding.tvStatTotal.setText(String.valueOf(total));
        binding.tvStatCorrect.setText(String.valueOf(correct));
        binding.tvStatWrong.setText(String.valueOf(wrong));
        binding.tvStatSkipped.setText(String.valueOf(skipped));

        int scoredQuestions = total - skipped;
        if (scoredQuestions <= 0) {
            binding.tvScorePercentage.setText("Không có điểm");
            binding.tvScorePercentage.setTextSize(32);
            binding.tvResultTitle.setText("Chưa hoàn thành");
            binding.tvBestScore.setText("Toàn bộ câu hỏi đã bị bỏ qua");
        } else {
            binding.tvScorePercentage.setText(String.format(Locale.getDefault(), "%.1f%%", score));
            binding.tvBestScore.setText(String.format(Locale.getDefault(), "Điểm cao nhất: %.1f%%", bestScore));

            if (score >= 80) {
                binding.tvResultTitle.setText("Hoàn thành xuất sắc!");
            } else if (score >= 50) {
                binding.tvResultTitle.setText("Kết quả khá tốt!");
            } else {
                binding.tvResultTitle.setText("Cần cố gắng ôn tập thêm!");
            }
        }

        binding.tvDuration.setText("Thời gian làm bài: " + DateUtils.formatDuration(durationSec));

        binding.btnRetryQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizPlayActivity.class);
            intent.putExtra("DECK_ID", deckId);
            startActivity(intent);
            finish();
        });

        binding.btnBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        binding.btnBackToHome.performClick();
    }
}
