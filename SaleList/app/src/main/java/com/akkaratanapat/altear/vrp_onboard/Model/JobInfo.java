package com.akkaratanapat.altear.vrp_onboard.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by altear on 8/9/2017.
 */

public class JobInfo {

    @SerializedName("booking_type")
    private int bookingType;

    @SerializedName("cus_id")
    private int customerID;

    @SerializedName("job_id")
    private int jobID;

    @SerializedName("job_code")
    private String jobCode;

    @SerializedName("job_type")
    private int jobType;

    @SerializedName("hour_rental")
    private int hourRental;

    @SerializedName("job_price")
    private int jobPrice;

    @SerializedName("pickup_datetime")
    private String pickupDateTime;

    @SerializedName("job_status")
    private int jobStatus;

    @SerializedName("detail")
    private List<Place> places;

    public int getBookingType() {
        return bookingType;
    }

    public void setBookingType(int bookingType) {
        this.bookingType = bookingType;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getHourRental() {
        return hourRental;
    }

    public void setHourRental(int hourRental) {
        this.hourRental = hourRental;
    }

    public int getJobPrice() {
        return jobPrice;
    }

    public void setJobPrice(int jobPrice) {
        this.jobPrice = jobPrice;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public Place getOriginPlace(){
        Place origin = new Place();
        for(int count = 0;count<places.size();count++){
            if(places.get(count).getPlaceType().equals("origin")){
                origin = places.get(count);
            }
        }
        return origin;
    }

    public Place getDestinationPlace(){
        Place destination = new Place();
        for(int count = 0;count<places.size();count++){
            if(places.get(count).getPlaceType().equals("destination")){
                destination = places.get(count);
            }
        }
        return destination;
    }

    public String getStringDropOff(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int count = 0;count<places.size();count++){
            if(places.get(count).getPlaceType().equals("dropoff")){
                stringBuilder.append( places.get(count).getPlaceName()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public ArrayList<Place> getDropOffPlace(){
        ArrayList<Place> result = new ArrayList<>();
        for(int count = 0;count<places.size();count++){
            if(places.get(count).getPlaceType().equals("dropoff")){
                result.add(places.get(count));
            }
        }
        return result;
    }
}
