package com.akkaratanapat.altear.vrp_onboard.Activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.akkaratanapat.altear.vrp_onboard.Dialog.OnDismissDialogListener;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.akkaratanapat.altear.vrp_onboard.Utility.DataPreferences;


public class SplashActivity extends AppCompatActivity implements OnDismissDialogListener{

    Handler handler;
    Runnable nextActivityAction,appearLogoAction,disappearLogoAction;
    ImageView logo;
    long delayTime;
    long time = 4500L;
    int onJob;
    DataPreferences dataPreferences;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        setPreferences();
        logo = (ImageView)findViewById(R.id.imageViewIntro);
        handler = new Handler();
//        dataPreferences.edit().removeCounterLength().removeJobCode()
//                .removeOnJob().removeCounterSecond().removeAcceptTime().apply();
//        dataPreferences.edit().removeCounterLength().removeDevice().removeCounterSecond()
//                .removeDispatcher().removeDriver().removeJobCode().removeOnJob()
//                .removeVehicle().removeOnRecall().removeOnLogin().apply();
        onJob = dataPreferences.getOnJob();

        appearLogoAction = new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofFloat(logo, View.ALPHA, 1f);
                anim.setDuration(800);
                anim.start();
            }
        };

        disappearLogoAction = new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofFloat(logo, View.ALPHA, 0f);
                anim.setDuration(800);
                anim.start();
            }
        };

        nextActivityAction = new Runnable() {
            public void run() {
                if(dataPreferences.getOnLogin() == 1){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void setPreferences(){
        dataPreferences = new DataPreferences(getApplicationContext());
    }

    public void onResume() {
        super.onResume();
        delayTime = time;
        handler.postDelayed(appearLogoAction, delayTime-3500);
        handler.postDelayed(disappearLogoAction, delayTime - 1500);
        handler.postDelayed(nextActivityAction, delayTime);
        time = System.currentTimeMillis();
    }

    public void onPause() {
        super.onPause();
        handler.removeCallbacks(appearLogoAction);
        handler.removeCallbacks(disappearLogoAction);
        handler.removeCallbacks(nextActivityAction);
        time =  delayTime - (System.currentTimeMillis() - time);
    }


    @Override
    public void onDismiss(String s) {

    }
}