package com.cardmaster.app.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cardmaster.app.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSendReset.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Email không hợp lệ");
                return;
            }
            binding.tilEmail.setError(null);

            new AlertDialog.Builder(this)
                    .setTitle("Đã gửi liên kết")
                    .setMessage("Liên kết đặt lại mật khẩu đã được gửi đến email: " + email + ".\nVui lòng kiểm tra hộp thư đến (và thư rác nếu cần).")
                    .setPositiveButton("Đã hiểu", (dialog, which) -> finish())
                    .show();
        });
    }
}
