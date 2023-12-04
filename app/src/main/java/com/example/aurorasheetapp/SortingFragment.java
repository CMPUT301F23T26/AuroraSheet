package com.example.aurorasheetapp;

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
        
        nameswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                view.getContext();
            }
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
                if (nameswitch.isActivated()) { nameMode = 2; } else { nameMode = 1; }
            }
            if (datecheckbox.isChecked()) {
                if (dateswitch.isActivated()) { dateMode = 2; } else { dateMode = 1; }
            }
            if (valuecheckbox.isChecked()) {
                if (valueswitch.isActivated()) { valueMode = 2; } else { valueMode = 1; }
            }
            if (makecheckbox.isChecked()) {
                if (makeswitch.isActivated()) { makeMode = 2; } else { makeMode = 1; }
            }
            if (desccheckbox.isChecked()) {
                if (descswitch.isActivated()) { descMode = 2; } else { descMode = 1; }
            }




            if (mListener != null) {
                mListener.receiveSortingData(nameMode,dateMode,valueMode,makeMode,descMode);
            }
            dismiss();
        });

        return view;
    }




}

