package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

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
    private FloatingActionButton deselectAllButton;

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

    private Boolean multiSelectMode;

    private int itemIndex;
    String documentId;

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
        deselectAllButton = findViewById(R.id.buttonDeselectAll);


        tagView = findViewById(R.id.tag_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagView.setLayoutManager(layoutManager);
        selected_tags = new ArrayList<>();
        selected_tag = null;

        multiSelectMode = true;
        initialiseAsUnselected();

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

                itemIndex = getTheOneSelectedItem();
                if(itemIndex > -1 && !listItems.isEmpty()){
                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                    launchEditData(intent, itemIndex);
                }
            }
        });
        // display total value for all the items
        totalAmountTextView = findViewById(R.id.totalValue);
        totalAmountTextView.setText(computeTotal());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> selected_items = getListOfSelectedItems();
                Collections.reverse(selected_items); // Reverse to prevent index out of bounds
                for (Integer selectedItemIndex : selected_items) {
                    if (selectedItemIndex > -1 && !listItems.isEmpty()) {
                        Item itemToDelete = listItems.remove((int) selectedItemIndex);
                        adapter.notifyDataSetChanged();
                        deleteItemFromFirestore(itemToDelete.getDocumentId());
                    }
                }
                update_selection();
                totalAmountTextView.setText(computeTotal());
            }
        });



        deselectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllItems();
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
            intent.putExtra("documentId", itemToEdit.getDocumentId());
            intent.putExtra("name", itemToEdit.getName());
            intent.putExtra("value", itemToEdit.getEstimatedValue());
            intent.putExtra("time", itemToEdit.getDateOfPurchase().toString());
            intent.putExtra("make", itemToEdit.getMake());
            intent.putExtra("comment", itemToEdit.getComment());
            intent.putExtra("model", itemToEdit.getModel());
            intent.putExtra("serial", itemToEdit.getSerialNumber());
            intent.putExtra("description", itemToEdit.getBriefDescription());
            intent.putStringArrayListExtra("images", (ArrayList<String>)itemToEdit.getImage());
            intent.putExtra("index", i);
            intent.putExtra("imageIndex", itemToEdit.getTopImageIndex());
            editItemLauncher.launch(intent);
        }
    }

    private void handleAddItemResult(Intent data) {
        if (data != null) {
            String documentId = data.getStringExtra("documentId");
            // Display a Toast message to show the documentId

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
                    Double.parseDouble(value),
                    comment,
                    documentId

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
                    Item itemToDelete = listItems.remove(index);
                    adapter.notifyDataSetChanged();
                    deleteItemFromFirestore( itemToDelete.getDocumentId());
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
                ArrayList<String> image = data.getStringArrayListExtra("images");
                int imageTopIndex = data.getIntExtra("imageIndex", -1);

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
                    item.setImage(image);
                    item.setTopImageIndex(imageTopIndex);
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
        //Log.w("debug","there was a click after all");
        itemIndex = position;

        if (multiSelectMode) {
            //Log.w("debug","item clicked while in select mode");
            listItems.get(position).toggleSelect();

            update_selection();
        }
        else {
            //shows two buttons once clicked


            if (countSelectedItems() == 1);
            //Log.w("debug","item clicked while not in select mode");
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
        //toggleSelectButton.setBackgroundColor(Color.argb(255, 200, 200, 255));
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
        //toggleSelectButton.setBackgroundColor(Color.argb(255, 60, 60, 255));
    }
    /**
     * Calculate how many items are currently selected
     * @return The number of items that are currently selected
     *
     */
    public int countSelectedItems() {
        int count = 0;
        for (Item thisitem:listItems) {
            if (thisitem.getSelection()) {
                count += 1;
            }
        }
        return count;
    }

    private void initialiseAsUnselected() {
        for (Item thisitem:listItems) {
            thisitem.unselect();
        }
    }

    /**
     * Make all items unselected
     *
     */
    public void deselectAllItems() {
        for (Item thisitem:listItems) {
            thisitem.unselect();

        }
        update_selection();
    }
    /**
     * Update the display, as is appropriate for the current selection of items
     *
     */
    public void update_selection() {
        int count = countSelectedItems();

        if (count == 0) {
            addButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            deselectAllButton.setVisibility(View.INVISIBLE);
        }

        if (count == 1) {
            addButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            deselectAllButton.setVisibility(View.VISIBLE);
        }

        if (count > 1) {
            addButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            deselectAllButton.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * Get the index of the currently selected item.
     * @return The index of the item that is selected. If no item is selected, it returns -1
     *
     */
    public int getTheOneSelectedItem() {
        int listsize = listItems.size();
        for (int index = 0; index < listsize; index++) {
            if (listItems.get(index).getSelection()) {
                return index;
            }
        }
        return -1;
    }
    /**
     * Get a list containing the indices of all currently selected item.
     * @return The index of the item that is selected. If no item is selected, it returns -1
     *
     */
    private ArrayList<Integer> getListOfSelectedItems() {
        ArrayList<Integer> selected_items = new ArrayList<Integer>();
        int listsize = listItems.size();
        for (int index = 0; index < listsize; index++) {
            if (listItems.get(index).getSelection()) {
                selected_items.add(index);
            }
        }
        return selected_items;
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
                                    document.getDouble("serial"),
                                    document.getString("model"),
                                    document.getDouble("value"),
                                    document.getString("comment")

                            );
                            item.setDocumentId(document.getId()); // Save the document ID
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

    private void deleteItemFromFirestore(String documentId) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && documentId != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("items")
                    .document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w("Firestore", "Error deleting document", e));
        }
    }







}