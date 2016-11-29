package com.app.kitchen.jar.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lalit T. Poptani on 10/28/2016.
 */

public class JarInfo implements Parcelable {

    //CREATE TABLE `jar_info` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `mac_address` TEXT, `item_name` TEXT, `current_weight` REAL, `total_consumed` REAL )
    private int id;
    private String macAddress;
    private String itemName;


    private double currentWeight;
    private double totalConsumed;

    public JarInfo() {
    }

    public JarInfo(int id, String macAddress, String itemName, double currentWeight, double totalConsumed) {
        this.id = id;
        this.macAddress = macAddress;
        this.itemName = itemName;
        this.currentWeight = currentWeight;
        this.totalConsumed = totalConsumed;

    }

    public int getId() {
        return id;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getItemName() {
        return itemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.macAddress);
        dest.writeString(this.itemName);
        dest.writeDouble(currentWeight);
        dest.writeDouble(totalConsumed);
    }

    protected JarInfo(Parcel in) {
        this.id = in.readInt();
        this.macAddress = in.readString();
        this.itemName = in.readString();
        this.currentWeight = in.readDouble();
        this.totalConsumed = in.readDouble();
    }

    public static final Creator<JarInfo> CREATOR = new Creator<JarInfo>() {
        @Override
        public JarInfo createFromParcel(Parcel source) {
            return new JarInfo(source);
        }

        @Override
        public JarInfo[] newArray(int size) {
            return new JarInfo[size];
        }
    };


    public double getTotalConsumed() {
        return totalConsumed;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }
}
