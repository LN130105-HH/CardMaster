package com.cardmaster.app.ui.ai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.databinding.ItemAiPreviewBinding;

import java.util.ArrayList;
import java.util.List;

public class AiPreviewAdapter extends RecyclerView.Adapter<AiPreviewAdapter.AiViewHolder> {

    public static class AiDraftItem {
        public CardEntity card;
        public boolean isSelected = true;

        public AiDraftItem(CardEntity card) {
            this.card = card;
        }
    }

    private final Context context;
    private List<AiDraftItem> items = new ArrayList<>();

    public AiPreviewAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<CardEntity> cards) {
        items.clear();
        if (cards != null) {
            for (CardEntity c : cards) {
                items.add(new AiDraftItem(c));
            }
        }
        notifyDataSetChanged();
    }

    public List<AiDraftItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public AiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAiPreviewBinding binding = ItemAiPreviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AiViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AiViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class AiViewHolder extends RecyclerView.ViewHolder {
        private final ItemAiPreviewBinding binding;

        public AiViewHolder(ItemAiPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AiDraftItem item) {
            CardEntity card = item.card;
            boolean isQuiz = CardEntity.TYPE_QUIZ.equals(card.getType());

            if (isQuiz) {
                binding.tvAiItemTypeBadge.setText("Quiz Card (AI)");
                binding.tvAiItemTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_quiz));
                binding.tvAiItemTitle.setText(card.getQuestion());
                binding.tvAiItemSubtitle.setText("A: " + card.getOptionA() + "\nB: " + card.getOptionB() +
                        (card.getOptionC() != null && !card.getOptionC().isEmpty() ? "\nC: " + card.getOptionC() : "") +
                        "\n💡 Giải thích: " + card.getExplanation());
            } else {
                binding.tvAiItemTypeBadge.setText("Flashcard (AI)");
                binding.tvAiItemTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_flashcard));
                binding.tvAiItemTitle.setText(card.getFront());
                binding.tvAiItemSubtitle.setText(card.getBack());
            }

            binding.cbSelectAiItem.setOnCheckedChangeListener(null);
            binding.cbSelectAiItem.setChecked(item.isSelected);

            binding.cbSelectAiItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.isSelected = isChecked;
            });
        }
    }
}
