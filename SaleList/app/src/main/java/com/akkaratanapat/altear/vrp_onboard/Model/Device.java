package com.akkaratanapat.altear.vrp_onboard.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aa on 8/7/2017.
 */

public class Device {

    private int id;
    private String IMEI;
    private String name;
    @SerializedName("serial_number")
    private String serialNumber;

    public void setId(int id) {
        this.id = id;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getId(){
        return id;
    }

    public String getIMEI(){
        return IMEI;
    }

    public String getName(){
        return name;
    }

    public String getSerialNumber(){
        return serialNumber;
    }
}
