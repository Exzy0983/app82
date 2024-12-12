package com.cookandroid.app82;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DiaryActivity extends AppCompatActivity {

    private String selectedDate; // 사용자가 선택한 날짜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        // XML 요소 연결
        TextView dateField = findViewById(R.id.dateField); // 날짜를 표시할 TextView
        TextView emotionField = findViewById(R.id.emotionField); // 감정을 표시할 TextView
        EditText diaryContent = findViewById(R.id.diaryContent); // 사용자가 작성할 내용
        Button saveButton = findViewById(R.id.saveButton);
        Button pickDateButton = findViewById(R.id.pickDateButton);

        // 현재 날짜를 기본으로 설정
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        selectedDate = sdf.format(calendar.getTime());
        dateField.setText(selectedDate);

        // 감정 설정
        String emotion = getIntent().getStringExtra("emotion");
        if (emotion != null) {
            emotionField.setText(emotion);
        }

        // 날짜 선택 버튼
        pickDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        selectedDate = sdf.format(calendar.getTime());
                        dateField.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // 저장 버튼 클릭 시 저장 로직
        saveButton.setOnClickListener(v -> {
            String content = diaryContent.getText().toString();
            if (content.isEmpty()) {
                Toast.makeText(this, "Please write something!", Toast.LENGTH_SHORT).show();
            } else {
                saveDiary(content, emotion);
            }
        });
    }

    private void saveDiary(String content, String emotion) {
        if (emotion == null || emotion.isEmpty()) {
            Toast.makeText(this, "Emotion is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            boolean isSaved = dbHelper.saveDiary(selectedDate, emotion, content);

            if (isSaved) {
                Toast.makeText(this, "Diary Saved!", Toast.LENGTH_SHORT).show();
                Log.d("DiaryActivity", "Saved: " + selectedDate + ", " + emotion + ", " + content);
                finish();
            } else {
                Toast.makeText(this, "Failed to save diary.", Toast.LENGTH_SHORT).show();
                Log.e("DiaryActivity", "Failed to save diary to database.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DiaryActivity", "Error while saving diary: " + e.getMessage());
        }
    }
}
