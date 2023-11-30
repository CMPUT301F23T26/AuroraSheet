package com.example.aurorasheetapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class sortingActivity extends AppCompatActivity {

    // 0 means off. 1 means ascending. 2 means descending.
    private RadioButton[][] allbuttons;

    private RadioGroup[] allgroups;
    private int[] selections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sorting_list_activity);


        allbuttons = new RadioButton[][]
                {
                    {findViewById(R.id.offname), findViewById(R.id.ascendingname), findViewById(R.id.descendingname)},
                    {findViewById(R.id.offdate), findViewById(R.id.ascendingdate), findViewById(R.id.descendingdate)},
                    {findViewById(R.id.offvalue), findViewById(R.id.ascendingvalue), findViewById(R.id.descendingvalue)},
                    {findViewById(R.id.offmake), findViewById(R.id.ascendingmake), findViewById(R.id.descendingmake)},
                    {findViewById(R.id.offdescription), findViewById(R.id.ascendingdescription), findViewById(R.id.descendingdescription)}
                };

        allgroups = new RadioGroup[] {findViewById(R.id.namegroup), findViewById(R.id.dategroup), findViewById(R.id.valuegroup), findViewById(R.id.makegroup), findViewById(R.id.descgroup)};

        selections = new int[] {0,0,0,0,0};



        for (int i = 0; i < 5; i++) {
            int finalI = i;
            allgroups[i].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == allbuttons[finalI][2].getId()) { selections[finalI] = 2;}
                    else if (checkedId == allbuttons[finalI][1].getId()) { selections[finalI] = 1;}
                    else { selections[finalI] = 0;}

                }
            });

        }
    }


    public int[] getSelections() {
        return selections;
    }
}