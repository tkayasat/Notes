package com.example.notes.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.cards.CardsSource;
import com.example.notes.cards.CardsSourceImpl;
import com.example.notes.cards.Note;
import com.example.notes.holer.ViewHolderAdapter;

public class FragmentNotes extends Fragment {

    public static final String CURRENT_NOTE = "CurrentNote";
    private int mLustSelectedPosition = -1;
    private Note currentNote;
    private boolean isLandscape;
    private CardsSource mCardDataSource;
    private ViewHolderAdapter mViewHolderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        DividerItemDecoration decoration = new DividerItemDecoration(requireActivity(),
                LinearLayoutManager.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.decorate));
        recyclerView.addItemDecoration(decoration);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        mCardDataSource = CardsSourceImpl.getInstance(getResources());
        mViewHolderAdapter = new ViewHolderAdapter(this, mCardDataSource);
        mViewHolderAdapter.setOnClickListener((v, position) -> {
            currentNote = new Note(getResources().getStringArray(R.array.notes_names)[position],
                    getResources().getStringArray(R.array.notes_descriptions)[position]);
            showNoteContent(currentNote);
        });
        recyclerView.setAdapter(mViewHolderAdapter);

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_list_add_new) {
            mCardDataSource.add(new Note("New note (edit me)", "edit me!!!"));
            int position = mCardDataSource.size() - 1;
            mViewHolderAdapter.notifyItemInserted(position);
            ((RecyclerView) getView()).scrollToPosition(position);
        } else if (item.getItemId() == R.id.menu_list_clear_all_note) {
            mCardDataSource.clear();
            mViewHolderAdapter.notifyDataSetChanged();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.note_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.context_note_edit) {
            if (mLustSelectedPosition != 1) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.notes_fragment_container, FragmentNoteEditor.newInstance(mLustSelectedPosition));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (item.getItemId() == R.id.context_note_delete) {
            if (mLustSelectedPosition != 1) {
                mCardDataSource.remove(mLustSelectedPosition);
                mViewHolderAdapter.notifyItemRemoved(mLustSelectedPosition);
            }
        } else {
            return super.onContextItemSelected(item);
        }
        return true;
    }

    public interface OnClickListener {
        void onItemClick(View v, int position);
    }

    public void setLustSelectedPosition(int lustSelectedPosition) {
        mLustSelectedPosition = lustSelectedPosition;
    }
}