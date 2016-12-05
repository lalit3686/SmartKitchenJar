package com.app.kitchen.jar.databases;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.kitchen.jar.application.MyApplication;
import com.app.kitchen.jar.beans.JarInfo;
import com.app.kitchen.jar.commons.AppLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lalit T. Poptani on 10/27/2016.
 */

public class TableJarInfo {

    private static final String TAG = TableJarInfo.class.getSimpleName();
    private static final String TABLE_JAR_WEIGHT_INFO = "jar_info";
    private static final String COL_ID = "id";
    private static final String COL_MAC_ADDRESS = "mac_address";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_CURRENT_WEIGHT = "current_weight";
    private static final String COL_TOTAL_CONSUMED = "total_consumed";
    private static final String COL_RESET_WEIGHT = "reset_weight";


    public static void insertIntoTable(String macAddress, String itemName, double itemWeight, double itemConsumed, double resetWeight) {

        AppLogs.e(TAG, "Inserting into " + TABLE_JAR_WEIGHT_INFO + " table");
        ContentValues values = new ContentValues();
        values.put(COL_MAC_ADDRESS, macAddress);
        values.put(COL_ITEM_NAME, itemName);
        values.put(COL_CURRENT_WEIGHT, itemWeight);
        values.put(COL_TOTAL_CONSUMED, itemConsumed);
        values.put(COL_RESET_WEIGHT, resetWeight);

        MyApplication.getDatabaseInstance().insert(TABLE_JAR_WEIGHT_INFO, null, values);
    }


    public static List<JarInfo> getAllJarWeightInfo() {

        int id;
        String macAddress, itemName;
        double itemWeight;
        double itemConsumed;
        double resetWeight;
        List<JarInfo> listJarInfos = new ArrayList<>();

        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select * from " + TABLE_JAR_WEIGHT_INFO, null);

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                macAddress = cursor.getString(cursor.getColumnIndex(COL_MAC_ADDRESS));
                itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME));
                itemWeight = cursor.getDouble(cursor.getColumnIndex(COL_CURRENT_WEIGHT));
                itemConsumed = cursor.getDouble(cursor.getColumnIndex(COL_TOTAL_CONSUMED));
                resetWeight = cursor.getDouble(cursor.getColumnIndex(COL_RESET_WEIGHT));

                listJarInfos.add(new JarInfo(id, macAddress, itemName, itemWeight, itemConsumed, resetWeight));
                AppLogs.i(TAG, String.valueOf(listJarInfos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return listJarInfos;
    }

    public static JarInfo getJarInfoByBluetoothAddress(String macAddress) {
        int id;
        String itemName;
        double itemWeight;
        double itemConsumed;
        double resetWeight;
        JarInfo weightInfo = null;

        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select * from " + TABLE_JAR_WEIGHT_INFO + " where " + COL_MAC_ADDRESS + "=?", new String[]{macAddress});

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                macAddress = cursor.getString(cursor.getColumnIndex(COL_MAC_ADDRESS));
                itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME));
                itemWeight = cursor.getDouble(cursor.getColumnIndex(COL_CURRENT_WEIGHT));
                itemConsumed = cursor.getDouble(cursor.getColumnIndex(COL_TOTAL_CONSUMED));
                resetWeight = cursor.getDouble(cursor.getColumnIndex(COL_RESET_WEIGHT));

                weightInfo = new JarInfo(id, macAddress, itemName, itemWeight, itemConsumed, resetWeight);
                AppLogs.i(TAG, String.valueOf(weightInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return weightInfo;
    }

    public static void updateWeight(String macAddress, double itemWeight, double itemConsumed) {

        ContentValues values = new ContentValues();
        values.put(COL_CURRENT_WEIGHT, itemWeight);
        values.put(COL_TOTAL_CONSUMED, itemConsumed);

        MyApplication.getDatabaseInstance().update(TABLE_JAR_WEIGHT_INFO, values, COL_MAC_ADDRESS + "= ?", new String[]{macAddress});
    }

    public static void updateResetWeight(String macAddress, double resetWeight) {

        ContentValues values = new ContentValues();
        values.put(COL_RESET_WEIGHT, resetWeight);

        MyApplication.getDatabaseInstance().update(TABLE_JAR_WEIGHT_INFO, values, COL_MAC_ADDRESS + "= ?", new String[]{macAddress});
    }

    public static double getResetWeight(String macAddress) {
        double resetWeight = 0;
        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select " + COL_RESET_WEIGHT + " from " + TABLE_JAR_WEIGHT_INFO + " where " + COL_MAC_ADDRESS + "=?", new String[]{macAddress});

        try {
            while (cursor.moveToNext()) {
                resetWeight = cursor.getDouble(cursor.getColumnIndex(COL_RESET_WEIGHT));
                AppLogs.i(TAG, "Reset Weight is :: " + String.valueOf(resetWeight));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return resetWeight;
    }
}