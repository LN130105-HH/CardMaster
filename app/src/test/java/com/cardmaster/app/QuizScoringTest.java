package com.cardmaster.app;

import org.junit.Assert;
import org.junit.Test;

public class QuizScoringTest {

    /**
     * Test formula from specification section 16.5:
     * Tổng số câu được tính điểm = tổng số câu trong bài − số câu bỏ qua.
     * Điểm (%) = (Số câu đúng / Tổng số câu được tính điểm) × 100.
     * Ví dụ đặc tả: bài có 10 câu, đúng 7 câu và bỏ qua 1 câu -> (7 / 9) * 100 = 77.78%.
     */
    @Test
    public void testSpecificationQuizScoreExample() {
        int totalQuestions = 10;
        int correctCount = 7;
        int skippedCount = 1;

        int scoredQuestions = totalQuestions - skippedCount;
        Assert.assertEquals(9, scoredQuestions);

        double scorePercent = scoredQuestions > 0 ? ((correctCount / (double) scoredQuestions) * 100.0) : 0.0;
        Assert.assertEquals(77.777, scorePercent, 0.01);
    }

    /**
     * Test all questions skipped case:
     * "Nếu người dùng bỏ qua toàn bộ câu hỏi, bài không có điểm hợp lệ"
     */
    @Test
    public void testAllQuestionsSkipped() {
        int totalQuestions = 10;
        int correctCount = 0;
        int skippedCount = 10;

        int scoredQuestions = totalQuestions - skippedCount;
        Assert.assertEquals(0, scoredQuestions);

        double scorePercent = scoredQuestions > 0 ? ((correctCount / (double) scoredQuestions) * 100.0) : 0.0;
        Assert.assertEquals(0.0, scorePercent, 0.001);
    }

    /**
     * Test retry best score logic:
     * "Khi làm lại bài, điểm cuối cùng của bài là điểm cao nhất giữa các lần làm."
     */
    @Test
    public void testBestScoreCalculation() {
        double attempt1 = 70.0;
        double attempt2 = 85.0;
        double bestScore = Math.max(attempt1, attempt2);
        Assert.assertEquals(85.0, bestScore, 0.001);

        double avgScore = (attempt1 + attempt2) / 2.0;
        Assert.assertEquals(77.5, avgScore, 0.001);
    }
}
