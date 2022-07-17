package com.example.notes.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.notes.MainActivity;
import com.example.notes.R;
import com.example.notes.data.CardData;
import com.example.notes.observer.Publisher;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_DATA = "Param_CardData";

    private CardData cardData;
    private Publisher publisher;

    private TextInputEditText title;
    private TextInputEditText description;
    private DatePicker datePicker;

    public static CardFragment newInstance(CardData cardData) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_DATA, cardData);
        fragment.setArguments(args);
        return fragment;
    }

    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardData = getArguments().getParcelable(ARG_CARD_DATA);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initView(view);
        if (cardData != null) {
            populateView();
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        cardData = collectCardData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(cardData);
    }

    private CardData collectCardData(){
        String title = Objects.requireNonNull(this.title.getText()).toString();
        String description = Objects.requireNonNull(this.description.getText()).toString();
        Date date = getDateFromDatePicker();
        if (cardData != null){
            CardData answer;
            answer = new CardData(title, description, date);
            answer.setId(cardData.getId());
            return answer;
        } else {
            return new CardData(title, description, date);
        }
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }

    private void initView(View view) {
        title = view.findViewById(R.id.inputTitle);
        description = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
    }

    private void populateView(){
        title.setText(cardData.getTitle());
        description.setText(cardData.getDescription());
        initDatePicker(cardData.getDate());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }
}
