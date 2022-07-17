package com.example.notes.data;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CardsSourceFirebaseImpl implements CardsSource {

    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[CardsSourceFirebaseImpl]";
    private final FirebaseFirestore store = FirebaseFirestore.getInstance();
    private final CollectionReference collection = store.collection(CARDS_COLLECTION);
    private List<CardData> cardsData = new ArrayList<>();

    @Override
    public CardsSource init(final CardsSourceResponse cardsSourceResponse) {
        collection.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        cardsData = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            CardData cardData = CardDataMapping.toCardData(id, doc);
                            cardsData.add(cardData);
                        }
                        Log.d(TAG, "success " + cardsData.size() + " qnt");
                        cardsSourceResponse.initialized(CardsSourceFirebaseImpl.this);
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "get failed with ", e));
        return this;
    }

    @Override
    public CardData getCardData(int position) {
        return cardsData.get(position);
    }

    @Override
    public int size() {
        if (cardsData == null) {
            return 0;
        }
        return cardsData.size();
    }

    @Override
    public void deleteCardData(int position) {
        collection.document(cardsData.get(position).getId()).delete();
        cardsData.remove(position);
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        String id = cardData.getId();
        collection.document(id).set(CardDataMapping.toDocument(cardData));
    }

    @Override
    public void addCardData(final CardData cardData) {
        collection.add(CardDataMapping.toDocument(cardData)).
                addOnSuccessListener(documentReference -> cardData.setId(documentReference.getId()));
    }

    @Override
    public void clearCardData() {
        for (CardData cardData : cardsData) {
            collection.document(cardData.getId()).delete();
        }
        cardsData = new ArrayList<CardData>();
    }
}