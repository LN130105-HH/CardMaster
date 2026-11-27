package com.cardmaster.app.ui.study;

import com.cardmaster.app.data.local.entity.CardEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Các hàm trộn dữ liệu cho Flashcard và Quiz.
 * Mọi hàm đều làm việc trên BẢN SAO => danh sách lấy từ Database không bị thay đổi.
 */
public final class ShuffleUtils {
    private ShuffleUtils() {}

    /** Trả về danh sách mới đã trộn, danh sách gốc giữ nguyên. */
    public static <T> List<T> shuffled(List<T> source) {
        return shuffled(source, null);
    }

    /** nhận thêm Random để test lặp lại được  */
    public static <T> List<T> shuffled(List<T> source, Random random) {
        List<T> copy = new ArrayList<>(source);   // sao chép trước khi trộn
        if (random == null) Collections.shuffle(copy);
        else Collections.shuffle(copy, random);
        return copy;
    }

    public static QuizItem buildQuizItem(CardEntity card, boolean shuffleOptions) {
        return buildQuizItem(card, shuffleOptions, null);
    }

    /**
     * Tạo QuizItem từ một CardEntity loại quizz, có thể trộn đáp án A, B, C, D.
     * Cách giữ đáp án đúng: trộn danh sách chỉ số [0,1,2,3] thay vì trộn chữ.
     * order.get(viTriMoi) = viTriGoc  =>  vị trí mới của đáp án đúng = order.indexOf(card.getCorrectIndex()).
     */
    public static QuizItem buildQuizItem(CardEntity card, boolean shuffleOptions, Random random) {
        // (1) Đáp án gốc theo thứ tự A, B, C, D (0..3 giống cột correctIndex trong DB)
        String[] original = {card.getOptionA(), card.getOptionB(), card.getOptionC(), card.getOptionD()};

        // (2) Chỉ số các đáp án được hiển thị: A, B luôn có; C, D chỉ có khi có nội dung
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < original.length; i++) {
            if (i < 2 || (original[i] != null && !original[i].isEmpty())) order.add(i);
        }

        // (3) Trộn danh sách chỉ số (chỉ khi dữ liệu hợp lệ: đáp án đúng nằm trong các đáp án hiển thị)
        int correctOriginal = card.getCorrectIndex();
        if (shuffleOptions && order.contains(correctOriginal)) {
            if (random == null) Collections.shuffle(order);
            else Collections.shuffle(order, random);
        }

        // (4) Dựng danh sách đáp án mới + bảng đổi vị trí mới -> vị trí gốc
        List<String> options = new ArrayList<>();
        int[] originalIndexes = new int[order.size()];
        for (int pos = 0; pos < order.size(); pos++) {
            int oldIndex = order.get(pos);
            options.add(original[oldIndex]);
            originalIndexes[pos] = oldIndex;
        }

        // (5) Vị trí mới của đáp án đúng (-1 nếu dữ liệu lỗi: không có đáp án nào được tính là đúng)
        int correctNew = order.indexOf(correctOriginal);
        return new QuizItem(card, options, originalIndexes, correctNew);
    }

    public static List<QuizItem> buildQuizSession(List<CardEntity> source,
                                                  boolean shuffleQuestions, boolean shuffleOptions) {
        return buildQuizSession(source, shuffleQuestions, shuffleOptions, null);
    }

    /** Tạo cả phiên Quiz: trộn câu hỏi (tuỳ chọn) rồi trộn đáp án (tuỳ chọn). */
    public static List<QuizItem> buildQuizSession(List<CardEntity> source,
                                                  boolean shuffleQuestions, boolean shuffleOptions,
                                                  Random random) {
        List<CardEntity> cards = shuffleQuestions ? shuffled(source, random) : new ArrayList<>(source);
        List<QuizItem> session = new ArrayList<>();
        for (CardEntity card : cards) {
            session.add(buildQuizItem(card, shuffleOptions, random));
        }
        return session;
    }
}
