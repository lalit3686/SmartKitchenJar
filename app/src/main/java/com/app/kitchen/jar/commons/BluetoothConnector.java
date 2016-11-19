package com.app.kitchen.jar.commons;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;

import com.app.kitchen.jar.application.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Lalit T. Poptani on 10/29/2016.
 */

public class BluetoothConnector {

    private boolean isConnected;
    private boolean isListening;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private static BluetoothConnector instance;

    public static BluetoothConnector getInstance(){
        if(instance == null){
            instance = new BluetoothConnector();
        }
        return instance;
    }
    public boolean bluetoothConnect(BluetoothDevice device) {
        mDevice = device;

        try {
            mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(getUUID());
            mSocket.connect();

            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();

            isConnected = true;
            return true;
        } catch (java.io.IOException io) {
            io.printStackTrace();
            bluetoothDisconnect();
            return false;
        }
    }

    private UUID getUUID() {
        return UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int bulkWrite(byte[] data) {
        try {
            if (isConnected) {
                mOutputStream.write(data);
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            bluetoothDisconnect();
        }

        return -1;
    }

    public String bulkRead() {
        try {
            byte buffer[];
            buffer = new byte[1024];
            if (isConnected && mInputStream.available() > 0) {
                mInputStream.read(buffer);
                StringBuilder builder = new StringBuilder();
                for (byte read : buffer) {
                    builder.append((char) read);
                }
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            bluetoothDisconnect();
        }

        return null;
    }

    public void listen() {
        byte buffer[];
        buffer = new byte[1024];

        isListening = true;

        while(isListening){
            try {
                if (isConnected && mInputStream.available() > 0) {
                    mInputStream.read(buffer);
                    StringBuilder builder = new StringBuilder();
                    for (byte read : buffer) {
                        builder.append((char) read);
                    }
                    MyApplication.getEventBusInstance().post(builder.toString());
                    SystemClock.sleep(1000);
                }
            } catch (IOException e) {
                e.printStackTrace();
                bluetoothDisconnect();
                break;
            }
        }
    }

    public void stopListening(){
        isListening = false;
    }

    public void bluetoothDisconnect() {
        isConnected = false;

        try {
            if (mInputStream != null) {
                mInputStream.close();
            }

            if (mOutputStream != null) {
                mOutputStream.close();
            }

            if (mSocket != null) {
                mSocket.close();
            }

            mDevice = null;
        } catch (java.io.IOException io) {
            io.printStackTrace();
        }
    }
}