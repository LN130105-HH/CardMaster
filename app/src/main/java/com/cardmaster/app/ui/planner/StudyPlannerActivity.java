package com.cardmaster.app.ui.planner;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.calendar.GoogleCalendarSyncManager;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityStudyPlannerBinding;
import com.cardmaster.app.ui.planner.adapter.StudyPlanAdapter;
import com.cardmaster.app.util.DateUtils;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class StudyPlannerActivity extends AppCompatActivity {

    private ActivityStudyPlannerBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private StudyPlanAdapter adapter;
    private GoogleCalendarSyncManager syncManager;

    private long defaultDeckId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyPlannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);
        syncManager = new GoogleCalendarSyncManager(this);

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
                .setItems(new String[]{"Chỉnh sửa", "Xóa lịch (Đồng bộ xóa Calendar)", "Mô phỏng xung đột với Calendar"}, (dialog, which) -> {
                    if (which == 0) {
                        StudyPlanEditorDialog.show(this, repository, sessionManager.getUserId(), plan, plan.getDeckId(), null);
                    } else if (which == 1) {
                        new AlertDialog.Builder(this)
                                .setTitle("Xóa lịch ôn")
                                .setMessage("Lịch ôn sẽ bị xóa trong CardMaster và tự động xóa sự kiện tương ứng trên Google Calendar.")
                                .setPositiveButton("Xóa", (d, w) -> {
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        plan.setSyncStatus(StudyPlanEntity.SYNC_DELETED);
                                        db.studyPlanDao().update(plan);
                                        syncManager.syncTwoWay(sessionManager.getUserId(), new GoogleCalendarSyncManager.SyncCallback() {
                                            @Override
                                            public void onSyncComplete(int c, int u, int conf, int del) {
                                                runOnUiThread(() -> Toast.makeText(StudyPlannerActivity.this, "Đã xóa lịch trong app và Google Calendar", Toast.LENGTH_SHORT).show());
                                            }

                                            @Override
                                            public void onConflictDetected(StudyPlanEntity p, long rTime, Runnable kApp, Runnable kRem) {}

                                            @Override
                                            public void onError(String error) {}
                                        });
                                    });
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    } else if (which == 2) {
                        // Simulate conflict
                        Executors.newSingleThreadExecutor().execute(() -> {
                            plan.setSyncStatus(StudyPlanEntity.SYNC_CONFLICT);
                            plan.setLastModifiedAt(System.currentTimeMillis());
                            db.studyPlanDao().update(plan);
                            runOnUiThread(() -> Toast.makeText(this, "Đã đánh dấu trạng thái xung đột! Hãy bấm 'Đồng bộ ngay' để kiểm tra tự động giải quyết xung đột.", Toast.LENGTH_LONG).show());
                        });
                    }
                })
                .show();
    }

    private void performGoogleCalendarSync() {
        Toast.makeText(this, "Đang kết nối Google Calendar API và đồng bộ hai chiều...", Toast.LENGTH_SHORT).show();

        syncManager.syncTwoWay(sessionManager.getUserId(), new GoogleCalendarSyncManager.SyncCallback() {
            @Override
            public void onSyncComplete(int created, int updated, int conflictResolved, int deleted) {
                new AlertDialog.Builder(StudyPlannerActivity.this)
                        .setTitle("Đồng bộ Google Calendar thành công")
                        .setMessage("Kết quả đồng bộ hai chiều:\n\n" +
                                "• Tạo mới trên Calendar: " + created + " sự kiện\n" +
                                "• Cập nhật tương ứng: " + updated + " sự kiện\n" +
                                "• Xung đột đã giải quyết: " + conflictResolved + " sự kiện\n" +
                                "• Đã xóa đồng bộ: " + deleted + " sự kiện\n\n" +
                                "Quy tắc chống trùng lặp hoạt động chính xác (Ánh xạ 1:1 qua calendarEventId).")
                        .setPositiveButton("Đóng", null)
                        .show();
            }

            @Override
            public void onConflictDetected(StudyPlanEntity plan, long remoteTime, Runnable onKeepApp, Runnable onKeepRemote) {
                new AlertDialog.Builder(StudyPlannerActivity.this)
                        .setTitle("Phát hiện xung đột lịch ôn")
                        .setMessage("Sự kiện này được sửa ở cả CardMaster và Google Calendar.\n\n" +
                                "• Giờ trong App: " + DateUtils.formatDateTime(plan.getScheduledTime()) + "\n" +
                                "• Giờ trên Google Calendar: " + DateUtils.formatDateTime(remoteTime) + "\n\n" +
                                "Bạn muốn giữ phiên bản nào?")
                        .setPositiveButton("Giữ phiên bản App", (dialog, which) -> onKeepApp.run())
                        .setNegativeButton("Giữ phiên bản Google Calendar", (dialog, which) -> onKeepRemote.run())
                        .show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(StudyPlannerActivity.this, "Lỗi đồng bộ: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
