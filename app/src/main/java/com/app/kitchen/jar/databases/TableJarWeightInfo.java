package com.app.kitchen.jar.databases;

import android.content.ContentValues;
import android.database.Cursor;

import com.app.kitchen.jar.application.MyApplication;
import com.app.kitchen.jar.beans.JarWeightInfo;
import com.app.kitchen.jar.commons.AppLogs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lalit T. Poptani on 10/27/2016.
 */

public class TableJarWeightInfo {

    private static final String TAG = TableJarWeightInfo.class.getSimpleName();
    private static final String TABLE_JAR_WEIGHT_INFO = "jar_weight_info";
    private static final String COL_ID = "id";
    private static final String COL_MAC_ADDRESS = "mac_address";
    private static final String COL_ITEM_NAME = "item_name";
    private static final String COL_ITEM_WEIGHT = "item_weight";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_ITEM_CONSUMED = "item_consumed";

    public static void insertIntoTable(String macAddress, String itemName, double itemWeight, long timestamp, double itemConsumed) {

        ContentValues values = new ContentValues();
        values.put(COL_MAC_ADDRESS, macAddress);
        values.put(COL_ITEM_NAME, itemName);
        values.put(COL_ITEM_WEIGHT, itemWeight);
        values.put(COL_TIMESTAMP, timestamp);
        values.put(COL_ITEM_CONSUMED, itemConsumed);

        MyApplication.getDatabaseInstance().insert(TABLE_JAR_WEIGHT_INFO, null, values);
    }

    public static List<JarWeightInfo> getAllJarWeightInfo() {

        int id;
        String macAddress, itemName;
        double itemWeight;
        double itemConsumed;
        long timestamp;
        List<JarWeightInfo> listJarWeightInfos = new ArrayList<>();

        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select * from " + TABLE_JAR_WEIGHT_INFO, null);

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                macAddress = cursor.getString(cursor.getColumnIndex(COL_MAC_ADDRESS));
                itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME));
                itemWeight = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_WEIGHT));
                timestamp = cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP));
                itemConsumed = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_CONSUMED));

                listJarWeightInfos.add(new JarWeightInfo(id, macAddress, itemName, itemWeight, timestamp, itemConsumed));
                AppLogs.i(TAG, String.valueOf(listJarWeightInfos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return listJarWeightInfos;
    }

    public static List<JarWeightInfo> getDistinctJarWeightInfo() {

        int id;
        String macAddress, itemName;
        double itemWeight;
        double itemConsumed;
        long timestamp;
        List<JarWeightInfo> listJarWeightInfos = new ArrayList<>();

        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select * from " + TABLE_JAR_WEIGHT_INFO + " GROUP BY " + COL_MAC_ADDRESS + " ORDER BY " + COL_TIMESTAMP, null);

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                macAddress = cursor.getString(cursor.getColumnIndex(COL_MAC_ADDRESS));
                itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME));
                itemWeight = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_WEIGHT));
                timestamp = cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP));
                itemConsumed = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_CONSUMED));

                listJarWeightInfos.add(new JarWeightInfo(id, macAddress, itemName, itemWeight, timestamp, itemConsumed));
                AppLogs.i(TAG, String.valueOf(listJarWeightInfos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return listJarWeightInfos;
    }

    public static JarWeightInfo getJarWeightInfoByBluetoothAddress(String macAddress) {
        int id;
        String itemName;
        double itemWeight;
        double itemConsumed;
        long timestamp;
        JarWeightInfo weightInfo = null;

        Cursor cursor = MyApplication.getDatabaseInstance().rawQuery("select * from " + TABLE_JAR_WEIGHT_INFO + " where " + COL_MAC_ADDRESS + "=? ORDER BY " + COL_TIMESTAMP, new String[]{macAddress});

        try {
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                macAddress = cursor.getString(cursor.getColumnIndex(COL_MAC_ADDRESS));
                itemName = cursor.getString(cursor.getColumnIndex(COL_ITEM_NAME));
                itemWeight = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_WEIGHT));
                timestamp = cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP));
                itemConsumed = cursor.getDouble(cursor.getColumnIndex(COL_ITEM_CONSUMED));

                weightInfo = new JarWeightInfo(id, macAddress, itemName, itemWeight, timestamp, itemConsumed);
                AppLogs.i(TAG, String.valueOf(weightInfo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return weightInfo;
    }
}
