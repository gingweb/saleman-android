package com.akkaratanapat.altear.vrp_onboard.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akkaratanapat.altear.vrp_onboard.Activity.HomeActivity;
import com.akkaratanapat.altear.vrp_onboard.Dialog.ConcludeJobDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.ForceDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.OnDismissDialogListener;
import com.akkaratanapat.altear.vrp_onboard.Dialog.RatingDialog;
import com.akkaratanapat.altear.vrp_onboard.Dialog.SubmitDialog;
import com.akkaratanapat.altear.vrp_onboard.Model.Place;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.akkaratanapat.altear.vrp_onboard.Utility.Zoom;
import com.cengalabs.flatui.views.FlatButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapWorkingFragment extends Fragment implements OnMapReadyCallback
        , OnDismissDialogListener, ForceDialog.onForceDialogListener
        , ConcludeJobDialog.onConcludeListener
        , RatingDialog.onRatingDialogListener
        , SubmitDialog.OnSubmitDialogListener
        , CameraFragment.OnCameraResultListener {

    public FlatButton buttonCommand;
    private GoogleMap mMap;
    private HomeActivity activity;
    private boolean isForce;
    //private LatLng currentLocation;
//    CircleOptions rad = new CircleOptions(), blink = new CircleOptions();
//    Circle currentCircle, blinkCircle;
    Handler handler;
//    LatLng temp;
//    int radiusCurrent = 0;
    //Runnable drawBlink;
    private int rating = 0;

    public static MapWorkingFragment newInstance() {
        return new MapWorkingFragment();
    }

    public MapWorkingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_working, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == savedInstanceState) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.camera, CameraFragment.newInstance(), "camera").commit();
        }

        buttonCommand = (FlatButton) view.findViewById(R.id.buttonCommand);
        FloatingActionButton buttonForce = (FloatingActionButton) view.findViewById(R.id.buttonForce);
        buttonCommand.getAttributes().setTheme(R.array.custom_gold_dark, getContext().getResources());
        SupportMapFragment myMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_working);
        myMapFragment.getMapAsync(this);

        handler = new Handler();

        initialButtonCommand();

        buttonForce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForceDialog();
            }
        });

        buttonCommand.setOnClickListener(onClickListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) getActivity();
    }

    private void alertForceDialog() {

        ForceDialog.show(getContext(), "ขั้นตอนการข้ามงาน", "หลีกเลี่ยงการใช้งานในสถานการณ์ผิดปกติ", buttonCommand.getTag().toString(),buttonCommand.isShown()
                , activity.jobInfo.getJobStatus(), activity.jobInfo.getBookingType(), false, this, this);
    }

    private void alertSubmitForceDialog(int jobstatus, int mode) {
        SubmitDialog.show(getContext(), jobstatus, mode, false, this, this);
    }

    private void alertRatingDialog() {
        RatingDialog.show(getContext(), false, this, this);
    }

    private void alertConcludeDialog() {
        ConcludeJobDialog.show(getContext(), false, activity.driver,activity.myBitmap
                , activity.vehicle, activity.jobInfo, activity.dataPreferences.getAcceptTime()
                , activity.sdfTime.format(new Date()), activity.dataPreferences.getCounterLength(), this, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        if(activity.jobInfo.getJobStatus()<5){
            pinDropOff();
            pinOriginDestination();
        }
        else{
            Place central = activity.centralPlaceInfo;
            mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(central.getPlaceLocation()
                    .getLatitude(), central.getPlaceLocation().getLongitude())).title(central.getPlaceName())
                    .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("", R.drawable.hotel))));
            drawCircle(0x6072b6f2, new LatLng(central.getPlaceLocation().getLatitude(), central.getPlaceLocation().getLongitude()));
        }
        zoom();
        //drawBlinkCircle();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(activity.getApplicationContext(), marker.getPosition().toString(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void initialButtonCommand() {
        int bookingType = activity.jobInfo.getBookingType();
        int jobStatus = activity.jobInfo.getJobStatus();
        if (bookingType == 1) {
            if (jobStatus == 1) {
                setTextButtonCommand("ไปยังจุดรับผู้โดยสาร");
                isForce = true;
                setVisibleCommandButton(true);
            } else if (jobStatus == 2) {
                setVisibleCommandButton(false);
                setTextButtonCommand("รอรับผู้โดยสาร");
            } else if (jobStatus == 3) {
                setTextButtonCommand("ผู้โดยสารขึ้นรถ");
                setVisibleCommandButton(true);
                setForce(true);
            } else if (jobStatus == 4) {
                activity.isCounter = true;
                activity.setVisibleDismissJobTime(true);
                setVisibleCommandButton(false);
                setTextButtonCommand("ผู้โดยสารลงรถ");
            } else if (jobStatus == 5) {
                activity.isCounter = true;
                setTextButtonCommand("ถึงศูนย์");
                activity.setVisibleDismissJobTime(true);
                setVisibleCommandButton(false);
            }

        } else if (bookingType >= 2) {
            if (jobStatus == 1) {
                if(bookingType == 2){
                    setTextButtonCommand("ไปยังจุดส่ง");
                }
                else{
                    setTextButtonCommand("ออกรถ");
                }
                isForce = true;
                setVisibleCommandButton(true);
            } else if (jobStatus == 4) {
                activity.isCounter = true;
                activity.setVisibleDismissJobTime(true);
                setVisibleCommandButton(false);
                setTextButtonCommand("ผู้โดยสารลงรถ");
            } else if (jobStatus == 5) {
                activity.isCounter = true;
                activity.setVisibleDismissJobTime(true);
                setVisibleCommandButton(false);
                setTextButtonCommand("ถึงศูนย์");
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int bookingType = activity.jobInfo.getBookingType();
            String command = buttonCommand.getTag().toString();
            if (bookingType == 1) {
                if (command.equals("ไปยังจุดรับผู้โดยสาร")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 2);
                    activity.jobInfo.setJobStatus(2);
                    isForce = false;
                    setVisibleCommandButton(false);
                    setTextButtonCommand("รอรับผู้โดยสาร");
                } else if (command.equals("รอรับผู้โดยสาร")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 3);
                    activity.jobInfo.setJobStatus(3);
                    setTextButtonCommand("ผู้โดยสารขึ้นรถ");
                    isForce = true;
                    setVisibleCommandButton(true);
                } else if (command.equals("ผู้โดยสารขึ้นรถ")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 4);
                    activity.jobInfo.setJobStatus(4);
                    if (activity.dataPreferences.getAcceptTime().equals(" ")) {
                        activity.dataPreferences.edit().putAcceptTime(activity.sdfTime.format(new Date())).apply();
                    }
                    activity.isCounter = true;
                    activity.setVisibleDismissJobTime(true);
                    isForce = false;
                    setVisibleCommandButton(false);
                    setTextButtonCommand("ผู้โดยสารลงรถ");
                } else if (command.equals("ผู้โดยสารลงรถ")) {
                    if(!activity.isNear){
                        activity.apiHandle.reCall();
                        activity.apiHandle.sendNearJob(activity.jobInfo.getJobID() + "");
                        activity.isNear = true;
                    }
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 5);
                    activity.jobInfo.setJobStatus(5);
                    //activity.isCounter = false;
                    //activity.setVisibleDismissJobTime(true);
                    alertConcludeDialog();
                } else if (command.equals("ถึงศูนย์")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 9);
                    //isForce = true;
                    activity.isCounter = false;
                    activity.setVisibleDismissJobTime(true);
                    activity.setEndJob();
                    getFragmentManager().popBackStack();
                } else if (command.equals("คืนงาน")) {
                    activity.setEndJob();
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 12);
                    getFragmentManager().popBackStack();
                }
            }
            if (bookingType >= 2) {
                if (command.equals("ไปยังจุดส่ง") || command.equals("ออกรถ")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 4);
                    activity.jobInfo.setJobStatus(4);
                    if (activity.dataPreferences.getAcceptTime().equals(" ")) {
                        activity.dataPreferences.edit().putAcceptTime(activity.sdfTime.format(new Date())).apply();
                    }
                    activity.isCounter = true;
                    activity.setVisibleDismissJobTime(true);
                    isForce = false;
                    setVisibleCommandButton(false);
                    setTextButtonCommand("ผู้โดยสารลงรถ");
                } else if (command.equals("ผู้โดยสารลงรถ")) {
                    if(!activity.isNear){
                        activity.apiHandle.reCall();
                        activity.apiHandle.sendNearJob(activity.jobInfo.getJobID() + "");
                        activity.isNear = true;
                    }
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 5);
                    alertConcludeDialog();
                } else if (command.equals("ถึงศูนย์")) {
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 9);
                    //isForce = true;
                    activity.isCounter = false;
                    activity.setVisibleDismissJobTime(true);
                    setVisibleCommandButton(true);
                    activity.setEndJob();
                    getFragmentManager().popBackStack();
                } else if (command.equals("คืนงาน")) {
                    activity.setEndJob();
                    activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                                    activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                            , activity.jobInfo.getJobCode(), 12);
                    getFragmentManager().popBackStack();
                }
            }
        }
    };

    public void setForce(boolean isForce) {
        this.isForce = isForce;
    }

    public void setVisibleCommandButton(boolean isShow) {
        if (isForce) {
            buttonCommand.setVisibility(View.VISIBLE);
        } else {
            if (isShow) {
                buttonCommand.setVisibility(View.VISIBLE);
            } else {
                buttonCommand.setVisibility(View.GONE);
            }
        }
    }

    public void setTextButtonCommand(String command) {
        int idBackground;
        if(command.equals("ไปยังจุดรับผู้โดยสาร")){
            idBackground = R.drawable.go_to_psg_button;
        }
        else if(command.equals("รอรับผู้โดยสาร")){
            idBackground = R.drawable.waiting_for_psg_button;
        }
        else if(command.equals("ผู้โดยสารขึ้นรถ")){
            idBackground = R.drawable.psg_pick_up_button;
        }
        else if(command.equals("ผู้โดยสารลงรถ")){
            idBackground = R.drawable.psg_put_down_button;
        }
        else if(command.equals("ถึงศูนย์")){
            idBackground = R.drawable.to_central_button;
        }
        else if(command.equals("ไปยังจุดส่ง")){
            idBackground = R.drawable.go_to_destination_button;
        }
        else if(command.equals("คืนงาน")){
            idBackground = R.drawable.return_job_button;
        }
        else{
            idBackground = R.drawable.start_button;
        }
        buttonCommand.setTag(command);
        buttonCommand.setBackground(getResources().getDrawable(idBackground));
        //buttonCommand.setText(command);
    }

    public void zoom() {
        Zoom myZoom = new Zoom(activity.jobInfo.getOriginPlace()
                , activity.jobInfo.getDestinationPlace(), activity.jobInfo.getDropOffPlace());

        if (activity.jobInfo.getBookingType() < 4) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myZoom.getZoom_location(), myZoom.getZoom_level()));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(activity.jobInfo.getOriginPlace().getPlaceLocation().getLatitude(),
                            activity.jobInfo.getOriginPlace().getPlaceLocation().getLongitude()), 12));
        }
    }

