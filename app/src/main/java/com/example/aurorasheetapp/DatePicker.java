package com.example.aurorasheetapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePicker extends DialogFragment {
    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startYear, int startMonth, int startDay,
                                 int endYear, int endMonth, int endDay);
    }

    private OnDateRangeSelectedListener listener;
    private boolean isStartDate = true; // To track if we are setting the start or end date

    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, selectedYear, selectedMonth, selectedDay) -> {
            if (isStartDate) {
                // User has selected the start date, now show the dialog for the end date
                isStartDate = false;
                new DatePickerDialog(getActivity(), (view2, endYear, endMonth, endDay) -> {
                    // Both start and end dates are selected
                    if (listener != null) {
                        listener.onDateRangeSelected(selectedYear, selectedMonth, selectedDay, endYear, endMonth, endDay);
                    }
                    isStartDate = true; // Reset for the next use
                }, year, month, day).show();
            }
        }, year, month, day);

        if (isStartDate) {
            datePickerDialog.setTitle("Select Start Date");
        } else {
            datePickerDialog.setTitle("Select End Date");
        }

        return datePickerDialog;
    }
}


