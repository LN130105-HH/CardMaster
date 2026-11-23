package com.cardmaster.app.ui.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cardmaster.app.databinding.FragmentProfileBinding;
import com.cardmaster.app.ui.auth.LoginActivity;
import com.cardmaster.app.ui.planner.StudyPlannerActivity;
import com.cardmaster.app.util.SessionManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(requireContext());

        binding.tvProfileName.setText(sessionManager.getUserName());
        binding.tvProfileEmail.setText(sessionManager.getUserEmail());

        setupListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) {
            binding.tvProfileName.setText(sessionManager.getUserName());
            binding.tvProfileEmail.setText(sessionManager.getUserEmail());
        }
    }

    private void setupListeners() {
        binding.btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), EditProfileActivity.class));
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });

        binding.btnGeminiConfig.setOnClickListener(v -> showApiKeyDialog());

        binding.btnPlannerSync.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), StudyPlannerActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản CardMaster?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        sessionManager.logout();
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void showApiKeyDialog() {
        final EditText input = new EditText(requireContext());
        input.setHint("Nhập Gemini API Key");
        input.setText(sessionManager.getGeminiApiKey());
        input.setPadding(40, 30, 40, 30);

        new AlertDialog.Builder(requireContext())
                .setTitle("Cấu hình Gemini API Key")
                .setMessage("API Key phục vụ sinh câu hỏi tự động và chat hỏi đáp học tập. Khóa được lưu an toàn trên máy.")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String key = input.getText().toString().trim();
                    sessionManager.setGeminiApiKey(key);
                    Toast.makeText(requireContext(), "Đã lưu Gemini API Key", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
