package com.cardmaster.app.ui.study;

import com.cardmaster.app.data.local.entity.CardEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Chỉ dùng để hiển thị + chấm điểm trong bộ nhớ.
 * Không ghi ngược vào CardEntity => dữ liệu gốc không bị thay đổi.
 */
public class    QuizItem {

    private final CardEntity source;          // câu hỏi gốc
    private final List<String> options;       // đáp án đã trôn: vị trí 0..3 = A..D trên màn hình
    private final int[] originalIndexes;      // originalIndexes[viTriMoi] = viTriGoc
    private final int correctIndex;           // vị trí đáp án đúng sau khi trộn

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

    /** Đổi vị trí đang hiển thị -> vị trí gốc trong DB */
    public int getOriginalIndex(int displayedIndex) {
        if (displayedIndex < 0 || displayedIndex >= originalIndexes.length) return -1;
        return originalIndexes[displayedIndex];
    }

    /** Chấm theo vị trí, không so chữ => vẫn đúng kể cả khi 2 đáp án có nội dung giống nhau */
    public boolean isCorrect(int displayedIndex) {
        return correctIndex >= 0 && displayedIndex == correctIndex;
    }
}
