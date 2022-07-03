package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class App_Main extends AppCompatActivity {

    static ArrayList<Measurement> arrayList = new ArrayList<>();
    TextView nameMain, emailMain, usernameMain, ageMain;
    TextView measureTV, reportTV, logout;
    ImageView iv_bluetooth;
    String username;
    CardView pulseCard;
    public static Connection connection = new Connection();

    Measurement tempMeasurement = new Measurement("" , "0" , "0", "0", "0", "0","0");

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
            Log.e("Tag", "BT Error : " + e.getMessage());
        }

        // User information
        showAllUserData();

        Measurement.dataCount(username);


        iv_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Measurement.dataCount(username);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connection.endConnection();
                Toast.makeText(App_Main.this, "Successfully logout.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainPage.class));
                finish();
            }
        });

        measureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String resultString = connection.sendRequestString(109);
                String resultStr = resultString;

                String outTemp = removeZeros(resultStr.substring(0,10));
                String airPressure = removeZeros(resultStr.substring(10,20));
                String heartRate = removeZeros(resultStr.substring(20,30));
                String Spo2 = removeZeros(resultStr.substring(30,40));
                String bodytemp = removeZeros(resultStr.substring(40,50));
                String humidity = removeZeros(resultStr.substring(50,60));

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String dateStr = formatter.format(date);

                if(Measurement.healthDataCount == 0)
                    Measurement.dataCount(username);

                double randomPulseData = Double.parseDouble(heartRate);
                double randomSpo2Data = Double.parseDouble(Spo2);
                double randomBodyTempData = Double.parseDouble(bodytemp);
                double randomOutsideTempData = Double.parseDouble(outTemp);
                double randomHumidityData = Double.parseDouble(humidity);
                double randomAirPressureData = Double.parseDouble(airPressure);

                Measurement temp2 = new Measurement(dateStr, "" + randomPulseData, "" + randomSpo2Data, "" + randomBodyTempData,
                        "" + randomOutsideTempData, "" + randomHumidityData, "" + randomAirPressureData);

                long newId = Measurement.healthDataCount + 1;
                Measurement.uploadData2(newId, username , temp2);

                Toast.makeText(App_Main.this, "Measurements are uploaded.", Toast.LENGTH_SHORT).show();

                tempMeasurement = temp2;
                Measurement.healthDataCount = newId;
            }
        });

        reportTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(App_Main.this, ReportScreen.class);

                intent.putExtra("username", username);
                intent.putExtra("date",tempMeasurement.getDate());
                intent.putExtra("pulse",tempMeasurement.getPulseData());
                intent.putExtra("spo2",tempMeasurement.getSpo2Data());
                intent.putExtra("bodyTemp",tempMeasurement.getBodyTempData());
                intent.putExtra("outTemp",tempMeasurement.getOutsideTempData());
                intent.putExtra("humidity",tempMeasurement.getHumidityData());
                intent.putExtra("airPressure",tempMeasurement.getAirPressureData());

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

    private static String removeZeros(String asd) {

        String temp = "";

        for (int i = 0 ; i< asd.length(); i++){
            String s = String.valueOf(asd.charAt(i));
            if(!s.equals("_")){
                temp += s;
            }
        }
        return temp;
    }

    private void showAllUserData() {

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String surname = intent.getStringExtra("surname");
        String name = firstName + " " + surname;
        String email = intent.getStringExtra("email");
        String age = intent.getStringExtra("age");
        String password = intent.getStringExtra("password");

        String tempUsername = email.substring(0,email.indexOf("@"));
        if (tempUsername.contains("."))
            tempUsername = tempUsername.replace(".","");
        username = tempUsername;


        nameMain.setText("Name: " + name);
        emailMain.setText("Email: " + email);
        usernameMain.setText("Username: " + username);
        ageMain.setText("Age: " + age);

    }
}