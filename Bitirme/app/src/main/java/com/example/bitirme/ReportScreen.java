package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ReportScreen extends AppCompatActivity {

    static String username;
    static ArrayList<Measurement> arrayList;
    static DatabaseReference reference;
    Button butPulse, butStep, butBodyTemp, butHumidity;
    Button butUpdate, butGraph;
    ListView listView;
    ArrayAdapter arrayAdapter;
    static String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);
        butPulse = findViewById(R.id.butPulse);
        butStep = findViewById(R.id.butStep);
        butBodyTemp = findViewById(R.id.butBodyTemp);
        butHumidity = findViewById(R.id.butHumidity);
        butUpdate = findViewById(R.id.butUpdate);
        butGraph = findViewById(R.id.butGraph);
        listView = findViewById(R.id.listview);

        userData();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        reference = database.child("healthData").child(username);
        arrayList = new ArrayList<>();

        Measurement.downloadData(username);

        Toast.makeText(getApplicationContext(), "Data is updated", Toast.LENGTH_SHORT).show();


        butPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Pulse: " + arrayList.get(i).getPulse();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "pulse";
            }
        });

        butStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Step: " + arrayList.get(i).getStep();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "step";

            }
        });

        butBodyTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Body Temp: " + arrayList.get(i).getBodyTemp();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "bodyTemp";
            }
        });

        butHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Humidity: %" + arrayList.get(i).getHumidity();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "humidity";

            }
        });

        butUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                reference = database.child("healthData").child(username);
                arrayList = new ArrayList<>();

                Measurement.downloadData(username);

                Toast.makeText(getApplicationContext(), "Data is updated", Toast.LENGTH_SHORT).show();

            }
        });

        butGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportScreen.this, BarChartPage.class);
                //Intent intent = new Intent(ReportScreen.this, Graph.class);
                intent.putExtra("username", username);

                String[] arrayDate = new String[arrayList.size()];
                double[] arrayPulse = new double[arrayList.size()];
                double[] arrayStep = new double[arrayList.size()];
                double[] arrayBodyTemp = new double[arrayList.size()];
                double[] arrayHumidity = new double[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    arrayDate[i] = arrayList.get(i).getDate();
                    arrayPulse[i] = Double.parseDouble(arrayList.get(i).getPulse());
                    arrayStep[i] = Double.parseDouble(arrayList.get(i).getStep());
                    arrayBodyTemp[i] = Double.parseDouble(arrayList.get(i).getBodyTemp());
                    arrayHumidity[i] = Double.parseDouble(arrayList.get(i).getHumidity());

                }

                intent.putExtra("date",arrayDate);
                switch (status){
                    case "pulse":
                        intent.putExtra("graph", "pulse");
                        intent.putExtra("array", arrayPulse);
                        break;

                    case "step":
                        intent.putExtra("graph", "step");
                        intent.putExtra("array", arrayStep);
                        break;

                    case "bodyTemp":
                        intent.putExtra("graph", "bodyTemp");
                        intent.putExtra("array", arrayBodyTemp);
                        break;

                    case "humidity":
                        intent.putExtra("graph", "humidity");
                        intent.putExtra("array", arrayHumidity);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "First select the data.", Toast.LENGTH_SHORT).show();
                        break;
                }

                startActivity(intent);

            }
        });

    }
    private void userData() {

        Intent intent = getIntent();
        username = intent.getStringExtra("username");



    }


}