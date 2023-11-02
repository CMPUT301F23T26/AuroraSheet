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

public class Login extends AppCompatActivity {


        //this will store user entry in the email text box
        private EditText LoginEmail;
        //this will store user entry in the password text box
        private EditText LoginPassword;
        //this will activate if the signup button is triggered
        private Button Login;
        //this will store weather the text box at the bottom is clicked
        private TextView NewUser;

        private TextView ForgetPassword;

        private ProgressDialog mdial;
        private FirebaseAuth authorization;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.signup_layout);
            user_cred();
            login();
            Register();
            authorization = FirebaseAuth.getInstance();
            mdial = new ProgressDialog(this);


        }

        private void user_cred() {
            LoginEmail = findViewById(R.id.email_login);
            LoginPassword= findViewById(R.id.password_login);
            Login = findViewById(R.id.loginButton);
            NewUser = findViewById(R.id.signup);
        }

        private void login() {
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = LoginEmail.getText().toString().trim();
                    String password = LoginPassword.getText().toString().trim();

                    if (email.isEmpty() == true) {
                        LoginEmail.setError("Email is required");
                    }
                    if (password.isEmpty() == true) {
                        LoginPassword.setError("Password is required");
                    }
                    else {
                        mdial.setMessage("Processing ...");
                        mdial.show();
                        authorization.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mdial.dismiss();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    mdial.dismiss();
                                }
                            }


                        });

                    }

                }
            });

        }

        private void Register(){

            NewUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),Registration.class);
                    startActivity(intent);

                }
            });








    }
}
