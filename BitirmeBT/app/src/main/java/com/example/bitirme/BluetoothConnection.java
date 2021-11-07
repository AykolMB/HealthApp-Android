package com.example.bitirme;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.UUID;

public class BluetoothConnection extends AppCompatActivity {

    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);


        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println(btAdapter.getBondedDevices());

        BluetoothDevice hc05 = btAdapter.getRemoteDevice("00:21:13:01:2A:BF");
        System.out.println(hc05.getName());
        //Toast.makeText(getApplicationContext(),hc05.getName(), Toast.LENGTH_SHORT).show();
        BluetoothSocket socket = null;
        int counter = 0;

        do {
            try {
                socket = hc05.createRfcommSocketToServiceRecord(mUUID);
                socket.connect();
                Toast.makeText(getApplicationContext(),"" +socket.isConnected(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        }while (!socket.isConnected() && counter < 3);

        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(109);
        }catch (IOException e){
            e.printStackTrace();
        }

        try {
            InputStream inputStream = socket.getInputStream();
            inputStream.skip(inputStream.available());
/*
            for (int i = 0 ; i< 26 ; i++){
                byte b = (byte) inputStream.read();
                Log.d("Tag", String.valueOf((char) b));
                Toast.makeText(getApplicationContext(), "Value : " + b, Toast.LENGTH_SHORT).show();
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
    }
}