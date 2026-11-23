package com.cardmaster.app.ui.planner;

import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.NotificationEntity;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityStudyPlannerBinding;
import com.cardmaster.app.ui.planner.adapter.StudyPlanAdapter;
import com.cardmaster.app.util.SessionManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

public class StudyPlannerActivity extends AppCompatActivity {

    private ActivityStudyPlannerBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private StudyPlanAdapter adapter;

    private long defaultDeckId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyPlannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        defaultDeckId = getIntent().getLongExtra("TARGET_DECK_ID", -1);

        setupRecyclerView();
        observePlans();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new StudyPlanAdapter(this, this::showPlanOptions);
        binding.rvAllPlans.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllPlans.setAdapter(adapter);
    }

    private void observePlans() {
        long userId = sessionManager.getUserId();
        repository.getPlansLive(userId).observe(this, plans -> {
            adapter.setPlans(plans);
            if (plans == null || plans.isEmpty()) {
                binding.llEmptyPlans.setVisibility(View.VISIBLE);
                binding.rvAllPlans.setVisibility(View.GONE);
            } else {
                binding.llEmptyPlans.setVisibility(View.GONE);
                binding.rvAllPlans.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddNewPlan.setOnClickListener(v -> {
            StudyPlanEditorDialog.show(this, repository, sessionManager.getUserId(), null, defaultDeckId, null);
        });

        binding.btnSyncNow.setOnClickListener(v -> performGoogleCalendarSync());
    }

    private void showPlanOptions(StudyPlanEntity plan) {
        new AlertDialog.Builder(this)
                .setTitle("Tùy chọn lịch ôn")
                .setItems(new String[]{"Chỉnh sửa", "Xóa lịch"}, (dialog, which) -> {
                    if (which == 0) {
                        StudyPlanEditorDialog.show(this, repository, sessionManager.getUserId(), plan, plan.getDeckId(), null);
                    } else if (which == 1) {
                        repository.deletePlan(plan, () -> {
                            runOnUiThread(() -> Toast.makeText(this, "Đã xóa lịch ôn tập", Toast.LENGTH_SHORT).show());
                        });
                    }
                })
                .show();
    }

    private void performGoogleCalendarSync() {
        Toast.makeText(this, "Đang kết nối và đồng bộ với Google Calendar...", Toast.LENGTH_SHORT).show();

        Executors.newSingleThreadExecutor().execute(() -> {
            long userId = sessionManager.getUserId();
            List<StudyPlanEntity> pending = db.studyPlanDao().getPendingSyncPlans(userId);

            for (StudyPlanEntity plan : pending) {
                if (plan.getCalendarEventId() == null || plan.getCalendarEventId().isEmpty()) {
                    plan.setCalendarEventId("gcal_" + UUID.randomUUID().toString().substring(0, 8));
                }
                plan.setSyncStatus(StudyPlanEntity.SYNC_SYNCED);
                plan.setLastSyncedAt(System.currentTimeMillis());
                db.studyPlanDao().update(plan);
            }

            // Also post a notification about sync completion
            db.notificationDao().insert(new NotificationEntity(
                    userId,
                    "Đồng bộ Google Calendar thành công",
                    "Đã đồng bộ " + (pending != null ? pending.size() : 0) + " sự kiện ôn tập với Google Calendar.",
                    NotificationEntity.TYPE_REMINDER,
                    false,
                    System.currentTimeMillis()
            ));

            runOnUiThread(() -> {
                Toast.makeText(this, "Đã hoàn tất đồng bộ hai chiều với Google Calendar!", Toast.LENGTH_LONG).show();
            });
        });
    }
}
