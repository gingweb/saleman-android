package com.akkaratanapat.altear.vrp_onboard.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.akkaratanapat.altear.vrp_onboard.Utility.APIHandle;
import com.akkaratanapat.altear.vrp_onboard.Dialog.LoadingDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.OnDismissDialogListener;
import com.akkaratanapat.altear.vrp_onboard.Model.Device;
import com.akkaratanapat.altear.vrp_onboard.Model.Dispatcher;
import com.akkaratanapat.altear.vrp_onboard.Model.Driver;
import com.akkaratanapat.altear.vrp_onboard.Model.Vehicle;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.akkaratanapat.altear.vrp_onboard.Utility.DataPreferences;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, APIHandle.ApiHandlerListener, OnDismissDialogListener {

    private FlatEditText dispatcherText, driverText, vehicleText;

    private GoogleApiClient googleApiClient;

    private APIHandle myApiHandle;
    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    private static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private static final int REQUEST_PHONE_STATE = 1;
    private String IMEI = "2323rj293jr823rij240r";
    private double lat = 0, lng = 0;

    public DataPreferences dataPreferences;
    Dispatcher dispatcher;
    Driver driver;
    Vehicle vehicle;
    Device device;
    Place central;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setCustomComponent();
        setPreferences();
        setHttpListener();
        checkForPhoneStatePermission();
        setGoogleApiClient();

    }

    private void setCustomComponent() {

        FlatTextView welcome_text;
        FlatButton login_btn, dispatcher_btn, driver_btn, vehicle_btn;

        welcome_text = (FlatTextView) findViewById(R.id.text_login);

        dispatcherText = (FlatEditText) findViewById(R.id.editText_dispatch);
        driverText = (FlatEditText) findViewById(R.id.editText_driver);
        vehicleText = (FlatEditText) findViewById(R.id.editText_vehicle);

        dispatcher_btn = (FlatButton) findViewById(R.id.button_dispatch);
        driver_btn = (FlatButton) findViewById(R.id.button_driver);
        vehicle_btn = (FlatButton) findViewById(R.id.button_vehicle);
        login_btn = (FlatButton) findViewById(R.id.button_login);

        welcome_text.getAttributes().setTheme(FlatUI.DEEP, getResources());
        dispatcherText.getAttributes().setTheme(FlatUI.DEEP, getResources());
        driverText.getAttributes().setTheme(FlatUI.DEEP, getResources());
        vehicleText.getAttributes().setTheme(FlatUI.DEEP, getResources());
        dispatcher_btn.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        driver_btn.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        vehicle_btn.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        login_btn.getAttributes().setTheme(R.array.custom_gold_dark, getResources());

        dispatcher_btn.setOnClickListener(button_event);
        driver_btn.setOnClickListener(button_event);
        vehicle_btn.setOnClickListener(button_event);
        login_btn.setOnClickListener(button_event);
    }

    private void setPreferences() {
        dataPreferences = new DataPreferences(getApplicationContext());
    }

    private void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private View.OnClickListener button_event = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_dispatch:
                    driverText.requestFocus();
                    break;
                case R.id.button_driver:
                    vehicleText.requestFocus();
                    break;
                case R.id.button_vehicle:
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    break;
                case R.id.button_login:
                    //myApiHandle.reCall();
                    loginByAPIHandle();
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void setHttpListener() {
        myApiHandle = new APIHandle(this);
        myApiHandle.setApiHandlerListener(this);
        myApiHandle.setDataPreferences(dataPreferences);
        //myApiHandle.setDialogDismissListener(this);
    }

    private void checkForPhoneStatePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, READ_PHONE_STATE)) {
                    showPermissionMessage();
                } else {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{READ_PHONE_STATE}, REQUEST_PHONE_STATE);
                }
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = telephonyManager.getDeviceId();
        }
    }

    private void showPermissionMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Read phone state")
                .setMessage("This app requires the permission to read phone state to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{READ_PHONE_STATE},
                                REQUEST_PHONE_STATE);
                    }
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI = telephonyManager.getDeviceId();
                    //IMEI = "2323rj293jr823rij240r";
                } else {
                    Toast.makeText(LoginActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void loginByAPIHandle() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        String[] value = new String[]{IMEI, getTimeISO8601()};
        String[] codeValue = new String[]{dispatcherText.getText().toString(), driverText.getText().toString(),
                vehicleText.getText().toString()};
        double[] latlngValue = new double[]{lat, lng};
        myApiHandle.reCall();
        myApiHandle.requestLogin(codeValue, value, latlngValue);
    }

    public String getTimeISO8601() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.TAIWAN);
        return df.format(new Date());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(500);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when Location Provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onSuccess(final String name, final JSONObject json) throws JSONException {
        if (name.equals(APIHandle.APIName.SIGNIN.toString())) {
            if (json.getInt("code") == 1) {
                try {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String dispatcherInfo = json.getJSONObject("DispatcherInfo").toString();
                    String driverInfo = json.getJSONObject("DriverInfo").toString();
                    String vehicleInfo = json.getJSONObject("VehicleInfo").toString();
                    String deviceInfo = json.getJSONObject("DeviceInfo").toString();
                    String centralInfo = json.getJSONObject("CentralPlaceInfo").toString();

                    dispatcher = gson.fromJson(dispatcherInfo, Dispatcher.class);
                    driver = gson.fromJson(driverInfo, Driver.class);
                    vehicle = gson.fromJson(vehicleInfo, Vehicle.class);
                    device = gson.fromJson(deviceInfo, Device.class);
                    central = gson.fromJson(centralInfo,Place.class);

                    dataPreferences.edit()
                            .putDispatcher(dispatcher)
                            .putDriver(driver)
                            .putVehicle(vehicle)
                            .putDevice(device)
                            .putCentralPlace(central)
                            .putOnLogin(1)
                            .apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(getBaseContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            LoginActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(getBaseContext(), name + " " + json.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onBodyError(ResponseBody responseBodyError) {

    }

    @Override
    public void onBodyErrorIsNull() {

    }

    @Override
    public void onFailure(String name, String url, String param, final Exception e) {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStartLoading() {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertLoading();
            }
        });
    }

    private void alertLoading() {
        loadingDialog = new LoadingDialog();
        loadingDialog.show(this, "กรุณารอสักครู่", true, this);
    }

    @Override
    public void onFinishLoading() {
        LoginActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public void onDismiss(String name) {

    }
}