package com.example.tasks;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show the notes fragment when the activity is created
        if (!isLoggedIn()) {
            // If user is not logged in, show login fragment
            showLoginFragment();
        } else {
            // If user is logged in, show notes fragment
            showNotesFragment();
            }


    }

    // Method to show the notes fragment
    private void showNotesFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NotesFragment())
                .commit();
    }

    private void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .commit();
    }

    private boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void logout() {
        // Update the login status to false
        setLoggedIn(false);
        showLoginFragment();
    }

    // Method to navigate to the notes fragment
    public void navigateToNotesFragment() {
        showNotesFragment();
    }

    // Method to navigate to the add note fragment
    public void navigateToAddNoteFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddNoteFragment())
                .addToBackStack(null)
                .commit();
    }

    // Method to navigate to the edit note fragment
    public void navigateToEditNoteFragment(int noteId) {
        // Pass the note ID to the edit note fragment
        EditNoteFragment editNoteFragment = EditNoteFragment.newInstance(noteId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, editNoteFragment)
                .addToBackStack(null)
                .commit();
    }
}
