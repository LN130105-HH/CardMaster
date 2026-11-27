package com.main.java.com.cardmaster.app.ui.study;

import com.cardmaster.app.data.local.entity.CardEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Một câu hỏi trắc nghiệm trong MỘT phiên làm bài (đã trộn đáp án).
 *
 * Chỉ dùng để hiển thị + chấm điểm trong bộ nhớ.
 * KHÔNG ghi ngược vào CardEntity / Database => dữ liệu gốc không bị thay đổi.
 */
public class QuizItem {

    private final CardEntity source;          // câu hỏi gốc (lấy question, explanation, id...)
    private final List<String> options;       // đáp án ĐÃ TRỘN: vị trí 0..3 = A..D trên màn hình
    private final int[] originalIndexes;      // originalIndexes[viTriMoi] = viTriGoc (0=A..3=D trong DB)
    private final int correctIndex;           // vị trí đáp án đúng SAU KHI TRỘN (-1 nếu dữ liệu lỗi)

    public QuizItem(CardEntity source, List<String> options, int[] originalIndexes, int correctIndex) {
        this.source = source;
        this.options = Collections.unmodifiableList(new ArrayList<>(options));
        this.originalIndexes = originalIndexes.clone();
        this.correctIndex = correctIndex;
    }

    public CardEntity getSource() { return source; }

    public int getOptionCount() { return options.size(); }

    public String getOption(int displayedIndex) { return options.get(displayedIndex); }

    public int getCorrectIndex() { return correctIndex; }

    /** Đổi vị trí đang hiển thị -> vị trí gốc trong DB (dùng khi lưu AttemptAnswerEntity). -1 nếu bỏ qua. */
    public int getOriginalIndex(int displayedIndex) {
        if (displayedIndex < 0 || displayedIndex >= originalIndexes.length) return -1;
        return originalIndexes[displayedIndex];
    }

    /** Chấm theo VỊ TRÍ, không so chữ => vẫn đúng kể cả khi 2 đáp án có nội dung giống nhau. */
    public boolean isCorrect(int displayedIndex) {
        return correctIndex >= 0 && displayedIndex == correctIndex;
    }
}
