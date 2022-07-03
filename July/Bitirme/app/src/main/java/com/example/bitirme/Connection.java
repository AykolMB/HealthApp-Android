package com.example.bitirme;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Connection {
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //hc05
    public static BluetoothSocket socket = null;
    //public static final String HC05Address = "00:21:13:01:2A:BF"; // hc05
    public static final String HC05Address = "34:94:54:24:8B:3A"; // esp32
    public static boolean connectionStatus = false;
    public static int received;
    public static long receivedLong;
    public static String receivedString = "";

    public Connection() {

    }

    public boolean isConnectionEstablished(){
        if (connectionStatus)
            return true;
        return false;
    }

    public boolean startConnection(){
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice hc05 = btAdapter.getRemoteDevice(HC05Address);

        int counter = 0;

        do {
            try {
                socket = hc05.createRfcommSocketToServiceRecord(mUUID);
                socket.connect();
                if (socket.isConnected()){
                    connectionStatus = true;
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            counter++;
        } while (!socket.isConnected() && counter < 3);
        return false;
    }

    public int sendRequest(int asciiRequest){
        String temp = "";
        if(isConnectionEstablished()){
            if(asciiRequest == 0)
                asciiRequest = 109; //109 = m in ascii
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(asciiRequest);
            }catch (IOException e){
                e.printStackTrace();
            }
            received=0;
            try {
                InputStream inputStream = socket.getInputStream();
                inputStream.skip(inputStream.available());
                Thread.sleep(10);
                for (int i = 0 ; i < 100 ; i++){
                    int b = inputStream.read();
                    Log.d("Karakter:" + (i+1) , ""+(char) b);
                    temp += Integer.parseInt(String.valueOf( (char) b ));
                    //received += b; //Integer.parseInt(String.valueOf((char) b));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        received = Integer.parseInt(temp);
        return received;
    }

    public String sendRequestString(int asciiRequest){
        String temp = "";
        if(isConnectionEstablished()){
            if(asciiRequest == 0)
                asciiRequest = 109; //109 = m in ascii
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(asciiRequest);
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                InputStream inputStream = socket.getInputStream();
                inputStream.skip(inputStream.available());
                Thread.sleep(50);
                for (int i = 0 ; i < 60 ; i++){
                    int b = inputStream.read();
                    Log.d("Karakter:" + (i+1) , ""+(char) b);
                    temp += (String.valueOf( (char) b ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        receivedString = temp;
        return receivedString;
    }

    public boolean endConnection(){
        if (connectionStatus){
            try {
                socket.close();
                connectionStatus = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
