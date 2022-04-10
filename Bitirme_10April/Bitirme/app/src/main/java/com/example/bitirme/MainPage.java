package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainPage extends AppCompatActivity {
    TextView newAcc_mainPage,email_mainPage,pw_mainPage;
    Button login_mainPage;
    ProgressBar progressBar_mainPage;
    public static Connection connection = new Connection();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newAcc_mainPage = findViewById(R.id.newAcc_mainPage);  // Register
        email_mainPage = findViewById(R.id.email_mainPage);    // Email
        pw_mainPage = findViewById(R.id.pw_mainPage);          // Password
        login_mainPage = findViewById(R.id.login_mainPage);      // MainPage Button
        progressBar_mainPage = findViewById(R.id.progressBar_mainPage);

        newAcc_mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainPage.this, Register.class));
            }
        });

        login_mainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

    }

    public void loginUser(){
        if(!validateUser() || !validatePassword()){
            return;
        }else{
            isUser();
        }
    }

    private void isUser() {
        String email = email_mainPage.getText().toString().trim();
        String tempUsername = email.substring(0, email.indexOf("@"));
        String password = pw_mainPage.getText().toString().trim();

        if(tempUsername.contains("."))
            tempUsername = tempUsername.replace(".","");

        String username = tempUsername;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    email_mainPage.setError(null);

                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                    if (passwordFromDB.equals(md5(password))){
                        String firstNameFromDB = snapshot.child(username).child("firstName").getValue(String.class);
                        String surnameFromDB = snapshot.child(username).child("surname").getValue(String.class);
                        String emailFromDB = snapshot.child(username).child("email").getValue(String.class);
                        String ageFromDB = snapshot.child(username).child("age").getValue(String.class);

                        Toast.makeText(MainPage.this, "Successfully Login",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainPage.this, App_Main.class);
                        intent.putExtra("firstName", firstNameFromDB);
                        intent.putExtra("surname", surnameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("age", ageFromDB);

                        startActivity(intent);
                    }else {
                        pw_mainPage.setError("Wrong Password");
                        pw_mainPage.requestFocus();
                    }
                }else {
                    email_mainPage.setError("No such User Exist");
                    email_mainPage.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean validatePassword() {
        String password = pw_mainPage.getText().toString().trim();

        if (password.isEmpty()){
            pw_mainPage.setError("Field can not be empty");
            pw_mainPage.requestFocus();
            return false;
        }else{
            pw_mainPage.setError(null);
            return true;
        }

    }

    private boolean validateUser() {
        String email = email_mainPage.getText().toString().trim();

        if (email.isEmpty()){
            email_mainPage.setError("Field can not be empty");
            email_mainPage.requestFocus();
            return false;
        }else if(!email.contains("@")){
            email_mainPage.setError("Please check entered email");
            email_mainPage.requestFocus();
            return false;
        }
        else {
            email_mainPage.setError(null);
            return true;
        }
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}