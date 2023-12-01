package com.example.aurorasheetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.grpc.Compressor;
import io.grpc.Context;

/**
 * This class manages adding new items to the item list. It gets user input and validates
 * all the item fields before sending the data back to the main item list activity.
 */
public class AddItemActivity extends AppCompatActivity {
    private StorageReference storageReference;
    private LinearProgressIndicator progress;

    private Button chooseImageButton;
    private ImageView itemImage;
    private EditText itemName;
    private EditText itemDescription;
    private TextView dateText;
    private Button itemDate;
    private Button imageDelete;
    private Button imageLeft;
    private Button imageRight;
    private Button cameraImage;
    private EditText itemValue;
    private EditText itemSerial;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemComment;
    private FloatingActionButton addItemButton;
    ArrayList<String> images;
    int imageIndex;
    String path;

    //created instance
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //init a firebase
        firestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(AddItemActivity.this);
        storageReference = FirebaseStorage.getInstance().getReference();

        chooseImageButton = findViewById(R.id.selectImageButton);
        itemImage = findViewById(R.id.imageViewItem);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemDate = findViewById(R.id.itemDate);
        dateText = findViewById(R.id.dateText);
        itemValue = findViewById(R.id.itemValue);
        itemSerial = findViewById(R.id.itemSerialNumber);
        itemMake = findViewById(R.id.itemMake);
        itemModel = findViewById(R.id.itemModel);
        itemComment = findViewById(R.id.itemComment);
        addItemButton = findViewById(R.id.addItemButton);
        imageDelete = findViewById(R.id.deleteImageButton_add);
        imageLeft = findViewById(R.id.imageLeft_add);
        imageRight = findViewById(R.id.imageRight_add);
        cameraImage = findViewById(R.id.cameraButton_add);

        imageIndex = -1;
        images = new ArrayList<>();

        itemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddItemActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateText.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });


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
                String date = dateText.getText().toString();
                String value = itemValue.getText().toString();
                String serial = itemSerial.getText().toString();
                String make = itemMake.getText().toString();
                String model = itemModel.getText().toString();
                String comment = itemComment.getText().toString();

                if(!validateInput()){
                    return;
                }

                double newvalue = 0.0;
                long newserial = 0;

                try {
                    newvalue = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddItemActivity.this, "Please enter a valid value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    newserial = Long.parseLong(serial);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddItemActivity.this, "Please enter a valid serial number", Toast.LENGTH_SHORT).show();
                    return;
                }

                //info bout current user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(AddItemActivity.this, "User not signed in", Toast.LENGTH_SHORT).show();
                    return;
                }

                //put all obj details into a hashmap so we can push into database
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("name", name);
                newItem.put("description", description);
                newItem.put("date", date);
                newItem.put("value", newvalue);
                newItem.put("serial", newserial);
                newItem.put("make", make);
                newItem.put("model", model);
                newItem.put("comment", comment);

                //adding objects into database based on user
                firestore.collection("users")
                        .document(currentUser.getUid())
                        .collection("items")
                        .add(newItem)
                        .addOnSuccessListener(documentReference -> {
                            String newDocumentId = documentReference.getId(); // Get the new document ID

                            // Create an Intent to send the result back to MainActivity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("documentId", newDocumentId);

                            intent.putExtra("name", name);
                            intent.putExtra("description", description);
                            intent.putExtra("date", date);
                            intent.putExtra("value", value);
                            intent.putExtra("serial", serial);
                            intent.putExtra("make", make);
                            intent.putExtra("model", model);
                            intent.putExtra("comment", comment);
                            intent.putExtra("images", images);
                            intent.putExtra("imageIndex", imageIndex);
                            intent.putExtra("path", path);
                            setResult(1, intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddItemActivity.this, "Error adding item", Toast.LENGTH_SHORT).show());


            }
        });
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if only one image and coming from input
                if(images.size() == 1){
                    images.remove(imageIndex);
                    itemImage.setImageDrawable(null);
                    imageIndex--;
                    itemImage.setVisibility(View.GONE);
                }
                else if(images.size() == 0){
                }
                //TODO might need to delete from local repository, as well
                //if multiple, set to the next one on the stack
                else{
                    images.remove(imageIndex);
                    imageIndex = 0;
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
        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                cameraActivity.launch(cameraIntent);
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
                if (data != null
                        && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    Bitmap selectedImageBitmap;
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        itemImage.setImageBitmap(selectedImageBitmap);
                        imageIndex++;
                        String uniqueID = UUID.randomUUID().toString();
                        path = ImageHelpers.saveToInternalStorage(this, selectedImageBitmap, uniqueID);
                        images.add(uniqueID);
                        itemImage.setVisibility(View.VISIBLE);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    ActivityResultLauncher<Intent> cameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null){
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        itemImage.setImageBitmap(imageBitmap);
                        imageIndex++;
                        String uniqueID = UUID.randomUUID().toString();
                        path = ImageHelpers.saveToInternalStorage(this, imageBitmap, uniqueID);
                        images.add(uniqueID);
                        itemImage.setVisibility(View.VISIBLE);
                        uploadImage(imageBitmap, uniqueID, path);
                    }
                }
            });
    private void uploadImage(Bitmap bitmap, String name, String path){
        StorageReference reference = storageReference.child(path + '\\' + name);
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                storageReference.child(path + '\\' + name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getBaseContext(), "Upload Failed - Duplicate image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getBaseContext(), "Upload successful", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void checkCloudForImage(String path, String name){

    }
}