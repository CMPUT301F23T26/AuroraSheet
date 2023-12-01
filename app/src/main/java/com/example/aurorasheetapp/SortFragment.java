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
    private TextView date;
    private Button btnConfirm;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

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
        date = view.findViewById(R.id.filter_date);
        btnConfirm = view.findViewById(R.id.done);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify MainActivity and close the fragment
                ((MainActivity)getActivity()).filterItemsByDate(startYear, startMonth, startDay, endYear, endMonth, endDay);
                dismiss();
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        DataFilter datePicker = new DataFilter();
        datePicker.setOnDateRangeSelectedListener((selectedStartYear, selectedStartMonth, selectedStartDay, selectedEndYear, selectedEndMonth, selectedEndDay) -> {
            // Save the selected dates
            startYear = selectedStartYear;
            startMonth = selectedStartMonth;
            startDay = selectedStartDay;
            endYear = selectedEndYear;
            endMonth = selectedEndMonth;
            endDay = selectedEndDay;

            // Update the TextView
            String startDate = formatDateString(startYear, startMonth, startDay);
            String endDate = formatDateString(endYear, endMonth, endDay);
            date.setText(startDate + " - " + endDate);
        });
        datePicker.show(getFragmentManager(), "datePicker");
    }

    private String formatDateString(int year, int month, int day) {
        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }
}
