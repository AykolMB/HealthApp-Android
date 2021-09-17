package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class App_Main extends AppCompatActivity {

    static ArrayList<Measurement> arrayList = new ArrayList<>();
    TextView nameMain, emailMain, usernameMain, ageMain;
    TextView measureTV, reportTV;
    ImageView iv_bluetooth;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        nameMain = findViewById(R.id.name_AppMain);
        emailMain = findViewById(R.id.email_AppMain);
        usernameMain = findViewById(R.id.username_AppMain);
        ageMain = findViewById(R.id.age_AppMain);
        measureTV = findViewById(R.id.measure_AppMain);
        reportTV = findViewById(R.id.report_AppMain);
        iv_bluetooth = findViewById(R.id.iv_bluetooth);

        TextView logout = findViewById(R.id.logout_App);

        // User information
        showAllUserData();


        iv_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(App_Main.this, bluetooth.class));

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(App_Main.this, "Successfully logout.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainPage.class));
                finish();
            }
        });

        measureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String dateStr = formatter.format(date);

                //Ölçümleri yaptır

                double randomPulse = Math.floor(Math.random()*(50) +50);    // 50 - 100
                double randomStep = Math.floor(Math.random()*(1000) +1000); // 1000 - 2000
                double randomBodyTemp = Math.floor(Math.random()*(4) +34);  // 34 - 38
                double randomHumidity = Math.floor(Math.random()*(30) +30); // 30 - 60


                Measurement temp = new Measurement(dateStr, ""+randomPulse, ""+randomStep, ""+randomBodyTemp, ""+randomHumidity);
                Measurement.uploadData(username, temp);

                Toast.makeText(getApplicationContext(), "Data upload", Toast.LENGTH_SHORT).show();

            }
        });

        reportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(App_Main.this, ReportScreen.class);
                intent.putExtra("username", username);
                startActivity(intent);

                /*
                arrayList = Measurement.downloadData(username);
                Log.d("TAG", "Arraylist Size: " + arrayList.size());
                */
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
        username = email.substring(0,email.indexOf("@"));

        nameMain.setText("Name: " + name);
        emailMain.setText("Email: " + email);
        usernameMain.setText("Username: " + username);
        ageMain.setText("Age: " + age);

    }
}