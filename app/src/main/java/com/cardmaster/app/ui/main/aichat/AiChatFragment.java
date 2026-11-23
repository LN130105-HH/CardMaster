package com.cardmaster.app.ui.main.aichat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.ai.GeminiService;
import com.cardmaster.app.databinding.FragmentAichatBinding;
import com.cardmaster.app.ui.main.aichat.adapter.ChatMessageAdapter;
import com.cardmaster.app.ui.main.aichat.model.ChatMessage;
import com.cardmaster.app.util.SessionManager;

public class AiChatFragment extends Fragment {

    private FragmentAichatBinding binding;
    private ChatMessageAdapter adapter;
    private GeminiService geminiService;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAichatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        geminiService = new GeminiService(requireContext());
        sessionManager = new SessionManager(requireContext());

        adapter = new ChatMessageAdapter(requireContext());
        binding.rvChatMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvChatMessages.setAdapter(adapter);

        // Add welcome message
        adapter.addMessage(new ChatMessage(
                ChatMessage.TYPE_AI,
                "Xin chào! Tôi là trợ lý học tập CardMaster AI tích hợp Gemini. Bạn có câu hỏi nào về môn học hay cần giải thích kiến thức không?",
                System.currentTimeMillis()
        ));

        setupListeners();
    }

    private void setupListeners() {
        binding.btnSendMessage.setOnClickListener(v -> sendMessage());

        binding.chipSuggest1.setOnClickListener(v -> {
            binding.etMessageInput.setText(binding.chipSuggest1.getText().toString());
            sendMessage();
        });
        binding.chipSuggest2.setOnClickListener(v -> {
            binding.etMessageInput.setText(binding.chipSuggest2.getText().toString());
            sendMessage();
        });
        binding.chipSuggest3.setOnClickListener(v -> {
            binding.etMessageInput.setText(binding.chipSuggest3.getText().toString());
            sendMessage();
        });

        binding.btnConfigApiKey.setOnClickListener(v -> showApiKeyDialog());
    }

    private void sendMessage() {
        String query = binding.etMessageInput.getText() != null ? binding.etMessageInput.getText().toString().trim() : "";
        if (TextUtils.isEmpty(query)) return;

        // Add User message
        adapter.addMessage(new ChatMessage(ChatMessage.TYPE_USER, query, System.currentTimeMillis()));
        binding.rvChatMessages.scrollToPosition(adapter.getItemCount() - 1);
        binding.etMessageInput.setText("");

        binding.llAiThinking.setVisibility(View.VISIBLE);

        geminiService.sendChat(query, new GeminiService.AiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (binding == null) return;
                        binding.llAiThinking.setVisibility(View.GONE);
                        adapter.addMessage(new ChatMessage(ChatMessage.TYPE_AI, result, System.currentTimeMillis()));
                        binding.rvChatMessages.scrollToPosition(adapter.getItemCount() - 1);
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        if (binding == null) return;
                        binding.llAiThinking.setVisibility(View.GONE);
                        adapter.addMessage(new ChatMessage(ChatMessage.TYPE_AI, "⚠️ " + errorMessage, System.currentTimeMillis()));
                        binding.rvChatMessages.scrollToPosition(adapter.getItemCount() - 1);
                    });
                }
            }
        });
    }

    private void showApiKeyDialog() {
        final EditText input = new EditText(requireContext());
        input.setHint("Nhập Gemini API Key của bạn");
        input.setText(sessionManager.getGeminiApiKey());
        input.setPadding(40, 30, 40, 30);

        new AlertDialog.Builder(requireContext())
                .setTitle("Cấu hình Gemini API Key")
                .setMessage("API Key được lưu bảo mật trong cấu hình thiết bị của bạn và không bao giờ được chia sẻ công khai.")
                .setView(input)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String key = input.getText().toString().trim();
                    sessionManager.setGeminiApiKey(key);
                    Toast.makeText(requireContext(), "Đã lưu API Key", Toast.LENGTH_SHORT).show();
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
