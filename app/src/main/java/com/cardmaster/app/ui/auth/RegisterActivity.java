package com.cardmaster.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.databinding.ActivityRegisterBinding;
import com.cardmaster.app.ui.main.MainActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private SessionManager sessionManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        binding.btnRegister.setOnClickListener(v -> performRegister());
        binding.tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void performRegister() {
        String name = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            binding.tilFullName.setError("Vui lòng nhập họ và tên");
            return;
        } else {
            binding.tilFullName.setError(null);
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email không hợp lệ");
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.tilPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity existing = db.userDao().findByEmail(email);
            if (existing != null) {
                runOnUiThread(() -> {
                    binding.tilEmail.setError("Email này đã được sử dụng");
                });
                return;
            }

            UserEntity newUser = new UserEntity(name, email, password, null, System.currentTimeMillis());
            long newId = db.userDao().insert(newUser);

            runOnUiThread(() -> {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                sessionManager.createLoginSession(newId, name, email);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        });
    }
}
