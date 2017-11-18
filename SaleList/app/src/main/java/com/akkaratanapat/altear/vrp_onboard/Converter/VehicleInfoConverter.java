package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.akkaratanapat.altear.vrp_onboard.Model.Vehicle;
import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by altear on 8/8/2017.
 */

public class VehicleInfoConverter implements Converter<Vehicle,String> {
    @NonNull
    @Override
    public Vehicle toConverted(@Nullable String value) {
        return new Gson().fromJson(value, Vehicle.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Vehicle vehicle) {
        return new Gson().toJson(vehicle, Vehicle.class);    }
    }

