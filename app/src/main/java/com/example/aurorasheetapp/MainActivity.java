package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Item> listItems;

    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
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
                          "item",
                    new ItemDate(i+12, i+1, i+1999),
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
        editButton = findViewById(R.id.buttonEdit);
        int itemIndex = 1;

        // navigate to the add item activity on click of the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                addItemLauncher.launch(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                launchEditData(intent, itemIndex);
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

    private void launchEditData(Intent intent, int i){
        if (intent != null){
            Item itemToEdit = listItems.get(i);
            intent.putExtra("name", itemToEdit.getName());
            intent.putExtra("value", itemToEdit.getEstimatedValue());
            intent.putExtra("time", itemToEdit.getDateOfPurchase().toString());
            intent.putExtra("make", itemToEdit.getMake());
            intent.putExtra("comment", itemToEdit.getComment());
            intent.putExtra("model", itemToEdit.getModel());
            intent.putExtra("description", itemToEdit.getBriefDescription());
            intent.putExtra("index", i);
            editItemLauncher.launch(intent);
        }
    }
    private void handleAddItemResult(Intent data) {
        if (data != null) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            String value = data.getStringExtra("value");
            String serial = data.getStringExtra("serial");
            String make = data.getStringExtra("make");
            String model = data.getStringExtra("model");
            String comment = data.getStringExtra("comment");

            Item listItem = new Item(

                    name,
                    new ItemDate(20, 12, 2012),
                    description,
                    make,
                    1,
                    model,
                    Integer.parseInt(value),
                    comment
            );
            listItems.add(listItem);
            adapter.notifyDataSetChanged();
            totalAmountTextView.setText(computeTotal());
        }
    }

    private void EditItemResult(Intent data) {
        if (data != null) {
            Boolean isDelete = data.getBooleanExtra("isDelete", false);
            if(isDelete){
                int index = data.getIntExtra("index", -1);
                if(index > -1){
                    listItems.remove(index);
                    adapter.notifyDataSetChanged();
                    totalAmountTextView.setText(computeTotal());
                }
            }
            else{
                String name = data.getStringExtra("name");
                String description = data.getStringExtra("description");
                String value = data.getStringExtra("value");
                String make = data.getStringExtra("make");
                String model = data.getStringExtra("model");
                String comment = data.getStringExtra("comment");
                ItemDate date = new ItemDate(data.getStringExtra("time"));
                int index = data.getIntExtra("index", -1);

                if(index != -1){
                    Item item = listItems.get(index);
                    item.setMake(make);
                    item.setComment(comment);
                    item.setName(name);
                    item.setEstimatedValue(Double.parseDouble(value));
                    item.setModel(model);
                    item.setDateOfPurchase(date);
                    item.setBriefDescription(description);
                }
                adapter.notifyDataSetChanged();
                totalAmountTextView.setText(computeTotal());
            }
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
    private final ActivityResultLauncher<Intent> editItemLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            if (data != null) {
                                EditItemResult(data);
                            }
                        }
                    });

    @Override
    public void onItemClick(int position) {
        // TODO
        // change this to go to view activity instead of add activity
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        addItemLauncher.launch(intent);
    }
}