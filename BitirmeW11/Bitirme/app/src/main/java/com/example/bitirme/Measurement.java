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


    String date;
    String pulse, step, bodyTemp, humidity;
    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference("healthData");
    private static DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("healthData");
    private static Query query;

    // Aynı verileri tekrar listeye ekleme hatası buradan kaynaklanmakta.
    //
    static ArrayList<Measurement> arrayList = new ArrayList<>();

    static ArrayList<Measurement> temp;

    public Measurement() {

    }
    public Measurement(String date, String pulse, String step, String bodyTemp, String humidity) {

        this.date = date;
        this.pulse = pulse;
        this.step = step;
        this.bodyTemp = bodyTemp;
        this.humidity = humidity;
    }

    public static void uploadData(String username, Measurement measurement) {

        String date = measurement.getDate();
        String pulse = measurement.getPulse();
        String step = measurement.getStep();
        String bodyTemp = measurement.getBodyTemp();
        String humidity = measurement.getHumidity();

        Query upload = reference.orderByChild("username").equalTo(username);

        upload.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    reference.child(username).child(date).child("date").setValue(date);
                    reference.child(username).child(date).child("pulse").setValue(pulse);
                    reference.child(username).child(date).child("step").setValue(step);
                    reference.child(username).child(date).child("bodyTemp").setValue(bodyTemp);
                    reference.child(username).child(date).child("humidity").setValue(humidity);
                } else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("healthData");
                    reference.child(username).child(date).child("date").setValue(date);
                    reference.child(username).child(date).child("pulse").setValue(pulse);
                    reference.child(username).child(date).child("step").setValue(step);
                    reference.child(username).child(date).child("bodyTemp").setValue(bodyTemp);
                    reference.child(username).child(date).child("humidity").setValue(humidity);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void downloadData(String username) {
        query = ref2.child(username).limitToLast(10);
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

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getBodyTemp() {
        return bodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        this.bodyTemp = bodyTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public static ArrayList<Measurement> getArrayList() {
        return arrayList;
    }

    public static void setArrayList(ArrayList<Measurement> arrayList) {
        Measurement.arrayList = arrayList;
    }


}
