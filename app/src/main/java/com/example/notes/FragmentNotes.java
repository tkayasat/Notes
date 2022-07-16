package com.example.notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class FragmentNotes extends Fragment {

    public static final String CURRENT_NOTE = "CurrentNote";
    private Note currentNote;
    private boolean isLandscape;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.notes_names);

        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            TextView tv = new TextView(getContext());
            tv.setText(note);
            tv.setTextSize(30);
            layoutView.addView(tv);

            final int fi = i;
            tv.setOnClickListener(v -> {
                currentNote = new Note(getResources().getStringArray(R.array.notes_names)[fi],
                        getResources().getStringArray(R.array.notes_descriptions)[fi],
                        getResources().getStringArray(R.array.notes_date)[fi]);
                showNoteContent(currentNote);
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = new Note(getResources().getStringArray(R.array.notes_names)[0],
                    getResources().getStringArray(R.array.notes_descriptions)[0],
                    getResources().getStringArray(R.array.notes_date)[0]);
        }
        if (isLandscape) {
            showLandscapeContentNote(currentNote);
        }
    }

    private void showNoteContent(Note currentNote) {
        if (isLandscape) {
            showLandscapeContentNote(currentNote);
        } else {
            showPortraitContentNote(currentNote);
        }
    }

    private void showLandscapeContentNote(Note currentNote) {

        FragmentNoteContent detail = FragmentNoteContent.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_content, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortraitContentNote(Note currentNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ActivityNoteContent.class);
        intent.putExtra(FragmentNoteContent.ARG_NOTE, currentNote);
        startActivity(intent);
    }
}
