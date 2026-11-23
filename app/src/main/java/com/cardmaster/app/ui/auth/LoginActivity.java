package com.cardmaster.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.UserEntity;
import com.cardmaster.app.databinding.ActivityLoginBinding;
import com.cardmaster.app.ui.main.MainActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager sessionManager;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        binding.btnLogin.setOnClickListener(v -> performLogin());

        binding.btnQuickDemoLogin.setOnClickListener(v -> {
            sessionManager.createLoginSession(1, "Hoàng Vũ Lê Huy", "user@cardmaster.app");
            goToMain();
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void performLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Vui lòng nhập email");
            return;
        } else {
            binding.tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        } else {
            binding.tilPassword.setError(null);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            UserEntity user = db.userDao().findByEmail(email);
            runOnUiThread(() -> {
                if (user != null && password.equals(user.getPasswordHash())) {
                    sessionManager.createLoginSession(user.getId(), user.getFullName(), user.getEmail());
                    goToMain();
                } else {
                    Toast.makeText(this, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
