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

    /**
     * Sets the listener for the date range selection. The provided listener will be notified
     * when the user has selected both the start and end dates.
     *
     * @param listener The listener that will be called with the selected date range.
     */
    public void setOnDateRangeSelectedListener(OnDateRangeSelectedListener listener) {
        this.listener = listener;
    }



    /**
     * Creates and returns a DatePickerDialog instance. This dialog allows users to select a date range (start and end dates).
     * Once the start date is selected, another DatePickerDialog is shown for the end date. After both dates are selected,
     * the listener is notified with the chosen date range.
     *
     * @param savedInstanceState If the dialog is being reinitialized after a prior shutdown, this bundle
     *                           contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * @return Returns a new dialog instance to be displayed by the fragment.
     */
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


