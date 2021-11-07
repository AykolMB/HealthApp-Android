package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bitirme.databinding.ActivityMainBinding;
import com.example.bitirme.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    TextView firstName_R, surname_R, email_R, password_R, age_R, login_Register;
    Button register_R;
    ProgressBar pBar;

    String firstName, surname, email, username, password, age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firstName_R = findViewById(R.id.firstName_Register);
        surname_R = findViewById(R.id.surname_Register);
        email_R = findViewById(R.id.email_Register);
        password_R = findViewById(R.id.password_Register);
        age_R = findViewById(R.id.age_Register);
        register_R = findViewById(R.id.butRegister_Register);
        login_Register = findViewById(R.id.login_Register);
        pBar = findViewById(R.id.progressBar);

        register_R.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = firstName_R.getText().toString().trim();
                surname = surname_R.getText().toString().trim();
                email = email_R.getText().toString().trim();
                password = password_R.getText().toString().trim();
                age = age_R.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_R.setError("Email is required");
                    email_R.requestFocus();
                    return;
                } else {
                    if (!email.contains("@")) {
                        email_R.setError("Please check entered email");
                        email_R.requestFocus();
                        return;
                    }
                }
                if (TextUtils.isEmpty(password)) {
                    password_R.setError("Password is required");
                    password_R.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    password_R.setError("Password must be at least 6 characters");
                    password_R.requestFocus();
                    return;
                }

                username = email.substring(0, email.indexOf("@"));
                pBar.setVisibility(View.VISIBLE);
                Users user = new Users(firstName, surname, email, username, password, age);

                if (!firstName.isEmpty() && !surname.isEmpty() && !age.isEmpty()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    Query checkUser = reference.orderByChild("username").equalTo(username);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                String emailDB = snapshot.child(username).child("email").getValue(String.class);
                                if (email.equals(emailDB)) {
                                    email_R.setError("This email is exist.");
                                    pBar.setVisibility(View.INVISIBLE);
                                    email_R.requestFocus();
                                    Toast.makeText(Register.this, "This mail is already registered.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                ref.child(username).setValue(user);
                                Toast.makeText(Register.this, "User Created..", Toast.LENGTH_SHORT).show();
                                DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("healthData");
                                ref2.child(username);
                                startActivity(new Intent(Register.this, MainPage.class));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });

        login_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainPage.class);
                startActivity(intent);
            }
        });

    }

    private void addUser(Users user) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(username).setValue(user);
    }

}