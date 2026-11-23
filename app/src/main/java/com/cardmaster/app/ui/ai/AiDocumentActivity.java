package com.cardmaster.app.ui.ai;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.data.ai.GeminiService;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityAiDocumentBinding;
import com.cardmaster.app.ui.ai.adapter.AiPreviewAdapter;
import com.cardmaster.app.util.FileUtils;
import com.cardmaster.app.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AiDocumentActivity extends AppCompatActivity {

    private ActivityAiDocumentBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private GeminiService geminiService;
    private AiPreviewAdapter adapter;

    private List<DeckEntity> deckList = new ArrayList<>();
    private List<String> deckTitles = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private Uri chosenDocUri;
    private String chosenDocName = "";
    private String extractedContent = "";

    private final ActivityResultLauncher<String[]> docPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    processChosenDocument(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAiDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);
        geminiService = new GeminiService(this);

        long preselectedDeckId = getIntent().getLongExtra("TARGET_DECK_ID", -1);

        setupSpinner(preselectedDeckId);
        setupRecyclerView();
        setupListeners();
    }

    private void setupSpinner(long preselectedDeckId) {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deckTitles);
        binding.spAiTargetDeck.setAdapter(spinnerAdapter);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<DeckEntity> list = db.deckDao().getDecks(sessionManager.getUserId());
            runOnUiThread(() -> {
                deckList.clear();
                deckTitles.clear();
                if (list != null && !list.isEmpty()) {
                    deckList.addAll(list);
                    int selectIdx = 0;
                    for (int i = 0; i < list.size(); i++) {
                        DeckEntity d = list.get(i);
                        deckTitles.add(d.getTitle());
                        if (d.getId() == preselectedDeckId) {
                            selectIdx = i;
                        }
                    }
                    spinnerAdapter.notifyDataSetChanged();
                    binding.spAiTargetDeck.setSelection(selectIdx);
                } else {
                    deckTitles.add("Chưa có bộ thẻ (Vui lòng tạo trước)");
                    spinnerAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void setupRecyclerView() {
        adapter = new AiPreviewAdapter(this);
        binding.rvAiDrafts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAiDrafts.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnChooseDocument.setOnClickListener(v -> {
            docPickerLauncher.launch(new String[]{
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "text/plain",
                    "application/pdf",
                    "*/*"
            });
        });

        binding.btnGenerateWithAi.setOnClickListener(v -> startAiGeneration());

        binding.btnSaveAiDrafts.setOnClickListener(v -> saveApprovedDrafts());
    }

    private void processChosenDocument(Uri uri) {
        chosenDocUri = uri;
        chosenDocName = FileUtils.getFileName(this, uri);
        long size = FileUtils.getFileSize(this, uri);

        if (size > FileUtils.MAX_FILE_SIZE_BYTES) {
            new AlertDialog.Builder(this)
                    .setTitle("File quá lớn")
                    .setMessage("Tài liệu \"" + chosenDocName + "\" vượt quá giới hạn 20 MB của hệ thống!")
                    .setPositiveButton("Đã hiểu", null)
                    .show();
            chosenDocUri = null;
            return;
        }

        binding.tvChosenDocName.setText(chosenDocName);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                extractedContent = FileUtils.extractTextContent(this, uri, chosenDocName);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đã đọc tài liệu thành công!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Không thể đọc nội dung file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void startAiGeneration() {
        if (chosenDocUri == null || extractedContent.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn tài liệu hợp lệ trước", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = binding.spAiTargetDeck.getSelectedItemPosition();
        if (pos < 0 || pos >= deckList.size()) {
            Toast.makeText(this, "Vui lòng chọn bộ thẻ đích", Toast.LENGTH_SHORT).show();
            return;
        }
        long targetDeckId = deckList.get(pos).getId();

        String mode = GeminiService.MODE_CREATE_QUIZ;
        if (binding.rbModeFlashcard.isChecked()) {
            mode = GeminiService.MODE_CREATE_FLASHCARD;
        } else if (binding.rbModeExtract.isChecked()) {
            mode = GeminiService.MODE_EXTRACT_EXISTING;
        }

        binding.llAiLoading.setVisibility(View.VISIBLE);
        binding.btnGenerateWithAi.setEnabled(false);

        geminiService.generateCardsFromDocument(extractedContent, mode, 5, chosenDocName, targetDeckId,
                new GeminiService.AiCallback<List<CardEntity>>() {
                    @Override
                    public void onSuccess(List<CardEntity> result) {
                        runOnUiThread(() -> {
                            binding.llAiLoading.setVisibility(View.GONE);
                            binding.btnGenerateWithAi.setEnabled(true);

                            if (result != null && !result.isEmpty()) {
                                adapter.setItems(result);
                                binding.tvDraftHeader.setVisibility(View.VISIBLE);
                                binding.btnSaveAiDrafts.setVisibility(View.VISIBLE);
                                binding.btnSaveAiDrafts.setText("Lưu " + result.size() + " thẻ vào bộ");
                                Toast.makeText(AiDocumentActivity.this, "Đã tạo thành công " + result.size() + " câu hỏi!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AiDocumentActivity.this, "Không thể sinh câu hỏi từ tài liệu này", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        runOnUiThread(() -> {
                            binding.llAiLoading.setVisibility(View.GONE);
                            binding.btnGenerateWithAi.setEnabled(true);
                            Toast.makeText(AiDocumentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }

    private void saveApprovedDrafts() {
        int pos = binding.spAiTargetDeck.getSelectedItemPosition();
        if (pos < 0 || pos >= deckList.size()) return;
        long targetDeckId = deckList.get(pos).getId();

        List<CardEntity> approved = new ArrayList<>();
        for (AiPreviewAdapter.AiDraftItem item : adapter.getItems()) {
            if (item.isSelected) {
                item.card.setDeckId(targetDeckId);
                approved.add(item.card);
            }
        }

        if (approved.isEmpty()) {
            Toast.makeText(this, "Chưa chọn câu nào để lưu", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.insertCards(approved, () -> {
            runOnUiThread(() -> {
                Toast.makeText(this, "Đã lưu thành công " + approved.size() + " thẻ vào bộ thẻ!", Toast.LENGTH_LONG).show();
                finish();
            });
        });
    }
}
