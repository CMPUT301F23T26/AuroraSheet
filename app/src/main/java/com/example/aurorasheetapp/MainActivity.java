package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Item> listItems;

    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        listItems = new ArrayList<>();

// just for texting you can delete later
        for (int i=0;i<10;i++){
            Item listItem = new Item(
                    +i+1,
                    "good " +i+1,
                    "wooden "+i+1,
                    20 + i+1,
                    "tfd "+ i+1,
                    13 +i+1,
                    "nahh "+i+1
            );
            listItems.add(listItem);
        }

        adapter = new CustomArrayAdapter(listItems,this);
        recyclerView.setAdapter(adapter);

        totalAmountTextView = findViewById(R.id.totalValue);
        addButton = findViewById(R.id.buttonAdd);

        // navigate to the add item activity on click of the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                addItemLauncher.launch(intent);
            }
        });

        // display total value for all the items
        totalAmountTextView = findViewById(R.id.totalValue);
        totalAmountTextView.setText(computeTotal());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            handleAddItemResult(data);
        }
    }


    // move this to item list class?
    public String computeTotal() {
        double total = 0;
        for (Item item : listItems) {
            total += item.getEstimatedValue();
        }
        return String.format("%.2f", total);
    }

    private void handleAddItemResult(Intent data) {
        if (data != null) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            String value = data.getStringExtra("value");
            String make = data.getStringExtra("make");
            String model = data.getStringExtra("model");
            String comment = data.getStringExtra("comment");

            Item listItem = new Item(
                    1,
                    name,
                    description,
                    Integer.parseInt(value),
                    make,
                    1,
                    comment
            );
            listItems.add(listItem);
            adapter.notifyDataSetChanged();
            totalAmountTextView.setText(computeTotal());
        }
    }


    private final ActivityResultLauncher<Intent> addItemLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            if (data != null) {
                                handleAddItemResult(data);
                            }
                        }
                    });

}