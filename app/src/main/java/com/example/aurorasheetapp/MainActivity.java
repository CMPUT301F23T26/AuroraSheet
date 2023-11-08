package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity implements
        RecyclerViewInterface,
        TagFragment.OnFragmentInteractionListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Item> listItems;

    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
    private FloatingActionButton deleteButton;

    private RecyclerView tagView;
    private RecyclerView.Adapter tagAdapter;
    private ArrayList<Tag> tags; // keeps track of all tags
    private ArrayList<Tag> selected_tags; // keeps track of tags to display items
    private Tag selected_tag;
    private FloatingActionButton addTag_btn;

    private ImageButton profile_btn;
    private ImageButton sort_btn;
    private ImageButton search_btn;

    private FirebaseFirestore firestore;

    private int itemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access database
        firestore = FirebaseFirestore.getInstance();
        tags = new ArrayList<>();
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

        tagView = findViewById(R.id.tag_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagView.setLayoutManager(layoutManager);
        selected_tags = new ArrayList<>();
        selected_tag = null;

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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemIndex > -1) {
                    listItems.remove(itemIndex);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        tagAdapter = new CustomTagAdapter(tags, this,
                new CustomTagAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tag tag) {
                        boolean status = tag.getStatus();
                        if (status){
                            tag.unselect_tag();
                        } else {
                            tag.select_tag();
                        }
                        tagAdapter.notifyDataSetChanged();
                    }
                },
                new CustomTagAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(Tag tag) {
                        new TagFragment(tag).show(getSupportFragmentManager(), "edit_tag");
                    }
                });
        tagView.setAdapter(tagAdapter);
        addTag_btn = findViewById(R.id.addTagButton);
        profile_btn = findViewById(R.id.userProfile_btn);
        sort_btn = findViewById(R.id.sortItem_btn);
        search_btn = findViewById(R.id.searchItem_btn);

        addTag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_tag = null;
                new TagFragment(selected_tag).show(getSupportFragmentManager(), "add_tag");
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

    @Override
    public void onItemClick(int position) {
        itemIndex = position;

        //shows two buttons once clicked
        editButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);

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
        firestore.collection("users")
                .document(currentUser.getUid())
                .collection("tags")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tags.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", document.getId() + " => " + document.getData());

                            Tag tag = new Tag(
                                    document.getString("name")
                            );

                            tags.add(tag);
                        }
                        tagAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        Toast.makeText(MainActivity.this, "Error getting tags.", Toast.LENGTH_SHORT).show();
                    }
                });
        for (Tag tag: tags) {
            ArrayList<Item> taggedItems = new ArrayList<>();
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("tags")
                    .document(tag.getName())
                    .collection("tagged_items")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            tags.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firestore", document.getId() + " => " + document.getData());

                                Item newItem = new Item(
                                        document.getString("name"),
                                        new ItemDate(document.getString("date")),
                                        document.getString("description"),
                                        document.getString("make"),
                                        Double.parseDouble(document.getString("serial")),
                                        document.getString("model"),
                                        Double.parseDouble(document.getString("value")),
                                        document.getString("comment")
                                );

                                tag.tagItem(newItem);
                            }
                            tagAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                            Toast.makeText(MainActivity.this, "Error getting tags.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void db_add_tag(Tag tag){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        //put all obj details into a hashmap so we can push into database
        Map<String, Object> newTag = new HashMap<>();
        newTag.put("name", tag.getName());

        //adding objects into database based on user
        firestore.collection("users")
                .document(currentUser.getUid())
                .collection("tags")
                .add(newTag)
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Tag added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding tag", Toast.LENGTH_SHORT).show());
        ArrayList<Item> tagged_items = tag.getTagged_items();
        for (Item item: tagged_items){
            String name = item.getName();
            String description = item.getBriefDescription();
            String date = item.getDateOfPurchase().toString();
            String value = String.valueOf(item.getEstimatedValue());
            String serial = String.valueOf(item.getSerialNumber());
            String make = item.getMake();
            String model = item.getModel();
            String comment = item.getComment();
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("name", name);
            newItem.put("description", description);
            newItem.put("date", date);
            newItem.put("value", value);
            newItem.put("serial", serial);
            newItem.put("make", make);
            newItem.put("model", model);
            newItem.put("comment", comment);
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("tag")
                    .document(tag.getName())
                    .collection("tagged_items")
                    .add(newItem)
                    .addOnSuccessListener(documentReference -> Toast.makeText(this, "Tag added", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error adding tag", Toast.LENGTH_SHORT).show());
        }
    }

    private void db_del_tag(Tag tag){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("users")
                .document(currentUser.getUid())
                .collection("tags")
                .document(tag.getName())
                .delete()
                .addOnSuccessListener(documentReference -> Toast.makeText(this, "Tag deleted", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting tag", Toast.LENGTH_SHORT).show());
    }
    // Override interface methods for add tag fragment
    @Override
    public void onCancelPressed() {
        selected_tag = null;
    }

    @Override
    public void onOkPressed(Tag newTag, Tag oldTag, Boolean editing) {
        // is editing a selected expense
        if (editing){
            //find the index within ArrayList
            Integer index = tags.indexOf(oldTag);
            // set new expense information to current index
            tags.set(index, new Tag(newTag.getName()));
            tagAdapter.notifyDataSetChanged();
            db_del_tag(oldTag);
            db_add_tag(newTag);
            // adding a new expense
        } else if (!Objects.equals(newTag.getName(), "default")){
            tags.add(newTag); // add to ArrayList
            tagAdapter.notifyDataSetChanged();
            db_add_tag(newTag);
        }
        selected_tag = null;
    }

    @Override
    public void onDeletePressed(Tag tag){
        tags.remove(tag);
        tagAdapter.notifyDataSetChanged();
        db_del_tag(tag);
        selected_tag = null;
    }


}