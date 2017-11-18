package com.akkaratanapat.altear.vrp_onboard.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aa on 8/7/2017.
 */

public class Vehicle {

    @SerializedName("veh_id")
    private int vehicleID;
    @SerializedName("veh_code")
    private String vehicleCode;
    @SerializedName("veh_comp_id")
    private int vehicleCompanyID;
    @SerializedName("veh_comp_name")
    private String vehicleCompanyName;
    @SerializedName("veh_brand_id")
    private int vehicleBrandID;
    @SerializedName("veh_brand_name")
    private String vehicleBrandName;
    @SerializedName("veh_brand_code")
    private String vehicleBrandCode;
    @SerializedName("veh_model_id")
    private int vehicleModelID;
    @SerializedName("veh_model_name")
    private String vehicleModelName;
    @SerializedName("veh_model_code")
    private String vehicleModelCode;
    @SerializedName("license_plate_no")
    private String licencePlateNumber;
    @SerializedName("license_plate_province_id")
    private int licencePlateProvinceID;
    @SerializedName("province_name")
    private String provinceName;
    @SerializedName("chassis_no")
    private String chassisNO;
    @SerializedName("max_passenger")
    private int maxPassenger;
    @SerializedName("max_large_suitcase")
    private int maxLargeSuitcase;
    @SerializedName("max_small_suitcase")
    private int maxSmallSuitCase;
    @SerializedName("veh_model_img")
    private String vehicleModelImage;

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public void setVehicleCompanyID(int vehicleCompanyID) {
        this.vehicleCompanyID = vehicleCompanyID;
    }

    public void setVehicleCompanyName(String vehicleCompanyName) {
        this.vehicleCompanyName = vehicleCompanyName;
    }

    public void setVehicleBrandID(int vehicleBrandID) {
        this.vehicleBrandID = vehicleBrandID;
    }

    public void setVehicleBrandName(String vehicleBrandName) {
        this.vehicleBrandName = vehicleBrandName;
    }

    public void setVehicleBrandCode(String vehicleBrandCode) {
        this.vehicleBrandCode = vehicleBrandCode;
    }

    public void setVehicleModelID(int vehicleModelID) {
        this.vehicleModelID = vehicleModelID;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public void setVehicleModelCode(String vehicleModelCode) {
        this.vehicleModelCode = vehicleModelCode;
    }

    public void setLicencePlateNumber(String licencePlateNumber) {
        this.licencePlateNumber = licencePlateNumber;
    }

    public void setLicencePlateProvinceID(int licencePlateProvinceID) {
        this.licencePlateProvinceID = licencePlateProvinceID;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setChassisNO(String chassisNO) {
        this.chassisNO = chassisNO;
    }

    public void setMaxPassenger(int maxPassenger) {
        this.maxPassenger = maxPassenger;
    }

    public void setMaxLargeSuitcase(int maxLargeSuitcase) {
        this.maxLargeSuitcase = maxLargeSuitcase;
    }

    public void setMaxSmallSuitCase(int smallSuitCase) {
        this.maxSmallSuitCase = smallSuitCase;
    }

    public void setVehicleModelImage(String vehicleModelImage) {
        this.vehicleModelImage = vehicleModelImage;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public int getVehicleCompanyID() {
        return vehicleCompanyID;
    }

    public String getVehicleCompanyName() {
        return vehicleCompanyName;
    }


    public int getVehicleBrandID() {
        return vehicleBrandID;
    }

    public String getVehicleBrandName() {
        return vehicleBrandName;
    }

    public String getVehicleBrandCode() {
        return vehicleBrandCode;
    }

    public int getVehicleModelID() {
        return vehicleModelID;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public String getVehicleModelCode() {
        return vehicleModelCode;
    }

    public String getLicencePlateNumber() {
        return licencePlateNumber;
    }

    public int getLicencePlateProvinceID() {
        return licencePlateProvinceID;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getChassisNO() {
        return chassisNO;
    }

    public int getMaxPassenger() {
        return maxPassenger;
    }

    public int getMaxLargeSuitcase() {
        return maxLargeSuitcase;
    }

    public int getSmallSuitCase() {
        return maxSmallSuitCase;
    }

    public String getVehicleModelImage() {
        return vehicleModelImage;
    }
}
