package com.app.kitchen.jar.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lalit T. Poptani on 10/28/2016.
 */

public class JarWeightInfo implements Parcelable {
    private int id;
    private String macAddress;
    private String itemName;
    private double itemWeight;
    private long timestamp;

    public JarWeightInfo() {
    }

    public JarWeightInfo(int id, String macAddress, String itemName, double itemWeight, long timestamp) {
        this.id = id;
        this.macAddress = macAddress;
        this.itemName = itemName;
        this.itemWeight = itemWeight;
        this.timestamp = timestamp;
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

    public double getItemWeight() {
        return itemWeight;
    }

    public long getTimestamp() {
        return timestamp;
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
        dest.writeDouble(this.itemWeight);
        dest.writeLong(this.timestamp);
    }

    protected JarWeightInfo(Parcel in) {
        this.id = in.readInt();
        this.macAddress = in.readString();
        this.itemName = in.readString();
        this.itemWeight = in.readDouble();
        this.timestamp = in.readLong();
    }

    public static final Parcelable.Creator<JarWeightInfo> CREATOR = new Parcelable.Creator<JarWeightInfo>() {
        @Override
        public JarWeightInfo createFromParcel(Parcel source) {
            return new JarWeightInfo(source);
        }

        @Override
        public JarWeightInfo[] newArray(int size) {
            return new JarWeightInfo[size];
        }
    };
}
