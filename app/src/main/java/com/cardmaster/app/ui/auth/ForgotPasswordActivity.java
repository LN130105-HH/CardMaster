package com.cardmaster.app.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.data.auth.FirebaseAuthManager;
import com.cardmaster.app.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = new FirebaseAuthManager(this);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSendReset.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Email không hợp lệ");
                return;
            }
            binding.tilEmail.setError(null);

            binding.btnSendReset.setEnabled(false);

            authManager.sendPasswordReset(email, new FirebaseAuthManager.SimpleCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        binding.btnSendReset.setEnabled(true);
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle("Đã gửi email khôi phục")
                                .setMessage("Firebase Authentication đã gửi liên kết đặt lại mật khẩu đến email: " + email + ".\nVui lòng kiểm tra hộp thư đến (hoặc thư rác).")
                                .setPositiveButton("Đã hiểu", (dialog, which) -> finish())
                                .show();
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        binding.btnSendReset.setEnabled(true);
                        new AlertDialog.Builder(ForgotPasswordActivity.this)
                                .setTitle("Thông báo khôi phục")
                                .setMessage("Yêu cầu gửi tới Firebase: " + message + "\nNếu tài khoản tồn tại, email sẽ được gửi tới: " + email)
                                .setPositiveButton("Đã hiểu", (dialog, which) -> finish())
                                .show();
                    });
                }
            });
        });
    }
}
