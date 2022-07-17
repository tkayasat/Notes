package com.example.notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.MainActivity;
import com.example.notes.MyBottomSheetDialogFragmentClearNotes;
import com.example.notes.MyBottomSheetDialogFragmentDeleteNote;
import com.example.notes.Navigation;
import com.example.notes.OnDialogListener;
import com.example.notes.R;
import com.example.notes.data.CardsSource;
import com.example.notes.data.CardsSourceFirebaseImpl;
import com.example.notes.observer.Publisher;

import java.util.Objects;

public class SocialNetworkFragment extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;
    private CardsSource data;
    private SocialNetworkAdapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static SocialNetworkFragment newInstance() {
        return new SocialNetworkFragment();
    }

    private final OnDialogListener dialogListenerClearNotes = new OnDialogListener() {
        @Override
        public void onDialogYes() {
            data.clearCardData();
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onDialogCancel() {
            onResume();
        }
    };

    private final OnDialogListener dialogListenerDeleteNote = new OnDialogListener() {
        @Override
        public void onDialogYes() {
            int deletePosition = adapter.getMenuPosition();
            data.deleteCardData(deletePosition);
            adapter.notifyItemRemoved(deletePosition);
        }

        @Override
        public void onDialogCancel() {
            onResume();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_socialnetwork, container, false);
        initView(view);
        setHasOptionsMenu(true);
        data = new CardsSourceFirebaseImpl().init(cardsData -> adapter.notifyDataSetChanged());
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cards_menu, menu);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "DefaultLocale"})
    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SocialNetworkAdapter(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(Objects.requireNonNull(getContext()), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.decorate, null));
        recyclerView.addItemDecoration(itemDecoration);
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);
        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onContextItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_add:
                navigation.addFragment(CardFragment.newInstance(), true);
                publisher.subscribe(cardData -> {
                    data.addCardData(cardData);
                    adapter.notifyItemInserted(data.size() - 1);
                    moveToFirstPosition = true;
                });
                return true;
            case R.id.action_update:
                final int updatePosition = adapter.getMenuPosition();
                navigation.addFragment(CardFragment.newInstance(data.getCardData(updatePosition)), true);
                publisher.subscribe(cardData -> {
                    data.updateCardData(updatePosition, cardData);
                    adapter.notifyItemChanged(updatePosition);
                });
                return true;
            case R.id.action_delete:
                MyBottomSheetDialogFragmentDeleteNote dialogFragment =
                        MyBottomSheetDialogFragmentDeleteNote.newInstance();
                dialogFragment.setOnDialogListener(dialogListenerDeleteNote);
                assert getFragmentManager() != null;
                dialogFragment.show(getFragmentManager(),
                        "dialog_fragment_delete");
                return true;
            case R.id.action_clear:
                MyBottomSheetDialogFragmentClearNotes dialogFragment2 =
                        MyBottomSheetDialogFragmentClearNotes.newInstance();
                dialogFragment2.setOnDialogListener(dialogListenerClearNotes);
                assert getFragmentManager() != null;
                dialogFragment2.show(getFragmentManager(),
                        "dialog_fragment_clear");
                return true;
        }
        return false;
    }
}