package com.example.aurorasheetapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

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

    //TODO improve this to return more meaningful data since the default location for app is constant.
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
}
