package com.cookandroid.app82;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "diary.db";
    private static final int DATABASE_VERSION = 4; // 버전 업데이트

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS diaries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT NOT NULL, " +
                "emotion TEXT NOT NULL, " +
                "content TEXT)";
        db.execSQL(createTableQuery);
        Log.d("DatabaseHelper", "Table 'diaries' created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS diaries");
        onCreate(db);
        Log.d("DatabaseHelper", "Table 'diaries' dropped and recreated.");
    }

    public boolean saveDiary(String date, String emotion, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("emotion", emotion);
        values.put("content", content);

        long result = db.insert("diaries", null, values);
        db.close();

        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert diary entry.");
        } else {
            Log.d("DatabaseHelper", "Diary entry inserted successfully: ID=" + result);
        }
        return result != -1;
    }

    public ArrayList<String> getAllDiaries() {
        ArrayList<String> diaryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT date, emotion, content FROM diaries", null);
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(0);
                    String emotion = cursor.getString(1);
                    String content = cursor.getString(2);
                    diaryList.add(date + ": " + emotion + " - " + content);
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseHelper", "No data found in the database.");
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while retrieving data: " + e.getMessage());
        } finally {
            db.close();
        }

        return diaryList;
    }

    public Cursor getEmotionStatistics() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT emotion, COUNT(*) FROM diaries GROUP BY emotion", null);
    }

    public Cursor getEmotionStatisticsForDateRange(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT emotion, COUNT(*) FROM diaries WHERE date BETWEEN ? AND ? GROUP BY emotion",
                new String[]{startDate, endDate}
        );
    }

    public HashMap<Integer, String> getEmotionByDate(int month, int year) {
        HashMap<Integer, String> emotionMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch emotions for the given month and year
        String query = "SELECT date, emotion FROM diaries WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.format("%02d", month), String.valueOf(year)
        });

        if (cursor.moveToFirst()) {
            do {
                String dateString = cursor.getString(0);
                String emotion = cursor.getString(1);

                // Extract day from the date (YYYY-MM-DD)
                int day = Integer.parseInt(dateString.split("-")[2]);
                emotionMap.put(day, emotion);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        Log.d("DatabaseHelper", "Emotion map retrieved: " + emotionMap);
        return emotionMap;
    }

    public void logAllDiaries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM diaries", null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String emotion = cursor.getString(cursor.getColumnIndexOrThrow("emotion"));
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));

                Log.d("DatabaseHelper", "Diary Entry: ID=" + id + ", Date=" + date + ", Emotion=" + emotion + ", Content=" + content);
            } while (cursor.moveToNext());
        } else {
            Log.d("DatabaseHelper", "No diary entries found.");
        }

        cursor.close();
        db.close();
    }

    public String getEmotionForDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String emotion = null;

        Cursor cursor = db.rawQuery("SELECT emotion FROM diaries WHERE date = ?", new String[]{date});
        if (cursor.moveToFirst()) {
            emotion = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return emotion;
    }
}
