package com.cardmaster.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cards")
public class CardEntity {
    public static final String TYPE_FLASHCARD = "FLASHCARD";
    public static final String TYPE_QUIZ = "QUIZ";

    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_MEMORIZED = "MEMORIZED";
    public static final String STATUS_NEED_REVIEW = "NEED_REVIEW";

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long deckId;
    private String type; // FLASHCARD or QUIZ

    // Flashcard fields
    private String front;
    private String back;
    private String status; // NEW, MEMORIZED, NEED_REVIEW

    // Quiz fields
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private int correctIndex; // 0=A, 1=B, 2=C, 3=D
    private String explanation;

    // Optional Source info
    private String sourceFile;
    private String sourcePage;
    private String sourceExcerpt;

    private long createdAt;
    private long updatedAt;

    public CardEntity() {}

    // Factory method for Flashcard
    public static CardEntity createFlashcard(long deckId, String front, String back) {
        CardEntity card = new CardEntity();
        card.deckId = deckId;
        card.type = TYPE_FLASHCARD;
        card.front = front;
        card.back = back;
        card.status = STATUS_NEW;
        card.createdAt = System.currentTimeMillis();
        card.updatedAt = System.currentTimeMillis();
        return card;
    }

    // Factory method for Quiz Card
    public static CardEntity createQuizCard(long deckId, String question,
                                            String optionA, String optionB, String optionC, String optionD,
                                            int correctIndex, String explanation,
                                            String sourceFile, String sourcePage, String sourceExcerpt) {
        CardEntity card = new CardEntity();
        card.deckId = deckId;
        card.type = TYPE_QUIZ;
        card.question = question;
        card.optionA = optionA;
        card.optionB = optionB;
        card.optionC = optionC;
        card.optionD = optionD;
        card.correctIndex = correctIndex;
        card.explanation = explanation;
        card.sourceFile = sourceFile;
        card.sourcePage = sourcePage;
        card.sourceExcerpt = sourceExcerpt;
        card.status = STATUS_NEW;
        card.createdAt = System.currentTimeMillis();
        card.updatedAt = System.currentTimeMillis();
        return card;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getDeckId() { return deckId; }
    public void setDeckId(long deckId) { this.deckId = deckId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getFront() { return front; }
    public void setFront(String front) { this.front = front; }

    public String getBack() { return back; }
    public void setBack(String back) { this.back = back; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }

    public int getCorrectIndex() { return correctIndex; }
    public void setCorrectIndex(int correctIndex) { this.correctIndex = correctIndex; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getSourceFile() { return sourceFile; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }

    public String getSourcePage() { return sourcePage; }
    public void setSourcePage(String sourcePage) { this.sourcePage = sourcePage; }

    public String getSourceExcerpt() { return sourceExcerpt; }
    public void setSourceExcerpt(String sourceExcerpt) { this.sourceExcerpt = sourceExcerpt; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
