package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.akkaratanapat.altear.vrp_onboard.Model.Dispatcher;
import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by altear on 8/8/2017.
 */

public class DispatcherInfoConverter implements Converter<Dispatcher,String>{

    @NonNull
    @Override
    public Dispatcher toConverted(@Nullable String value) {
        return new Gson().fromJson(value, Dispatcher.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Dispatcher dispatcher) {
        return new Gson().toJson(dispatcher, Dispatcher.class);    }
}
