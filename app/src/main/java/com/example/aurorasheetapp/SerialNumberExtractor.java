package com.example.aurorasheetapp;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

/**
 * This class is used to extract the serial number from an image. It uses the Google ML Kit
 * to extract the text from the image and then searches for the serial number in the text.
 */
public class SerialNumberExtractor {
    /**
     * Extracts the serial number from the image and calls the callback function with the result.
     * @param imageBitmap
     * @param rotationDegree
     * @param callback
     */
    public void extractSerialNumberFromImage(Bitmap imageBitmap, Integer rotationDegree, SerialNumberCallback callback) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(imageBitmap, rotationDegree);
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Use visionText to get the recognized text
                        String resultText = visionText.getText();
                        String serialNumber = findSerialNumber(resultText);
                        callback.onSerialNumberExtracted(serialNumber);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * Callback interface for the serial number extraction.
     */
    public interface SerialNumberCallback {
        void onSerialNumberExtracted(String serialNumber);
    }

    /**
     * Searches for the serial number in the text.
     * @param text
     * @return The serial number if found, null otherwise.
     */
    public String findSerialNumber(String text) {
        text = text.toLowerCase();
        String[] keywords = {"serial no", "sn", "s/n", "serlal no"};

        for (String keyword : keywords) {
            int index = text.indexOf(keyword);
            if (index != -1) {
                // Check if the keyword is followed by a colon and a space
                int endIndex = text.indexOf(":", index + keyword.length());
                if (endIndex != -1) {
                    String serialNumber = text.substring(endIndex + 1).trim();
                    Log.d("SerialNumberExtractor", "Substring: " + serialNumber);
                    serialNumber = serialNumber.replaceAll("[^a-zA-Z0-9]", "");

                    if (!serialNumber.isEmpty()) {
                        return serialNumber;
                    }
                }
            }
        }
        return null;
    }

}
