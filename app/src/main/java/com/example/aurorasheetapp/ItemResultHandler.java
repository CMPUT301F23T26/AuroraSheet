package com.example.aurorasheetapp;

import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class handles the result of the add item activity and the edit item activity to update the
 * list of items.
 */
public class ItemResultHandler {

    private final MainActivity mainActivity;

    public ItemResultHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * This method takes in the intent data from the add item activity and updates the list of items
     * @param data
     * @param itemManager
     * @param adapter
     */
    public void addItemResult(Intent data, ItemManager itemManager, RecyclerView.Adapter adapter) {
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
            itemManager.add(listItem);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * This method takes in the intent data from the edit item activity and updates the list of items
     * @param data
     * @param itemManager
     * @param adapter
     */
    public void editItemResult(Intent data, ItemManager itemManager, RecyclerView.Adapter adapter) {
        if (data != null) {
            Boolean isDelete = data.getBooleanExtra("isDelete", false);
            if (isDelete) {
                int index = data.getIntExtra("index", -1);
                if (index > -1) {
                    Item itemToDelete = itemManager.remove(index);
                    adapter.notifyDataSetChanged();
                    mainActivity.deleteItemFromFirestore( itemToDelete.getDocumentId());
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

                if (index != -1) {
                    Item item = itemManager.getItem(index);
                    item.setMake(make);
                    item.setComment(comment);
                    item.setName(name);
                    item.setEstimatedValue(Double.parseDouble(value));
                    item.setModel(model);
                    item.setDateOfPurchase(date);
                    item.setBriefDescription(description);
                    item.setSerialNumber(serial);
                    item.setImage(image);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
