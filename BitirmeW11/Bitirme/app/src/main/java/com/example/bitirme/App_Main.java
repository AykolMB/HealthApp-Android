package com.example.bitirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class App_Main extends AppCompatActivity {

    static ArrayList<Measurement> arrayList = new ArrayList<>();
    TextView nameMain, emailMain, usernameMain, ageMain;
    TextView measureTV, reportTV, logout;
    ImageView iv_bluetooth;
    String username;
    CardView pulseCard;
    public static Connection connection = new Connection();

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
        pulseCard = findViewById(R.id.pulseCard);
        logout = findViewById(R.id.logout_App);


        try{
        if (!connection.isConnectionEstablished()){
            if (connection.startConnection()){
                Toast.makeText(this, "Connection Established", Toast.LENGTH_SHORT).show();
                buttonSetVisible(true);
            }else{
                Toast.makeText(this, "Connection CAN NOT Established\nMeasureButton will be disabled", Toast.LENGTH_SHORT).show();
                buttonSetVisible(false);
            }
        }
        }catch (Exception e){

        }


        // User information
        showAllUserData();


        iv_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(App_Main.this, BluetoothConnection.class));
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

                int result =connection.sendRequest(109); // 109 == m
                //Log.d("Result", ""+result);
                //Toast.makeText(getApplicationContext(), "Result: "+ result , Toast.LENGTH_SHORT).show();


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String dateStr = formatter.format(date);

                //Ölçümleri yaptır
                double randomPulse = Math.floor(Math.random() * (result) + 30);    // 50 - 100
                double randomStep = Math.floor(Math.random() * ((result * 100)) + 1000); // 1000 - 2000
                double randomBodyTemp = Math.floor(Math.random() * (4) + (result-30));  // 34 - 38
                double randomHumidity = Math.floor(Math.random() * (30) + (result - 30)); // 30 - 60

                Measurement temp = new Measurement(dateStr, "" + randomPulse, "" + randomStep, "" + randomBodyTemp, "" + randomHumidity);
                Measurement.uploadData(username, temp);

            }
        });

        reportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(App_Main.this, ReportScreen.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void buttonSetVisible(boolean status) {

        if (status == true){
            pulseCard.setVisibility(View.VISIBLE);
            measureTV.setVisibility(View.VISIBLE);
            pulseCard.setEnabled(true);
            measureTV.setEnabled(true);
        }else{
            pulseCard.setVisibility(View.INVISIBLE);
            measureTV.setVisibility(View.INVISIBLE);
            pulseCard.setEnabled(false);
            measureTV.setEnabled(false);
        }
    }



    private void showAllUserData() {

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String surname = intent.getStringExtra("surname");
        String name = firstName + " " + surname;
        String email = intent.getStringExtra("email");
        String age = intent.getStringExtra("age");
        String password = intent.getStringExtra("password");
        username = email.substring(0, email.indexOf("@"));

        nameMain.setText("Name: " + name);
        emailMain.setText("Email: " + email);
        usernameMain.setText("Username: " + username);
        ageMain.setText("Age: " + age);

    }
}