package com.cardmaster.app.ui.main.storage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.entity.SubjectEntity;
import com.cardmaster.app.databinding.ItemSubjectChipBinding;

import java.util.ArrayList;
import java.util.List;

public class SubjectChipAdapter extends RecyclerView.Adapter<SubjectChipAdapter.ChipViewHolder> {

    public interface OnSubjectSelectedListener {
        void onSubjectSelected(long subjectId);
    }

    private final Context context;
    private final OnSubjectSelectedListener listener;
    private List<SubjectEntity> subjects = new ArrayList<>();
    private long selectedSubjectId = -1; // -1 = All

    public SubjectChipAdapter(Context context, OnSubjectSelectedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setSubjects(List<SubjectEntity> list) {
        subjects.clear();
        // Index 0 is "Tất cả"
        SubjectEntity all = new SubjectEntity(0, "Tất cả", "Tất cả môn học", 0);
        all.setId(-1);
        subjects.add(all);
        if (list != null) subjects.addAll(list);
        notifyDataSetChanged();
    }

    public void setSelectedSubjectId(long id) {
        this.selectedSubjectId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubjectChipBinding binding = ItemSubjectChipBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChipViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        SubjectEntity subject = subjects.get(position);
        holder.bind(subject);
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    class ChipViewHolder extends RecyclerView.ViewHolder {
        private final ItemSubjectChipBinding binding;

        public ChipViewHolder(ItemSubjectChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SubjectEntity subject) {
            binding.tvChipSubjectName.setText(subject.getName());
            boolean isSelected = (subject.getId() == selectedSubjectId);

            if (isSelected) {
                binding.tvChipSubjectName.setBackgroundResource(R.drawable.bg_chip_selected);
                binding.tvChipSubjectName.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                binding.tvChipSubjectName.setBackgroundResource(R.drawable.bg_chip_unselected);
                binding.tvChipSubjectName.setTextColor(ContextCompat.getColor(context, R.color.color_text_secondary));
            }

            binding.getRoot().setOnClickListener(v -> {
                selectedSubjectId = subject.getId();
                notifyDataSetChanged();
                if (listener != null) listener.onSubjectSelected(subject.getId());
            });
        }
    }
}
