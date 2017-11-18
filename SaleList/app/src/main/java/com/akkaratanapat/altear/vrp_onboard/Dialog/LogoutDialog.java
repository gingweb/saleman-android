package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by altear on 8/8/2017.
 */

public class LogoutDialog {

    public static void show(Context context, boolean cancelable, final OnDismissDialogListener listener,final LogoutDialog.OnLogoutDialogListener onLogoutDialogListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.setCancelable(cancelable);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("logout");
            }
        });

        FlatTextView textViewLogout = (FlatTextView) dialog.findViewById(R.id.textViewLogout);
        final FlatEditText editTextLogout = (FlatEditText) dialog.findViewById(R.id.editTextLogout);
        FlatButton buttonOKLogout = (FlatButton) dialog.findViewById(R.id.buttonOKLogout);
        FlatButton buttonCancelLogout = (FlatButton) dialog.findViewById(R.id.buttonCancelLogout);

        textViewLogout.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        editTextLogout.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonOKLogout.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonCancelLogout.getAttributes().setTheme(R.array.custom_gold_light, context.getResources());

        buttonOKLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dispatcherCode = editTextLogout.getText().toString();
                onLogoutDialogListener.onLogout(dispatcherCode);
                dialog.dismiss();

            }
        });

        buttonCancelLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnLogoutDialogListener {
        void onLogout(String dispatcherCode);
    }
}
