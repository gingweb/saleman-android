package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.akkaratanapat.altear.vrp_onboard.Model.Device;
import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by altear on 8/8/2017.
 */

public class DeviceInfoConverter implements Converter<Device,String> {
    @NonNull
    @Override
    public Device toConverted(@Nullable String value) {
        return new Gson().fromJson(value, Device.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Device device) {
        return new Gson().toJson(device, Device.class);    }
    }

