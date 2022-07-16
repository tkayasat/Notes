package com.example.notes.holer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.cards.CardsSource;
import com.example.notes.cards.Note;
import com.example.notes.fragment.FragmentNotes;

public class ViewHolderAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final FragmentNotes mFragment;
    private final LayoutInflater mInflater;
    private final CardsSource mSource;

    private FragmentNotes.OnClickListener mOnClickListener;

    public ViewHolderAdapter(FragmentNotes fragment, CardsSource source) {
        mFragment = fragment;
        mInflater = fragment.getLayoutInflater();
        mSource = source;
    }

    public void setOnClickListener(FragmentNotes.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mSource.getNoteId(position);
        holder.populate(mFragment, note);
        holder.itemView.setOnClickListener(v -> {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.clear(mFragment);
    }

    @Override
    public int getItemCount() {
        return mSource.size();
    }
}
