package com.example.aurorasheetapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SortFragment extends DialogFragment {
    private TextView date;
    private Button btnConfirm;
    private Spinner spinner;
    private RadioButton radioAscending, radioDescending;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;

    public interface OnDateRangeSelectedListener {
        void onDateRangeSelected(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay, String filterBy, String descriptionKeyword, String make, String sortOrder);
    }

    private OnDateRangeSelectedListener mListener;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDateRangeSelectedListener) {
            mListener = (OnDateRangeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDateRangeSelectedListener");
        }
    }

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
        View view = inflater.inflate(R.layout.sort_frag, container, false);

        date = view.findViewById(R.id.filter_date);
        btnConfirm = view.findViewById(R.id.done);
        spinner = view.findViewById(R.id.my_spinner);
        radioAscending = view.findViewById(R.id.radioAscending);
        radioDescending = view.findViewById(R.id.radioDescending);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_options, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedOption = spinner.getSelectedItem().toString();
                String descriptionKeyword = ((EditText) view.findViewById(R.id.filter_keyword)).getText().toString();
                String make = ((EditText) view.findViewById(R.id.filter_make)).getText().toString();
                String sortOrder = radioAscending.isChecked() ? "Ascending" : "Descending";

                if (mListener != null) {
                    mListener.onDateRangeSelected(startYear, startMonth, startDay, endYear, endMonth, endDay, selectedOption, descriptionKeyword, make, sortOrder);
                }
                dismiss();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Add functionality to the reset button
        Button resetButton = view.findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset logic here
            }
        });

        return view;
    }

    private void showDatePickerDialog() {
        DatePicker datePicker = new DatePicker();
        datePicker.setOnDateRangeSelectedListener(new DatePicker.OnDateRangeSelectedListener() {
            @Override
            public void onDateRangeSelected(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
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

        datePicker.show(getParentFragmentManager(), "datePicker");
    }

    private String formatDateString(int year, int month, int day) {
        return String.format("%02d/%02d/%04d", day, month + 1, year);
    }
}

