package com.example.mad_login;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mad_login.Model.FeedbackApp;

public class FeedbackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "feedback_database";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_NAME = "feedback_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_COMMENTS = "comments";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING + " REAL, " +
                    COLUMN_COMMENTS + " TEXT, " +
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_TIMESTAMP + " INTEGER)";

    public FeedbackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Recreate the table with the new schema
        onCreate(db);
    }

    public long addFeedback(FeedbackApp feedback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, feedback.getRating());
        values.put(COLUMN_COMMENTS, feedback.getComments());
        values.put(COLUMN_USER_ID, feedback.getUid());
        values.put(COLUMN_TIMESTAMP, feedback.getTimestamp());

        long newRowId = db.insert(TABLE_NAME, null, values);

        return newRowId;
    }
}
