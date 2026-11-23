package com.cardmaster.app.ui.main.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.databinding.ActivityEditProfileBinding;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private SessionManager sessionManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        binding.etName.setText(sessionManager.getUserName());
        binding.etEmail.setText(sessionManager.getUserEmail());

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String name = binding.etName.getText() != null ? binding.etName.getText().toString().trim() : "";
        if (TextUtils.isEmpty(name)) {
            binding.tilName.setError("Vui lòng nhập họ và tên");
            return;
        }

        sessionManager.setUserName(name);

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = db.userDao().getById(sessionManager.getUserId());
            if (user != null) {
                user.setFullName(name);
                db.userDao().update(user);
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Đã cập nhật thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
