package com.example.aurorasheetapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Helper functions for local image storage operations
 */
public class ImageHelpers {
    /**
     * Takes in any context of the application activity, the bitmap of the image want to be saved
     * and the name of the image to be saved. Saves the image in the default app storage space with
     * given name and returns the path to the file
     * @param context The context of the application activities
     * @param bitmapImage the image to be saved as Bitmap object
     * @param imageName the name of the image to be saved
     * @return the path to the saved image directory as String
     */

    public static String saveToInternalStorage(Context context, Bitmap bitmapImage, String imageName){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    /**
     * Takes in the path of the file to be read and the name of the image
     * to load it as a Bitmap object
     * @param path the path to the saved image directory as String
     * @param name the name of the image to be loaded
     * @return the path to the saved image directory as String
     */
    public static Bitmap loadImageFromStorage(String path, String name)
    {

        try {
            File f=new File(path, name);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        //returns null on error!
        return null;
    }
    public static boolean deleteFromStorage(StorageReference storageReference, Context context, String name){
        final boolean[] remoteDeleteSuccess = new boolean[1];
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory, name);
        StorageReference reference = storageReference.child("images/" + name);
        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                remoteDeleteSuccess[0] = true;
            }
        });
        return myPath.delete() && remoteDeleteSuccess[0];
    }
    public static void uploadImage(StorageReference storageReference, Context context, Bitmap bitmap, String name){
        StorageReference reference = storageReference.child("images/" + name);
        ByteArrayOutputStream baos  = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                storageReference.child("images/" + name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(context, "Upload Failed - Duplicate image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Upload successful", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void downloadImage(StorageReference storageReference, Context context, String name){
        StorageReference imageRef = storageReference.child("images/" + name);
        final long ONE_MB = 1024 * 1024;
        imageRef.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageHelpers.saveToInternalStorage(context, bitmap, name);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static Drawable getDefaultDrawable(Context context) {
        return ResourcesCompat.getDrawable(context.getResources(), R.drawable.default_image, null);
    }
}
