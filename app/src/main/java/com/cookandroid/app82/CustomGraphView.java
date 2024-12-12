package com.cookandroid.app82;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class CustomGraphView extends View {
    private Map<String, Integer> data = new HashMap<>();
    private Paint barPaint;
    private Paint textPaint;

    public CustomGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / data.size();

        int maxCount = 0;
        for (int value : data.values()) {
            if (value > maxCount) maxCount = value;
        }

        int x = 0;
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / (float) maxCount) * height);

            // 감정별 색상 지정
            switch (entry.getKey()) {
                case "Happy":
                    barPaint.setColor(Color.YELLOW);
                    break;
                case "Sad":
                    barPaint.setColor(Color.BLUE);
                    break;
                case "Neutral":
                    barPaint.setColor(Color.GRAY);
                    break;
                case "Angry":
                    barPaint.setColor(Color.RED);
                    break;
                case "Excited":
                    barPaint.setColor(Color.GREEN);
                    break;
                default:
                    barPaint.setColor(Color.BLACK);
                    break;
            }

            // Draw Bar
            canvas.drawRect(
                    x,
                    height - barHeight,
                    x + barWidth - 20,
                    height,
                    barPaint
            );

            // Draw Label
            canvas.drawText(
                    entry.getKey(),
                    x + (barWidth / 4f),
                    height - 10,
                    textPaint
            );

            x += barWidth;
        }
    }
}