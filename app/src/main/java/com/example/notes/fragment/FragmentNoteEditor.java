package com.example.notes.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.notes.R;
import com.example.notes.cards.CardsSourceImpl;
import com.example.notes.cards.Note;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentNoteEditor extends Fragment {

    private static final String ARG_ITEM_IDX = "FragmentNoteEditor.item_idx";

    private int mCurrentItemIdx = -1;

    public FragmentNoteEditor() {
    }
    public static FragmentNoteEditor newInstance(int currentItemIdx) {
        FragmentNoteEditor fragment = new FragmentNoteEditor();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_IDX, currentItemIdx);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentItemIdx = getArguments().getInt(ARG_ITEM_IDX, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_editor, container, false);

        final CardsSourceImpl source = CardsSourceImpl.getInstance(getResources());
        Note noteId = source.getNoteId(mCurrentItemIdx);

        final TextInputEditText editTextName = view.findViewById(R.id.note_name_edit);
        final TextInputEditText editTextDescription = view.findViewById(R.id.note_description_edit);

        final MaterialButton btnSave = view.findViewById(R.id.btn_save);

        btnSave.setOnClickListener((v) -> {
            noteId.setNoteName(editTextName.getText().toString());
            noteId.setNoteDescription(editTextDescription.getText().toString());

            getFragmentManager().popBackStack();
        });
        return view;
    }
}
