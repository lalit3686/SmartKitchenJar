package com.app.kitchen.jar.beans;

/**
 * Created by Lalit.Poptani on 9/9/2016.
 */
public class BlueToothInfo {

    public BlueToothInfo(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    private String name;
    private String address;

    @Override
    public String toString() {
        return getName() + " " + getAddress();
    }
}
