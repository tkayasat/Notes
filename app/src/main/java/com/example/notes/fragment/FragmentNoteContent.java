package com.example.notes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.notes.Note;
import com.example.notes.R;

public class FragmentNoteContent extends Fragment {

    static final String ARG_NOTE = "note";
    private Note note;

    public static FragmentNoteContent newInstance(Note note) {
        FragmentNoteContent f = new FragmentNoteContent();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_content, container, false);
        TextView TextViewName = view.findViewById(R.id.note_name_tv);
        TextViewName.setText(note.getNoteName());
        TextView TextViewDescription = view.findViewById(R.id.note_description_tv);
        TextViewDescription.setText(note.getNoteDescription());
        TextView TextViewDate = view.findViewById(R.id.note_date_tv);
        TextViewDate.setText(note.getNoteDate());
        return view;
    }
}