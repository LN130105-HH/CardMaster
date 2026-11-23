package com.cardmaster.app.ui.main.statistics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cardmaster.app.R;

public class DonutChartView extends View {

    private Paint paintSuccess;
    private Paint paintError;
    private Paint paintSkipped;
    private Paint paintCenterTextPercent;
    private Paint paintCenterTextLabel;

    private int correctCount = 0;
    private int wrongCount = 0;
    private int skippedCount = 0;

    private final RectF oval = new RectF();
    private float strokeWidth = 36f;

    public DonutChartView(Context context) {
        super(context);
        init();
    }

    public DonutChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonutChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        strokeWidth = getResources().getDisplayMetrics().density * 16; // 16dp thickness as per DESIGN.md

        paintSuccess = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSuccess.setStyle(Paint.Style.STROKE);
        paintSuccess.setStrokeWidth(strokeWidth);
        paintSuccess.setColor(ContextCompat.getColor(getContext(), R.color.color_success));

        paintError = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintError.setStyle(Paint.Style.STROKE);
        paintError.setStrokeWidth(strokeWidth);
        paintError.setColor(ContextCompat.getColor(getContext(), R.color.color_error));

        paintSkipped = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintSkipped.setStyle(Paint.Style.STROKE);
        paintSkipped.setStrokeWidth(strokeWidth);
        paintSkipped.setColor(ContextCompat.getColor(getContext(), R.color.color_surface_variant));

        paintCenterTextPercent = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenterTextPercent.setTextAlign(Paint.Align.CENTER);
        paintCenterTextPercent.setTextSize(getResources().getDisplayMetrics().scaledDensity * 28);
        paintCenterTextPercent.setFakeBoldText(true);
        paintCenterTextPercent.setColor(ContextCompat.getColor(getContext(), R.color.color_text_primary));

        paintCenterTextLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCenterTextLabel.setTextAlign(Paint.Align.CENTER);
        paintCenterTextLabel.setTextSize(getResources().getDisplayMetrics().scaledDensity * 14);
        paintCenterTextLabel.setColor(ContextCompat.getColor(getContext(), R.color.color_text_secondary));
    }

    public void setData(int correct, int wrong, int skipped) {
        this.correctCount = correct;
        this.wrongCount = wrong;
        this.skippedCount = skipped;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int total = correctCount + wrongCount + skippedCount;
        int width = getWidth();
        int height = getHeight();

        float padding = strokeWidth / 2f + 8f;
        float size = Math.min(width, height) - padding * 2;
        float left = (width - size) / 2f;
        float top = (height - size) / 2f;
        oval.set(left, top, left + size, top + size);

        if (total == 0) {
            // Draw empty ring
            canvas.drawArc(oval, 0, 360, false, paintSkipped);
            canvas.drawText("—", width / 2f, height / 2f, paintCenterTextPercent);
            canvas.drawText("Chưa có", width / 2f, height / 2f + 40f, paintCenterTextLabel);
            return;
        }

        float sweepCorrect = (correctCount / (float) total) * 360f;
        float sweepWrong = (wrongCount / (float) total) * 360f;
        float sweepSkipped = (skippedCount / (float) total) * 360f;

        float startAngle = -90f;

        if (sweepCorrect > 0) {
            canvas.drawArc(oval, startAngle, sweepCorrect, false, paintSuccess);
            startAngle += sweepCorrect;
        }
        if (sweepWrong > 0) {
            canvas.drawArc(oval, startAngle, sweepWrong, false, paintError);
            startAngle += sweepWrong;
        }
        if (sweepSkipped > 0) {
            canvas.drawArc(oval, startAngle, sweepSkipped, false, paintSkipped);
        }

        // Center Text: Percentage of correct over answered questions
        int scoredQuestions = correctCount + wrongCount;
        int percent = scoredQuestions > 0 ? (int) Math.round((correctCount / (double) scoredQuestions) * 100) : 0;

        canvas.drawText(percent + "%", width / 2f, height / 2f, paintCenterTextPercent);
        canvas.drawText("Đúng", width / 2f, height / 2f + 42f, paintCenterTextLabel);
    }
}
