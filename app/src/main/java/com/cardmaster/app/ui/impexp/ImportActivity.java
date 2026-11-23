package com.cardmaster.app.ui.impexp;

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

import com.cardmaster.app.data.io.CsvImporter;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityImportBinding;
import com.cardmaster.app.ui.impexp.adapter.ImportPreviewAdapter;
import com.cardmaster.app.util.FileUtils;
import com.cardmaster.app.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ImportActivity extends AppCompatActivity {

    private ActivityImportBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private ImportPreviewAdapter adapter;

    private List<DeckEntity> deckList = new ArrayList<>();
    private List<String> deckTitles = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private long preselectedDeckId = -1;

    private final ActivityResultLauncher<String[]> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.OpenDocument(),
            uri -> {
                if (uri != null) {
                    processSelectedFile(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        preselectedDeckId = getIntent().getLongExtra("TARGET_DECK_ID", -1);

        setupSpinner();
        setupRecyclerView();
        setupListeners();
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deckTitles);
        binding.spTargetDeck.setAdapter(spinnerAdapter);

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
                    binding.spTargetDeck.setSelection(selectIdx);
                } else {
                    deckTitles.add("Chưa có bộ thẻ (Vui lòng tạo trước)");
                    spinnerAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void setupRecyclerView() {
        adapter = new ImportPreviewAdapter(this, this::updateSelectionSummary);
        binding.rvPreview.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPreview.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSelectFile.setOnClickListener(v -> {
            filePickerLauncher.launch(new String[]{
                    "text/comma-separated-values",
                    "text/csv",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/octet-stream",
                    "*/*"
            });
        });

        binding.cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (CsvImporter.ImportItem item : adapter.getItems()) {
                if (item.isValid) {
                    item.isSelected = isChecked;
                }
            }
            adapter.notifyDataSetChanged();
            updateSelectionSummary();
        });

        binding.btnConfirmImport.setOnClickListener(v -> confirmAndSave());
    }

    private void processSelectedFile(Uri uri) {
        String fileName = FileUtils.getFileName(this, uri);
        long size = FileUtils.getFileSize(this, uri);

        // Section 16.2: Size limit checks
        if (size > FileUtils.MAX_FILE_SIZE_BYTES) {
            new AlertDialog.Builder(this)
                    .setTitle("File vượt quá dung lượng")
                    .setMessage("File \"" + fileName + "\" có dung lượng lớn hơn 20 MB. Hệ thống không cho phép xử lý file vượt quá 20 MB!")
                    .setPositiveButton("Đã hiểu", null)
                    .show();
            return;
        }

        if (size > FileUtils.WARN_FILE_SIZE_BYTES) {
            Toast.makeText(this, "Cảnh báo: File lớn hơn 10 MB, quá trình đọc có thể mất thêm vài giây.", Toast.LENGTH_LONG).show();
        }

        binding.tvSelectedFileName.setText(fileName);

        int pos = binding.spTargetDeck.getSelectedItemPosition();
        long targetDeckId = (pos >= 0 && pos < deckList.size()) ? deckList.get(pos).getId() : 1;

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<CsvImporter.ImportItem> items = CsvImporter.parseFile(this, uri, fileName, targetDeckId);
                runOnUiThread(() -> {
                    adapter.setItems(items);
                    binding.llPreviewStats.setVisibility(View.VISIBLE);
                    updateSelectionSummary();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi đọc file: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void updateSelectionSummary() {
        int validCount = 0;
        int errorCount = 0;
        int selectedCount = 0;

        for (CsvImporter.ImportItem item : adapter.getItems()) {
            if (item.isValid) {
                validCount++;
                if (item.isSelected) selectedCount++;
            } else {
                errorCount++;
            }
        }

        binding.tvPreviewStats.setText("Hợp lệ: " + validCount + " • Lỗi: " + errorCount + " (Đã chọn: " + selectedCount + ")");
        binding.btnConfirmImport.setEnabled(selectedCount > 0);
        binding.btnConfirmImport.setText("Lưu " + selectedCount + " thẻ đã chọn");
    }

    private void confirmAndSave() {
        int pos = binding.spTargetDeck.getSelectedItemPosition();
        if (pos < 0 || pos >= deckList.size()) {
            Toast.makeText(this, "Vui lòng chọn bộ thẻ đích", Toast.LENGTH_SHORT).show();
            return;
        }
        long targetDeckId = deckList.get(pos).getId();

        List<CardEntity> cardsToSave = new ArrayList<>();
        for (CsvImporter.ImportItem item : adapter.getItems()) {
            if (item.isValid && item.isSelected) {
                item.card.setDeckId(targetDeckId);
                cardsToSave.add(item.card);
            }
        }

        if (cardsToSave.isEmpty()) return;

        repository.insertCards(cardsToSave, () -> {
            runOnUiThread(() -> {
                Toast.makeText(this, "Đã nhập thành công " + cardsToSave.size() + " thẻ vào bộ!", Toast.LENGTH_LONG).show();
                finish();
            });
        });
    }
}
