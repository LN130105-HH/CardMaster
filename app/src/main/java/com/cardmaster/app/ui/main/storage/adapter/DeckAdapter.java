package com.cardmaster.app.ui.main.storage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.SubjectEntity;
import com.cardmaster.app.databinding.ItemDeckBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {

    public interface OnDeckActionListener {
        void onDeckClick(DeckEntity deck);
        void onStudyFlashcard(DeckEntity deck);
        void onStartQuiz(DeckEntity deck);
        void onEditDeck(DeckEntity deck);
        void onDeleteDeck(DeckEntity deck);
    }

    private final Context context;
    private final OnDeckActionListener listener;
    private List<DeckEntity> decks = new ArrayList<>();
    private final AppDatabase db;

    public DeckAdapter(Context context, OnDeckActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.db = AppDatabase.getInstance(context);
    }

    public void setDecks(List<DeckEntity> decks) {
        this.decks = decks != null ? decks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDeckBinding binding = ItemDeckBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DeckViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        DeckEntity deck = decks.get(position);
        holder.bind(deck);
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    class DeckViewHolder extends RecyclerView.ViewHolder {
        private final ItemDeckBinding binding;

        public DeckViewHolder(ItemDeckBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DeckEntity deck) {
            binding.tvDeckTitle.setText(deck.getTitle());

            // Load Subject name and card statistics asynchronously
            Executors.newSingleThreadExecutor().execute(() -> {
                SubjectEntity subject = db.subjectDao().getById(deck.getSubjectId());
                int total = db.cardDao().countTotalInDeck(deck.getId());
                int memorized = db.cardDao().countMemorizedInDeck(deck.getId());
                int flashcards = db.cardDao().countFlashcardsInDeck(deck.getId());
                int quizCards = db.cardDao().countQuizCardsInDeck(deck.getId());

                int percent = flashcards > 0 ? (int) Math.round((memorized / (double) flashcards) * 100) : 0;

                binding.getRoot().post(() -> {
                    if (subject != null) {
                        binding.tvSubjectName.setText(subject.getName());
                    } else {
                        binding.tvSubjectName.setText("Chung");
                    }

                    binding.tvCardStats.setText(memorized + "/" + flashcards + " thẻ đã nhớ • " + quizCards + " câu quiz");
                    binding.progressBar.setProgress(percent);
                    binding.tvProgressPercent.setText(percent + "%");
                });
            });

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onDeckClick(deck);
            });

            binding.btnStudyFlashcard.setOnClickListener(v -> {
                if (listener != null) listener.onStudyFlashcard(deck);
            });

            binding.btnStartQuiz.setOnClickListener(v -> {
                if (listener != null) listener.onStartQuiz(deck);
            });

            binding.btnMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenu().add("Chỉnh sửa bộ thẻ");
                popup.getMenu().add("Xóa bộ thẻ");
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Chỉnh sửa bộ thẻ")) {
                        if (listener != null) listener.onEditDeck(deck);
                    } else if (item.getTitle().equals("Xóa bộ thẻ")) {
                        if (listener != null) listener.onDeleteDeck(deck);
                    }
                    return true;
                });
                popup.show();
            });
        }
    }
}
