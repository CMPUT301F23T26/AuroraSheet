package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity implements RecyclerViewInterface {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Item> listItems;

    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
    private FloatingActionButton deleteButton;


    private FloatingActionButton toggleSelectButton;
    private Boolean multiSelectMode;

    private FirebaseFirestore firestore;

    private int itemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access database
        firestore = FirebaseFirestore.getInstance();
        loadItemsFromFirestore();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        listItems = new ArrayList<>();

        adapter = new CustomArrayAdapter(listItems, this);
        recyclerView.setAdapter(adapter);

        totalAmountTextView = findViewById(R.id.totalValue);
        addButton = findViewById(R.id.buttonAdd);
        editButton = findViewById(R.id.buttonEdit);
        deleteButton = findViewById(R.id.buttonDelete);
        toggleSelectButton = findViewById(R.id.toggleSelectionButton);
        multiSelectMode = false;

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
        // toggle the selection mode
        toggleSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (multiSelectMode) {
                    exitMultiSelectMode();
                } else {
                    enterMultiSelectMode();
                }
            }
        });


        // display total value for all the items
        totalAmountTextView = findViewById(R.id.totalValue);
        totalAmountTextView.setText(computeTotal());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemIndex > -1) {
                    listItems.remove(itemIndex);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    // move computeTotal to item list class?
    /**
     * Computes the total value of all the items in the list.
     * @return A string representation of the total value of all the items.
     */
    public String computeTotal() {
        double total = 0;
        for (Item item : listItems) {
            total += item.getEstimatedValue();
        }
        return String.format("%.2f", total);
    }

    private void launchEditData(Intent intent, int i) {
        if (intent != null) {
            Item itemToEdit = listItems.get(i);
            intent.putExtra("name", itemToEdit.getName());
            intent.putExtra("value", itemToEdit.getEstimatedValue());
            intent.putExtra("time", itemToEdit.getDateOfPurchase().toString());
            intent.putExtra("make", itemToEdit.getMake());
            intent.putExtra("comment", itemToEdit.getComment());
            intent.putExtra("model", itemToEdit.getModel());
            intent.putExtra("serial", itemToEdit.getSerialNumber());
            intent.putExtra("description", itemToEdit.getBriefDescription());
            intent.putExtra("index", i);
            editItemLauncher.launch(intent);
        }
    }

    private void handleAddItemResult(Intent data) {
        if (data != null) {
            String name = data.getStringExtra("name");
            String description = data.getStringExtra("description");
            ItemDate date = new ItemDate(data.getStringExtra("date"));
            String value = data.getStringExtra("value");
            String serial = data.getStringExtra("serial");
            String make = data.getStringExtra("make");
            String model = data.getStringExtra("model");
            String comment = data.getStringExtra("comment");

            Item listItem = new Item(
                    name,
                    date,
                    description,
                    make,
                    Integer.parseInt(serial),
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
            if (isDelete) {
                int index = data.getIntExtra("index", -1);
                if (index > -1) {
                    listItems.remove(index);
                    adapter.notifyDataSetChanged();
                    totalAmountTextView.setText(computeTotal());
                }
            } else {
                String name = data.getStringExtra("name");
                String description = data.getStringExtra("description");
                String value = data.getStringExtra("value");
                String make = data.getStringExtra("make");
                String model = data.getStringExtra("model");
                String comment = data.getStringExtra("comment");
                ItemDate date = new ItemDate(data.getStringExtra("time"));
                Double serial = Double.parseDouble(data.getStringExtra("serial"));
                int index = data.getIntExtra("index", -1);

                if (index != -1) {
                    Item item = listItems.get(index);
                    item.setMake(make);
                    item.setComment(comment);
                    item.setName(name);
                    item.setEstimatedValue(Double.parseDouble(value));
                    item.setModel(model);
                    item.setDateOfPurchase(date);
                    item.setBriefDescription(description);
                    item.setSerialNumber(serial);
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
    /**
     * When an item in the list is clicked, this is called
     * @param position the index of the clicked item in the listItems
     */
    @Override
    public void onItemClick(int position) {
        Log.w("debug","there was a click after all");
        itemIndex = position;

        if (multiSelectMode) {
            Log.w("debug","item clicked while in select mode");
            listItems.get(position).toggleSelect();
            adapter.notifyDataSetChanged();
        }
        else {
            //shows two buttons once clicked
            Log.w("debug","item clicked while not in select mode");
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Prepare the activity for item selection mode
     *
     */
    public void enterMultiSelectMode() {
        multiSelectMode = true;
        // hide the edit button; show the delete button
        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        toggleSelectButton.setBackgroundColor(Color.argb(255, 200, 200, 255));
    }
    /**
     * Have the activity exit item selection mode and reset to default behaviour
     *
     */
    public void exitMultiSelectMode() {
        multiSelectMode = false;
        // hide both buttons. selecting an individual item can bring them back
        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        toggleSelectButton.setBackgroundColor(Color.argb(255, 60, 60, 255));
    }



        //i added the following to access database and clear lisst of items and only display the ones in the database
        private void loadItemsFromFirestore() {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser == null) {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listItems.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firestore", document.getId() + " => " + document.getData());

                                Item item = new Item(
                                        document.getString("name"),
                                        new ItemDate(document.getString("date")),
                                        document.getString("description"),
                                        document.getString("make"),
                                        Double.parseDouble(document.getString("serial")),
                                        document.getString("model"),
                                        Double.parseDouble(document.getString("value")),
                                        document.getString("comment")
                                );

                                listItems.add(item);
                            }
                            adapter.notifyDataSetChanged();
                            totalAmountTextView.setText(computeTotal());
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                            Toast.makeText(MainActivity.this, "Error getting items.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


}