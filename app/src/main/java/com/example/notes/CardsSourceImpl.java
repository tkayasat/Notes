package com.example.notes;

import android.content.res.Resources;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardsSourceImpl implements CardsSource {
    private final LinkedList<Note> noteSource = new LinkedList<>();
    private Resources resources;

    public CardsSourceImpl(Resources resources) {
        this.resources = resources;
    }

    public CardsSourceImpl init() {
        String[] titles = resources.getStringArray(R.array.notes_names);
        String[] descriptions = resources.getStringArray(R.array.notes_descriptions);
        for (int i = 0; i < descriptions.length; i++) {
            noteSource.add(new Note(titles[i], descriptions[i]));
        }
        return this;
    }

    @Override
    public List<Note> getCardData() {
        return Collections.unmodifiableList(noteSource);
    }

    public Note getNote(int position) {
        return noteSource.get(position);
    }

    public int size() {
        return noteSource.size();
    }
}
