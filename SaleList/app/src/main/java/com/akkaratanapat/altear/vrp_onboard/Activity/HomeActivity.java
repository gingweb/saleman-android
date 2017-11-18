package com.akkaratanapat.altear.vrp_onboard.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.telephony.TelephonyManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.akkaratanapat.altear.vrp_onboard.Utility.APIHandle;
import com.akkaratanapat.altear.vrp_onboard.Utility.APIHandle.APIName;
import com.akkaratanapat.altear.vrp_onboard.Dialog.DecisionDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.JobInfoDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.LoadingDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.LogoutDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.MessageDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.OnDismissDialogListener;
import com.akkaratanapat.altear.vrp_onboard.Fragment.MapWorkingFragment;
import com.akkaratanapat.altear.vrp_onboard.Fragment.QRFragment;
import com.akkaratanapat.altear.vrp_onboard.Fragment.StartFragment;
import com.akkaratanapat.altear.vrp_onboard.Model.Device;
import com.akkaratanapat.altear.vrp_onboard.Model.Dispatcher;
import com.akkaratanapat.altear.vrp_onboard.Model.Driver;
import com.akkaratanapat.altear.vrp_onboard.Model.JobInfo;
import com.akkaratanapat.altear.vrp_onboard.Model.Vehicle;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.akkaratanapat.altear.vrp_onboard.Utility.CalendarHandler;
import com.akkaratanapat.altear.vrp_onboard.Utility.DataPreferences;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatTextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;

public class HomeActivity extends AppCompatActivity implements
        APIHandle.ApiHandlerListener, OnDismissDialogListener, JobInfoDialog.onJobInfoListener
        , DecisionDialog.OnDecisionDialogListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public DataPreferences dataPreferences;
    public APIHandle apiHandle;
    private static final int REQUEST_PHONE_STATE = 1;
    public Dispatcher dispatcher;
    public Driver driver;
    public Vehicle vehicle;
    public Device device;
    public JobInfo jobInfo;
    public Place centralPlaceInfo;
    private Handler handler;
    private Runnable tickTock;
    private int timeCounter, tiktok;
    public boolean isCounter, isNear = false;
    private Calendar finishTime;
    private ImageView originImage, dropoffImage, destinationImage;
    private FlatTextView textViewDate, textViewDateCounter, textViewTime, textViewTimeCounter, textViewLength,
            textViewLengthCounter, textViewOriginPlace, textViewDropOffPlace, textViewDestinationPlace;
    public SimpleDateFormat sdfDate, sdfTime;
    private JobInfoDialog jobInfoDialog;
    private LoadingDialog loadingDialog;
    private GoogleApiClient googleApiClient;
    public Location location, counterLocation;
    public Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            changeFragment("start");
            checkForPhoneStatePermission();
        }

        setPreferences();
        setApiHandle();
        setActionBar();
        setComponent();
        setHandler();
        setGoogleApiClient();

        if (dataPreferences.getOnJob() == 1) {
            apiHandle.requestFindJob(dispatcher.getEmpID(), driver.getEmpID(),
                    vehicle.getVehicleID(), dataPreferences.getJobCode());
        }
    }

    private void setApiHandle() {
        apiHandle = new APIHandle(this);
        apiHandle.setApiHandlerListener(this);
        apiHandle.setDataPreferences(dataPreferences);
    }

    private void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(false);
            if (driver != null) {
                actionBar.setTitle("\t\t" + driver.getFirstNameTH() + "\t" + driver.getLastNameTH());
                actionBar.setSubtitle("\t\t" + driver.getFirstNameEN() + "\t" + driver.getLastNameEN());
            } else {
                actionBar.setTitle("\t\tคนขับรถ");
                actionBar.setSubtitle("\t\tDriver");
            }
            final ImageView im = (ImageView) findViewById(R.id.car);
