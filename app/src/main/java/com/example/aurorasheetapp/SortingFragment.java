package com.example.aurorasheetapp;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class SortingFragment extends DialogFragment {
    private TextView PageTitle;
    private Button btnConfirm;

    private CheckBox namecheckbox;
    private CheckBox datecheckbox;
    private CheckBox valuecheckbox;
    private CheckBox makecheckbox;
    private CheckBox desccheckbox;

    private Switch nameswitch;
    private Switch dateswitch;
    private Switch valueswitch;
    private Switch makeswitch;
    private Switch descswitch;




    // this imitates the class found in Mehreen's code for the mListener for the FilterFragment
    public interface OnSortingConfirmListener {
        void receiveSortingData(int nameMode, int dateMode, int valueMode, int makeMode, int descMode);
    }

    private OnSortingConfirmListener mListener;

    /**
     * Connects the Sorting Fragment to a context, where it wil send its data upon clicking confirm
     *
     * @param context the context that called this sorting fragment into being
     */
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSortingConfirmListener) {
            mListener = (OnSortingConfirmListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDateRangeSelectedListener");
        }
    }


    // Copied from Mehreen's code. Part of the Fragment interface.
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sorting_frag, container, false);


        btnConfirm = view.findViewById(R.id.done);

        namecheckbox = view.findViewById(R.id.NameCheckBox);
        datecheckbox = view.findViewById(R.id.DateCheckBox);
        valuecheckbox = view.findViewById(R.id.ValueCheckBox);
        makecheckbox = view.findViewById(R.id.MakeCheckBox);
        desccheckbox = view.findViewById(R.id.DescCheckBox);

        nameswitch = view.findViewById(R.id.NameSwitch);
        dateswitch = view.findViewById(R.id.DateSwitch);
        valueswitch = view.findViewById(R.id.ValueSwitch);
        makeswitch = view.findViewById(R.id.MakeSwitch);
        descswitch = view.findViewById(R.id.DescSwitch);


        // if the buttons are on, show the ascending/descending switch. otherwise, hide it
        namecheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { nameswitch.setVisibility(VISIBLE); }
            else {nameswitch.setVisibility(INVISIBLE); }
        });
        datecheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { dateswitch.setVisibility(VISIBLE); }
            else {dateswitch.setVisibility(INVISIBLE); }
        });
        valuecheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { valueswitch.setVisibility(VISIBLE); }
            else {valueswitch.setVisibility(INVISIBLE); }
        });
        makecheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { makeswitch.setVisibility(VISIBLE); }
            else {makeswitch.setVisibility(INVISIBLE); }
        });
        desccheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { descswitch.setVisibility(VISIBLE); }
            else {descswitch.setVisibility(INVISIBLE); }
        });


        // to make something easier, we will turn off the button everytime this fragment comes on
        nameswitch.setChecked(false); dateswitch.setChecked(false); valueswitch.setChecked(false); makeswitch.setChecked(false); descswitch.setChecked(false);

        
        
        
        
        
        nameswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { nameswitch.setText(R.string.descending); }
            else {nameswitch.setText(R.string.ascending); }
        });
        dateswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { dateswitch.setText(R.string.descending); }
            else {dateswitch.setText(R.string.ascending); }
        });
        valueswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { valueswitch.setText(R.string.descending); }
            else {valueswitch.setText(R.string.ascending); }
        });
        makeswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { makeswitch.setText(R.string.descending); }
            else {makeswitch.setText(R.string.ascending); }
        });
        descswitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { descswitch.setText(R.string.descending); }
            else {descswitch.setText(R.string.ascending); }
        });
        



        btnConfirm.setOnClickListener(v -> {
            // 0 means do not sort by this feature.
            // 1 means sort ascending.
            // 2 means sort descending.

            int nameMode = 0;
            int dateMode = 0;
            int valueMode = 0;
            int makeMode = 0;
            int descMode = 0;


            if (namecheckbox.isChecked()) {
                if (nameswitch.isChecked()) { nameMode = 2; } else { nameMode = 1; }
            }
            if (datecheckbox.isChecked()) {
                if (dateswitch.isChecked()) { dateMode = 2; } else { dateMode = 1; }
            }
            if (valuecheckbox.isChecked()) {
                if (valueswitch.isChecked()) { valueMode = 2; } else { valueMode = 1; }
            }
            if (makecheckbox.isChecked()) {
                if (makeswitch.isChecked()) { makeMode = 2; } else { makeMode = 1; }
            }
            if (desccheckbox.isChecked()) {
                if (descswitch.isChecked()) { descMode = 2; } else { descMode = 1; }
            }




            if (mListener != null) {
                mListener.receiveSortingData(nameMode,dateMode,valueMode,makeMode,descMode);
            }
            dismiss();
        });

        return view;
    }




}

