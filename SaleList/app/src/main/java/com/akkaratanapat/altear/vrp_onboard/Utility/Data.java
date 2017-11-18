package com.akkaratanapat.altear.vrp_onboard.Utility;

import android.content.Context;
import android.graphics.Bitmap;

import com.akkaratanapat.altear.vrp_onboard.Converter.PlaceConverter;
import com.akkaratanapat.altear.vrp_onboard.Converter.DeviceInfoConverter;
import com.akkaratanapat.altear.vrp_onboard.Converter.DispatcherInfoConverter;
import com.akkaratanapat.altear.vrp_onboard.Converter.BitmapConverter;
import com.akkaratanapat.altear.vrp_onboard.Converter.DriverInfoConverter;
import com.akkaratanapat.altear.vrp_onboard.Converter.VehicleInfoConverter;
import com.akkaratanapat.altear.vrp_onboard.Model.Device;
import com.akkaratanapat.altear.vrp_onboard.Model.Dispatcher;
import com.akkaratanapat.altear.vrp_onboard.Model.Driver;
import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.akkaratanapat.altear.vrp_onboard.Model.Vehicle;

import io.t28.shade.annotation.Preferences;
import io.t28.shade.annotation.Property;

/**
 * Created by altear on 8/7/2017.
 */

@Preferences(name = "altear.data.preferences", mode = Context.MODE_PRIVATE)
public abstract class Data {

    @Property(key = "key_dispatcher", converter = DispatcherInfoConverter.class)
    public abstract Dispatcher dispatcher();

    @Property(key = "key_driver", converter = DriverInfoConverter.class)
    public abstract Driver driver();

    @Property(key = "key_vehicle", converter = VehicleInfoConverter.class)
    public abstract Vehicle Vehicle();

    @Property(key = "key_device", converter = DeviceInfoConverter.class)
    public abstract Device device();

    @Property(key = "key_central_place", converter = PlaceConverter.class)
    public abstract Place centralPlace();

    @Property(key = "key_drawable_profile",  converter = BitmapConverter.class)
    public abstract Bitmap bitmapProfile();

    @Property(key = "key_on_login", defValue = "0")
    public abstract int onLogin();

    @Property(key = "key_on_job", defValue = "0")
    public abstract int onJob();

    @Property(key = "key_on_recall", defValue = "0")
    public abstract int onRecall();

    @Property(key = "key_counter_length", defValue = "0")
    public abstract float counterLength();

    @Property(key = "key_job_code", defValue = " ")
    public abstract String jobCode();

    @Property(key = "key_counter_second", defValue = "0")
    public abstract int counterSecond();

    @Property(key = "key_accept_time", defValue = " ")
    public abstract String acceptTime();

}
