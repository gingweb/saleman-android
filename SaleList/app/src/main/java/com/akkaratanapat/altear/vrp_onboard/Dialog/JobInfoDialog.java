package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.akkaratanapat.altear.vrp_onboard.Model.JobInfo;
import com.akkaratanapat.altear.vrp_onboard.R;
import com.akkaratanapat.altear.vrp_onboard.Utility.CalendarHandler;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

import java.util.Date;

/**
 * Created by altear on 8/9/2017.
 */

public class JobInfoDialog {

    private Dialog dialog;
    private RelativeLayout title;
    private FlatTextView textViewJobInfoType, textViewJobInfoCalendar, textViewJobInfoTime, textViewJobInfoOrigin, textViewJobInfoDestination;
    private ImageView imageViewJobInfoOrigin, imageViewJobInfoDestination,imageViewJobInfoDot;

    public JobInfoDialog(){

    }

    public void show(Context context, boolean cancelable, final JobInfo jobInfo
            , final OnDismissDialogListener listener,
                     final JobInfoDialog.onJobInfoListener onJobInfoListener) {

        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.job_info_dialog);
        dialog.setCancelable(cancelable);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("jobInfo");
            }
        });

        title = (RelativeLayout) dialog.findViewById(R.id.title);
        textViewJobInfoType = (FlatTextView) dialog.findViewById(R.id.textViewJobInfoType);
        textViewJobInfoCalendar = (FlatTextView) dialog.findViewById(R.id.textViewJobInfoCalendar);
        textViewJobInfoTime = (FlatTextView) dialog.findViewById(R.id.textViewJobInfoTime);
        textViewJobInfoOrigin = (FlatTextView) dialog.findViewById(R.id.textViewJobInfoOrigin);
        textViewJobInfoDestination = (FlatTextView) dialog.findViewById(R.id.textViewJobInfoDestination);

        FlatButton buttonJobInfoOk = (FlatButton) dialog.findViewById(R.id.buttonJobInfoOk);
        FlatButton buttonJobInfoCancel = (FlatButton) dialog.findViewById(R.id.buttonJobInfoCancel);

        imageViewJobInfoOrigin = (ImageView) dialog.findViewById(R.id.imageViewJobInfoOrigin);
        imageViewJobInfoDot = (ImageView) dialog.findViewById(R.id.imageViewJobInfoDot);
        imageViewJobInfoDestination = (ImageView) dialog.findViewById(R.id.imageViewJobInfoDestination);

        title.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        textViewJobInfoType.getAttributes().setTheme(FlatUI.SNOW, context.getResources());
        textViewJobInfoCalendar.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        textViewJobInfoTime.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        textViewJobInfoOrigin.getAttributes().setTheme(FlatUI.GRASS, context.getResources());
        textViewJobInfoDestination.getAttributes().setTheme(FlatUI.BLOOD, context.getResources());

        buttonJobInfoOk.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonJobInfoCancel.getAttributes().setTheme(R.array.custom_gold_light, context.getResources());
        if (jobInfo != null) {
            int jobType = jobInfo.getBookingType();
            Date dateString = CalendarHandler.getCalendarJobInfo(jobInfo.getPickupDateTime()).getTime();

            if (jobType == 1) {
                setPickupInfo(context, jobInfo, dateString);
            } else if (jobType == 2) {
                setDelivery(context, jobInfo, dateString);
            } else if (jobType == 4) {
                setPerHour(context, jobInfo, dateString);
            } else if (jobType == 5) {
                setPerDay(context, jobInfo, dateString);
            }
        }
        buttonJobInfoOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onJobInfoListener.onOKJobInfo(jobInfo.getJobCode());
                dialog.dismiss();
            }
        });

        buttonJobInfoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJobInfoListener.onCancelJobInfo();
            }
        });
        dialog.show();
    }

    private void setPickupInfo(Context context, JobInfo jobInfo, Date date) {
        //title.setBackgroundColor(context.getResources().getColor(R.color.red));
        title.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        textViewJobInfoType.setText("Pickup");
        textViewJobInfoCalendar.setText("วันที่ : " + CalendarHandler.getPickupDate(date));
        textViewJobInfoTime.setText("เวลา : " + CalendarHandler.getPickupTime(date));
        textViewJobInfoOrigin.setText(jobInfo.getOriginPlace().getPlaceName());
        textViewJobInfoDestination.setText(jobInfo.getDestinationPlace().getPlaceName());
        imageViewJobInfoOrigin.setBackground(context.getResources().getDrawable(R.drawable.greenplace));
        imageViewJobInfoDot.setBackground(context.getResources().getDrawable(R.drawable.dot_hori_red));
        imageViewJobInfoDestination.setBackground(context.getResources().getDrawable(R.drawable.hotel_red));
    }

    private void setDelivery(Context context, JobInfo jobInfo, Date date) {
        //title.setBackgroundColor(context.getResources().getColor(R.color.green));
        title.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        textViewJobInfoType.setText("Delivery");
        textViewJobInfoCalendar.setText("วันที่ : " + CalendarHandler.getPickupDate(date));
        textViewJobInfoTime.setText("เวลา : " + CalendarHandler.getPickupTime(date));
        textViewJobInfoOrigin.setText(jobInfo.getOriginPlace().getPlaceName());
        textViewJobInfoDestination.setText(jobInfo.getDestinationPlace().getPlaceName());
        imageViewJobInfoOrigin.setBackground(context.getResources().getDrawable(R.drawable.hotel_green));
        imageViewJobInfoDot.setBackground(context.getResources().getDrawable(R.drawable.dot_hori_green));
        imageViewJobInfoDestination.setBackground(context.getResources().getDrawable(R.drawable.redplace));
    }

    private void setPerHour(Context context, JobInfo jobInfo, Date date) {
        title.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        String rental;
        if (jobInfo.getHourRental() > 24) {
            if (jobInfo.getHourRental() % 24 == 0) {
                rental = "Rental for hour : " + jobInfo.getHourRental() / 24 + " วัน ";
            } else {
                rental = "Rental for hour : " + jobInfo.getHourRental() / 24 + " วัน "
                        + jobInfo.getHourRental() % 24 + " ชั่วโมง";
            }
        } else {
            rental = "Rental for hour : " + jobInfo.getHourRental() % 24 + " ชั่วโมง";
        }
        textViewJobInfoType.setText(rental);
        textViewJobInfoCalendar.setText("วันที่ : " + CalendarHandler.getPickupDate(date));
        textViewJobInfoTime.setText("เวลา : " + CalendarHandler.getPickupTime(date));
        textViewJobInfoOrigin.setText(jobInfo.getOriginPlace().getPlaceName());
        textViewJobInfoDestination.setText("");
        imageViewJobInfoOrigin.setBackground(context.getResources().getDrawable(R.drawable.hotel));
        imageViewJobInfoDot.setBackground(context.getResources().getDrawable(R.drawable.dot_hori_green));
        imageViewJobInfoDestination.setBackground(context.getResources().getDrawable(R.drawable.grayplace));
    }

    private void setPerDay(Context context, JobInfo jobInfo, Date date) {
        title.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        String rental;
//        if (jobInfo.getHourRental() > 24) {
//            if (jobInfo.getHourRental() % 24 == 0) {
//                rental = "Rental for day : " + jobInfo.getHourRental() / 24 + " วัน ";
//            } else {
//                rental = "Rental for day : " + jobInfo.getHourRental() / 24 + " วัน "
//                        + (jobInfo.getHourRental() / 24) % 24 + " ชั่วโมง";
//            }
//        } else {
//            rental = "Rental for day : " + jobInfo.getHourRental() % 24 + " ชั่วโมง";
//        }
        rental = "Rental for day : 1 วัน ";
        textViewJobInfoType.setText(rental);
        textViewJobInfoCalendar.setText("วันที่ : " + CalendarHandler.getPickupDate(date));
        textViewJobInfoTime.setText("เวลา : " + CalendarHandler.getPickupTime(date));
        textViewJobInfoOrigin.setText(jobInfo.getOriginPlace().getPlaceName());
        textViewJobInfoDestination.setText("");
        imageViewJobInfoOrigin.setBackground(context.getResources().getDrawable(R.drawable.hotel));
        imageViewJobInfoDot.setBackground(context.getResources().getDrawable(R.drawable.dot_hori_green));
        imageViewJobInfoDestination.setBackground(context.getResources().getDrawable(R.drawable.grayplace));
    }

    public void complete() {
        dialog.dismiss();
    }

    public interface onJobInfoListener {
        void onOKJobInfo(String jobCode);

        void onCancelJobInfo();
    }
}
