package com.cardmaster.app.ui.card;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cardmaster.app.R;
import com.cardmaster.app.data.local.AppDatabase;
import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.data.repository.AppRepository;
import com.cardmaster.app.databinding.ActivityCardEditorBinding;

import java.util.concurrent.Executors;

public class CardEditorActivity extends AppCompatActivity {

    private ActivityCardEditorBinding binding;
    private AppRepository repository;
    private AppDatabase db;

    private long deckId = -1;
    private long cardId = -1;
    private CardEntity editingCard;
    private boolean isFlashcardMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        db = AppDatabase.getInstance(this);

        deckId = getIntent().getLongExtra("DECK_ID", -1);
        cardId = getIntent().getLongExtra("CARD_ID", -1);

        if (cardId != -1) {
            binding.tvEditorTitle.setText("Chỉnh sửa thẻ");
            binding.btnSaveAndCreateNext.setVisibility(View.GONE);
            loadExistingCard();
        }

        setupTypeSelector();
        setupListeners();
    }

    private void setupTypeSelector() {
        binding.btnTypeFlashcard.setOnClickListener(v -> setCardMode(true));
        binding.btnTypeQuiz.setOnClickListener(v -> setCardMode(false));
    }

    private void setCardMode(boolean flashcard) {
        isFlashcardMode = flashcard;
        if (flashcard) {
            binding.btnTypeFlashcard.setBackgroundResource(R.drawable.bg_chip_selected);
            binding.btnTypeFlashcard.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.btnTypeQuiz.setBackground(null);
            binding.btnTypeQuiz.setTextColor(ContextCompat.getColor(this, R.color.color_text_secondary));

            binding.llFlashcardForm.setVisibility(View.VISIBLE);
            binding.llQuizForm.setVisibility(View.GONE);
        } else {
            binding.btnTypeQuiz.setBackgroundResource(R.drawable.bg_chip_selected);
            binding.btnTypeQuiz.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.btnTypeFlashcard.setBackground(null);
            binding.btnTypeFlashcard.setTextColor(ContextCompat.getColor(this, R.color.color_text_secondary));

            binding.llFlashcardForm.setVisibility(View.GONE);
            binding.llQuizForm.setVisibility(View.VISIBLE);
        }
    }

    private void loadExistingCard() {
        Executors.newSingleThreadExecutor().execute(() -> {
            editingCard = db.cardDao().getById(cardId);
            runOnUiThread(() -> {
                if (editingCard != null) {
                    if (CardEntity.TYPE_QUIZ.equals(editingCard.getType())) {
                        setCardMode(false);
                        binding.etQuizQuestion.setText(editingCard.getQuestion());
                        binding.etOptionA.setText(editingCard.getOptionA());
                        binding.etOptionB.setText(editingCard.getOptionB());
                        binding.etOptionC.setText(editingCard.getOptionC());
                        binding.etOptionD.setText(editingCard.getOptionD());
                        binding.etQuizExplanation.setText(editingCard.getExplanation());
                        binding.etSourceFile.setText(editingCard.getSourceFile());
                        binding.etSourcePage.setText(editingCard.getSourcePage());

                        if (editingCard.getCorrectIndex() == 0) binding.rbOptionA.setChecked(true);
                        else if (editingCard.getCorrectIndex() == 1) binding.rbOptionB.setChecked(true);
                        else if (editingCard.getCorrectIndex() == 2) binding.rbOptionC.setChecked(true);
                        else if (editingCard.getCorrectIndex() == 3) binding.rbOptionD.setChecked(true);
                    } else {
                        setCardMode(true);
                        binding.etFlashcardFront.setText(editingCard.getFront());
                        binding.etFlashcardBack.setText(editingCard.getBack());
                    }
                }
            });
        });
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSaveCard.setOnClickListener(v -> saveCard(false));
        binding.btnSaveAndCreateNext.setOnClickListener(v -> saveCard(true));
    }

    private void saveCard(boolean createNext) {
        if (isFlashcardMode) {
            String front = binding.etFlashcardFront.getText() != null ? binding.etFlashcardFront.getText().toString().trim() : "";
            String back = binding.etFlashcardBack.getText() != null ? binding.etFlashcardBack.getText().toString().trim() : "";

            if (TextUtils.isEmpty(front)) {
                Toast.makeText(this, "Vui lòng nhập mặt trước của flashcard", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(back)) {
                Toast.makeText(this, "Vui lòng nhập mặt sau của flashcard", Toast.LENGTH_SHORT).show();
                return;
            }

            if (editingCard != null) {
                editingCard.setFront(front);
                editingCard.setBack(back);
                editingCard.setType(CardEntity.TYPE_FLASHCARD);
                editingCard.setUpdatedAt(System.currentTimeMillis());
                repository.updateCard(editingCard, () -> runOnUiThread(() -> {
                    Toast.makeText(this, "Đã cập nhật flashcard", Toast.LENGTH_SHORT).show();
                    finish();
                }));
            } else {
                CardEntity card = CardEntity.createFlashcard(deckId, front, back);
                repository.insertCard(card, () -> runOnUiThread(() -> {
                    Toast.makeText(this, "Đã lưu flashcard", Toast.LENGTH_SHORT).show();
                    if (createNext) {
                        binding.etFlashcardFront.setText("");
                        binding.etFlashcardBack.setText("");
                        binding.etFlashcardFront.requestFocus();
                    } else {
                        finish();
                    }
                }));
            }
        } else {
            // Quiz Card
            String question = binding.etQuizQuestion.getText() != null ? binding.etQuizQuestion.getText().toString().trim() : "";
            String optA = binding.etOptionA.getText() != null ? binding.etOptionA.getText().toString().trim() : "";
            String optB = binding.etOptionB.getText() != null ? binding.etOptionB.getText().toString().trim() : "";
            String optC = binding.etOptionC.getText() != null ? binding.etOptionC.getText().toString().trim() : "";
            String optD = binding.etOptionD.getText() != null ? binding.etOptionD.getText().toString().trim() : "";
            String explanation = binding.etQuizExplanation.getText() != null ? binding.etQuizExplanation.getText().toString().trim() : "";
            String sourceFile = binding.etSourceFile.getText() != null ? binding.etSourceFile.getText().toString().trim() : "";
            String sourcePage = binding.etSourcePage.getText() != null ? binding.etSourcePage.getText().toString().trim() : "";

            if (TextUtils.isEmpty(question)) {
                Toast.makeText(this, "Vui lòng nhập câu hỏi trắc nghiệm", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(optA) || TextUtils.isEmpty(optB)) {
                Toast.makeText(this, "Vui lòng nhập ít nhất lựa chọn A và B", Toast.LENGTH_SHORT).show();
                return;
            }

            int correctIdx = 0;
            if (binding.rbOptionB.isChecked()) correctIdx = 1;
            else if (binding.rbOptionC.isChecked()) correctIdx = 2;
            else if (binding.rbOptionD.isChecked()) correctIdx = 3;

            if (editingCard != null) {
                editingCard.setType(CardEntity.TYPE_QUIZ);
                editingCard.setQuestion(question);
                editingCard.setOptionA(optA);
                editingCard.setOptionB(optB);
                editingCard.setOptionC(optC);
                editingCard.setOptionD(optD);
                editingCard.setCorrectIndex(correctIdx);
                editingCard.setExplanation(explanation);
                editingCard.setSourceFile(sourceFile);
                editingCard.setSourcePage(sourcePage);
                editingCard.setUpdatedAt(System.currentTimeMillis());
                repository.updateCard(editingCard, () -> runOnUiThread(() -> {
                    Toast.makeText(this, "Đã cập nhật quiz card", Toast.LENGTH_SHORT).show();
                    finish();
                }));
            } else {
                CardEntity card = CardEntity.createQuizCard(deckId, question, optA, optB, optC, optD, correctIdx, explanation, sourceFile, sourcePage, "");
                repository.insertCard(card, () -> runOnUiThread(() -> {
                    Toast.makeText(this, "Đã lưu quiz card", Toast.LENGTH_SHORT).show();
                    if (createNext) {
                        binding.etQuizQuestion.setText("");
                        binding.etOptionA.setText("");
                        binding.etOptionB.setText("");
                        binding.etOptionC.setText("");
                        binding.etOptionD.setText("");
                        binding.etQuizExplanation.setText("");
                        binding.rbOptionA.setChecked(true);
                        binding.etQuizQuestion.requestFocus();
                    } else {
                        finish();
                    }
                }));
            }
        }
    }
}
