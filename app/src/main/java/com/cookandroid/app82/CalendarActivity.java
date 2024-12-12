package com.cookandroid.app82;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private static final String SAD = "Sad";
    private static final String HAPPY = "Happy";
    private static final String NEUTRAL = "Neutral";
    private static final String ANGRY = "Angry";
    private static final String EXCITED = "Excited";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dbHelper = new DatabaseHelper(this);

        GridLayout calendarGrid = findViewById(R.id.calendarGrid);
        TextView todayDate = findViewById(R.id.todayDate);

        // 오늘 날짜 표시
        Calendar calendar = Calendar.getInstance();
        todayDate.setText("Today: " + DateFormat.getDateInstance().format(calendar.getTime()));

        // 캘린더 구성
        int daysInMonth = 31; // 12월 고정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());

        for (int day = 1; day <= daysInMonth; day++) {
            TextView dayView = new TextView(this);
            dayView.setText(String.valueOf(day));
            dayView.setGravity(Gravity.CENTER);
            dayView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24); // 크기 증가
            dayView.setPadding(24, 24, 24, 24); // 패딩 증가
            dayView.setBackgroundColor(Color.WHITE);
            dayView.setTextColor(Color.BLACK);

            // 날짜 문자열 생성
            calendar.set(2024, Calendar.DECEMBER, day);
            String dateString = sdf.format(calendar.getTime());

            // 데이터베이스에서 감정 가져오기
            String emotion = dbHelper.getEmotionForDate(dateString);
            if (emotion != null) {
                dayView.setBackgroundColor(getColorForEmotion(emotion));
                dayView.setTextColor(Color.WHITE); // 텍스트 대비 색상
            }

            // GridLayout에 추가
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setMargins(6, 6, 6, 6); // 칸 간격
            params.rowSpec = GridLayout.spec((day - 1) / 7); // 행 계산
            params.columnSpec = GridLayout.spec((day - 1) % 7); // 열 계산
            dayView.setLayoutParams(params);

            calendarGrid.addView(dayView);
        }
    }

    // 감정에 따른 색상 반환
    private int getColorForEmotion(String emotion) {
        switch (emotion) {
            case SAD:
                return Color.BLUE;
            case HAPPY:
                return Color.YELLOW;
            case NEUTRAL:
                return Color.GREEN;
            case ANGRY:
                return Color.RED;
            case EXCITED:
                return Color.MAGENTA;
            default:
                return Color.GRAY; // 기본값
        }
    }
}
