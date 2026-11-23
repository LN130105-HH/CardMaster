package com.cardmaster.app.ui.planner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.databinding.ItemStudyPlanBinding;
import com.cardmaster.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class StudyPlanAdapter extends RecyclerView.Adapter<StudyPlanAdapter.PlanViewHolder> {

    public interface OnPlanClickListener {
        void onPlanClick(StudyPlanEntity plan);
    }

    private final Context context;
    private final OnPlanClickListener listener;
    private List<StudyPlanEntity> plans = new ArrayList<>();
    private final AppDatabase db;

    public StudyPlanAdapter(Context context, OnPlanClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.db = AppDatabase.getInstance(context);
    }

    public void setPlans(List<StudyPlanEntity> plans) {
        this.plans = plans != null ? plans : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStudyPlanBinding binding = ItemStudyPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlanViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        holder.bind(plans.get(position));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {
        private final ItemStudyPlanBinding binding;

        public PlanViewHolder(ItemStudyPlanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(StudyPlanEntity plan) {
            Executors.newSingleThreadExecutor().execute(() -> {
                DeckEntity deck = db.deckDao().getById(plan.getDeckId());
                binding.getRoot().post(() -> {
                    if (deck != null) {
                        binding.tvPlanDeckTitle.setText(deck.getTitle());
                    } else {
                        binding.tvPlanDeckTitle.setText("Bộ thẻ đã xóa");
                    }
                });
            });

            String repeatStr = "Một lần";
            if (StudyPlanEntity.REPEAT_DAILY.equals(plan.getRepeatMode())) repeatStr = "Hàng ngày";
            else if (StudyPlanEntity.REPEAT_WEEKLY.equals(plan.getRepeatMode())) repeatStr = "Hàng tuần";

            binding.tvPlanTime.setText(DateUtils.formatDateTime(plan.getScheduledTime()) + " • " + repeatStr);

            // Sync Status
            String syncText = "Đã đồng bộ";
            int bgDrawable = R.drawable.bg_badge_info;
            int textColor = R.color.color_on_secondary_container;

            if (StudyPlanEntity.SYNC_PENDING.equals(plan.getSyncStatus())) {
                syncText = "Chờ đồng bộ";
                bgDrawable = R.drawable.bg_badge_warning;
                textColor = R.color.color_on_warning_container;
            } else if (StudyPlanEntity.SYNC_CONFLICT.equals(plan.getSyncStatus())) {
                syncText = "Xung đột";
                bgDrawable = R.drawable.bg_badge_error;
                textColor = R.color.color_on_error_container;
            }

            binding.tvSyncStatus.setText(syncText);
            binding.tvSyncStatus.setBackgroundResource(bgDrawable);
            binding.tvSyncStatus.setTextColor(ContextCompat.getColor(context, textColor));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) listener.onPlanClick(plan);
            });
        }
    }
}