//    private void drawBlinkCircle() {
//        drawBlink = new Runnable() {
//            @Override
//            public void run() {
//                if (activity.location != null) {
//                    radiusCurrent += 2;
//                    if (radiusCurrent >= 500) {
//                        radiusCurrent = 1;
//                    }
//                    temp = new LatLng(activity.location.getLatitude()
//                            , activity.location.getLongitude());
//                    rad.center(temp).radius(500).fillColor(0x6072b6f2);
//                    blink.center(temp).radius(radiusCurrent + 50).fillColor(0x6072b6f2);
//                    currentCircle = mMap.addCircle(rad);
//                    currentCircle.setStrokeColor(0x6072b6f2);
//                    currentCircle.setStrokeWidth(5);
//                    blinkCircle = mMap.addCircle(blink);
//                    blinkCircle.setStrokeColor(0xffffffff);
//                    blinkCircle.setStrokeWidth(3);
//                    currentCircle.setCenter(temp);
//                    blinkCircle.setRadius(radiusCurrent);
//                    blinkCircle.setCenter(temp);
//                }
//                handler.postDelayed(this, 1);
//            }
//            //}
//        };
//        handler.postDelayed(drawBlink, 1);
//    }

    private void pinDropOff() {
        ArrayList<Place> dropOffPlace = activity.jobInfo.getDropOffPlace();
        for (int i = 0; i < dropOffPlace.size(); i++) {
            LatLng temp;
            if (dropOffPlace.get(i).getPlaceLocation().getLatitude() != 0.0
                    | dropOffPlace.get(i).getPlaceLocation().getLongitude() != 0.0) {
                temp = new LatLng(dropOffPlace.get(i).getPlaceLocation().getLatitude(),
                        dropOffPlace.get(i).getPlaceLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(temp)
                        .title(dropOffPlace.get(i).getPlaceName()).icon(BitmapDescriptorFactory
                                .fromBitmap(createClusterBitmap(dropOffPlace.get(i).getSequence() + ""
                                        , R.drawable.yellow_radius))));
                drawCircle(0x60ffd633, temp);
            }
        }
    }

    private void pinOriginDestination() {
        Place origin = activity.jobInfo.getOriginPlace();
        Place destination = activity.jobInfo.getDestinationPlace();

        mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(origin.getPlaceLocation()
                .getLatitude(), origin.getPlaceLocation().getLongitude())).title(origin.getPlaceName())
                .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("1", R.drawable.greenradius))));
        mMap.addMarker(new MarkerOptions().anchor(0.5f, 1f).position(new LatLng(origin.getPlaceLocation()
                .getLatitude(), origin.getPlaceLocation().getLongitude())).title(origin.getPlaceName())
                .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("", R.drawable.greenflag))));
        drawCircle(0x6048d9b0, new LatLng(origin.getPlaceLocation().getLatitude(), origin.getPlaceLocation().getLongitude()));

        //mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(Double.parseDouble(activity.lat_d), Double.parseDouble(activity.lng_d))).title(activity.name_d).icon(BitmapDescriptorFactory.fromResource(R.drawable.redredius)));
        if (activity.jobInfo.getBookingType() < 4) {
            mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(destination.getPlaceLocation()
                    .getLatitude(), destination.getPlaceLocation().getLongitude())).title(destination.getPlaceName())
                    .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap(destination.getSequence() + "", R.drawable.redredius))));
            mMap.addMarker(new MarkerOptions().anchor(0.5f, 1f).position(new LatLng(destination.getPlaceLocation()
                    .getLatitude(), destination.getPlaceLocation().getLongitude())).title(destination.getPlaceName())
                    .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("", R.drawable.redflag))));
            drawCircle(0x60d34a42, new LatLng(destination.getPlaceLocation().getLatitude(), destination.getPlaceLocation().getLongitude()));
        }
    }

    private void drawCircle(int color, LatLng latlng) {
        mMap.addCircle(new CircleOptions()
                .center(latlng)
                .radius(1000)
                .fillColor(color)).setStrokeColor(color);
    }

    private Bitmap createClusterBitmap(String text, int idImage) {
        View cluster = LayoutInflater.from(getContext()).inflate(R.layout.green_radius, null);
        TextView clusterSizeText = (TextView) cluster.findViewById(R.id.textView23);
        clusterSizeText.setText(text);
        ImageView image = (ImageView) cluster.findViewById(R.id.imageView19);
        image.setImageResource(idImage);
        cluster.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        cluster.layout(0, 0, cluster.getMeasuredWidth(), cluster.getMeasuredHeight());

        final Bitmap clusterBitmap = Bitmap.createBitmap(cluster.getMeasuredWidth(),
                cluster.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(clusterBitmap);
        cluster.draw(canvas);

        return clusterBitmap;
    }

//    private void stopDrawBlinkCircle() {
//        handler.removeCallbacks(drawBlink);
//    }

    public void onResume() {
        super.onResume();
        //handler.postDelayed(drawBlink, 1);

    }

    public void onPause() {
        //stopDrawBlinkCircle();

        super.onPause();

    }

    @Override
    public void onDismiss(String name) {
        //Toast.makeText(getContext(), "Dissmiss : " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn(int jobStatus) {
        alertSubmitForceDialog(jobStatus, 0);
    }

    @Override
    public void onPickup(int jobStatus) {
        alertSubmitForceDialog(jobStatus, 2);
    }

    @Override
    public void onPutPassenger(int jobStatus) {
        alertSubmitForceDialog(jobStatus, 4);
    }

    @Override
    public void onEnd(int jobStatus) {
        alertSubmitForceDialog(jobStatus, 9);
    }

    @Override
    public void onOKConclude() {
        activity.apiHandle.requestTrackingJob(new int[]{activity.dispatcher.getEmpID(),
                        activity.driver.getEmpID(), activity.vehicle.getVehicleID(), activity.jobInfo.getCustomerID()}
                , activity.jobInfo.getJobCode(), 5);
        isForce = false;
        setVisibleCommandButton(false);
        setTextButtonCommand("ถึงศูนย์");
        activity.jobInfo.setJobStatus(5);
        alertRatingDialog();
    }

    @Override
    public void onResult(int rate) {
        this.rating = rate;
        CameraFragment cameraFragment = (CameraFragment) getChildFragmentManager()
                .findFragmentByTag("camera");
        if (cameraFragment != null) {
            cameraFragment.takePicture();
        }

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                activity.setEndJob();
//                activity.apiHandle.requestReviewJob(rating, activity.jobInfo.getJobCode(), new byte[]{1});
//                getFragmentManager().popBackStack();
//            }
//        });

        //set button command
        mMap.clear();
        Place central = activity.centralPlaceInfo;

        mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(central.getPlaceLocation()
                .getLatitude(), central.getPlaceLocation().getLongitude())).title(central.getPlaceName())
                .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("", R.drawable.hotel))));
        drawCircle(0x6072b6f2, new LatLng(central.getPlaceLocation().getLatitude(), central.getPlaceLocation().getLongitude()));
    }

    @Override
    public void onOKSubmit(int oldJobStatus, int newJobStatus, String text, String cause) {
        //change command button and send log
        if (activity.location != null) {
            activity.apiHandle.sendLogJob(activity.jobInfo.getJobID() + "", new int[]{oldJobStatus, newJobStatus},
                    activity.getTimeISO8601(), new double[]{activity.location.getLatitude()
                            , activity.location.getLongitude()}, text + ":" + cause);
        } else {
            activity.apiHandle.sendLogJob(activity.jobInfo.getJobID() + "", new int[]{oldJobStatus, newJobStatus},
                    activity.getTimeISO8601(), new double[]{0.0, 0.0}, text + ":" + cause);
        }
        if (newJobStatus == 0) {
            setTextButtonCommand("คืนงาน");
        } else if (newJobStatus == 2) {
            setTextButtonCommand("รอรับผู้โดยสาร");
        } else if (newJobStatus == 4) {
            setTextButtonCommand("ผู้โดยสารลงรถ");
        } else if (newJobStatus == 9) {
            setTextButtonCommand("ถึงศูนย์");
            mMap.clear();
            Place central = activity.centralPlaceInfo;
            mMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).position(new LatLng(central.getPlaceLocation()
                    .getLatitude(), central.getPlaceLocation().getLongitude())).title(central.getPlaceName())
                    .icon(BitmapDescriptorFactory.fromBitmap(createClusterBitmap("", R.drawable.hotel))));
            drawCircle(0x6072b6f2, new LatLng(central.getPlaceLocation().getLatitude(), central.getPlaceLocation().getLongitude()));
        }
        isForce = true;
        setVisibleCommandButton(true);
    }

    @Override
    public void onTakePicture(final byte[] picture) {
        activity.apiHandle.requestReviewJob(rating, activity.jobInfo.getJobCode(), picture);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                activity.setEndJob();
//                activity.apiHandle.requestReviewJob(rating, activity.jobInfo.getJobCode(), picture);
//                getFragmentManager().popBackStack();
//            }
//        });
    }
}
