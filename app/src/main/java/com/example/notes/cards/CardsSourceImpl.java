package com.example.notes.cards;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.example.notes.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardsSourceImpl implements CardsSource {
    private static final Object LOCK = new Object();
    private volatile static CardsSourceImpl sInstance;
    private final LinkedList<Note> mNoteSource = new LinkedList<>();

    public static CardsSourceImpl getInstance(Resources resources) {
        CardsSourceImpl instance = sInstance;
        if (instance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    instance = new CardsSourceImpl(resources);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private CardsSourceImpl(Resources resources) {
        String[] titles = resources.getStringArray(R.array.notes_names);
        String[] descriptions = resources.getStringArray(R.array.notes_descriptions);
        for (int i = 0; i < descriptions.length; i++) {
            mNoteSource.add(new Note(titles[i], descriptions[i]));
        }
    }

    @Override
    public List<Note> getCardData() {
        return Collections.unmodifiableList(mNoteSource);
    }

    public Note getNoteId(int position) {
        return mNoteSource.get(position);
    }

    public int size() {
        return mNoteSource.size();
    }

    @Override
    public void add(@NonNull Note note) {
        mNoteSource.add(note);
    }

    @Override
    public void clear() {
        mNoteSource.clear();
    }

    @Override
    public void remove(int position) {
        mNoteSource.remove(position);
    }
}