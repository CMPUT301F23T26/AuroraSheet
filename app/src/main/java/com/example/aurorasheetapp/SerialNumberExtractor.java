package com.example.aurorasheetapp;

import android.graphics.Bitmap;

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
    public String extractSerialNumberFromImage(Bitmap imageBitmap, Integer rotationDegree) {
        String serialNumber = null;

        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(imageBitmap, rotationDegree);
        Task<Text> result = recognizer.process(image)
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        // Use visionText to get the recognized text
                        String resultText = visionText.getText();
                        String serialNumber = findSerialNumber(resultText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return serialNumber;
    }
    public String findSerialNumber(String text) {
        String[] keywords = {"Serial", "SN", "S/N"};

        for (String keyword : keywords) {
            int index = text.indexOf(keyword);
            if (index != -1) {
                String substring = text.substring(index + keyword.length()).trim();
                String serialNumber = substring.replaceAll("[^a-zA-Z0-9]", "");

                if (!serialNumber.isEmpty()) {
                    return serialNumber;
                }
            }
        }
        return null;
    }
}
