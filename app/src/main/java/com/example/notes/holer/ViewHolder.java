package com.example.notes.holer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.cards.Note;
import com.example.notes.fragment.FragmentNotes;

public class ViewHolder extends RecyclerView.ViewHolder {

    public final TextView text;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.textView);
    }

    public void populate(FragmentNotes fragment, Note note) {
        text.setText(note.getNoteName());
        itemView.setOnLongClickListener((v) -> {
            fragment.setLustSelectedPosition(getLayoutPosition());
            return false;
        });
        fragment.registerForContextMenu(itemView);
    }

    public void clear(Fragment fragment) {
        itemView.setOnLongClickListener(null);
        fragment.unregisterForContextMenu(itemView);
    }
}