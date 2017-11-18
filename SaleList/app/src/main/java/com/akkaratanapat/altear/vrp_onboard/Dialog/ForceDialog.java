package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by altear on 8/10/2017.
 */

public class ForceDialog {
    public static void show(Context context, CharSequence title, CharSequence message
            , String command,boolean isVisibility, final int jobStatus, int jobType, boolean cancelable, final OnDismissDialogListener listener
            , final ForceDialog.onForceDialogListener onForceDialogListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.force_dialog);
        dialog.setCancelable(cancelable);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(listener != null)
                    listener.onDismiss("message");
            }
        });

        FlatTextView dialog_title = (FlatTextView) dialog.findViewById(R.id.forceDialogTitle);
        FlatTextView dialog_message = (FlatTextView) dialog.findViewById(R.id.forceDialogMessage);
        FloatingActionButton button_close_message_dialog = (FloatingActionButton)dialog.findViewById(R.id.buttonCloseForceDialog);

        FlatButton buttonToReturn = (FlatButton)dialog.findViewById(R.id.buttonToReturn);
        FlatButton buttonToPickup = (FlatButton)dialog.findViewById(R.id.buttonToPickup);
        FlatButton buttonToPutPassenger = (FlatButton)dialog.findViewById(R.id.buttonToPutPassenger);
        FlatButton buttonToEnd = (FlatButton)dialog.findViewById(R.id.buttonToEnd);

        dialog_title.getAttributes().setTheme(FlatUI.SNOW, context.getResources());
        dialog_message.getAttributes().setTheme(FlatUI.DARK, context.getResources());
        buttonToReturn.getAttributes().setTheme(FlatUI.GRASS,context.getResources());
        buttonToPickup.getAttributes().setTheme(R.array.custom_sand,context.getResources());
        buttonToPutPassenger.getAttributes().setTheme(R.array.custom_sand,context.getResources());
        buttonToEnd.getAttributes().setTheme(FlatUI.CANDY,context.getResources());

        dialog_title.setText(title);
        dialog_message.setText(message);
        //| command.equals("รอรับผู้โดยสาร")
        if(jobType != 1 ){
            buttonToPickup.setVisibility(View.GONE);
        }
        if(command.equals("ไปยังจุดรับผู้โดยสาร")){
            buttonToPickup.setVisibility(View.GONE);
            buttonToPutPassenger.setVisibility(View.GONE);
        }
        if(command.equals("รอรับผู้โดยสาร")){
            buttonToPutPassenger.setVisibility(View.GONE);
            if(isVisibility){
                buttonToPickup.setVisibility(View.GONE);
            }
        }
        if(command.equals("ผู้โดยสารขึ้นรถ")){
//            if(isVisibility){
//
//            }
            buttonToPickup.setVisibility(View.GONE);
            buttonToPutPassenger.setVisibility(View.GONE);
        }
        if(command.equals("ผู้โดยสารลงรถ")){
            if(isVisibility){
                buttonToPutPassenger.setVisibility(View.GONE);
            }
            buttonToPickup.setVisibility(View.GONE);
        }
        if(command.equals("ไปยังจุดส่ง")){
            buttonToPutPassenger.setVisibility(View.GONE);
        }
        if(command.equals("ออกรถ")){
            buttonToPutPassenger.setVisibility(View.GONE);
        }
        if(command.equals("ถึงศูนย์")){
            if(isVisibility){
                buttonToEnd.setVisibility(View.GONE);
            }
            buttonToReturn.setVisibility(View.GONE);
            buttonToPutPassenger.setVisibility(View.GONE);
            buttonToPickup.setVisibility(View.GONE);
        }
        if(command.equals("คืนงาน")  && isVisibility){
            buttonToReturn.setVisibility(View.GONE);
            buttonToPutPassenger.setVisibility(View.GONE);
        }

        buttonToReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForceDialogListener.onReturn(jobStatus);
                dialog.dismiss();
            }
        });

        buttonToPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForceDialogListener.onPickup(jobStatus);
                dialog.dismiss();
            }
        });

        buttonToPutPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForceDialogListener.onPutPassenger(jobStatus);
                dialog.dismiss();
            }
        });

        buttonToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForceDialogListener.onEnd(jobStatus);
                dialog.dismiss();
            }
        });

        button_close_message_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface onForceDialogListener{
        void onReturn(int jobStatus);
        void onPickup(int jobStatus);
        void onPutPassenger(int jobStatus);
        void onEnd(int jobStatus);
    }
}
