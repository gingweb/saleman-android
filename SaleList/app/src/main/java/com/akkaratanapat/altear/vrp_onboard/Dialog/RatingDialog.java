package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by aa on 8/10/2017.
 */

public class RatingDialog {

    public static void show(final Context context,boolean cancelable, final OnDismissDialogListener listener
            , final RatingDialog.onRatingDialogListener onRatingDialogListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_dialog);
        dialog.setCancelable(cancelable);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("rating");
            }
        });

        FlatTextView dialog_title = (FlatTextView) dialog.findViewById(R.id.ratingDialogTitle);
        FloatingActionButton button_close_message_dialog = (FloatingActionButton) dialog.findViewById(R.id.buttonCloseRatingDialog);
        final Button veryPoor, poor, fair, good, excellent;
        veryPoor = (Button) dialog.findViewById(R.id.buttonVeryPoor);
        poor = (Button) dialog.findViewById(R.id.buttonPoor);
        fair = (Button) dialog.findViewById(R.id.buttonFair);
        good = (Button) dialog.findViewById(R.id.buttonGood);
        excellent = (Button) dialog.findViewById(R.id.buttonExcellent);

        dialog_title.getAttributes().setTheme(FlatUI.DARK, context.getResources());

        dialog_title.setText("Rating");

        veryPoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veryPoor.setBackground(context.getResources().getDrawable(R.drawable.emo1_1));
                onRatingDialogListener.onResult(1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });
        poor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poor.setBackground(context.getResources().getDrawable(R.drawable.emo2_1));
                onRatingDialogListener.onResult(2);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });
        fair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fair.setBackground(context.getResources().getDrawable(R.drawable.emo3_1));
                onRatingDialogListener.onResult(3);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                good.setBackground(context.getResources().getDrawable(R.drawable.emo4_1));
                onRatingDialogListener.onResult(4);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });
        excellent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excellent.setBackground(context.getResources().getDrawable(R.drawable.emo5_1));
                onRatingDialogListener.onResult(5);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                }, 500);
            }
        });

        button_close_message_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRatingDialogListener.onResult(0);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface onRatingDialogListener {
        void onResult(int rating);
    }
}
