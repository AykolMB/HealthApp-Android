package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App_Main extends AppCompatActivity {
    TextView nameMain, emailMain, usernameMain, ageMain;
    TextView pulseTV, tempTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        nameMain = findViewById(R.id.name_AppMain);
        emailMain = findViewById(R.id.email_AppMain);
        usernameMain = findViewById(R.id.username_AppMain);
        ageMain = findViewById(R.id.age_AppMain);
        pulseTV = findViewById(R.id.pulse_AppMain);
        tempTV = findViewById(R.id.temp_AppMain);

        TextView logout = findViewById(R.id.logout_App);

        // User information
        showAllUserData();



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(App_Main.this, "Successfully logout.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainPage.class));
                finish();
            }
        });

        pulseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App_Main.this, pulsePage.class);
                intent.putExtra("username", usernameMain.getText().toString());
                startActivity(intent);
            }
        });

        tempTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showAllUserData() {

        Intent intent =getIntent();
        String firstName = intent.getStringExtra("firstName");
        String surname = intent.getStringExtra("surname");
        String name = firstName + " " + surname;
        String email = intent.getStringExtra("email");
        String age = intent.getStringExtra("age");
        String password = intent.getStringExtra("password");
        String username = email.substring(0,email.indexOf("@"));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SensorValues");
        ref.child(username).setValue("");

        nameMain.setText("Name: " + name);
        emailMain.setText("Email: " + email);
        usernameMain.setText("Username: " + username);
        ageMain.setText("Age: " + age);

    }
}