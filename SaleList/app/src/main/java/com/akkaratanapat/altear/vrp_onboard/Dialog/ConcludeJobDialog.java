package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.akkaratanapat.altear.vrp_onboard.Model.Driver;
import com.akkaratanapat.altear.vrp_onboard.Model.JobInfo;
import com.akkaratanapat.altear.vrp_onboard.Model.Vehicle;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

import java.text.DecimalFormat;

/**
 * Created by altear on 8/9/2017.
 */

public class ConcludeJobDialog {

    public static void show(final Context context, boolean cancelable,
                            Driver driver, Bitmap myBitmap, Vehicle vehicle, JobInfo jobInfo, String acceptTime, String now, float distance
            , final OnDismissDialogListener listener
            , final onConcludeListener onConcludeListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conclude_job_dialog);
        dialog.setCancelable(cancelable);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("conclude");
            }
        });

        FlatTextView textViewJobIDConclude = (FlatTextView) dialog.findViewById(R.id.textViewJobIDConclude);
        FlatTextView textViewJobConclude = (FlatTextView) dialog.findViewById(R.id.textViewJobConclude);
        FlatTextView textViewNameConclude = (FlatTextView) dialog.findViewById(R.id.textViewNameConclude);
        FlatTextView textViewLicenceConclude = (FlatTextView) dialog.findViewById(R.id.textViewLicenceConclude);
        FlatTextView textViewBrandConclude = (FlatTextView) dialog.findViewById(R.id.textViewBrandConclude);
        FlatTextView textViewOriginPlaceConclude = (FlatTextView) dialog.findViewById(R.id.textViewOriginPlaceConclude);
        FlatTextView textViewDropOffPlaceConclude = (FlatTextView) dialog.findViewById(R.id.textViewDropOffPlaceConclude);
        FlatTextView textViewDestinationPlaceConclude = (FlatTextView) dialog.findViewById(R.id.textViewDestinationPlaceConclude);
        FlatTextView textViewTimeConclude = (FlatTextView) dialog.findViewById(R.id.textViewTimeConclude);
        FlatTextView textViewTimeCounterConclude = (FlatTextView) dialog.findViewById(R.id.textViewTimeCounterConclude);
        FlatTextView textViewStartTimeCounterConclude = (FlatTextView) dialog.findViewById(R.id.textViewStartTimeCounterConclude);
        FlatTextView textViewStartTimeCounterCounterConclude = (FlatTextView) dialog.findViewById(R.id.textViewStartTimeCounterCounterConclude);
        FlatTextView textViewLengthConclude = (FlatTextView) dialog.findViewById(R.id.textViewLengthConclude);
        FlatTextView textViewLengthCounterConclude = (FlatTextView) dialog.findViewById(R.id.textViewLengthCounterConclude);

        FlatButton buttonConcludeOk = (FlatButton) dialog.findViewById(R.id.buttonConcludeOk);
        FlatButton buttonConcludeCancel = (FlatButton) dialog.findViewById(R.id.buttonConcludeCancel);

        final ImageView imageViewProfileConclude = (ImageView)dialog.findViewById(R.id.imageViewProfileConclude);
//        ImageView imageViewOriginConclude = (ImageView) dialog.findViewById(R.id.imageViewOriginConclude);
//        ImageView imageViewDestinationConclude = (ImageView) dialog.findViewById(R.id.imageViewDestinationPlaceConclude);

        textViewJobIDConclude.getAttributes().setTheme(FlatUI.SNOW, context.getResources());
        textViewJobConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewNameConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewLicenceConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewBrandConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewOriginPlaceConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewDropOffPlaceConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewDestinationPlaceConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewTimeConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewTimeCounterConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewStartTimeCounterConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewStartTimeCounterCounterConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewLengthConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        textViewLengthCounterConclude.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());

        buttonConcludeOk.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonConcludeCancel.getAttributes().setTheme(R.array.custom_gold_light, context.getResources());

