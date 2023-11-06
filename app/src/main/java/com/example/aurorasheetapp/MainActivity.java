package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * This class serves as the main activity and manages a list of Item Records.
 */
public class MainActivity extends AppCompatActivity implements TagFragment.OnFragmentInteractionListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Item> listItems;

    private RecyclerView tagView;
    private RecyclerView.Adapter tagAdapter;
    private ArrayList<Tag> tags; // keeps track of all tags
    private ArrayList<Tag> selected_tags; // keeps track of tags to display items
    private Tag selected_tag;
    private FloatingActionButton addTag_btn;

    private ImageButton profile_btn;
    private ImageButton sort_btn;
    private ImageButton search_btn;

    private TextView totalAmountTextView;
    private FloatingActionButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        tagView = findViewById(R.id.tag_View);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        tagView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        tags = new ArrayList<>();
        selected_tags = new ArrayList<>();
        selected_tag = null;

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

        for (int i = 1; i < 11; i++){
            Tag tag = new Tag("Tag"+i);
            tags.add(tag);
        }

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

    // Override interface methods for add tag fragment
    @Override
    public void onCancelPressed() {
        selected_tag = null;
    }

    @Override
    public void onOkPressed(Tag newTag, Tag old_tag, Boolean editing) {
        // is editing a selected expense
        if (editing){
            //find the index within ArrayList
            Integer index = tags.indexOf(old_tag);
            // set new expense information to current index
            tags.set(index, new Tag(newTag.getName()));
            tagAdapter.notifyDataSetChanged();
            // adding a new expense
        } else if (!Objects.equals(newTag.getName(), "default")){
            tags.add(newTag); // add to ArrayList
            tagAdapter.notifyDataSetChanged();
        }
        selected_tag = null;
    }

    @Override
    public void onDeletePressed(Tag tag){
        tags.remove(tag);
        tagAdapter.notifyDataSetChanged();
        selected_tag = null;
    }


}