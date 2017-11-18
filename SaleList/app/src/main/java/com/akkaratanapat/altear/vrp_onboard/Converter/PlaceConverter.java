package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by aa on 9/8/2017.
 */

public class PlaceConverter implements Converter<Place,String> {
    @NonNull
    @Override
    public Place toConverted(@Nullable String s) {
        return new Gson().fromJson(s, Place.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Place place) {
        return new Gson().toJson(place, Place.class);
    }
}
