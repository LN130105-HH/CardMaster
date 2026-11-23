package com.cardmaster.app.ui.card.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.databinding.ItemCardBinding;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    public interface OnCardActionListener {
        void onCardClick(CardEntity card);
        void onEditCard(CardEntity card);
        void onDeleteCard(CardEntity card);
    }

    private final Context context;
    private final OnCardActionListener listener;
    private List<CardEntity> cards = new ArrayList<>();

    public CardAdapter(Context context, OnCardActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards != null ? cards : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private final ItemCardBinding binding;

        public CardViewHolder(ItemCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CardEntity card) {
            boolean isQuiz = CardEntity.TYPE_QUIZ.equals(card.getType());

            if (isQuiz) {
                binding.ivCardTypeIcon.setImageResource(R.drawable.ic_quiz);
                binding.ivCardTypeIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_quiz));
                binding.tvCardTypeBadge.setText("Quiz Card");
                binding.tvCardTypeBadge.setBackgroundResource(R.drawable.bg_badge_info);
                binding.tvCardTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_quiz));

                binding.tvCardQuestion.setText(card.getQuestion());
                binding.tvCardAnswer.setText("A: " + card.getOptionA() + " • B: " + card.getOptionB() +
                        (card.getOptionC() != null && !card.getOptionC().isEmpty() ? " • C: " + card.getOptionC() : ""));

                binding.tvCardStatusBadge.setText("Trắc nghiệm");
                binding.tvCardStatusBadge.setBackgroundResource(R.drawable.bg_badge_neutral);
                binding.tvCardStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_text_secondary));
            } else {
                binding.ivCardTypeIcon.setImageResource(R.drawable.ic_style);
                binding.ivCardTypeIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_flashcard));
                binding.tvCardTypeBadge.setText("Flashcard");
                binding.tvCardTypeBadge.setBackgroundResource(R.drawable.bg_badge_info);
                binding.tvCardTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_flashcard));

                binding.tvCardQuestion.setText(card.getFront());
                binding.tvCardAnswer.setText(card.getBack());

                if (CardEntity.STATUS_MEMORIZED.equals(card.getStatus())) {
                    binding.tvCardStatusBadge.setText("Đã nhớ");
                    binding.tvCardStatusBadge.setBackgroundResource(R.drawable.bg_badge_success);
                    binding.tvCardStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_on_success_container));
                } else if (CardEntity.STATUS_NEED_REVIEW.equals(card.getStatus())) {
                    binding.tvCardStatusBadge.setText("Cần ôn lại");
                    binding.tvCardStatusBadge.setBackgroundResource(R.drawable.bg_badge_warning);
                    binding.tvCardStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_on_warning_container));
                } else {
                    binding.tvCardStatusBadge.setText("Mới");
                    binding.tvCardStatusBadge.setBackgroundResource(R.drawable.bg_badge_neutral);
                    binding.tvCardStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_text_secondary));
                }
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onCardClick(card);
            });

            binding.btnCardMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenu().add("Chỉnh sửa");
                popup.getMenu().add("Xóa thẻ");
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().equals("Chỉnh sửa")) {
                        if (listener != null) listener.onEditCard(card);
                    } else if (item.getTitle().equals("Xóa thẻ")) {
                        if (listener != null) listener.onDeleteCard(card);
                    }
                    return true;
                });
                popup.show();
            });
        }
    }
}
