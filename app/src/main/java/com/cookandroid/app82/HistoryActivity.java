package com.cookandroid.app82;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // ListView 연결
        ListView historyListView = findViewById(R.id.historyListView);

        // SQLite에서 데이터 가져오기
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        ArrayList<String> diaryList = dbHelper.getAllDiaries();

        // 로그로 데이터 확인
        Log.d(TAG, "Loaded diaries: " + diaryList);

        // 데이터가 없는 경우 처리
        if (diaryList == null || diaryList.isEmpty()) {
            Toast.makeText(this, "No diaries found.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No data found in database.");
        } else {
            // ArrayAdapter를 사용하여 ListView에 데이터 설정
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1, // Android 기본 레이아웃
                    diaryList
            );
            historyListView.setAdapter(adapter);
        }
    }
}
