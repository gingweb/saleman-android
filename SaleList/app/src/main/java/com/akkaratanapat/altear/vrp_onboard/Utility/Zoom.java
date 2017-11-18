package com.akkaratanapat.altear.vrp_onboard.Utility;

import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by altear on 2/5/2017.
 */

public class Zoom {
    int zoom_level;
    Place origin, destination;
    ArrayList<Place> dropOff;
    ArrayList<Double> lat, lng;

    public Zoom(Place origin, Place destination, ArrayList<Place> dropOff) {
        this.origin = origin;
        this.destination = destination;
        this.dropOff = dropOff;
        lat = new ArrayList<>();
        lng = new ArrayList<>();
        lat.add(origin.getPlaceLocation().getLatitude());
        lat.add(destination.getPlaceLocation().getLatitude());

        lng.add(origin.getPlaceLocation().getLongitude());
        lng.add(destination.getPlaceLocation().getLongitude());

        int size = dropOff.size();

        for (int i = 0; i < size; i++) {
            if ((dropOff.get(i).getPlaceLocation().getLatitude() != 0.0) & (dropOff.get(i).getPlaceLocation().getLongitude()!= 0.0)) {
                lat.add(dropOff.get(i).getPlaceLocation().getLatitude());
                lng.add(dropOff.get(i).getPlaceLocation().getLongitude());
            }
        }
    }

//    public void setZoomLocation(LatLng location) {
//        zoom_location = location;
//    }
//
//    public void setZoom_level(int level) {
//        zoom_level = level;
//    }

    public LatLng getZoom_location() {
        double max_lat = Collections.max(lat), max_lng = Collections.max(lng),
                min_lat = Collections.min(lat), min_lng = Collections.min(lng),
                mid_lat = (max_lat - min_lat) / 2, mid_lng = (max_lng - min_lng) / 2;
        return new LatLng(max_lat - mid_lat, max_lng - mid_lng);
    }

    public int getZoom_level() {
        double max_lat = Collections.max(lat), max_lng = Collections.max(lng),
                min_lat = Collections.min(lat), min_lng = Collections.min(lng),
                distance_lat = (max_lat - min_lat), distance_lng = (max_lng - min_lng);
        int zoom_lng, zoom_lat;
        zoom_lng = 12 - (int)Math.sqrt((distance_lng/0.05));
        zoom_lat = 12 - (int)Math.sqrt((distance_lat/0.03));
        if(zoom_lat > zoom_lng)zoom_level = zoom_lng;
        else zoom_level = zoom_lat;
        return zoom_level;
    }
}
