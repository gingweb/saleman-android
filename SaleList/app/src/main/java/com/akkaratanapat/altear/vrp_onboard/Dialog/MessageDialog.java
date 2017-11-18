package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by altear on 8/6/2017.
 */

public class MessageDialog {

    public static void show(Context context, CharSequence title, CharSequence message
            , boolean cancelable, final OnDismissDialogListener listener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_dialog);
        dialog.setCancelable(cancelable);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(listener != null)
                    listener.onDismiss("message");
            }
        });

        FlatTextView dialog_title = (FlatTextView) dialog.findViewById(R.id.messageDialogTitle);
        FlatTextView dialog_message = (FlatTextView) dialog.findViewById(R.id.messageDialogMessage);
        FloatingActionButton button_close_message_dialog = (FloatingActionButton)dialog.findViewById(R.id.buttonCloseMessageDialog);

        dialog_title.getAttributes().setTheme(FlatUI.SNOW, context.getResources());
        dialog_message.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());

        dialog_title.setText(title);
        dialog_message.setText(message);

        button_close_message_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
