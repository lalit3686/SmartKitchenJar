package com.app.kitchen.jar.commons;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;

import com.app.kitchen.jar.application.MyApplication;
import com.app.kitchen.jar.databases.TableJarInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    //boolean isFinalCurrentWeight = false;
    String lastReceivedWeight = "0.0";


    public static BluetoothConnector getInstance() {
        if (instance == null) {
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

//    @Deprecated
//    public String bulkRead() {
//        try {
//            byte buffer[];
//            buffer = new byte[1024];
//            if (isConnected && mInputStream.available() > 0) {
//                mInputStream.read(buffer);
//                StringBuilder builder = new StringBuilder();
//                for (byte read : buffer) {
//                    builder.append((char) read);
//                }
//
//                String readData = builder.toString();
//                if (readData == null) {
//                    readData = "0.0";
//                }
//
//                String[] temp = readData.trim().split("\n");
//                if (temp.length > 1) {
//                    return temp[temp.length - 1];
//                }
//                return readData;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            bluetoothDisconnect();
//        }
//
//        return null;
//    }

    /*public void listen() {
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
                    SystemClock.sleep(2000);
                }
            } catch (IOException e) {
                e.printStackTrace();
                bluetoothDisconnect();
                break;
            }
        }
    }*/

    public void listen() {
        isListening = true;

        while (isListening) {
            //String readData = bulkRead();
            try {
                String currentWeight = readLine(mInputStream);

                AppLogs.e("BluetoothConnector", "Data from HC-05 :: Last Weight: "+lastReceivedWeight+", Current Weight: " + currentWeight);
                if (currentWeight != null) {
                    String[] weightInfo = new String[]{lastReceivedWeight, currentWeight.trim()};
                    MyApplication.getEventBusInstance().post(weightInfo);
                    lastReceivedWeight = currentWeight;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListening() {
        isListening = false;
    }

    public void bluetoothDisconnect() {
        isConnected = false;
        isListening = false;

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

    public static String readLine(InputStream inputStream) throws IOException {
        AppLogs.e("BluetoothConnector", "Reading line");
        InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        int c;
        for (c = reader.read(); c != '\n' && c != -1; c = reader.read()) {
            stringBuilder.append((char) c);
        }
        if (c == -1 && stringBuilder.length() == 0) {
            AppLogs.e("BluetoothConnector", "No data from Bluetooth");
            return null; // End of stream and nothing to return
        }
        return stringBuilder.toString();
    }

}
