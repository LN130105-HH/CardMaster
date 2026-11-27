package com.cardmaster.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.cardmaster.app.data.local.entity.CardEntity;
import com.cardmaster.app.ui.study.QuizItem;
import com.cardmaster.app.ui.study.ShuffleUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Kiểm tra chức năng trộn Flashcard/Quiz: chấm điểm vẫn đúng, dữ liệu gốc không đổi
public class ShuffleUtilsTest {

    // Tạo một câu hỏi Quiz mẫu
    private static CardEntity quiz(long id, int correct, String a, String b, String c, String d) {
        CardEntity e = CardEntity.createQuizCard(1, "Câu " + id, a, b, c, d, correct, "gt", null, null, null);
        e.setId(id);
        return e;
    }

    // Tạo 20 câu hỏi mẫu, đáp án đúng rải đều A..D
    private static List<CardEntity> sample() {
        List<CardEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(quiz(i, i % 4, "A" + i, "B" + i, "C" + i, "D" + i));
        }
        return list;
    }

    // Chuyển danh sách câu hỏi thành chuỗi để so sánh trước/sau khi trộn
    private static String snapshot(List<CardEntity> list) {
        StringBuilder sb = new StringBuilder();
        for (CardEntity c : list) {
            sb.append(c.getId()).append('|').append(c.getQuestion()).append('|')
                    .append(c.getOptionA()).append(c.getOptionB()).append(c.getOptionC()).append(c.getOptionD())
                    .append('|').append(c.getCorrectIndex()).append(';');
        }
        return sb.toString();
    }

    // Trộn đáp án xong vẫn xác định đúng đáp án đúng
    @Test
    public void correctAnswerSurvivesShuffle() {
        List<CardEntity> source = sample();
        for (int seed = 0; seed < 500; seed++) {
            for (QuizItem item : ShuffleUtils.buildQuizSession(source, true, true, new Random(seed))) {
                CardEntity c = item.getSource();
                String[] original = {c.getOptionA(), c.getOptionB(), c.getOptionC(), c.getOptionD()};
                assertTrue("mất đáp án đúng", item.getCorrectIndex() >= 0);
                assertEquals("đáp án đúng bị lệch", original[c.getCorrectIndex()], item.getOption(item.getCorrectIndex()));
                int correctCount = 0;
                for (int i = 0; i < item.getOptionCount(); i++) if (item.isCorrect(i)) correctCount++;
                assertEquals("phải có đúng 1 đáp án đúng", 1, correctCount);
            }
        }
    }

    // Đáp án đã chọn được lưu vào Database theo đúng thứ tự gốc
    @Test
    public void originalIndexMapsBackToDatabaseOrder() {
        for (QuizItem item : ShuffleUtils.buildQuizSession(sample(), false, true, new Random(7))) {
            assertEquals("originalIndex sai", item.getSource().getCorrectIndex(),
                    item.getOriginalIndex(item.getCorrectIndex()));
        }
    }

    // Trộn không làm thay đổi dữ liệu gốc
    @Test
    public void sourceDataIsNeverModified() {
        List<CardEntity> source = sample();
        String before = snapshot(source);
        ShuffleUtils.buildQuizSession(source, true, true, new Random(1));
        ShuffleUtils.shuffled(source, new Random(2));
        assertEquals("dữ liệu gốc bị thay đổi", before, snapshot(source));
    }

    // Trộn câu hỏi không làm mất hoặc trùng câu
    @Test
    public void questionShuffleKeepsAllQuestions() {
        List<CardEntity> source = sample();
        List<QuizItem> session = ShuffleUtils.buildQuizSession(source, true, true, new Random(3));
        Set<Long> ids = new HashSet<>();
        for (QuizItem item : session) ids.add(item.getSource().getId());
        assertEquals("sai số câu", source.size(), session.size());
        assertEquals("trùng hoặc mất câu", source.size(), ids.size());
    }

    // Tắt trộn thì giữ nguyên thứ tự gốc
    @Test
    public void shuffleOffKeepsOriginalOrder() {
        List<CardEntity> source = sample();
        List<QuizItem> session = ShuffleUtils.buildQuizSession(source, false, false);
        for (int i = 0; i < source.size(); i++) {
            assertTrue("đổi thứ tự câu hỏi", session.get(i).getSource() == source.get(i));
            assertEquals("đổi vị trí đáp án đúng", source.get(i).getCorrectIndex(), session.get(i).getCorrectIndex());
        }
    }

    // Câu chỉ có 2 đáp án thì chỉ hiện 2 đáp án
    @Test
    public void questionWithTwoOptionsOnlyShowsTwo() {
        QuizItem item = ShuffleUtils.buildQuizItem(quiz(100, 1, "Đúng", "Sai", null, ""), true, new Random(5));
        assertEquals("câu 2 đáp án hiện sai", 2, item.getOptionCount());
        assertEquals("đáp án đúng lệch", "Sai", item.getOption(item.getCorrectIndex()));
    }

    // Dữ liệu lỗi (đáp án đúng trỏ vào ô trống) không làm app bị crash
    @Test
    public void invalidCorrectIndexDoesNotCrash() {
        QuizItem item = ShuffleUtils.buildQuizItem(quiz(200, 2, "a", "b", "", ""), true);
        assertEquals("phải là -1", -1, item.getCorrectIndex());
        assertTrue("không được tính đúng", !item.isCorrect(0) && !item.isCorrect(1));
    }
}
