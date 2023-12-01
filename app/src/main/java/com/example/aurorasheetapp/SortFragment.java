package com.example.aurorasheetapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SortFragment extends DialogFragment {
    private Button btnOpenDatePicker;
    private TextView date;


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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_fragment, container, false);
        btnOpenDatePicker = view.findViewById(R.id.d_filter);
        date = view.findViewById(R.id.filter_date);

        btnOpenDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        return view;
    }

    private void showDatePickerDialog() {
        DataFilter datePicker = new DataFilter();
        datePicker.setOnDateRangeSelectedListener((startYear, startMonth, startDay, endYear, endMonth, endDay) -> {
            ((MainActivity)getActivity()).filterItemsByDate(startYear, startMonth, startDay, endYear, endMonth, endDay);
        });
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private String formatDateString(int year, int month, int day) {
        return String.format("%02d/%02d/%02d", day, month + 1, year % 100);
    }
}
