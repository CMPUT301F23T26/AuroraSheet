package com.example.aurorasheetapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class EditItemActivity extends AppCompatActivity {
    private Button chooseImageButton, deleteImageButton, backButton;
    private ImageView itemImage;
    private EditText itemName, itemDescription, itemValue, itemMake, itemModel, itemComment,
                        itemDate, itemSerial;
    private FloatingActionButton confirmButton, deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        chooseImageButton = findViewById(R.id.selectImageButton_edit);
        deleteImageButton = findViewById(R.id.deleteImageButton_edit);
        backButton = findViewById(R.id.backButton_edit);
        deleteButton = findViewById(R.id.deleteItemButton_edit);
        itemImage = findViewById(R.id.imageViewItem_edit);
        itemName = findViewById(R.id.itemName_edit);
        itemDescription = findViewById(R.id.itemDescription_edit);
        itemValue = findViewById(R.id.itemValue_edit);
        itemMake = findViewById(R.id.itemMake_edit);
        itemModel = findViewById(R.id.itemModel_edit);
        itemComment = findViewById(R.id.itemComment_edit);
        itemDate = findViewById(R.id.date_edit);
        itemSerial = findViewById(R.id.itemSerialNumber_edit);
        confirmButton = findViewById(R.id.confirmButton_edit);
        deleteButton = findViewById(R.id.deleteItemButton_edit);

        //TODO: store / pass in image
        String name, description, make, model, comment, time, serial;
        Double value;
        int index;
        Intent inputIntent = getIntent();
        name = inputIntent.getStringExtra("name");
        description = inputIntent.getStringExtra("description");
        value = inputIntent.getDoubleExtra("value", -1);
        make = inputIntent.getStringExtra("make");
        model = inputIntent.getStringExtra("model");
        comment = inputIntent.getStringExtra("comment");
        time = inputIntent.getStringExtra("time");
        index = inputIntent.getIntExtra("index", -1);
        serial = inputIntent.getStringExtra("serial").toString();

        itemName.setText(name);
        itemDescription.setText(description);
        itemValue.setText(value.toString());
        itemMake.setText(make);
        itemModel.setText(model);
        itemComment.setText(comment);
        itemDate.setText(time);
        itemSerial.setText(serial);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                    outputIntent.putExtra("index", index);
                    setResult(1, outputIntent);
                    finish();
                }
            }
        });

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

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemImage.setImageDrawable(null);
            }
        });

    }
    public void imageChooser() {
        Intent imageIntent = new Intent();
        imageIntent.setType("image/*");
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        launchImageChoseActivity.launch(Intent.createChooser(imageIntent, "Select Picture"));
    }
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
                            itemImage.setImageBitmap(selectedImageBitmap);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}
