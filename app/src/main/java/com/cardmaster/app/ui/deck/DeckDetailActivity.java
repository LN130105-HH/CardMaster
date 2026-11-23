package com.cardmaster.app.ui.deck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cardmaster.app.R;
import com.cardmaster.app.data.io.CsvExporter;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.local.entity.DeckEntity;
import com.cardmaster.app.data.local.entity.SubjectEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityDeckDetailBinding;
import com.cardmaster.app.ui.ai.AiDocumentActivity;
import com.cardmaster.app.ui.card.CardEditorActivity;
import com.cardmaster.app.ui.card.adapter.CardAdapter;
import com.cardmaster.app.ui.impexp.ImportActivity;
import com.cardmaster.app.ui.planner.StudyPlannerActivity;
import com.cardmaster.app.ui.quiz.QuizSetupActivity;
import com.cardmaster.app.ui.study.FlashcardStudyActivity;
import com.cardmaster.app.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DeckDetailActivity extends AppCompatActivity implements CardAdapter.OnCardActionListener {

    private ActivityDeckDetailBinding binding;
    private AppRepository repository;
    private SessionManager sessionManager;
    private AppDatabase db;
    private CardAdapter cardAdapter;

    private long deckId = -1;
    private DeckEntity currentDeck;
    private SubjectEntity currentSubject;
    private List<CardEntity> allCards = new ArrayList<>();
    private int currentFilterType = 0; // 0 = all, 1 = flashcard, 2 = quiz

    private final ActivityResultLauncher<String> createDocLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("text/csv"),
            uri -> {
                if (uri != null) {
                    performExportCsv(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeckDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sessionManager = new SessionManager(this);
        db = AppDatabase.getInstance(this);

        deckId = getIntent().getLongExtra("DECK_ID", -1);
        if (deckId == -1) {
            Toast.makeText(this, "Không tìm thấy bộ thẻ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupAdapter();
        loadDeckData();
        setupListeners();
    }

    private void setupAdapter() {
        cardAdapter = new CardAdapter(this, this);
        binding.rvCards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCards.setAdapter(cardAdapter);
    }

    private void loadDeckData() {
        repository.getDeckLive(deckId).observe(this, deck -> {
            if (deck == null) {
                finish();
                return;
            }
            currentDeck = deck;
            binding.tvHeaderTitle.setText(deck.getTitle());
            binding.tvDeckName.setText(deck.getTitle());
            binding.tvDeckDescription.setText(deck.getDescription());

            Executors.newSingleThreadExecutor().execute(() -> {
                currentSubject = db.subjectDao().getById(deck.getSubjectId());
                runOnUiThread(() -> {
                    if (currentSubject != null) {
                        binding.tvSubject.setText(currentSubject.getName());
                    }
                });
            });
        });

        repository.getCardsByDeckLive(deckId).observe(this, cards -> {
            allCards = cards != null ? cards : new ArrayList<>();
            updateCountsAndList();
        });
    }

    private void updateCountsAndList() {
        int total = allCards.size();
        int flashcards = 0;
        int quizCards = 0;

        for (CardEntity c : allCards) {
            if (CardEntity.TYPE_QUIZ.equals(c.getType())) quizCards++;
            else flashcards++;
        }

        binding.tvTotalCardsCount.setText(String.valueOf(total));
        binding.tvFlashcardCount.setText(String.valueOf(flashcards));
        binding.tvQuizCount.setText(String.valueOf(quizCards));

        filterCardsList();
    }

    private void filterCardsList() {
        List<CardEntity> filtered = new ArrayList<>();
        for (CardEntity c : allCards) {
            if (currentFilterType == 1 && !CardEntity.TYPE_FLASHCARD.equals(c.getType())) continue;
            if (currentFilterType == 2 && !CardEntity.TYPE_QUIZ.equals(c.getType())) continue;
            filtered.add(c);
        }

        cardAdapter.setCards(filtered);
        if (filtered.isEmpty()) {
            binding.llEmptyCards.setVisibility(View.VISIBLE);
            binding.rvCards.setVisibility(View.GONE);
        } else {
            binding.llEmptyCards.setVisibility(View.GONE);
            binding.rvCards.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnDeckMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add("Chỉnh sửa bộ thẻ");
            popup.getMenu().add("Xóa bộ thẻ");
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Chỉnh sửa bộ thẻ")) {
                    DeckEditorDialog.show(this, repository, sessionManager.getUserId(), currentDeck, null);
                } else if (item.getTitle().equals("Xóa bộ thẻ")) {
                    new AlertDialog.Builder(this)
                            .setTitle("Xóa bộ thẻ")
                            .setMessage("Bạn có chắc chắn muốn xóa bộ thẻ này cùng toàn bộ thẻ bên trong?")
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                repository.deleteDeck(currentDeck, () -> finish());
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }
                return true;
            });
            popup.show();
        });

        binding.btnStudy.setOnClickListener(v -> {
            sessionManager.setLastStudiedDeckId(deckId);
            Intent intent = new Intent(this, FlashcardStudyActivity.class);
            intent.putExtra("DECK_ID", deckId);
            startActivity(intent);
        });

        binding.btnQuiz.setOnClickListener(v -> {
            sessionManager.setLastStudiedDeckId(deckId);
            Intent intent = new Intent(this, QuizSetupActivity.class);
            intent.putExtra("DECK_ID", deckId);
            startActivity(intent);
        });

        binding.btnAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardEditorActivity.class);
            intent.putExtra("DECK_ID", deckId);
            startActivity(intent);
        });

        binding.btnImport.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImportActivity.class);
            intent.putExtra("TARGET_DECK_ID", deckId);
            startActivity(intent);
        });

        binding.btnAiCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, AiDocumentActivity.class);
            intent.putExtra("TARGET_DECK_ID", deckId);
            startActivity(intent);
        });

        binding.btnExport.setOnClickListener(v -> {
            String defaultFileName = (currentDeck != null ? currentDeck.getTitle() : "Deck") + ".csv";
            createDocLauncher.launch(defaultFileName);
        });

        binding.btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudyPlannerActivity.class);
            intent.putExtra("TARGET_DECK_ID", deckId);
            startActivity(intent);
        });

        binding.tabAllCards.setOnClickListener(v -> selectTab(0, binding.tabAllCards));
        binding.tabFlashcards.setOnClickListener(v -> selectTab(1, binding.tabFlashcards));
        binding.tabQuizCards.setOnClickListener(v -> selectTab(2, binding.tabQuizCards));
    }

    private void selectTab(int type, TextView selectedTab) {
        currentFilterType = type;
        resetTab(binding.tabAllCards);
        resetTab(binding.tabFlashcards);
        resetTab(binding.tabQuizCards);

        selectedTab.setBackgroundResource(R.drawable.bg_chip_selected);
        selectedTab.setTextColor(ContextCompat.getColor(this, R.color.white));

        filterCardsList();
    }

    private void resetTab(TextView tab) {
        tab.setBackgroundResource(R.drawable.bg_chip_unselected);
        tab.setTextColor(ContextCompat.getColor(this, R.color.color_text_secondary));
    }

    private void performExportCsv(Uri uri) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                CsvExporter.exportDeckToCsv(this, uri, currentSubject, currentDeck, allCards);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Xuất file CSV thành công!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Lỗi xuất file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public void onCardClick(CardEntity card) {
        onEditCard(card);
    }

    @Override
    public void onEditCard(CardEntity card) {
        Intent intent = new Intent(this, CardEditorActivity.class);
        intent.putExtra("DECK_ID", deckId);
        intent.putExtra("CARD_ID", card.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteCard(CardEntity card) {
        repository.deleteCard(card, () -> {
            runOnUiThread(() -> Toast.makeText(this, "Đã xóa thẻ", Toast.LENGTH_SHORT).show());
        });
    }
}
