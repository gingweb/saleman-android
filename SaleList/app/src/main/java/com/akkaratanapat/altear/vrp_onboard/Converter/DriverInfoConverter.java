package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.akkaratanapat.altear.vrp_onboard.Model.Driver;
import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by altear on 8/8/2017.
 */

public class DriverInfoConverter implements Converter<Driver,String> {
    @NonNull
    @Override
    public Driver toConverted(@Nullable String value) {
        return new Gson().fromJson(value, Driver.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Driver driver) {
        return new Gson().toJson(driver, Driver.class);    }
    }

