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

public class EditNoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "noteId";

    private EditText titleEditText;
    private EditText contentEditText;
    private Button saveButton;

    private NoteDatabaseHelper databaseHelper;
    private int noteId;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    public static EditNoteFragment newInstance(int noteId) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getInt(ARG_NOTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        // Initialize views
        titleEditText = view.findViewById(R.id.editTextTitle);
        contentEditText = view.findViewById(R.id.editTextContent);
        saveButton = view.findViewById(R.id.buttonSaveNote);

        // Initialize database helper
        databaseHelper = new NoteDatabaseHelper(requireContext());

        // Load note details from the database
        loadNoteDetails();

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void loadNoteDetails() {
        // Retrieve note details from the database using the noteId
        Note note = databaseHelper.getNoteById(noteId);
        if (note != null) {
            // Populate the EditText fields with the note details
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
        } else {
            // Handle case where note is not found
            Toast.makeText(getContext(), "Note not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateNote() {
        // Get updated title and content from EditText fields
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        // Update the note in the database
        Note updatedNote = new Note();
        updatedNote.setId(noteId);
        updatedNote.setTitle(title);
        updatedNote.setContent(content);
        databaseHelper.updateNote(updatedNote);

        // Show toast message indicating successful update
        Toast.makeText(getContext(), "Note updated", Toast.LENGTH_SHORT).show();
    }
}
