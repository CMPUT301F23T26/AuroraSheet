package com.example.aurorasheetapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.aurorasheetapp.ImageHelpers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is responsible for providing the correct behavior for edit activities
 */
public class EditItemActivity extends AppCompatActivity {
    private Button chooseImageButton, deleteImageButton, backButton, dateEditButton
            , imageLeft, imageRight;
    private ImageView itemImage;
    private EditText itemName, itemDescription, itemValue, itemMake, itemModel, itemComment,
                         itemSerial;
    private TextView itemDate;
    private FloatingActionButton confirmButton, deleteButton;
    private ArrayList<String> images;
    int imageIndex;
    //this is supposed to be a constant path since it doesn't change, will remove after improving the image helper
    String path;
    //added this so we can reflect edits in firebase
    private FirebaseFirestore firestore;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser != null ? currentUser.getUid() : null;
        firestore = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        path = directory.getAbsolutePath();

        //view define block
        chooseImageButton = findViewById(R.id.selectImageButton_edit);
        deleteImageButton = findViewById(R.id.deleteImageButton_edit);
        backButton = findViewById(R.id.backButton_edit);
        deleteButton = findViewById(R.id.deleteItemButton_edit);
        itemImage = findViewById(R.id.imageViewItem_edit);
        itemName = findViewById(R.id.itemName_edit);
        itemDescription = findViewById(R.id.itemDescription_edit);
        itemValue = findViewById(R.id.itemValue_edit);
        dateEditButton = findViewById(R.id.date_edit_btn);
        itemMake = findViewById(R.id.itemMake_edit);
        itemModel = findViewById(R.id.itemModel_edit);
        itemComment = findViewById(R.id.itemComment_edit);
        itemDate = findViewById(R.id.date_edit);
        itemSerial = findViewById(R.id.itemSerialNumber_edit);
        confirmButton = findViewById(R.id.confirmButton_edit);
        deleteButton = findViewById(R.id.deleteItemButton_edit);
        imageLeft = findViewById(R.id.imageLeft_edit);
        imageRight = findViewById(R.id.imageRight_edit);


        //TODO: store / pass in image

        //get extras from passed in item
        String name, description, make, model, comment, time, serial;
        //String imageName;   //not implemented
        Double value;
        int index;

        images = new ArrayList<String>();
        imageIndex = 0;

        Intent inputIntent = getIntent();
        documentId = inputIntent.getStringExtra("documentId");
        name = inputIntent.getStringExtra("name");
        description = inputIntent.getStringExtra("description");
        value = inputIntent.getDoubleExtra("value", -1);
        make = inputIntent.getStringExtra("make");
        model = inputIntent.getStringExtra("model");
        comment = inputIntent.getStringExtra("comment");
        time = inputIntent.getStringExtra("time");
        index = inputIntent.getIntExtra("index", -1);
        serial = Double.toString(inputIntent.getDoubleExtra("serial", 0));
        imageIndex = inputIntent.getIntExtra("imageIndex", 0);
        //if the item already contains the image, initialize
         if(imageIndex != -1){
            images = inputIntent.getStringArrayListExtra("images");
            Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
            itemImage.setImageBitmap(bitmap);
        }

        //set the attributes in the view
        itemName.setText(name);
        itemDescription.setText(description);
        itemValue.setText(value.toString());
        itemMake.setText(make);
        itemModel.setText(model);
        itemComment.setText(comment);
        itemDate.setText(time);
        itemSerial.setText(serial);


