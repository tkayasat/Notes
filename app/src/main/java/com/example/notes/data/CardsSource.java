package com.example.notes.data;

import androidx.annotation.NonNull;

import com.example.notes.cards.Note;

import java.util.List;

public interface CardsSource {
    CardsSource init(CardsSourceResponse cardsSourceResponse);
    CardData getCardData(int position);
    int size();
    void deleteCardData(int position);
    void updateCardData(int position, CardData cardData);
    void addCardData(CardData cardData);
    void clearCardData();
}