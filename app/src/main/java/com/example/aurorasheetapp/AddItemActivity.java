package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

/**
 * This class manages adding new items to the item list. It gets user input and validates
 * all the item fields before sending the data back to the main item list activity.
 */
public class AddItemActivity extends AppCompatActivity {
    private Button chooseImageButton;
    private ImageView itemImage;
    private EditText itemName;
    private EditText itemDescription;
    private EditText itemValue;
    private EditText itemSerial;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemComment;
    private FloatingActionButton addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        chooseImageButton = findViewById(R.id.selectImageButton);
        itemImage = findViewById(R.id.imageViewItem);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemValue = findViewById(R.id.itemValue);
        itemSerial = findViewById(R.id.itemSerialNumber);
        itemMake = findViewById(R.id.itemMake);
        itemModel = findViewById(R.id.itemModel);
        itemComment = findViewById(R.id.itemComment);
        addItemButton = findViewById(R.id.addItemButton);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all of the user input for adding a new item
                String name = itemName.getText().toString();
                String description = itemDescription.getText().toString();
                String value = itemValue.getText().toString();
                String serial = itemSerial.getText().toString();
                String make = itemMake.getText().toString();
                String model = itemModel.getText().toString();
                String comment = itemComment.getText().toString();

                if(!validateInput()){
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("value", value);
                intent.putExtra("serial", serial);
                intent.putExtra("make", make);
                intent.putExtra("model", model);
                intent.putExtra("comment", comment);
                setResult(1, intent);
                finish();
            }
        });
    }

    /**
     * This method launches the image chooser activity for adding an image to the item.
     */
    public void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchImageChoseActivity.launch(Intent.createChooser(intent, "Select Picture"));
    }

    /**
     * This method validates all of the user input for adding a new item.
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
        return true;
    }
    ActivityResultLauncher<Intent> launchImageChoseActivity = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode()
                    == Activity.RESULT_OK) {
                Intent data = result.getData();
                // do your operation from here....
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