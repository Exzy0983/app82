package com.cookandroid.app82;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // 타이틀바 제거
        setContentView(R.layout.activity_main);

        TextView dateBar = findViewById(R.id.dateBar);
        dateBar.setText(java.text.DateFormat.getDateInstance().format(new java.util.Date()));

        Button happyButton = findViewById(R.id.happyButton);
        happyButton.setOnClickListener(v -> openDiary("Happy"));

        Button sadButton = findViewById(R.id.sadButton);
        sadButton.setOnClickListener(v -> openDiary("Sad"));

        Button neutralButton = findViewById(R.id.neutralButton);
        neutralButton.setOnClickListener(v -> openDiary("Neutral"));

        Button angryButton = findViewById(R.id.angryButton);
        angryButton.setOnClickListener(v -> openDiary("Angry"));

        Button excitedButton = findViewById(R.id.excitedButton);
        excitedButton.setOnClickListener(v -> openDiary("Excited"));

        Button navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(v -> openHome());

        Button navHistory = findViewById(R.id.navHistory);
        navHistory.setOnClickListener(v -> openHistory());

        Button navCalendar = findViewById(R.id.navCalendar);
        navCalendar.setOnClickListener(v -> openCalendar());

        Button navStatistics = findViewById(R.id.navStatistics);
        navStatistics.setOnClickListener(v -> openStatistics());
    }

    private void openDiary(String emotion) {
        Intent intent = new Intent(this, DiaryActivity.class);
        intent.putExtra("emotion", emotion);
        startActivity(intent);
    }

    private void openHome() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void openHistory() {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    private void openCalendar() { startActivity(new Intent(this, CalendarActivity.class)); }

    private void openStatistics() {
        startActivity(new Intent(this, StatisticsActivity.class));
    }
}
