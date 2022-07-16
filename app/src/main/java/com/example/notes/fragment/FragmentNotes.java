package com.example.notes.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.CardsSource;
import com.example.notes.CardsSourceImpl;
import com.example.notes.Note;
import com.example.notes.R;


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
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);

        LayoutInflater ltInflater = getLayoutInflater();

        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.decorate));
        recyclerView.addItemDecoration(decoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        ViewHolderAdapter viewHolderAdapter = new ViewHolderAdapter(ltInflater,
                new CardsSourceImpl(getResources()).init());
        viewHolderAdapter.setOnClickListener((v, position) -> {
            currentNote = new Note(getResources().getStringArray(R.array.notes_names)[position],
                    getResources().getStringArray(R.array.notes_descriptions)[position]);
            showNoteContent(currentNote);
        });
        recyclerView.setAdapter(viewHolderAdapter);

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
                    getResources().getStringArray(R.array.notes_descriptions)[0]);
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
        fragmentTransaction.addToBackStack("note_fragment_list");
        fragmentTransaction.replace(R.id.note_content, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortraitContentNote(Note currentNote) {
        FragmentNoteContent detail = FragmentNoteContent.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("note_fragment_list");
        fragmentTransaction.replace(R.id.notes_fragment_container, detail);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView);
        }

        public void populate(Note note) {
            text.setText(note.getNoteName());
        }
    }

    private interface OnClickListener {
        void onItemClick(View v, int position);
    }

    private static class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final LayoutInflater mInflater;
        private final CardsSource mSource;

        private FragmentNotes.OnClickListener mOnClickListener;

        public ViewHolderAdapter(LayoutInflater inflater, CardsSource source) {
            mInflater = inflater;
            mSource = source;
        }

        public void setOnClickListener(FragmentNotes.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = mInflater.inflate(R.layout.item_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Note note = mSource.getNote(position);
            holder.populate(note);
            holder.itemView.setOnClickListener(v -> {
                if (mOnClickListener != null) {
                    mOnClickListener.onItemClick(v, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSource.size();
        }
    }
}
