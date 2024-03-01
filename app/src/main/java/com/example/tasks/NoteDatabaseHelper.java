package com.example.tasks;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "NoteDatabaseHelper";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotesDB";
    private final String TABLE_NOTES;

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        TABLE_NOTES = "notes_" + userEmail.replace("@", "_").replace(".", "_");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createNotesTableIfNotExists(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    private void createNotesTableIfNotExists(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT" + ")";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_TITLE, note.getTitle());
                values.put(KEY_CONTENT, note.getContent());
                db.insert(TABLE_NOTES, null, values);
            } catch (Exception e) {
                Log.e(TAG, "Error adding note: " + e.getMessage());
            } finally {
                db.close();
            }
        }
    }

    public void updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_TITLE, note.getTitle());
                values.put(KEY_CONTENT, note.getContent());
                db.update(TABLE_NOTES, values, KEY_ID + " = ?",
                        new String[]{String.valueOf(note.getId())});
            } catch (Exception e) {
                Log.e(TAG, "Error updating note: " + e.getMessage());
            } finally {
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public Note getNoteById(int noteId) {
        SQLiteDatabase db = getReadableDatabase();
        Note note = null;
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT},
                        KEY_ID + "=?", new String[]{String.valueOf(noteId)}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                    note.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting note by ID: " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                db.close();
            }
        }
        return note;
    }

    @SuppressLint("Range")
    public List<Note> getAllNotes() {
        List<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        if (db != null) {
            try {
                createNotesTableIfNotExists(db); // Ensure table exists
                Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Note note = new Note();
                        note.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                        note.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                        note.setContent(cursor.getString(cursor.getColumnIndex(KEY_CONTENT)));
                        notesList.add(note);
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting all notes: " + e.getMessage());
            } finally {
                db.close();
            }
        }
        return notesList;
    }

    public void deleteNoteById(int noteId) {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) {
            try {
                db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(noteId)});
            } catch (Exception e) {
                Log.e(TAG, "Error deleting note by ID: " + e.getMessage());
            } finally {
                db.close();
            }
        }
    }
}
