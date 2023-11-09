package com.example.aurorasheetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the registration of new users. It gets user input and validates
 * all the user fields before sending the data to the database.
 */
public class Registration extends AppCompatActivity {
    private EditText Email, Password, Username;
    private Button sign_up;
    private TextView prev_user;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ProgressDialog mdial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registation);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mdial = new ProgressDialog(this);
        user_cred();
        signup();
        ReturnToLogin();
    }

    private void user_cred() {
        Email = findViewById(R.id.emailId);
        Password = findViewById(R.id.reg_pass);
        Username = findViewById(R.id.userid);
        sign_up = findViewById(R.id.waitButton);
        prev_user = findViewById(R.id.switch_tosignup);
    }

    private void signup() {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                final String username = Username.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Password.setError("Password is required");
                    return;
                }

                if (TextUtils.isEmpty(username)) {
                    Username.setError("Username is required");
                    return;
                }

                mdial.setMessage("Creating Account...");
                mdial.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username);
                                    user.put("email", email);

                                    db.collection("users")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .set(user)
                                            .addOnSuccessListener(aVoid -> {
                                                mdial.dismiss();
                                                Toast.makeText(Registration.this, "Sign up Complete", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e -> {
                                                mdial.dismiss();
                                                Toast.makeText(Registration.this, "Error adding user details", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    mdial.dismiss();
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(Registration.this, "This email is already connected to an account", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Registration.this, "Sign up Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void ReturnToLogin(){
        prev_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });
    }
}
