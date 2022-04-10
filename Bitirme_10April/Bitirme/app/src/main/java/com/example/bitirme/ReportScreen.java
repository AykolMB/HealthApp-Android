package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ReportScreen extends AppCompatActivity {

    static String username;
    static Measurement lastMeasurement = new Measurement();
    static ArrayList<Measurement> arrayList;
    static DatabaseReference reference;
    Button butPulse, butStep, butBodyTemp, butHumidity;
    Button butSpo2, butAirPressure, butOutTemp;
    Button butLastMeasReport;
    Button butUpdate, butGraph;
    TextView reportText;
    ListView listView;
    ArrayAdapter arrayAdapter;
    static String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_screen);
        butPulse = findViewById(R.id.butPulse);
        butSpo2 = findViewById(R.id.butSpo2);
        butOutTemp = findViewById(R.id.butOutTemp);
        butBodyTemp = findViewById(R.id.butBodyTemp);
        butHumidity = findViewById(R.id.butHumidity);
        butAirPressure = findViewById(R.id.butAirPressure);
        butUpdate = findViewById(R.id.butUpdate);
        butGraph = findViewById(R.id.butGraph);
        listView = findViewById(R.id.listview);
        reportText = findViewById(R.id.reportText);
        butLastMeasReport = findViewById(R.id.butLastMeasReport);

        userData();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        reference = database.child("healthData").child(username);

        arrayList = new ArrayList<>();

        Measurement.downloadData(username);

        //Toast.makeText(getApplicationContext(), "Data is updated", Toast.LENGTH_SHORT).show();

        PrepareLastMeasurementReport(lastMeasurement);

        butLastMeasReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrepareLastMeasurementReport(lastMeasurement);

            }
        });

        butPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Pulse: " + arrayList.get(i).getPulseData();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);
                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "PULSE";

                butGraph.performClick();

            }
        });

        butSpo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - SPO2: " + arrayList.get(i).getSpo2Data();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);
                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "SPO2";

                butGraph.performClick();

            }
        });

        butOutTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Outside Temperature: " + arrayList.get(i).getOutsideTempData();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "OUTTEMP";

                butGraph.performClick();
            }
        });

        butBodyTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Body Temp: " + arrayList.get(i).getBodyTempData();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "BODYTEMP";

                butGraph.performClick();
            }
        });

        butHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Humidity: %" + arrayList.get(i).getHumidityData();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "HUMIDITY";

                butGraph.performClick();

            }
        });

        butAirPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList = Measurement.arrayList;

                String[] strArray = new String[arrayList.size()];

                for (int i = 0 ; i<arrayList.size() ; i++){

                    strArray[i] = "Date: " + arrayList.get(i).getDate() + " - Air Pressure: " + arrayList.get(i).getAirPressureData();

                }

                arrayAdapter = new ArrayAdapter(ReportScreen.this, android.R.layout.simple_list_item_1, strArray);

                listView.setAdapter(arrayAdapter);

                Log.d("ReportScreen: ", arrayList.toString());
                status = "AIRPRESSURE";

                butGraph.performClick();

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
                intent.putExtra("username", username);

                String[] arrayDateData = new String[arrayList.size()];
                double[] arrayPulseData = new double[arrayList.size()];
                double[] arraySpo2Data = new double[arrayList.size()];
                double[] arrayBodyTempData = new double[arrayList.size()];
                double[] arrayOutsideTempData = new double[arrayList.size()];
                double[] arrayHumidityData = new double[arrayList.size()];
                double[] arrayAirPressureData = new double[arrayList.size()];

                for (int i = 0 ; i < arrayList.size() ; i++){

                    arrayDateData[i] = arrayList.get(i).getDate();
                    arrayPulseData[i] = Double.parseDouble(arrayList.get(i).getPulseData());
                    arraySpo2Data[i] = Double.parseDouble(arrayList.get(i).getSpo2Data());
                    arrayBodyTempData[i] = Double.parseDouble(arrayList.get(i).getBodyTempData());
                    arrayOutsideTempData[i]=Double.parseDouble(arrayList.get(i).getOutsideTempData());
                    arrayHumidityData[i] =Double.parseDouble(arrayList.get(i).getHumidityData());
                    arrayAirPressureData[i]=Double.parseDouble(arrayList.get(i).getAirPressureData());

                }

                intent.putExtra("dateData", arrayDateData);
                switch (status){
                    case "PULSE":
                        intent.putExtra("graph", "PULSE");
                        intent.putExtra("array", arrayPulseData);
                        break;
                    case "SPO2":
                        intent.putExtra("graph", "SPO2");
                        intent.putExtra("array", arraySpo2Data);
                        break;
                    case "BODYTEMP":
                        intent.putExtra("graph", "BODY TEMPERATURE");
                        intent.putExtra("array", arrayBodyTempData);
                        break;
                    case "OUTTEMP":
                        intent.putExtra("graph", "OUTSIDE TEMPERATURE");
                        intent.putExtra("array", arrayOutsideTempData);
                        break;
                    case "HUMIDITY":
                        intent.putExtra("graph", "HUMIDITY");
                        intent.putExtra("array", arrayHumidityData);
                        break;
                    case "AIRPRESSURE":
                        intent.putExtra("graph", "AIR PRESSURE");
                        intent.putExtra("array", arrayAirPressureData);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "First select the data.", Toast.LENGTH_SHORT).show();
                        break;
                }

                startActivity(intent);

            }
        });

    }

    private void PrepareLastMeasurementReport(Measurement lastMeasurement) {

        String report = "";
        double pulse, spo2, bodyTemp, outTemp, humidity, airPressure;
        pulse = Double.parseDouble(lastMeasurement.getPulseData());
        spo2 = Double.parseDouble(lastMeasurement.getSpo2Data());
        bodyTemp = Double.parseDouble(lastMeasurement.getBodyTempData());
        outTemp = Double.parseDouble(lastMeasurement.getOutsideTempData());
        humidity = Double.parseDouble(lastMeasurement.getHumidityData());
        airPressure = Double.parseDouble(lastMeasurement.getAirPressureData());

        if(pulse == 0 || spo2 == 0 || bodyTemp == 0 || outTemp == 0 || humidity == 0 || airPressure == 0)
            report = "";
        else {
            report += "Last measurement time: \n" + lastMeasurement.getDate() + "\n\n";

            report += GetPulseReport(pulse) + "\n";
            report += GetSPO2Report(spo2) + "\n";
            report += GetBodyTempReport(bodyTemp) + "\n";
            report += GetOutTempReport(outTemp) + "\n";
            report += GetHumidityReport(humidity) + "\n";
            report += GetAirPressureReport(airPressure) + "\n";
        }

        reportText.setText(report);

    }

    private String GetAirPressureReport(double airPressure) {
        String message = "Air pressure: " + airPressure + " => ";
        String keyword = "";
        if(airPressure <= 32  && airPressure >= 26)
            keyword = "Ideal";
        else
            keyword = "Not Ideal";

        return message + keyword + "\n";
    }

    private String GetHumidityReport(double humidity) {
        String message = "Humidity: " + humidity + " => ";
        String keyword = "";
        if (humidity >= 30 && humidity <= 50)
            keyword = "Ideal";
        else
            keyword = "Not Ideal";
        return message + keyword + "\n";
    }

    private String GetOutTempReport(double outTemp) {
        String message = "Outside temperature: " + outTemp + " => ";
        String keyword = "";
        if (outTemp >= 35)
            keyword = "High";
        else if (outTemp <= 10)
            keyword = "Low";
        else
            keyword = "Normal";
        return message + keyword + "\n";
    }

    private String GetBodyTempReport(double bodyTemp) {
        String message = "Body temperature: " + bodyTemp + " => ";
        String keyword = "";
        if (bodyTemp >= 38)
            keyword = "High Fever. Covid-19 Risk";
        else if (bodyTemp < 35)
            keyword = "Low Fever. Hypothermia Risk";
        else
            keyword = "Normal Fever";

        return message + keyword + "\n";
    }

    private String GetSPO2Report(double spo2) {
        String message = "SPO2 Level: " + spo2 + " => ";
        String keyword = "";

        if (spo2 >= 95)
            keyword = "Normal";
        else if (spo2 >= 90)
            keyword = "Acceptable";
        else
            keyword = "Call your health care provider";

        return message + keyword + "\n";
    }

    private String GetPulseReport(double pulse) {
        String message = "Pulse: " + pulse + " => ";
        String keyword = "";
        if (pulse <= 80 && pulse >= 55)
            keyword = "Normal";
        else if (pulse > 80)
            keyword = "High";
        else
            keyword = "Low";

        return message + keyword + "\n";
    }

    private void userData() {

        Intent intent = getIntent();
        username = intent.getStringExtra("username");


        lastMeasurement = new Measurement(intent.getStringExtra("date"),intent.getStringExtra("pulse"),
                intent.getStringExtra("spo2"),intent.getStringExtra("bodyTemp"),
                intent.getStringExtra("outTemp"),intent.getStringExtra("humidity"),
                intent.getStringExtra("airPressure"));


    }


}