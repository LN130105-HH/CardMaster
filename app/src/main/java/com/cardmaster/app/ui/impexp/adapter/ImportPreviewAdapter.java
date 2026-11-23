package com.cardmaster.app.ui.impexp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.io.CsvImporter;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.databinding.ItemImportPreviewBinding;

import java.util.ArrayList;
import java.util.List;

public class ImportPreviewAdapter extends RecyclerView.Adapter<ImportPreviewAdapter.PreviewViewHolder> {

    public interface OnSelectionChangedListener {
        void onSelectionChanged();
    }

    private final Context context;
    private final OnSelectionChangedListener listener;
    private List<CsvImporter.ImportItem> items = new ArrayList<>();

    public ImportPreviewAdapter(Context context, OnSelectionChangedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setItems(List<CsvImporter.ImportItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<CsvImporter.ImportItem> getItems() {
        return items;
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImportPreviewBinding binding = ItemImportPreviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PreviewViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PreviewViewHolder extends RecyclerView.ViewHolder {
        private final ItemImportPreviewBinding binding;

        public PreviewViewHolder(ItemImportPreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CsvImporter.ImportItem item) {
            CardEntity card = item.card;
            boolean isQuiz = CardEntity.TYPE_QUIZ.equals(card.getType());

            if (isQuiz) {
                binding.tvRowTypeBadge.setText("Quiz Card");
                binding.tvRowTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_quiz));
                binding.tvRowTitle.setText(card.getQuestion());
                binding.tvRowSubtitle.setText("A: " + card.getOptionA() + " • B: " + card.getOptionB());
            } else {
                binding.tvRowTypeBadge.setText("Flashcard");
                binding.tvRowTypeBadge.setTextColor(ContextCompat.getColor(context, R.color.color_flashcard));
                binding.tvRowTitle.setText(card.getFront());
                binding.tvRowSubtitle.setText(card.getBack());
            }

            if (item.isValid) {
                binding.tvRowStatusBadge.setText("Hợp lệ");
                binding.tvRowStatusBadge.setBackgroundResource(R.drawable.bg_badge_success);
                binding.tvRowStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_on_success_container));
            } else {
                binding.tvRowStatusBadge.setText("Lỗi: " + item.errorMessage);
                binding.tvRowStatusBadge.setBackgroundResource(R.drawable.bg_badge_error);
                binding.tvRowStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.color_on_error_container));
            }

            binding.cbSelectRow.setOnCheckedChangeListener(null);
            binding.cbSelectRow.setChecked(item.isSelected);
            binding.cbSelectRow.setEnabled(item.isValid);

            binding.cbSelectRow.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.isSelected = isChecked;
                if (listener != null) listener.onSelectionChanged();
            });
        }
    }
}
