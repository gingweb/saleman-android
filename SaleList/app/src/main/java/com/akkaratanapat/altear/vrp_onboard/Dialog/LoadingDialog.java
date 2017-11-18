package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by altear on 8/6/2017.
 */

public class LoadingDialog {

    private Dialog dialog;

    public void show(Context context, CharSequence message
            , boolean cancelable, final OnDismissDialogListener listener) {
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(cancelable);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(listener != null){
                    listener.onDismiss("loading");
                }
            }
        });

        FlatTextView dialog_message = (FlatTextView) dialog.findViewById(R.id.progress_dialog_message);
        //ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.loading_progressBar);
        dialog_message.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        dialog_message.setText(message);

        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

}
