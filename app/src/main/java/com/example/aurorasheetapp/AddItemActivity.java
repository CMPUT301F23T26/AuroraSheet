package com.example.aurorasheetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemName;
    private EditText itemDescription;
    private EditText itemValue;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemComment;
    private FloatingActionButton addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemValue = findViewById(R.id.itemValue);
        itemMake = findViewById(R.id.itemMake);
        itemModel = findViewById(R.id.itemModel);
        itemComment = findViewById(R.id.itemComment);
        addItemButton = findViewById(R.id.addItemButton);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all of the user input for adding a new item
                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                String value = itemValue.getText().toString();
                String make = itemMake.getText().toString();
                String model = itemModel.getText().toString();
                String comment = itemComment.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("value", value);
                intent.putExtra("make", make);
                intent.putExtra("model", model);
                intent.putExtra("comment", comment);
                setResult(1, intent);
                finish();
            }
        });
    }




}