package com.cardmaster.app.ui.planner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.calendar.GoogleCalendarSyncManager;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.StudyPlanEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityStudyPlannerBinding;
import com.cardmaster.app.ui.planner.adapter.StudyPlanAdapter;
import com.cardmaster.app.util.GoogleCalendarHelper;
import com.cardmaster.app.util.DateUtils;
import com.cardmaster.app.util.SessionManager;
import com.cardmaster.app.util.StudyReminderReceiver;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executors;

public class StudyPlannerActivity extends AppCompatActivity {

    private ActivityStudyPlannerBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private StudyPlanAdapter adapter;
    private GoogleCalendarSyncManager syncManager;

    private long defaultDeckId = -1;
    private boolean isSyncing = false;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Bạn đã từ chối quyền thông báo. Ứng dụng sẽ không thể nhắc giờ học.", Toast.LENGTH_LONG).show();
                }
            });

    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            executeCalendarSyncWithAccount(account);
                        } else {
                            Toast.makeText(this, "Chưa kết nối được tài khoản Google.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (ApiException e) {
                        Toast.makeText(this, "Đăng nhập Google thất bại (Mã lỗi: " + e.getStatusCode() + ")", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Chưa kết nối tài khoản Google để đồng bộ lịch.", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> calendarConsentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    GoogleSignInAccount account = GoogleCalendarHelper.getConnectedAccount(this);
                    if (account != null) {
                        executeCalendarSyncWithAccount(account);
                    }
                } else {
                    Toast.makeText(this, "Bạn chưa cấp quyền truy cập Google Calendar.", Toast.LENGTH_SHORT).show();
                }
            });

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

        StudyReminderReceiver.createNotificationChannel(this);
        requestNotificationPermissionIfNeeded();

        setupRecyclerView();
        observePlans();
        setupListeners();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
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
                        repository.deletePlan(plan, () -> {
                            runOnUiThread(() -> Toast.makeText(this, "Đã xóa lịch ôn tập và hủy nhắc nhở", Toast.LENGTH_SHORT).show());
                        });
                    }
                })
                .show();
    }

    private void performGoogleCalendarSync() {
        if (isSyncing) {
            Toast.makeText(this, "Đang đồng bộ, vui lòng đợi...", Toast.LENGTH_SHORT).show();
            return;
        }

        GoogleSignInAccount connectedAccount = GoogleCalendarHelper.getConnectedAccount(this);
        if (connectedAccount == null) {
            // Trạng thái chưa kết nối tài khoản -> mở hộp thoại đăng nhập & cấp quyền Google Calendar
            Toast.makeText(this, "Vui lòng chọn tài khoản Google để kết nối Calendar...", Toast.LENGTH_SHORT).show();
            GoogleSignInClient signInClient = GoogleCalendarHelper.getSignInClient(this);
            googleSignInLauncher.launch(signInClient.getSignInIntent());
            return;
        }

        executeCalendarSyncWithAccount(connectedAccount);
    }

    private void executeCalendarSyncWithAccount(GoogleSignInAccount account) {
        isSyncing = true;
        binding.btnSyncNow.setEnabled(false);
        Toast.makeText(this, "Đang đồng bộ lịch học với " + account.getEmail() + "...", Toast.LENGTH_SHORT).show();

        long userId = sessionManager.getUserId();
        Executors.newSingleThreadExecutor().execute(() -> {
            GoogleCalendarHelper.SyncResult result = GoogleCalendarHelper.syncUserPlans(this, account, userId);

            if (result.status == GoogleCalendarHelper.SyncResult.Status.SUCCESS && (result.syncedCount > 0 || result.deletedCount > 0)) {
                db.notificationDao().insert(new NotificationEntity(
                        userId,
                        "Đồng bộ Google Calendar thành công",
                        "Đã cập nhật " + result.syncedCount + " lịch học và xóa " + result.deletedCount + " lịch trên tài khoản " + result.accountEmail,
                        NotificationEntity.TYPE_REMINDER,
                        false,
                        System.currentTimeMillis()
                ));
            }

            runOnUiThread(() -> {
                isSyncing = false;
                binding.btnSyncNow.setEnabled(true);

                switch (result.status) {
                    case SUCCESS:
                        if (result.syncedCount == 0 && result.deletedCount == 0) {
                            Toast.makeText(this, "Tất cả lịch học đã đồng bộ sẵn với Google Calendar (" + result.accountEmail + ").", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Đồng bộ thành công " + result.syncedCount + " lịch học lên Google Calendar!", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case NEED_CONSENT:
                        if (result.consentIntent != null) {
                            calendarConsentLauncher.launch(result.consentIntent);
                        }
                        break;

                    case NOT_SIGNED_IN:
                        Toast.makeText(this, "Chưa kết nối tài khoản Google.", Toast.LENGTH_SHORT).show();
                        break;

                    case ERROR:
                        Toast.makeText(this, result.errorMessage != null ? result.errorMessage : "Đồng bộ thất bại.", Toast.LENGTH_LONG).show();
                        break;
                }
            });
        });
    }
}