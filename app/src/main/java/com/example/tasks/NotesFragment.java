package com.example.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<Note> notes;
    private NoteDatabaseHelper databaseHelper;
    private Button addNoteButton;
    private TextView emptyTextView;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        emptyTextView = view.findViewById(R.id.emptyTextView);
        recyclerView = view.findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notes = new ArrayList<>();
        adapter = new RecyclerViewAdapter(notes, getContext());
        recyclerView.setAdapter(adapter);

        databaseHelper = new NoteDatabaseHelper(getContext());
        loadNotesFromDatabase();

        addNoteButton = view.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).navigateToAddNoteFragment();
            }
        });

        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).logout();
            }
        });

        return view;
    }

    private void loadNotesFromDatabase() {
        notes.clear();
        notes.addAll(databaseHelper.getAllNotes());
        adapter.notifyDataSetChanged();
        if (notes.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
