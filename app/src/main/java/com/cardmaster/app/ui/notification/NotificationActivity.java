package com.cardmaster.app.ui.notification;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.local.entity.NotificationEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityNotificationBinding;
import com.cardmaster.app.ui.notification.adapter.NotificationAdapter;
import com.cardmaster.app.util.SessionManager;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationClickListener {

    private ActivityNotificationBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);

        setupRecyclerView();
        observeNotifications();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new NotificationAdapter(this, this);
        binding.rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        binding.rvNotifications.setAdapter(adapter);
    }

    private void observeNotifications() {
        long userId = sessionManager.getUserId();
        repository.getNotificationsLive(userId).observe(this, list -> {
            adapter.setNotifications(list);
            if (list == null || list.isEmpty()) {
                binding.llEmptyNotifications.setVisibility(View.VISIBLE);
                binding.rvNotifications.setVisibility(View.GONE);
                binding.btnClearAll.setVisibility(View.GONE);
            } else {
                binding.llEmptyNotifications.setVisibility(View.GONE);
                binding.rvNotifications.setVisibility(View.VISIBLE);
                binding.btnClearAll.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnEmptyBackHome.setOnClickListener(v -> finish());

        binding.btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa tất cả thông báo?")
                    .setMessage("Bạn có chắc chắn muốn xóa toàn bộ danh sách thông báo?")
                    .setPositiveButton("Xóa tất cả", (dialog, which) -> {
                        repository.deleteAllNotifications(sessionManager.getUserId(), () -> {
                            runOnUiThread(() -> Toast.makeText(this, "Đã xóa toàn bộ thông báo", Toast.LENGTH_SHORT).show());
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public void onNotificationClick(NotificationEntity notification) {
        if (!notification.isRead()) {
            repository.markNotificationRead(notification.getId());
        }
    }

    @Override
    public void onDeleteClick(NotificationEntity notification) {
        repository.deleteNotification(notification);
    }
}
