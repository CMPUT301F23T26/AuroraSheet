package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity implements
        RecyclerViewInterface,
        TagFragment.OnFragmentInteractionListener,
        TagItemsFragment.OnFragmentInteractionListener,
        FilterFragment.OnFilterConfirmListener,
        SortingFragment.OnSortingConfirmListener
{


    private StorageReference storageReference;
    private ItemDate startDate, endDate;

    private RecyclerView recyclerView;
    private CustomArrayAdapter adapter;
    private List<Item> listItems;
    private ItemManager itemManager;
    private ItemResultHandler itemResultHandler;
    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    private FloatingActionButton editButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton deselectAllButton;
    private FloatingActionButton tagItemButton;

    private RecyclerView tagView;
    private RecyclerView.Adapter tagAdapter;
    private ArrayList<Tag> tags; // keeps track of all tags
    private ArrayList<Tag> selected_tags; // keeps track of tags to display items
    private Tag selected_tag;
    private ArrayList<String> tagNames;
    private ArrayList<String> tagStatus;
    private ArrayList<Item> loaded_items;
    private FloatingActionButton addTag_btn;

    private ImageButton profile_btn;
    private ImageButton sort_btn;
    private ImageButton search_btn;


    private FirebaseFirestore firestore;

    private Boolean multiSelectMode;

    private int itemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //access database
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        tags = new ArrayList<>();
        loaded_items = new ArrayList<>();
        loadItemsFromFirestore();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        itemManager = new ItemManager();
        itemResultHandler = new ItemResultHandler(this);

        adapter = new CustomArrayAdapter(itemManager.getItems(), this, getApplicationContext());
        recyclerView.setAdapter(adapter);

        totalAmountTextView = findViewById(R.id.totalValue);
        addButton = findViewById(R.id.buttonAdd);
        editButton = findViewById(R.id.buttonEdit);
        deleteButton = findViewById(R.id.buttonDelete);
        deselectAllButton = findViewById(R.id.buttonDeselectAll);
        tagItemButton = findViewById(R.id.buttonTagItem);
        sort_btn = findViewById(R.id.sortItem_btn);
        search_btn = findViewById(R.id.searchItem_btn);

        updateTotalValue();

        tagView = findViewById(R.id.tag_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagView.setLayoutManager(layoutManager);
        selected_tags = new ArrayList<>();
        selected_tag = null;
        tagNames = new ArrayList<>();
        tagStatus = new ArrayList<>();

        multiSelectMode = true;
        initialiseAsUnselected();

        // navigate to the add item activity on click of the add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                for (Tag tag : tags){
                    tagNames.add(tag.getName());
                    tagStatus.add("false");
                }
                intent.putStringArrayListExtra("tags", tagNames);
                intent.putStringArrayListExtra("tagStatus", tagStatus);
                addItemLauncher.launch(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemIndex = getTheOneSelectedItem();
                if(itemIndex > -1 && !itemManager.isEmpty()){
                    Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                    Item thisItem = itemManager.getItem(itemIndex);
                    for (Tag tag : tags){
                        tagNames.add(tag.getName());
                        if(tag.getTagged_items().contains(thisItem)){
                            tagStatus.add("true");
                        } else {
                            tagStatus.add("false");
                        }
                    }
                    intent.putStringArrayListExtra("tags", tagNames);
                    intent.putStringArrayListExtra("tagStatus", tagStatus);
                    launchEditData(intent, itemIndex);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> selected_items = getListOfSelectedItems();
                Collections.reverse(selected_items); // Reverse to prevent index out of bounds
                for (Integer selectedItemIndex : selected_items) {
                    if (selectedItemIndex > -1 && !itemManager.isEmpty()) {
                        Item itemToDelete = itemManager.remove(selectedItemIndex);
                        for(Tag tag : tags) {
                            if(tag.getTagged_items().contains(itemToDelete)){
                                tag.untagItem(itemToDelete);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        deleteItemFromFirestore(itemToDelete.getDocumentId());
                    }
                }
                update_selection();
                updateTotalValue();
            }
        });
        deselectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllItems();
            }
        });

        sort_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the dialog fragment and show it
                FilterFragment filterFragment = new FilterFragment();
                filterFragment.show(getSupportFragmentManager(), "filter_fragment");
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of the dialog fragment and show it
                SortingFragment sortFragment = new SortingFragment();

                sortFragment.show(getSupportFragmentManager(), "sort_fragment");
            }
        });

        tagAdapter = new CustomTagAdapter(tags, this,
                new CustomTagAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Tag tag) {
                        boolean status = tag.getStatus();
                        if (status){
                            tag.unselect_tag();
                            selected_tags.remove(tag);
                        } else {
                            tag.select_tag();
                            selected_tags.add(tag);
                        }
                        tagAdapter.notifyDataSetChanged();
                        if (selected_tags.size() != 0){
                            Tag first_tag = selected_tags.get(0);
                            List<Item> tagged_items = new ArrayList<>(first_tag.getTagged_items());
                            for (Tag one_tag : selected_tags){
                                tagged_items.retainAll(one_tag.getTagged_items());
                            }
                            itemManager.setTagged_Items(tagged_items);
                            adapter = new CustomArrayAdapter(itemManager.getItems(true), (RecyclerViewInterface) recyclerView.getContext(), getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter = new CustomArrayAdapter(itemManager.getItems(), (RecyclerViewInterface) recyclerView.getContext(), getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        }
                    }
                },
                new CustomTagAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(Tag tag) {
                        new TagFragment(tag, tags).show(getSupportFragmentManager(), "edit_tag");
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
                new TagFragment(selected_tag, tags).show(getSupportFragmentManager(), "add_tag");
            }
        });

        tagItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new TagItemsFragment(tags, (ArrayList) itemManager.getItems(true)).show(getSupportFragmentManager(), "tag_item");
            }
        });
    }

    private void launchEditData(Intent intent, int i) {
        if (intent != null) {
            Item itemToEdit = itemManager.getItem(i);
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

    private final ActivityResultLauncher<Intent> addItemLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            if (data != null) {
                                tagNames = itemResultHandler.addItemResult(data, itemManager, adapter);
                                Item item = itemManager.get_lastAdded();
                                String itemName = tagNames.get(tagNames.size() - 1);
                                tagNames.remove(tagNames.size() - 1);
                                for(String name : tagNames){
                                    for (Tag tag : tags){
                                        if (Objects.equals(tag.getName(), name)){
                                            tag.tagItem(item);
                                        }
                                    }
                                }



                                updateTotalValue();
                            }
                        }
                    });
    private final ActivityResultLauncher<Intent> editItemLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == 1) {
                            Intent data = result.getData();
                            if (data != null) {
                                tagNames = itemResultHandler.editItemResult(data, itemManager, adapter);
                                updateTotalValue();
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
            Item item = itemManager.getItems().get(position);
            item.toggleSelect();
            if (item.getSelection()){
                itemManager.addTagged_Items(item);
            } else {
                itemManager.delTagged_Items(item);
            }

            update_selection();
        }
        else {
            //shows two buttons once clicked


            if (countSelectedItems() == 1);
            //Log.w("debug","item clicked while not in select mode");
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            tagItemButton.setVisibility(View.VISIBLE);
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
        tagItemButton.setVisibility(View.VISIBLE);
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
        tagItemButton.setVisibility(View.INVISIBLE);
        //toggleSelectButton.setBackgroundColor(Color.argb(255, 60, 60, 255));
    }
    /**
     * Calculate how many items are currently selected
     * @return The number of items that are currently selected
     *
     */
    public int countSelectedItems() {
        int count = 0;
        for (Item thisitem:itemManager.getItems()) {
            if (thisitem.getSelection()) {
                count += 1;
            }
        }
        return count;
    }

    private void initialiseAsUnselected() {
        for (Item thisitem:itemManager.getItems()) {
            thisitem.unselect();
        }
    }

    /**
     * Make all items unselected
     *
     */
    public void deselectAllItems() {
        for (Item thisitem:itemManager.getItems()) {
            thisitem.unselect();
            itemManager.delTagged_Items(thisitem);
        }
        ArrayList<Item> newArray = new ArrayList<>();
        itemManager.setTagged_Items(newArray);

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
            tagItemButton.setVisibility(View.INVISIBLE);

        }

        if (count == 1) {
            addButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            deselectAllButton.setVisibility(View.VISIBLE);
            tagItemButton.setVisibility(View.VISIBLE);
        }

        if (count > 1) {
            addButton.setVisibility(View.INVISIBLE);
            editButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            deselectAllButton.setVisibility(View.VISIBLE);
            tagItemButton.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * Get the index of the currently selected item.
     * @return The index of the item that is selected. If no item is selected, it returns -1
     *
     */
    public int getTheOneSelectedItem() {
        int listsize = itemManager.getItems().size();
        for (int index = 0; index < listsize; index++) {
            if (itemManager.getItems().get(index).getSelection()) {
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
        int listsize = itemManager.getItems().size();
        for (int index = 0; index < listsize; index++) {
            if (itemManager.getItems().get(index).getSelection()) {
                selected_items.add(index);
            }
        }
        return selected_items;
    }

    /**
     * Loads items from Firestore database and updates the UI accordingly.
     */
    public void loadItemsFromFirestore() {
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
                        itemManager.getItems().clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", document.getId() + " => " + document.getData());

                            Item item = new Item(
                                    document.getString("name"),
                                    new ItemDate(document.getString("date")),
                                    document.getString("description"),
                                    document.getString("make"),
                                    document.getString("serial"),
                                    document.getString("model"),
                                    document.getDouble("value"),
                                    document.getString("comment")

                            );
                            //check so that it doesn't break old accounts
                            if(document.get("images") != null){
                                //load image-related attributes and download images for the item
                                item.setTopImageIndex(document.getDouble("imageIndex").intValue());
                                item.setImage((ArrayList<String>) document.get("images"));
                                item.setPath(document.getString("path"));
                                for(String name : item.getImage()){
                                    ImageHelpers.downloadImage(storageReference, getApplicationContext(), name);
                                }
                            }
                            item.setDocumentId(document.getId()); // Save the document ID
                            itemManager.add(item);
                        }
                        adapter.notifyDataSetChanged();
                        totalAmountTextView.setText(itemManager.computeTotal());
                        loadTagsFromFirestore();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        Toast.makeText(MainActivity.this, "Error getting items.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Loads tags from Firestore database and updates the UI accordingly.
     */
    public void loadTagsFromFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
            return;
        }
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
                            tag.setDocumentID(document.getId());
                            tags.add(tag);
                            loadTaggedItems(tag);
                        }
                        tagAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        Toast.makeText(MainActivity.this, "Error getting tags.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * load the items with specified tag
     * @param tag tag of the item(s) to be loaaded
     */
    public void loadTaggedItems(Tag tag){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore.collection("users")
                .document(currentUser.getUid())
                .collection("tags")
                .document(tag.getDocumentID())
                .collection("tagged_items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("Firestore", document.getId() + " => " + document.getData());
                            Item newItem = new Item(
                                    document.getString("name"),
                                    new ItemDate(document.getString("date")),
                                    document.getString("description"),
                                    document.getString("make"),
                                    document.getString("serial"),
                                    document.getString("model"),
                                    document.getDouble("value"),
                                    document.getString("comment")
                            );
                            for (Item item : itemManager.getItems()){
                                if (Objects.equals(item.getName(), newItem.getName())
                                    && Objects.equals(item.getBriefDescription(), newItem.getBriefDescription())){
                                    Log.d("item equal", "true");
                                    item.setTaggedDocumentId(document.getId());
                                    tag.tagItem(item);
                                }
                            }
                        }
                        tagAdapter.notifyDataSetChanged();
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                        Toast.makeText(MainActivity.this, "Error getting tags.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * set the view to display on the tagged items
     * @param selected_tags the list of tags selected for filter
     */
    public void viewTaggedItems(ArrayList<Tag> selected_tags){
        if (selected_tags.size() > 0){
            Tag first_tag = selected_tags.get(0);
            List<Item> tagged_items = new ArrayList<>(first_tag.getTagged_items());
            for (Tag tag : selected_tags){
                tagged_items.retainAll(tag.getTagged_items());
            }
            itemManager.setTagged_Items(tagged_items);
            adapter = new CustomArrayAdapter(itemManager.getItems(true), this, getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * add a tag to firebase
     * @param tag the tag object to be added
     */
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
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding tag", Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(documentReference -> tag.setDocumentID(documentReference.getId()));
    }

    /**
     * method for deleting a tag from firebase
     * @param tag tag object to be deleted
     */
    private void db_del_tag(Tag tag){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tag.getDocumentID() != null) {
            firestore.collection("users")
                    .document(currentUser.getUid())
                    .collection("tags")
                    .document(tag.getDocumentID())
                    .delete()
                    .addOnSuccessListener(documentReference -> Toast.makeText(this, "Tag deleted", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error deleting tag", Toast.LENGTH_SHORT).show());
        }
    }

    // Override interface methods for add tag fragment
    @Override
    public void onCancelPressed()  {
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
        viewTaggedItems(selected_tags);
    }

    /**
     * Delete the specified tag object from program.
     * @param tag Tag object to be deleted
     *
     */
    @Override
    public void onDeletePressed(Tag tag){
        tags.remove(tag);
        selected_tags.remove(tag);
        tagAdapter.notifyDataSetChanged();
        db_del_tag(tag);
        selected_tag = null;
        viewTaggedItems(selected_tags);
    }

    /**
     * delete an item from firestore with given doucment ID
     * @param documentId the ID of the documnent to be deleted
     */
    public void deleteItemFromFirestore(String documentId) {
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

    /**
     * compute and update the total value of items
     */
    public void updateTotalValue() {
        totalAmountTextView.setText(itemManager.computeTotal());
    }

 

    /**
     * filter the items with given parameters
     * @param beforeDate  the date range at the before
     * @param afterDate   the date range at the after
     * @param descriptionKeyword  the description keyword to be used for sorting
     * @param make the make string to be used for sorting
     */
    public void onDateRangeSelected(ItemDate beforeDate, ItemDate afterDate, String descriptionKeyword, String make) {
        // Check if the start and end dates are real
        if (afterDate.getYear() == 0) {
            afterDate.setYear(9999);
        }
        // Filter items based on the dates
        itemManager.setFilterBeforeDate(beforeDate);
        itemManager.setFilterAfterDate(afterDate);
        itemManager.setFilterDescriptionSubstring(descriptionKeyword);
        itemManager.setFilterMake(make);


        itemManager.doFiltering();
        itemManager.update_shown_items();


        this.adapter.updateItems(itemManager.shownItems);
        adapter.notifyDataSetChanged();

        // Log the values

    }


    /**
     * Confirm button pressed from TagItemFragment
     * @param selected_tags The list of tags selected
     *
     */
    @Override
    public void onOK_Pressed(ArrayList<Tag> selected_tags) {
        this.selected_tags = selected_tags;
        viewTaggedItems(selected_tags);
        deselectAllItems();
    }

    /**
     * Unselect all items when cancel button pressed.
     *
     */
    @Override
    public void onCancel_Pressed() {
        deselectAllItems();
    }
    // is this related to a tag thing?

    /**
     * When the confirm button is pressed from the sortingFragment, pass the information along into the itemManager for sorting
     *
     * @param nameMode integer. 0 means do not sort by this, 1 means sort ascending, 2 means sort descending
     * @param dateMode integer. 0 means do not sort by this, 1 means sort ascending, 2 means sort descending
     * @param valueMode integer. 0 means do not sort by this, 1 means sort ascending, 2 means sort descending
     * @param makeMode integer. 0 means do not sort by this, 1 means sort ascending, 2 means sort descending
     * @param descMode integer. 0 means do not sort by this, 1 means sort ascending, 2 means sort descending
     */

    public void receiveSortingData(int nameMode, int dateMode, int valueMode, int makeMode, int descMode) {
        ArrayList<Integer> sortingmode = new ArrayList<Integer>();

        sortingmode.add(nameMode);
        sortingmode.add(dateMode);
        sortingmode.add(0);     // serial number
        sortingmode.add(0);     // model
        sortingmode.add(valueMode);
        sortingmode.add(makeMode);
        sortingmode.add(0);     // comment
        sortingmode.add(descMode);


        itemManager.setSortingStatus(sortingmode);

        itemManager.doSorting();
        itemManager.update_shown_items();


        this.adapter.updateItems(itemManager.shownItems);
        adapter.notifyDataSetChanged();

    }

}