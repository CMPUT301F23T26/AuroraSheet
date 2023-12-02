package com.example.aurorasheetapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
        View view = inflater.inflate(R.layout.sort_frag, container, false);
        date = view.findViewById(R.id.filter_date);
        btnConfirm = view.findViewById(R.id.done);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });



        Button resetButton = view.findViewById(R.id.reset); // Adjust ID as needed




        return view;
    }

    private void showDatePickerDialog() {
        DatePicker datePicker = new DatePicker();
        datePicker.setOnDateRangeSelectedListener(new DatePicker.OnDateRangeSelectedListener() {
            @Override
            public void onDateRangeSelected(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
                // Save the selected dates
                SortFragment.this.startYear = startYear;
                SortFragment.this.startMonth = startMonth;
                SortFragment.this.startDay = startDay;
                SortFragment.this.endYear = endYear;
                SortFragment.this.endMonth = endMonth;
                SortFragment.this.endDay = endDay;

                String startDate = formatDateString(startYear, startMonth, startDay);
                String endDate = formatDateString(endYear, endMonth, endDay);
                date.setText(startDate + " - " + endDate);
            }
        });
    }
    private String formatDateString(int year, int month, int day) {
        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }


}