//            Picasso.with(getApplicationContext()).load("http://www.uppic.org/image-2174_53D2A2DD.jpg")
//                    .placeholder(R.drawable.ic_account_box_white_48dp)
//                    .error(R.drawable.ic_account_box_black_48dp)
//                    .into(im, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(getApplicationContext(),"ss",Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onError() {
//                            Toast.makeText(getApplicationContext(),"fa",Toast.LENGTH_SHORT).show();
//                        }
//                    });

            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (dataPreferences.getOnJob() == 1) {
                        if (jobInfo != null) {
                            stringBuilder.append("job type : ");
                            stringBuilder.append(jobInfo.getBookingType());
                            stringBuilder.append("job status : ");
                            stringBuilder.append(jobInfo.getJobStatus());
                            if (location != null) {
                                stringBuilder.append("job ori dist : ");
                                stringBuilder.append((jobInfo.getOriginPlace().getPlaceLocation().distanceTo(location)) / 1000);
                                stringBuilder.append("job dest dist : ");
                                stringBuilder.append((jobInfo.getDestinationPlace().getPlaceLocation().distanceTo(location)) / 1000);
                                if (centralPlaceInfo != null) {
                                    stringBuilder.append("job cen dist : ");
                                    stringBuilder.append((centralPlaceInfo.getPlaceLocation().distanceTo(location)) / 1000);
                                }
                            }
                        }
                    } else {
                        if (driver != null) {
                            stringBuilder.append(driver.getEmpCode() + " : ");
                        }
                        if (vehicle != null) {
                            stringBuilder.append(vehicle.getVehicleCode() + " : ");
                        }
                        if (location != null) {
                            stringBuilder.append(location.getLatitude() + "," + location.getLongitude());
                        }
                    }
                    toastOnHome(stringBuilder.toString());
                }
            });
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.e("picasso", from.toString());
                    myBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    actionBar.setLogo(new BitmapDrawable(getResources(),
                            myBitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    actionBar.setLogo(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    actionBar.setLogo(placeHolderDrawable);
                }
            };

            String imageURL = "https://d30y9cdsu7xlg0.cloudfront.net/png/16652-200.png";
            if (!driver.getImageProfileURL().equals("n/a")) {
                if (!driver.getImageProfileURL().contains("aot-hotel.transcodeglobal")) {
                    imageURL = apiHandle.getBaseURL() + driver.getImageProfileURL();
                } else {
                    imageURL = driver.getImageProfileURL();
                }
            }
            Picasso.with(this).load(imageURL)
                    .placeholder(R.drawable.ic_account_box_white_48dp)
                    .error(R.drawable.ic_account_box_white_48dp)
                    .into(target);

            im.setTag(target);
        }
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    private void setPreferences() {
        dataPreferences = new DataPreferences(getApplicationContext());
        if (dataPreferences.containsDispatcher()) dispatcher = dataPreferences.getDispatcher();
        if (dataPreferences.containsDriver()) driver = dataPreferences.getDriver();
        if (dataPreferences.containsVehicle()) vehicle = dataPreferences.getVehicle();
        if (dataPreferences.containsDevice()) device = dataPreferences.getDevice();
        if (dataPreferences.containsCentralPlace())
            centralPlaceInfo = dataPreferences.getCentralPlace();
    }

    private void setComponent() {
        View menuBox = findViewById(R.id.menu_box);

        FlatTextView textViewLicensePlate = (FlatTextView) menuBox.findViewById(R.id.textViewLicensePlate);
        FlatTextView textViewBrand = (FlatTextView) menuBox.findViewById(R.id.textViewBrand);
        textViewDate = (FlatTextView) menuBox.findViewById(R.id.textViewDate);
        textViewDateCounter = (FlatTextView) menuBox.findViewById(R.id.textViewDateCounter);
        textViewTime = (FlatTextView) menuBox.findViewById(R.id.textViewTime);
        textViewTimeCounter = (FlatTextView) menuBox.findViewById(R.id.textViewTimeCounter);
        textViewLength = (FlatTextView) menuBox.findViewById(R.id.textViewLength);
        textViewLengthCounter = (FlatTextView) menuBox.findViewById(R.id.textViewLengthCounter);
        textViewOriginPlace = (FlatTextView) menuBox.findViewById(R.id.textViewOriginPlace);
        textViewDropOffPlace = (FlatTextView) menuBox.findViewById(R.id.textViewDropOffPlace);
        textViewDestinationPlace = (FlatTextView) menuBox.findViewById(R.id.textViewDestinationPlace);

        originImage = (ImageView) menuBox.findViewById(R.id.imageViewOrigin);
        dropoffImage = (ImageView) menuBox.findViewById(R.id.imageViewDot);
        destinationImage = (ImageView) menuBox.findViewById(R.id.imageViewDestination);

        textViewLicensePlate.getAttributes().setTheme(FlatUI.SNOW, getResources());
        textViewBrand.getAttributes().setTheme(FlatUI.SNOW, getResources());
        textViewDate.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewDateCounter.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewTime.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewTimeCounter.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewLength.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewLengthCounter.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewOriginPlace.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewDropOffPlace.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        textViewDestinationPlace.getAttributes().setTheme(R.array.custom_gold_dark, getResources());

        textViewLicensePlate.setText(vehicle.getLicencePlateNumber());
        textViewBrand.setText(vehicle.getVehicleBrandCode());
        setVisibleDismissJobTime(false);
        setVisibleDistance(false);
        setVisiblePlace(false);
    }

    private void setHandler() {
        sdfDate = new SimpleDateFormat("dd MMMM ", new Locale("th", "TH"));
        sdfTime = new SimpleDateFormat("HH:mm:ss", new Locale("th", "TH"));
        isCounter = false;
        tiktok = 0;
        handler = new Handler();
        tickTock = new Runnable() {
            @Override
            public void run() {
                tiktok++;
                Date now = new Date();
                String strDate = sdfDate.format(now);
                String strTime = sdfTime.format(now);
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR) + 543 - 2500;
//                if (strDate.substring(0, 1).equals("0")) {
//                    strDate = strDate.substring(1, 2);
//                }
                setCurrentTime(strDate + " " + year, strTime + "");

                if (isCounter) {
                    //may delete
//                    timeCounter = dataPreferences.getCounterSecond();
//                    timeCounter += 1;
//                    dataPreferences.edit().putCounterSecond(timeCounter).apply();
                    setJobTime();
                }
                if (tiktok % 10 == 0) {
                    //if on job ==0 then job id =""  cus id = "" job status =""
                    String[] strings = new String[5];
                    double[] doubles = new double[2];
                    if (location == null) {
                        doubles[0] = 0;
                        doubles[1] = 0;
                    } else {
                        doubles[0] = location.getLatitude();
                        doubles[1] = location.getLongitude();
                    }
                    if (dataPreferences.getOnJob() == 0 || jobInfo == null) {
                        strings[0] = vehicle.getVehicleID() + "";
                        strings[1] = driver.getEmpID() + "";
                        strings[2] = "";
                        strings[3] = "";
                        strings[4] = "";
                    } else if (dataPreferences.getOnJob() == 1 & jobInfo != null) {
                        strings[0] = vehicle.getVehicleID() + "";
                        strings[1] = driver.getEmpID() + "";
                        strings[2] = jobInfo.getCustomerID() + "";
                        strings[3] = jobInfo.getJobID() + "";
                        strings[4] = jobInfo.getJobStatus() + "";
                    }
                    apiHandle.requestIntervalTrackingJob(strings
                            , doubles, getTimeISO8601());
                }
                handler.postDelayed(this, 1000);
            }
        };
    }

    private void setStartFinishTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("th", "TH"));
        Calendar startTime = Calendar.getInstance();
        finishTime = Calendar.getInstance();
        Date date = CalendarHandler.getCalendarJobInfo(jobInfo.getPickupDateTime()).getTime();
        String pickup_date = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                + "-" + date.getDate();
        try {
            startTime.setTime(sdf.parse(pickup_date));
            finishTime.setTime(sdf.parse(pickup_date));
            finishTime.add(Calendar.HOUR, Integer.valueOf(CalendarHandler.getPickupTime(date).split(":")[0]));
            finishTime.add(Calendar.MINUTE, Integer.valueOf(CalendarHandler.getPickupTime(date).split(":")[1]));

            if (jobInfo.getBookingType() == 4) {
                //finishTime.add(Calendar.DATE, jobInfo.getHourRental() / 24);
                finishTime.add(Calendar.HOUR, jobInfo.getHourRental() % 24);
            } else if (jobInfo.getBookingType() == 5) {
                //finishTime.add(Calendar.DATE, jobInfo.getHourRental() / 24);
                finishTime.add(Calendar.HOUR, 10);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void changeFragment(String fragmentName) {

        if (fragmentName.equals("start")) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out)
                    .replace(R.id.container, StartFragment.newInstance(), fragmentName)
                    .addToBackStack(null)
                    .commit();
        } else if (fragmentName.equals("qr")) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out)
                    .replace(R.id.container, QRFragment.newInstance(), fragmentName)
                    .addToBackStack(null)
                    .commit();
        } else if (fragmentName.equals("map")) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_right_out)
                    .replace(R.id.container, MapWorkingFragment.newInstance(), fragmentName)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void checkForPhoneStatePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,
                        Manifest.permission.READ_PHONE_STATE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    showPermissionMessage();

                } else {

                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            REQUEST_PHONE_STATE);
                }
            }
