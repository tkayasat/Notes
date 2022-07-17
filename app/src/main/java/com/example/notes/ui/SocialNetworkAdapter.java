package com.example.notes.ui;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.data.CardData;
import com.example.notes.data.CardsSource;

import java.text.SimpleDateFormat;

public class SocialNetworkAdapter
        extends RecyclerView.Adapter<SocialNetworkAdapter.ViewHolder> {

    private final static String TAG = "SocialNetworkAdapter";
    private final Fragment fragment;
    private CardsSource dataSource;
    private OnItemClickListener itemClickListener;
    private int menuPosition;

    public SocialNetworkAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDataSource(CardsSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        Log.d(TAG, "onCreateViewHolder");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setData(dataSource.getCardData(i));
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView description;
        private final TextView date;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);

            registerContextMenu(itemView);

            title.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

            title.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.showContextMenu(10, 10);
                }
                return true;
            });
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(CardData cardData) {
            title.setText(cardData.getTitle());
            description.setText(cardData.getDescription());
            date.setText(new SimpleDateFormat("dd-MM-yy").format(cardData.getDate()));
        }
    }
}