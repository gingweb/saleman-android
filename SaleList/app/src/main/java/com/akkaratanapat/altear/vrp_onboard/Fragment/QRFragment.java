package com.akkaratanapat.altear.vrp_onboard.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akkaratanapat.altear.vrp_onboard.Activity.HomeActivity;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A simple {@link Fragment} subclass.
 */

public class QRFragment extends Fragment implements ZBarScannerView.ResultHandler {

    FlatEditText editTextQR;
    FlatButton button_qr_ok, button_qr_cancel;
    HomeActivity activity;
    private ZBarScannerView mScannerView;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    RelativeLayout contentFrame;
    Handler handler;
    Runnable qrView;
    private int isQr = 1,isOK = 1;

    public static QRFragment newInstance() {
        return new QRFragment();
    }

    public QRFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setComponent(view);
    }

    private void setComponent(View rootView) {
        //activity.setTimeToBlank();
        editTextQR = (FlatEditText) rootView.findViewById(R.id.editTextQR);
        button_qr_ok = (FlatButton) rootView.findViewById(R.id.buttonQROk);
        button_qr_cancel = (FlatButton) rootView.findViewById(R.id.buttonQRCancel);
        contentFrame = (RelativeLayout) rootView.findViewById(R.id.contentFrame);

        mScannerView = new ZBarScannerView(getContext());
        handler = new Handler();
        qrView = new Runnable() {
            @Override
            public void run() {
                contentFrame.removeAllViews();
                contentFrame.addView(mScannerView);
            }
        };

        handler.postDelayed(qrView,600);

        editTextQR.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        button_qr_ok.getAttributes().setTheme(R.array.custom_gold_dark, getResources());
        button_qr_cancel.getAttributes().setTheme(R.array.custom_gold_light, getResources());
        button_qr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOK == 1){
                    getFragmentManager().popBackStack();
                    //activity.apiHandle.reCall();
                    //add start time for conclude dialog later
                    if(activity.dispatcher!=null & activity.driver!=null&activity.vehicle!=null) {
                        activity.apiHandle.reCall();
                        activity.apiHandle.requestFindJob(activity.dispatcher.getEmpID(), activity.driver.getEmpID(),
                                activity.vehicle.getVehicleID(), editTextQR.getText().toString());
                    }
                    isOK = 0;
                }
            }
        });

        button_qr_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.setTimeToBlank();
                if(isQr == 1){
                    getFragmentManager().popBackStack();
                    isQr = 0;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(getContext(), "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        //handler.removeCallbacks(qrView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity) getActivity();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0 && resultCode == -1) {
            String content = intent.getStringExtra("CONTENT");
            String format = intent.getStringExtra("FORMAT");
            String type = intent.getStringExtra("TYPE");
            editTextQR.setText(content + "\n" + format + "\n" + type);
        } else if (requestCode == 0 && resultCode == 0) {
            editTextQR.setText("");
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(getContext(), "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        editTextQR.setText(rawResult.getContents());
        if(!rawResult.getContents().contains("0")){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(QRFragment.this);
                }
            }, 1500);
        }

    }
}