        //return button
        dateEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditItemActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                itemDate.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //confirm button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    Intent outputIntent = new Intent();
                    outputIntent.putExtra("name", itemName.getText().toString());
                    outputIntent.putExtra("description", itemDescription.getText().toString());
                    outputIntent.putExtra("value", itemValue.getText().toString());
                    outputIntent.putExtra("model", itemModel.getText().toString());
                    outputIntent.putExtra("make", itemMake.getText().toString());
                    outputIntent.putExtra("comment", itemComment.getText().toString());
                    outputIntent.putExtra("time", itemDate.getText().toString());
                    outputIntent.putExtra("serial", itemSerial.getText().toString());
                    outputIntent.putStringArrayListExtra("images", images);
                    outputIntent.putExtra("index", index);
                    outputIntent.putExtra("imageIndex", imageIndex);
                    outputIntent.putExtra("path", path);
                    setResult(1, outputIntent);

                    //put all updated values in a map
                    Map<String, Object> itemUpdate = new HashMap<>();
                    itemUpdate.put("name", itemName.getText().toString());
                    itemUpdate.put("description", itemDescription.getText().toString());
                    itemUpdate.put("value", Double.parseDouble(itemValue.getText().toString()));
                    itemUpdate.put("model", itemModel.getText().toString());
                    itemUpdate.put("make", itemMake.getText().toString());
                    itemUpdate.put("comment", itemComment.getText().toString());
                    itemUpdate.put("time", itemDate.getText().toString());
                    itemUpdate.put("serial", Double.parseDouble(itemSerial.getText().toString()));

                    if (documentId == null) {
                        // Show a Toast message to the user
                        Toast.makeText(EditItemActivity.this, "Can't update try again", Toast.LENGTH_LONG).show();
                        return; // Exit the method to prevent further execution
                    }




                    //trace the exact path of where the items are placed
                    firestore.collection("users").document(userId).collection("items").document(documentId).update(itemUpdate)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(EditItemActivity.this, "Item updated", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditItemActivity.this, "Error updating item: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }

                    finish();
                }

        });
        //pass delete signal to main if clicked delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outputIntent = new Intent();
                outputIntent.putExtra("isDelete", true);
                outputIntent.putExtra("index", index);
                setResult(1, outputIntent);
                finish();
            }
        });
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        //deletes the image from the image list and from view
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if only one image and coming from input
                if(images.size() == 1){
                    images.remove(imageIndex);
                    itemImage.setImageDrawable(null);
                    imageIndex--;
                }
                else if(images.size() == 0){
                }
                //TODO need to delete from local repository, as well
                //if multiple, set to the next one on the stack
                else{
                    images.remove(imageIndex);
                    imageIndex = 0;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });
        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageIndex + 1 < images.size()){
                    imageIndex++;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });

        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageIndex > 0){
                    imageIndex--;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });
    }

    /**
     * launch choose image with intent
     */
    public void imageChooser() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        launchImageChoseActivity.launch(Intent.createChooser(imageIntent, "Select Picture"));
    }

    /**
     * Validates all input field when called, return false if any of the field is invalid
     * @return boolean true when all fields are valid, false otherwise
     */
    public boolean validateInput(){
        if(!ItemValidator.validateItemName(itemName.getText().toString())){
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemDescription(itemDescription.getText().toString())){
            Toast.makeText(this, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemValue(itemValue.getText().toString())){
            Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemMake(itemMake.getText().toString())){
            Toast.makeText(this, "Please enter a valid make", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemModel(itemModel.getText().toString())){
            Toast.makeText(this, "Please enter a valid model", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemComment(itemComment.getText().toString())){
            Toast.makeText(this, "Please enter a valid comment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateDate(itemDate.getText().toString())){
            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    ActivityResultLauncher<Intent> launchImageChoseActivity = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode()
                        == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        Bitmap selectedImageBitmap;
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            //save to local storage, and add to the record to Item as String ArrayList
                            itemImage.setImageBitmap(selectedImageBitmap);
                            imageIndex++;
                            String uniqueID = UUID.randomUUID().toString();
                            path = ImageHelpers.saveToInternalStorage(this, selectedImageBitmap, uniqueID);
                            images.add(uniqueID);
                            //index for correctly selecting image, no need to implement in Add
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}
