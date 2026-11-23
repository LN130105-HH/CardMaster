package com.cardmaster.app.ui.main.statistics.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.QuizAttemptEntity;
import com.cardmaster.app.databinding.ItemQuizHistoryBinding;
import com.cardmaster.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class QuizHistoryAdapter extends RecyclerView.Adapter<QuizHistoryAdapter.HistoryViewHolder> {

    private final Context context;
    private List<QuizAttemptEntity> attempts = new ArrayList<>();
    private final AppDatabase db;

    public QuizHistoryAdapter(Context context) {
        this.context = context;
        this.db = AppDatabase.getInstance(context);
    }

    public void setAttempts(List<QuizAttemptEntity> attempts) {
        this.attempts = attempts != null ? attempts : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuizHistoryBinding binding = ItemQuizHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(attempts.get(position));
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuizHistoryBinding binding;

        public HistoryViewHolder(ItemQuizHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(QuizAttemptEntity attempt) {
            Executors.newSingleThreadExecutor().execute(() -> {
                DeckEntity deck = db.deckDao().getById(attempt.getDeckId());
                binding.getRoot().post(() -> {
                    if (deck != null) {
                        binding.tvAttemptDeckTitle.setText(deck.getTitle());
                    } else {
                        binding.tvAttemptDeckTitle.setText("Bộ thẻ đã xóa");
                    }
                });
            });

            String details = attempt.getTotalQuestions() + " câu • "
                    + attempt.getCorrectCount() + " đúng, "
                    + attempt.getWrongCount() + " sai, "
                    + attempt.getSkippedCount() + " bỏ qua • "
                    + DateUtils.formatDate(attempt.getEndTime() > 0 ? attempt.getEndTime() : attempt.getStartTime());

            binding.tvAttemptDetails.setText(details);
            binding.tvAttemptScore.setText(String.format(Locale.getDefault(), "%.1f%%", attempt.getScorePercentage()));
        }
    }
}
