package com.example.tasks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class AddNoteFragment extends Fragment {

    private NoteDatabaseHelper databaseHelper;

    public AddNoteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        // Initialize database helper
        databaseHelper = new NoteDatabaseHelper(requireContext());

        final EditText titleEditText = view.findViewById(R.id.editTextTitle);
        final EditText contentEditText = view.findViewById(R.id.editTextContent);
        Button saveNoteButton = view.findViewById(R.id.buttonSaveNote);
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                if (!title.isEmpty() && !content.isEmpty()) {
                    saveNoteToDatabase(title, content);
                    getParentFragmentManager().popBackStack();
                    // You can also navigate back to the previous fragment/activity here
                    // or display a message confirming that the note has been saved
                }else{
                    Toast.makeText(getContext(), "Kindly check if title or content is empty!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void saveNoteToDatabase(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        databaseHelper.addNote(note);
    }
}