//            else {
//
//            }
        }
        //else {
        //... Permission has already been granted, obtain the UUID
//            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
//            sn = telephonyManager.getDeviceId().toString();
        //}

    }

    private void showPermissionMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Read phone state")
                .setMessage("This app requires the permission to read phone state to continue")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                REQUEST_PHONE_STATE);
                    }
                }).create().show();
    }

    private void alertLogoutDialog() {
        LogoutDialog.show(HomeActivity.this, false, this, new LogoutDialog.OnLogoutDialogListener() {
            @Override
            public void onLogout(final String dispatcherCode) {
                logOutByAPIHandle(dispatcherCode);
                HomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Dispatch : " + dispatcherCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void logOutByAPIHandle(String dispatcherCode) {
        checkForPhoneStatePermission();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        //String imei = "2323rj293jr823rij240r";
        apiHandle.reCall();
        if (dispatcher != null & driver != null & vehicle != null) {
            double lat = 0, lng = 0;
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
            }
            apiHandle.requestLogout(dispatcherCode, driver.getEmpCode(), vehicle.getVehicleCode(), imei, getTimeISO8601(),
                    lat, lng);
        }
    }

    public String getTimeISO8601() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("th", "TH")); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private void toastOnHome(final String message) {
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void messageOnHome(final String title, final String message) {
        MessageDialog.show(this, title, message, true, this);
    }

    public void setEndJob() {
        //add variable later
        dataPreferences.edit().removeCounterLength().removeJobCode()
                .removeOnJob().removeCounterSecond().removeAcceptTime().apply();
        timeCounter = 0;
        isCounter = false;
        isNear = false;
        setVisibleDistance(false);
        setVisiblePlace(false);
        setVisibleDismissJobTime(false);
        textViewTimeCounter.setText("");
    }

    private void setEndSession() {
        dataPreferences.edit().removeCounterLength().removeDevice().removeCounterSecond()
                .removeDispatcher().removeDriver().removeJobCode().removeOnJob()
                .removeVehicle().removeOnRecall().removeOnLogin().removeCentralPlace().apply();
    }

    public void alertJobDialog(JobInfo jobInfo) {
        jobInfoDialog = new JobInfoDialog();
        jobInfoDialog.show(this, false, jobInfo, this, this);
    }

    public void setCurrentTime(String date, String time) {
        textViewDate.setText(date);
        textViewDateCounter.setText(time);
    }

//    public void setVisibleCurrentTime(boolean visible){
//        // true is visible
//        if(!visible){
//            textViewDate.setVisibility(View.GONE);
//            textViewDateCounter.setVisibility(View.GONE);
//        }else {
//            textViewDate.setVisibility(View.VISIBLE);
//            textViewDateCounter.setVisibility(View.VISIBLE);
//        }
//    }

    public void setJobTime() {
        if (jobInfo.getBookingType() < 4) {
            String accept = dataPreferences.getAcceptTime();
            String now = sdfTime.format(new Date());
            Date d, dd;
            long diff = 0;
            try {
                d = sdfTime.parse(accept);
                dd = sdfTime.parse(now);
                diff = dd.getTime() - d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int day = (int) (diff / (1000 * 60 * 60 * 24));
            int hour = (int) (diff / (1000 * 60 * 60)) % 24;
            int minute = (int) (diff / (1000 * 60)) % 60;
            //it should be a (accept time - now)

//            int day = (counter / (3600 * 24));
//            int hour = (counter / 3600) % 24;
//            int minute = (counter / 60) % 60;
            StringBuilder time = new StringBuilder();
            if (day > 0) {
                time.append(day).append("วัน");
            }
            if (hour > 0) {
                time.append(hour).append("ชม.");
            }
            if (minute >= 0) {
                time.append(minute).append("นาที");
            }
            textViewTime.setText("ระยะเวลาเดินทาง");
            textViewTimeCounter.setText(time.toString());

        } else {
            long diff = finishTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            textViewTime.setText("ระยะเวลาที่เหลือ");
            if (diff > 0) {
                int day = (int) (diff / (1000 * 60 * 60 * 24));
                int hour = (int) (diff / (1000 * 60 * 60)) % 24;
                int minute = (int) (diff / (1000 * 60)) % 60;
                StringBuilder stringBuilder = new StringBuilder();
                if (day > 0) {
                    stringBuilder.append(day).append(" วัน ");
                }
                if (hour > 0) {
                    stringBuilder.append(hour).append(" ชม ");
                }
                if (minute > 0) {
                    stringBuilder.append(minute).append(" นาที");
                }
                textViewTimeCounter.setText(stringBuilder);
                if (day == 0 & hour == 0) {//60 minute
                    FragmentManager fm = getSupportFragmentManager();
                    MapWorkingFragment fragment = (MapWorkingFragment) fm.findFragmentByTag("map");
                    if (!fragment.buttonCommand.getTag().equals("คืนงาน") && !fragment.buttonCommand.getTag().equals("ถึงศูนย์")) {
                        fragment.setTextButtonCommand("ผู้โดยสารลงรถ");
                    }
                    fragment.setVisibleCommandButton(true);
                    fragment.setForce(true);
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    MapWorkingFragment fragment = (MapWorkingFragment) fm.findFragmentByTag("map");
                    fragment.setVisibleCommandButton(false);
                }
            } else {
                FragmentManager fm = getSupportFragmentManager();
                MapWorkingFragment fragment = (MapWorkingFragment) fm.findFragmentByTag("map");

                if (!fragment.buttonCommand.getTag().equals("คืนงาน") && !fragment.buttonCommand.getTag().equals("ถึงศูนย์")) {
                    fragment.setTextButtonCommand("ผู้โดยสารลงรถ");
                }
                fragment.setVisibleCommandButton(true);
                fragment.setForce(true);
                textViewTimeCounter.setText("สิ้นสุด");
            }

            //define when time out show button command in map fragment below
        }
    }

    public void setVisibleDismissJobTime(boolean visible) {
        //true is visible
        if (!visible) {
            textViewTime.setVisibility(View.GONE);
            textViewTimeCounter.setVisibility(View.GONE);
        } else {
            textViewTime.setVisibility(View.VISIBLE);
            textViewTimeCounter.setVisibility(View.VISIBLE);
        }
    }

    public void setDistance(float meter) {
        textViewLength.setText("ระยะทาง :");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        textViewLengthCounter.setText(df.format(meter / 1000) + " KM");
    }

    public void setVisibleDistance(boolean visible) {
        // true is visible
        if (!visible) {
            textViewLength.setVisibility(View.GONE);
            textViewLengthCounter.setVisibility(View.GONE);
        } else {
            textViewLength.setVisibility(View.VISIBLE);
            textViewLengthCounter.setVisibility(View.VISIBLE);
        }
    }

    public void setPlace() {
        if (jobInfo != null) {
            String tempOrigin, tempDestination;
            tempOrigin = jobInfo.getOriginPlace().getPlaceName();
            tempDestination = jobInfo.getDestinationPlace().getPlaceName();
            if (tempOrigin.length() > 30) {
                tempOrigin = tempOrigin.substring(0, 29) + "...";
            }
            if (tempDestination.length() > 30) {
                tempDestination = tempDestination.substring(0, 29) + "...";
            }
            if (jobInfo.getBookingType() == 1) {
                originImage.setBackground(getResources().getDrawable(R.drawable.grayplace));
                destinationImage.setBackground(getResources().getDrawable(R.drawable.hotel));
                //textViewDestinationPlace.setText(jobInfo.getDestinationPlace().getPlaceName());
            } else if (jobInfo.getBookingType() == 2) {
                originImage.setBackground(getResources().getDrawable(R.drawable.hotel));
                destinationImage.setBackground(getResources().getDrawable(R.drawable.grayplace));
//                textViewDestinationPlace.setText(jobInfo.getDestinationPlace().getPlaceName());
            } else {
                originImage.setBackground(getResources().getDrawable(R.drawable.grayplace));
                destinationImage.setBackground(getResources().getDrawable(R.drawable.grayplace));
                tempDestination = "รายการเช่าเหมา";
//                textViewDestinationPlace.setText("รายการเช่าเหมา");
            }
            textViewOriginPlace.setText(tempOrigin);
            textViewDropOffPlace.setText(jobInfo.getStringDropOff());
            textViewDestinationPlace.setText(tempDestination);

        }
    }

    public void setVisiblePlace(boolean visible) {
        // true is visible
        if (!visible) {
            originImage.setVisibility(View.GONE);
            dropoffImage.setVisibility(View.GONE);
            destinationImage.setVisibility(View.GONE);
            textViewOriginPlace.setVisibility(View.GONE);
            textViewDropOffPlace.setVisibility(View.GONE);
            textViewDestinationPlace.setVisibility(View.GONE);
        } else {
            originImage.setVisibility(View.VISIBLE);
            dropoffImage.setVisibility(View.VISIBLE);
            destinationImage.setVisibility(View.VISIBLE);
            textViewOriginPlace.setVisibility(View.VISIBLE);
            textViewDropOffPlace.setVisibility(View.VISIBLE);
            textViewDestinationPlace.setVisibility(View.VISIBLE);
        }
    }

    public void onResume() {
        super.onResume();
        handler.postDelayed(tickTock, 1000);
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(tickTock);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new GsonBuilder().serializeNulls().create();
        outState.putString("key_dispatch", gson.toJson(dispatcher, Dispatcher.class));
        outState.putString("key_driver", gson.toJson(driver, Driver.class));
        outState.putString("key_vehicle", gson.toJson(vehicle, Vehicle.class));
        outState.putString("key_device", gson.toJson(device, Device.class));
        outState.putString("key_job_info", gson.toJson(jobInfo, JobInfo.class));
        outState.putBoolean("key_is_counter", isCounter);
        outState.putBoolean("key_is_near", isNear);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new GsonBuilder().serializeNulls().create();
        dispatcher = gson.fromJson(savedInstanceState.getString("key_dispatch"), Dispatcher.class);
        driver = gson.fromJson(savedInstanceState.getString("key_driver"), Driver.class);
        vehicle = gson.fromJson(savedInstanceState.getString("key_vehicle"), Vehicle.class);
        device = gson.fromJson(savedInstanceState.getString("key_device"), Device.class);
        jobInfo = gson.fromJson(savedInstanceState.getString("key_job_info"), JobInfo.class);
        isCounter = savedInstanceState.getBoolean("key_is_counter");
        isNear = savedInstanceState.getBoolean("key_is_near");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    telephonyManager.getDeviceId();
                } else {
                    Toast.makeText(HomeActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        toastOnHome("ออกจากแอพลิเคชั่นด้วยการออกจากระบบเท่านั้น");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            if (dataPreferences.getOnJob() == 0) {
                alertLogoutDialog();
            } else {
                toastOnHome("ไม่สามารถ logout ได้ขณะยังมีงานค้างอยู่ โปรดทำงานจนจบหรือกดคืนงาน");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String name, JSONObject json) throws JSONException {
        //200
        if (name.equals(APIName.FIND_JOB.toString())) {
            if (json.getInt("code") == 1) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                String jobList = json.getJSONObject("joblist").toString();
                jobInfo = gson.fromJson(jobList, JobInfo.class);
                HomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //centralPlaceInfo
                        alertJobDialog(jobInfo);
                    }
                });
            } else {
                toastOnHome("Can't found job pls try again");
            }
        } else if (name.equals(APIName.SIGNOUT.toString())) {
            if (json.getInt("code") == 1) {
                setEndSession();
                toastOnHome("ออกจากระบบ");
                finish();
            } else {
                toastOnHome("ออกจากระบบไม่สำเร็จ" + json.getString("message"));
            }
        }
//        else if (name.equals(APIHandle.APIName.TRACK_LOCATION.toString())) {
//            //nothing
//        } else if (name.equals(APIHandle.APIName.INTERVAL_TRACK.toString())) {
//            //nothing
//        }
        else if (name.equals(APIHandle.APIName.JOB_REVIEW.toString())) {
            if (json.getInt("code") == 1) {
                HomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //setEndJob();
                        messageOnHome("Message Notify", "Thank you for choosing us.");
                    }
                });

            } else {
                HomeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //setEndJob();
                        messageOnHome("Message Notify", "Thank you for choosing us.");
                    }
                });
            }
        } else if (name.equals(APIHandle.APIName.SEND_LOG.toString())) {
            if (json.getInt("code") == 1) {
                toastOnHome("ข้ามงานสำเร็จ");
            } else {
                toastOnHome("ข้ามงานไม่สำเร็จ");
            }
        } else if (name.equals(APIHandle.APIName.IMAGE.toString())) {
            if (json.getInt("code") == 1) {
                toastOnHome("ส่งรูปภาพสำเร็จ");
            } else {
                toastOnHome("ส่งรูปภาพสำเร็จไม่สำเร็จ");
            }
        } else if (name.equals(APIHandle.APIName.SEND_NEAR.toString())) {
            toastOnHome("ใกล้ถึงจุดหมาย");
        }
    }

    @Override
    public void onBodyError(ResponseBody responseBodyError) {
        //404 is not null
    }

    @Override
    public void onBodyErrorIsNull() {
        //404 is null
    }

    @Override
    public void onFailure(String name, String url, String param, Exception e) {
        //any problem
        if (name.equals(APIName.TRACK_LOCATION.toString()) ||
                name.equals(APIName.JOB_REVIEW.toString()) ||
                name.equals(APIName.SEND_LOG.toString())) {
            apiHandle.addAPIFailure(url, name, param);
            if (dataPreferences.getOnRecall() == 0) {
                dataPreferences.edit().putOnRecall(1).apply();
            }
            toastOnHome("Internet ใช้งานไม่ได้");
        }
    }

    @Override
    public void onStartLoading() {
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertLoading();
            }
        });
    }

    @Override
    public void onFinishLoading() {
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        });
    }

    private void alertLoading() {
        loadingDialog = new LoadingDialog();
        loadingDialog.show(this, "กรุณารอสักครู่", true, this);
    }

    @Override
    public void onDismiss(final String name) {
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "Dismiss" + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOKJobInfo(String jobCode) {
        Toast.makeText(getApplicationContext(), "เริ่มงาน", Toast.LENGTH_SHORT).show();
        dataPreferences.edit().putOnJob(1).putJobCode(jobCode).apply();
        //setVisibleDismissJobTime(true);
        setPlace();
        setVisiblePlace(true);
        setDistance(dataPreferences.getCounterLength());
        setVisibleDistance(true);
        if (jobInfo.getBookingType() > 3) {
            setStartFinishTime();
        }
        if (jobInfo.getJobStatus() == 0 | jobInfo.getJobStatus() == 11
                | jobInfo.getJobStatus() == 12 | jobInfo.getJobStatus() == 13) {
            //for start job

            apiHandle.requestTrackingJob(new int[]{dispatcher.getEmpID(), driver.getEmpID(),
                    vehicle.getVehicleID(), jobInfo.getCustomerID()}, jobInfo.getJobCode(), 1);
            jobInfo.setJobStatus(1);
        }
        apiHandle.reCall();
        changeFragment("map");
    }

    @Override
    public void onCancelJobInfo() {
        DecisionDialog.show(this, "หากยกเลิกใบงานจะถือว่าทำการคืนงาน", false, this, this);
    }

    @Override
    public void onOKDecision() {
        //return job
        apiHandle.reCall();
        apiHandle.requestTrackingJob(new int[]{dispatcher.getEmpID(), driver.getEmpID(),
                vehicle.getVehicleID(), jobInfo.getCustomerID()}, jobInfo.getJobCode(), 12);
        setEndJob();
        jobInfoDialog.complete();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            return;
        }
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            // Call Location Services
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(15000);
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
        this.location = location;
        if (dataPreferences.getOnJob() == 1) {
            if (counterLocation == null) {
                counterLocation = location;
            } else {
                if (location.distanceTo(counterLocation) >= 100) {
                    //Toast.makeText(getApplication(), "Length : " + location.distanceTo(counterLocation), Toast.LENGTH_SHORT).show();
                    float beforeLength = dataPreferences.getCounterLength();
                    beforeLength += location.distanceTo(counterLocation);
                    dataPreferences.edit().putCounterLength(beforeLength).apply();
                    setDistance(beforeLength);
                    counterLocation = location;
                }
            }
            FragmentManager fm = getSupportFragmentManager();
            MapWorkingFragment fragment = (MapWorkingFragment) fm.findFragmentByTag("map");
            if (fragment != null) {
                if (jobInfo.getBookingType() < 4) {
                    if ((jobInfo.getOriginPlace().getPlaceLocation().distanceTo(location) <= 1000
                            & (jobInfo.getBookingType() == 1 & jobInfo.getJobStatus() == 2))
                            | (centralPlaceInfo.getPlaceLocation().distanceTo(location) <= 1000
                            & (jobInfo.getBookingType() <= 2 & jobInfo.getJobStatus() == 5))
                            ) {
                        fragment.setVisibleCommandButton(true);
                        //Toast.makeText(getApplicationContext(),"12",Toast.LENGTH_SHORT).show();
                    } else if (jobInfo.getDestinationPlace().getPlaceLocation().distanceTo(location) <= 1000
                            & (jobInfo.getBookingType() <= 2 & jobInfo.getJobStatus() == 4)) {
                        if (!isNear) {
                            apiHandle.reCall();
                            apiHandle.sendNearJob(jobInfo.getJobID() + "");
                            isNear = true;
                        }
                        fragment.setVisibleCommandButton(true);
                        //Toast.makeText(getApplicationContext(),"13",Toast.LENGTH_SHORT).show();
                    } else {
                        fragment.setVisibleCommandButton(false);
                    }
                }
            }
        }
    }
}
