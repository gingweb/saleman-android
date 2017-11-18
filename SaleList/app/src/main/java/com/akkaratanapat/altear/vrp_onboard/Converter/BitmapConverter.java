package com.akkaratanapat.altear.vrp_onboard.Converter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import io.t28.shade.converter.Converter;

/**
 * Created by altear on 8/10/2017.
 */

public class BitmapConverter implements Converter<Bitmap,String> {
    @NonNull
    @Override
    public Bitmap toConverted(@Nullable String s) {
        return new Gson().fromJson(s, Bitmap.class);
    }

    @NonNull
    @Override
    public String toSupported(@Nullable Bitmap bitmap) {
        return new Gson().toJson(bitmap, Bitmap.class);
    }
}
