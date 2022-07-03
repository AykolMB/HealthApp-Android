package com.example.bitirme;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Measurement {


    // pulse + spo2 , bodyTemp , outsideTemp , humidity , airPressure(barometer)
    String date;
    String pulseData, spo2Data, bodyTempData, outsideTempData, humidityData, airPressureData;
    public static long healthDataCount = 0 ;


    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference("healthData");
    private static DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("healthData");
    private static Query query;

    // Aynı verileri tekrar listeye ekleme hatası buradan kaynaklanmakta.
    //
    static ArrayList<Measurement> arrayList = new ArrayList<>();

    static ArrayList<Measurement> temp;

    public Measurement() {

    }

    public Measurement(String date, String pulseData, String spo2Data, String bodyTempData, String outsideTempData, String humidityData, String airPressureData) {
        this.date = date;
        this.pulseData = pulseData;
        this.spo2Data = spo2Data;
        this.bodyTempData = bodyTempData;
        this.outsideTempData = outsideTempData;
        this.humidityData = humidityData;
        this.airPressureData = airPressureData;
    }

    public static void uploadData2(long newId, String username, Measurement measurement){
        String id = "" + newId;
        String dateData = measurement.getDate();

        String pulseData = measurement.getPulseData();
        String spo2Data = measurement.getSpo2Data();
        String bodyTempData = measurement.getBodyTempData();
        String outsideTempData = measurement.getOutsideTempData();
        String humidityData = measurement.getHumidityData();
        String airPressureData = measurement.getAirPressureData();

        Query upload = reference.orderByChild("username").equalTo(username);

        upload.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    reference.child(username).child(id).child("id").setValue(id);
                    reference.child(username).child(id).child("date").setValue(dateData);
                    reference.child(username).child(id).child("pulseData").setValue(pulseData);
                    reference.child(username).child(id).child("spo2Data").setValue(spo2Data);
                    reference.child(username).child(id).child("bodyTempData").setValue(bodyTempData);
                    reference.child(username).child(id).child("outsideTempData").setValue(outsideTempData);
                    reference.child(username).child(id).child("humidityData").setValue(humidityData);
                    reference.child(username).child(id).child("airPressureData").setValue(airPressureData);

                }else{

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("healthData");

                    reference.child(username).child(id).child("date").setValue(dateData);
                    reference.child(username).child(id).child("pulseData").setValue(pulseData);
                    reference.child(username).child(id).child("spo2Data").setValue(spo2Data);
                    reference.child(username).child(id).child("bodyTempData").setValue(bodyTempData);
                    reference.child(username).child(id).child("outsideTempData").setValue(outsideTempData);
                    reference.child(username).child(id).child("humidityData").setValue(humidityData);
                    reference.child(username).child(id).child("airPressureData").setValue(airPressureData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void dataCount(String username){
        query = ref2.child(username);
        temp = new ArrayList<>();
        readData(new Measurement.FirebaseCallback() {
            @Override
            public void onCallback(List<Measurement> list) {
                temp = (ArrayList<Measurement>) list;
                healthDataCount = temp.size();
                Log.d("TagD", "count : " + temp.size());
            }
        });
    }

    public static void downloadData(String username) {
        query = ref2.child(username).limitToLast(7);
        temp = new ArrayList<>();
        readData(new Measurement.FirebaseCallback() {
            @Override
            public void onCallback(List<Measurement> list) {
                Log.d("Tag: ", list.toString());
                temp = (ArrayList<Measurement>) list;

            }

        });

    }

    private static void readData(Measurement.FirebaseCallback firebaseCallback) {

        arrayList = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Measurement temp = ds.getValue(Measurement.class);
                    arrayList.add(temp);
                }
                firebaseCallback.onCallback(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        query.addListenerForSingleValueEvent(valueEventListener);


    }

    private interface FirebaseCallback {
        void onCallback(List<Measurement> list);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static ArrayList<Measurement> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<Measurement> arrayList) {
        Measurement.arrayList = arrayList;
    }

    public String getPulseData() {
        return pulseData;
    }

    public void setPulseData(String pulseData) {
        this.pulseData = pulseData;
    }

    public String getSpo2Data() {
        return spo2Data;
    }

    public void setSpo2Data(String spo2Data) {
        this.spo2Data = spo2Data;
    }

    public String getBodyTempData() {
        return bodyTempData;
    }

    public void setBodyTempData(String bodyTempData) {
        this.bodyTempData = bodyTempData;
    }

    public String getOutsideTempData() {
        return outsideTempData;
    }

    public void setOutsideTempData(String outsideTempData) {
        this.outsideTempData = outsideTempData;
    }

    public String getHumidityData() {
        return humidityData;
    }

    public void setHumidityData(String humidityData) {
        this.humidityData = humidityData;
    }

    public String getAirPressureData() {
        return airPressureData;
    }

    public void setAirPressureData(String airPressureData) {
        this.airPressureData = airPressureData;
    }


}
