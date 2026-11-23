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

import java.util.ArrayList;
import java.util.List;

public class BarChartView extends View {

    public static class BarItem {
        public String label;
        public float valueMinutes;
        public boolean isCurrentDay;

        public BarItem(String label, float valueMinutes, boolean isCurrentDay) {
            this.label = label;
            this.valueMinutes = valueMinutes;
            this.isCurrentDay = isCurrentDay;
        }
    }

    private Paint paintPrimaryBar;
    private Paint paintCurrentDayBar;
    private Paint paintGrid;
    private Paint paintLabel;

    private List<BarItem> items = new ArrayList<>();
    private final RectF barRect = new RectF();
    private float cornerRadius = 12f;

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        cornerRadius = getResources().getDisplayMetrics().density * 6; // 6dp as per DESIGN.md

        paintPrimaryBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPrimaryBar.setColor(ContextCompat.getColor(getContext(), R.color.color_primary));

        paintCurrentDayBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCurrentDayBar.setColor(ContextCompat.getColor(getContext(), R.color.color_secondary));

        paintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintGrid.setColor(ContextCompat.getColor(getContext(), R.color.color_divider));
        paintGrid.setStrokeWidth(getResources().getDisplayMetrics().density * 1);

        paintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLabel.setTextAlign(Paint.Align.CENTER);
        paintLabel.setTextSize(getResources().getDisplayMetrics().scaledDensity * 11); // label_small
        paintLabel.setColor(ContextCompat.getColor(getContext(), R.color.color_text_secondary));
    }

    public void setItems(List<BarItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (items.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();

        float bottomPadding = getResources().getDisplayMetrics().density * 28;
        float topPadding = getResources().getDisplayMetrics().density * 16;
        float chartHeight = height - bottomPadding - topPadding;
        float baselineY = height - bottomPadding;

        // Baseline grid line
        canvas.drawLine(16f, baselineY, width - 16f, baselineY, paintGrid);

        // Find max value
        float maxVal = 10f; // min floor 10 min
        for (BarItem item : items) {
            if (item.valueMinutes > maxVal) {
                maxVal = item.valueMinutes;
            }
        }

        int count = items.size();
        float barSlotWidth = (width - 32f) / count;
        float barWidth = Math.min(barSlotWidth * 0.55f, getResources().getDisplayMetrics().density * 32);

        for (int i = 0; i < count; i++) {
            BarItem item = items.get(i);
            float centerX = 16f + (i + 0.5f) * barSlotWidth;

            float barH = (item.valueMinutes / maxVal) * chartHeight;
            if (barH < 4f && item.valueMinutes > 0) barH = 4f;

            float topY = baselineY - barH;
            barRect.set(centerX - barWidth / 2f, topY, centerX + barWidth / 2f, baselineY);

            Paint paint = item.isCurrentDay ? paintCurrentDayBar : paintPrimaryBar;
            canvas.drawRoundRect(barRect, cornerRadius, cornerRadius, paint);

            // Draw label below baseline
            canvas.drawText(item.label, centerX, baselineY + getResources().getDisplayMetrics().density * 16, paintLabel);
        }
    }
}
