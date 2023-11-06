package com.example.aurorasheetapp;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


public class Registration extends AppCompatActivity {
    //this will store user entry in the email text box
    private EditText Email;
    //this will store user entry in the password text box
    private EditText Password;
    //this will activate if the signup button is triggered
    private Button sign_up;
    //this will store weather the text box at the bottom is clicked
    private TextView prev_user;

    private FirebaseAuth mAuth;

    private ProgressDialog mdial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registation);

        user_cred();
        signup();
        ReturnToLogin();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mdial = new ProgressDialog(this);

    }

    private void user_cred() {
        Email = findViewById(R.id.emailId);
        Password = findViewById(R.id.reg_pass);
        sign_up = findViewById(R.id.waitButton);
        prev_user = findViewById(R.id.switch_tosignup);


    }

    private void signup() {
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                if (email.isEmpty() == true) {
                    Email.setError("Email is required");

                }
                if (password.isEmpty() == true) {
                    Password.setError("Password is required");

                } else {
                    mdial.setMessage("Creating Account...");
                    mdial.show();
                    //connects to firebase for authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        mdial.dismiss();
                                        Toast.makeText(getApplicationContext(), "Sign up Complete", Toast.LENGTH_SHORT).show();
                                        //once we sign up we will be redirected to the home page
                                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthUserCollisionException) {
                                            mdial.dismiss();
                                            Toast.makeText(getApplicationContext(), "This email is already connected to an account", Toast.LENGTH_SHORT).show();


                                        } else {
                                            mdial.dismiss();
                                            Toast.makeText(getApplicationContext(), "Sign up Failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });


                }
            }
        });
    }


    private void ReturnToLogin(){
        prev_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),Login.class);
                startActivity(intent);
            }
        });

    }
}










