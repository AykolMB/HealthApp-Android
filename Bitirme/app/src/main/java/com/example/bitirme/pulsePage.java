package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class pulsePage extends AppCompatActivity {


    String username;
    Button pulseMeasure;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SensorValues");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse_page);

        pulseMeasure = findViewById(R.id.pulseMeasure);

        getUsername();

        pulseMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = 70;
                Toast.makeText(pulsePage.this, "The masurement result is " + result, Toast.LENGTH_SHORT).show();
                reference.child(username).child("Pulse").setValue(result);

            }
        });
    }

    private void getDataFromFDB() {

    }

    private void getUsername() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }
}