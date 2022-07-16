package com.example.notes.cards;

import androidx.annotation.NonNull;

import java.util.List;

public interface CardsSource {
    List<Note> getCardData();
    Note getNoteId(int position);
    int size();

    void add(@NonNull Note note);
    void clear();
    void remove(int position);
}