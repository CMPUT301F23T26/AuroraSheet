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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class manages the login of existing users. It gets user input and validates
 * all the user fields before sending the data to the database.
 */
public class Login extends AppCompatActivity {
    private EditText Username, LoginPassword;
    private Button Login;
    private TextView NewUser;

    private ProgressDialog mdial;
    private FirebaseAuth authorization;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        user_cred();
        login();
        Register();
        authorization = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mdial = new ProgressDialog(this);
    }

    private void user_cred() {
        Username = findViewById(R.id.user_login);
        LoginPassword = findViewById(R.id.password_login);
        Login = findViewById(R.id.loginButton);
        NewUser = findViewById(R.id.signup);
    }

    private void login() {
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = Username.getText().toString().trim();
                final String password = LoginPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    Username.setError("Username is required");
                    return;
                }

                if (password.isEmpty()) {
                    LoginPassword.setError("Password is required");
                    return;
                }

                mdial.setMessage("Processing ...");
                mdial.show();

                db.collection("users").whereEqualTo("username", username).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                    String email = document.getString("email");

                                    authorization.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        mdial.dismiss();
                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                                        mdial.dismiss();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(Login.this, "Username not found or login failed", Toast.LENGTH_SHORT).show();
                                    mdial.dismiss();
                                }
                            }
                        });
            }
        });
    }

    private void Register() {
        NewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Registration.class);
                startActivity(intent);
            }
        });
    }
}
