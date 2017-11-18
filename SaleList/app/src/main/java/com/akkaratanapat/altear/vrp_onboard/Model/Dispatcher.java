package com.akkaratanapat.altear.vrp_onboard.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aa on 8/7/2017.
 */

public class Dispatcher {

    @SerializedName("emp_id")
    private int empID;
    @SerializedName("emp_code")
    private String empCode;
    @SerializedName("firstname_th")
    private String firstNameTH;
    @SerializedName("lastname_th")
    private String lastNameTH;
    @SerializedName("firstname_en")
    private String firstNameEN;
    @SerializedName("lastname_en")
    private String lastNameEN;
    @SerializedName("img_profile_url")
    private String imageProfileURL;

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public void setEmpCode(String empCode){
        this.empCode = empCode;
    }

    public void setFirstNameTH(String firstNameTH){
        this.firstNameTH = firstNameTH;
    }

    public void setLastNameTH(String lastNameTH){
        this.lastNameTH = lastNameTH;
    }

    public void setFirstNameEN(String firstNameEN){
      this.firstNameEN = firstNameEN;
    }

    public void setLastNameEN(String lastNameEN){
        this.lastNameEN =lastNameEN;
    }

    public void setImgageProfileURL(String imageProfileURL){
        this.imageProfileURL = imageProfileURL;
    }

    public int getEmpID(){
        return empID;
    }

    public String getEmpCode(){
        return empCode;
    }

    public String getFirstNameTH(){
        return firstNameTH;
    }

    public String getLastNameTH(){
        return lastNameTH;
    }

    public String getFirstNameEN(){
        return firstNameEN;
    }

    public String getLastNameEN(){
        return lastNameEN;
    }

    public String getImageProfileURL(){
        return imageProfileURL;
    }
}