//        imageViewProfileConclude.setBackground(driverPicture);

        imageViewProfileConclude.setBackground(new BitmapDrawable(context.getResources(),myBitmap));

//        Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.e("picasso", from.toString());
//                imageViewProfileConclude.setBackground(new BitmapDrawable(context.getResources(),
//                        Bitmap.createScaledBitmap(bitmap, 100, 100, false)));
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//                imageViewProfileConclude.setBackground(errorDrawable);
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                imageViewProfileConclude.setBackground(placeHolderDrawable);
//            }
//        };
//
//        String imageURL = "http://i.imgur.com/CqmBjo5.jpg";
//        if (!driver.getImageProfileURL().equals("n/a")) {
//            if (!driver.getImageProfileURL().contains("aot-hotel.transcodeglobal")) {
//                imageURL = "https://fleet.aot-hotel.transcodeglobal.com/" + driver.getImageProfileURL();
//            } else {
//                imageURL = driver.getImageProfileURL();
//            }
//        }
//
////        Picasso.with(context).load(imageURL)
////                .fit()
////                .placeholder(R.drawable.ic_account_box_white_48dp)
////                .error(R.drawable.ic_account_box_white_48dp)
////                .into(imageViewProfileConclude);
//
//        Picasso.with(context).load(imageURL)
//                .placeholder(R.drawable.ic_account_box_white_48dp)
//                .error(R.drawable.ic_account_box_white_48dp)
//                .into(target);
//

//        imageViewProfileConclude.setTag(target);

        textViewJobIDConclude.setText(jobInfo.getJobCode());
        textViewNameConclude.setText(driver.getFirstNameTH() + " " + driver.getLastNameTH());
        textViewLicenceConclude.setText(vehicle.getLicencePlateNumber());
        textViewBrandConclude.setText(vehicle.getVehicleBrandCode());
        String tempOrigin = jobInfo.getOriginPlace().getPlaceName();
        if(tempOrigin.length() > 30){
            tempOrigin = tempOrigin.substring(0,29) + "...";
        }
        textViewOriginPlaceConclude.setText(tempOrigin);
        textViewDropOffPlaceConclude.setText(jobInfo.getStringDropOff());

        textViewTimeConclude.setText("Accept Time : ");
        textViewTimeCounterConclude.setText(acceptTime);
        textViewStartTimeCounterConclude.setText("Finish Time : ");
        textViewStartTimeCounterCounterConclude.setText(now);
        textViewLengthConclude.setText("Distance : ");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        textViewLengthCounterConclude.setText(df.format(distance/1000) + " KM.");
        int jobType = jobInfo.getBookingType();
        String tempDestination = jobInfo.getDestinationPlace().getPlaceName();
        if(tempDestination.length() > 30){
            tempDestination = tempDestination.substring(0,29) + "...";
        }
        if(jobType == 1){
            textViewJobConclude.setText("Pickup");
            //textViewDestinationPlaceConclude.setText(jobInfo.getDestinationPlace().getPlaceName());
        }else if(jobType == 2){
            textViewJobConclude.setText("Delivery");
            //textViewDestinationPlaceConclude.setText(jobInfo.getDestinationPlace().getPlaceName());
        }else if(jobType == 4){
            textViewJobConclude.setText("Rental for hour");
            tempDestination = "รายการเช่าเหมารายชั่วโมง";
            //textViewDestinationPlaceConclude.setText("รายการเช่าเหมารายชั่วโมง");
        }else if(jobType == 5){
            textViewJobConclude.setText("Rental for day");
            tempDestination = "รายการเช่าเหมารายวัน";
            //textViewDestinationPlaceConclude.setText("รายการเช่าเหมารายวัน");
        }
        textViewDestinationPlaceConclude.setText(tempDestination);


        buttonConcludeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConcludeListener.onOKConclude();
                dialog.dismiss();
            }
        });

        buttonConcludeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface onConcludeListener{
        void onOKConclude();
    }
}
