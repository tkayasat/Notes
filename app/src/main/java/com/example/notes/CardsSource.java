package com.example.notes;

import java.util.List;

public interface CardsSource {
    List<Note> getCardData();
    Note getNote(int position);
    int size();
}