package com.cookandroid.app82;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getEmotionStatistics();

        Map<String, Integer> graphData = new HashMap<>();
        StringBuilder statisticsData = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String emotion = cursor.getString(0);
                int count = cursor.getInt(1);
                graphData.put(emotion, count);

                statisticsData.append(emotion).append(": ").append(count).append(" times\n");
            } while (cursor.moveToNext());
        }

        cursor.close();

        // Update TextView
        TextView statisticsView = findViewById(R.id.statisticsView);
        statisticsView.setText(statisticsData.toString());

        // Update Graph
        CustomGraphView customGraphView = findViewById(R.id.customGraphView);
        customGraphView.setData(graphData);
    }
}
