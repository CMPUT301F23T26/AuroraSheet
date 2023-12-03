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
        String[] keywords = {"sn", "serlal no", "serial no", "s/n"};

        for (String keyword : keywords) {
            int index = text.indexOf(keyword);
            if (index != -1) {
                // Find the first character after the keyword until a new line is encountered
                StringBuilder serialNumberBuilder = new StringBuilder();
                for (int i = index + keyword.length(); i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (c == '\n' || c == '\r') {
                        break; // Stop when a new line is encountered
                    } else if (Character.isLetterOrDigit(c)) {
                        serialNumberBuilder.append(c);
                    }
                }

                String serialNumber = serialNumberBuilder.toString().trim();
                if (!serialNumber.isEmpty()) {
                    return serialNumber;
                }
            }
        }
        return null;
    }




}
