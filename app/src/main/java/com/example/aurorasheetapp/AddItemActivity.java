package com.example.aurorasheetapp;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import io.grpc.Compressor;
import io.grpc.Context;

/**
 * This class manages adding new items to the item list. It gets user input and validates
 * all the item fields before sending the data back to the main item list activity.
 */

public class AddItemActivity extends AppCompatActivity implements
        SerialNumberExtractor.SerialNumberCallback,
        TagAddItemFragment.OnFragmentInteractionListener {

    private Button scanBarcodeButton;

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
    private Button scanSerialButton;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemComment;
    private FloatingActionButton addItemButton;
    private FloatingActionButton addTagsButton;
    private StorageReference storageReference;
    private LinearProgressIndicator progress;
    private ArrayList<Tag> tags;
    private ArrayList<String> tagNames;
    private ArrayList<String> tagStatus;
    private ArrayList<String> selected_tagNames;
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

        tags = new ArrayList<>();
        tagNames = new ArrayList<>();
        selected_tagNames = new ArrayList<>();
        tagNames = this.getIntent().getStringArrayListExtra("tags");
        tagStatus = this.getIntent().getStringArrayListExtra("tagStatus");
        for(int i = 0; i < tagNames.size(); i++){
            String name = tagNames.get(i);
            boolean status;
            if (Objects.equals(tagStatus.get(i), "true")){
                status = true;
            } else {
                status = false;
            }
            Tag newTag = new Tag(name);
            newTag.setStatus(status);
            tags.add(newTag);
        }

        chooseImageButton = findViewById(R.id.selectImageButton);
        itemImage = findViewById(R.id.imageViewItem);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        itemDate = findViewById(R.id.itemDate);
        dateText = findViewById(R.id.dateText);
        itemValue = findViewById(R.id.itemValue);
        itemSerial = findViewById(R.id.itemSerialNumber);
        scanSerialButton = findViewById(R.id.scanSerialButton);
        itemMake = findViewById(R.id.itemMake);
        itemModel = findViewById(R.id.itemModel);
        itemComment = findViewById(R.id.itemComment);
        addItemButton = findViewById(R.id.addItemButton);
        imageDelete = findViewById(R.id.deleteImageButton_add);
        imageLeft = findViewById(R.id.imageLeft_add);
        imageRight = findViewById(R.id.imageRight_add);
        cameraImage = findViewById(R.id.cameraButton_add);
        addTagsButton = findViewById(R.id.addTagsButton);
        scanBarcodeButton = findViewById(R.id.scanBarcodeButton);
        scanBarcodeButton.setOnClickListener(v -> {
            scanbarcode();
        });


        imageIndex = -1;
        images = new ArrayList<>();

        /**
         * This method launches the barcode scanner activity for scanning a barcode.
         */
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

        scanSerialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    // Permission is granted, launch the camera intent
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    launchCameraActivity.launch(intent);
                } else {
                    // Permission is not granted, request it
                    requestCameraPermission();
                }
            }
        });

        addTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TagAddItemFragment(tags).show(getSupportFragmentManager(), "tag_item");
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

                if (!validateInput()) {
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
                newItem.put("serial", serial);
                newItem.put("make", make);
                newItem.put("model", model);
                newItem.put("comment", comment);
                newItem.put("path", path);
                newItem.put("images", images);
                newItem.put("imageIndex", imageIndex);

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

                            intent.putStringArrayListExtra("tags", selected_tagNames);
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
                    ImageHelpers.deleteFromStorage(storageReference,getApplicationContext(), images.get(imageIndex));
                    images.remove(imageIndex);

                    Drawable defaultImage = ImageHelpers.getDefaultDrawable(getApplicationContext());
                    itemImage.setImageDrawable(defaultImage);
                    imageIndex--;
                    itemImage.setVisibility(View.GONE);
                } else if (images.size() == 0) {
                }
                //if multiple, set to the next one on the stack
                else{
                    ImageHelpers.deleteFromStorage(storageReference,getApplicationContext(), images.get(imageIndex));
                    images.remove(imageIndex);
                    imageIndex = images.size() - 1;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });
        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageIndex > 0) {
                    imageIndex--;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });
        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageIndex + 1 < images.size()) {
                    imageIndex++;
                    Bitmap bitmap = ImageHelpers.loadImageFromStorage(path, images.get(imageIndex));
                    itemImage.setImageBitmap(bitmap);
                }
            }
        });
        cameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    // Permission is granted, launch the camera intent
                    Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    cameraActivity.launch(cameraIntent);
                } else {
                    // Permission is not granted, request it
                    requestCameraPermission();
                }
            }
        });
    }

    /**
     * This method validates all of the user input for adding a new item.
     */
    public boolean validateInput() {
        if (!ItemValidator.validateItemName(itemName.getText().toString())) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ItemValidator.validateItemDescription(itemDescription.getText().toString())) {
            Toast.makeText(this, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ItemValidator.validateItemValue(itemValue.getText().toString())) {
            Toast.makeText(this, "Please enter a valid value", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!ItemValidator.validateSerialNumber(itemSerial.getText().toString())){
            Toast.makeText(this, "Please enter a valid serial number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateItemMake(itemMake.getText().toString())){

            Toast.makeText(this, "Please enter a valid make", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ItemValidator.validateItemModel(itemModel.getText().toString())) {
            Toast.makeText(this, "Please enter a valid model", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ItemValidator.validateItemComment(itemComment.getText().toString())) {
            Toast.makeText(this, "Please enter a valid comment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ItemValidator.validateDate(dateText.getText().toString())){
            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
     * This method is called when the serial number is extracted from the image.
     * @param serialNumber
     */
    @Override
    public void onSerialNumberExtracted(String serialNumber) {
        if (serialNumber != null) {
            runOnUiThread(() -> itemSerial.setText(serialNumber));
        } else {
            // Handle the case when serial number extraction fails
            runOnUiThread(() -> Toast.makeText(AddItemActivity.this, "Failed to extract serial number", Toast.LENGTH_SHORT).show());
        }
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
                        ImageHelpers.uploadImage(storageReference, getApplicationContext(), selectedImageBitmap, uniqueID);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    ActivityResultLauncher<Intent> launchCameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // get the image bitmap from the camera activity and extract the serial number
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        ImageHelpers imageHelpers = new ImageHelpers();
                        Uri imageUri = imageHelpers.getImageUriFromBitmap(imageBitmap, getContentResolver(), AddItemActivity.this);
                        int rotationDegree = imageHelpers.getRotationDegree(imageUri);

                        SerialNumberExtractor serialNumberExtractor = new SerialNumberExtractor();
                        serialNumberExtractor.extractSerialNumberFromImage(imageBitmap, rotationDegree, AddItemActivity.this);
                    }
                }
            });

    ActivityResultLauncher<Intent> cameraActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        itemImage.setImageBitmap(imageBitmap);
                        imageIndex++;
                        String uniqueID = UUID.randomUUID().toString();
                        path = ImageHelpers.saveToInternalStorage(this, imageBitmap, uniqueID);
                        images.add(uniqueID);
                        itemImage.setVisibility(View.VISIBLE);
                        ImageHelpers.uploadImage(storageReference, getApplicationContext(), imageBitmap, uniqueID);
                    }
                }
            });
    /**
     * This method launches the barcode scanner activity for scanning a barcode.
     */
    private void scanbarcode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String barcode = result.getContents();
            itemDescription.setText(barcode);
        }
        else{
            Toast.makeText(this, "No barcode found", Toast.LENGTH_SHORT).show();
        }
    });

    @Override
    public void onOK_Pressed(ArrayList<Tag> selected_tags) {
        selected_tagNames.clear();
        for (Tag tag : selected_tags){
            selected_tagNames.add(tag.getName());
        }
        Log.d("selected tag names size", String.valueOf(selected_tagNames.size()));
        for (Tag tag : tags){
            if (selected_tagNames.contains(tag.getName())){
                tag.setStatus(true);
            } else {
                selected_tagNames.remove(tag.getName());
                tag.setStatus(false);
            }
            Log.d(tag.getName(), String.valueOf(tag.getStatus()));
        }
    }
    private boolean checkCameraPermission() {
        // Check if the camera permission is granted
        return ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        // Request camera permission
        ActivityCompat.requestPermissions(AddItemActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onCancel_Pressed() {
    }
}