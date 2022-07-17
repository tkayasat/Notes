package com.example.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MyBottomSheetDialogFragmentDeleteNote extends BottomSheetDialogFragment {

    private OnDialogListener dialogListener;

    public static MyBottomSheetDialogFragmentDeleteNote newInstance() {
        return new MyBottomSheetDialogFragmentDeleteNote();
    }

    public void setOnDialogListener(OnDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialogue_delete_note, container,
                false);

        setCancelable(false);

        view.findViewById(R.id.btnYes).setOnClickListener(view1 -> {
            dismiss();
            if (dialogListener != null) dialogListener.onDialogYes();
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(view12 -> {
            dismiss();
            if (dialogListener != null) dialogListener.onDialogCancel();
        });

        return view;
    }
}
