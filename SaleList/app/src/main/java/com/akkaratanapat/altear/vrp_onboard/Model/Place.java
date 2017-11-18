package com.akkaratanapat.altear.vrp_onboard.Model;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

/**
 * Created by altear on 8/9/2017.
 */

public class Place {

    @SerializedName("job_detail_id")
    private int jobDetailId;

    @SerializedName("sequence")
    private int sequence;

    @SerializedName("place_id")
    private int placeId;

    @SerializedName("place")
    private String placeName;

    @SerializedName("place_type")
    private String placeType;

    @SerializedName("place_location")
    private PlaceLocation placeLocation;

    public int getJobDetailId() {
        return jobDetailId;
    }

    public void setJobDetailId(int jobDetailId) {
        this.jobDetailId = jobDetailId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public PlaceLocation getPlaceLocationRaw(){
        return placeLocation;
    }

    public Location getPlaceLocation() {
        Location result = new Location(getPlaceName());
        result.setLatitude(getPlaceLocationRaw().latitude);
        result.setLongitude(getPlaceLocationRaw().longitude);
        return result;
    }

    public void setPlaceLocation(PlaceLocation placeLocation) {
        this.placeLocation = placeLocation;
    }
    private class PlaceLocation{
        @SerializedName("lat")
        private double latitude;

        @SerializedName("lng")
        private double longitude;
    }
}
