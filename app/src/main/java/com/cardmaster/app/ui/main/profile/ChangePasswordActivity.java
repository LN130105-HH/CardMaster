package com.cardmaster.app.ui.main.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.databinding.ActivityChangePasswordBinding;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private SessionManager sessionManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSavePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String current = binding.etCurrentPassword.getText() != null ? binding.etCurrentPassword.getText().toString().trim() : "";
        String newPass = binding.etNewPassword.getText() != null ? binding.etNewPassword.getText().toString().trim() : "";
        String confirm = binding.etConfirmNewPassword.getText() != null ? binding.etConfirmNewPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(current)) {
            binding.tilCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            return;
        } else {
            binding.tilCurrentPassword.setError(null);
        }

        if (TextUtils.isEmpty(newPass) || newPass.length() < 6) {
            binding.tilNewPassword.setError("Mật khẩu mới tối thiểu 6 ký tự");
            return;
        } else {
            binding.tilNewPassword.setError(null);
        }

        if (!newPass.equals(confirm)) {
            binding.tilConfirmNewPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        } else {
            binding.tilConfirmNewPassword.setError(null);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = db.userDao().getById(sessionManager.getUserId());
            if (user != null && current.equals(user.getPasswordHash())) {
                user.setPasswordHash(newPass);
                db.userDao().update(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                runOnUiThread(() -> {
                    binding.tilCurrentPassword.setError("Mật khẩu hiện tại không đúng");
                });
            }
        });
    }
}
