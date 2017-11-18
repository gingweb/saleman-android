package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by aa on 8/10/2017.
 */

public class DecisionDialog {
    public static void show(Context context, String message, boolean cancelable, final OnDismissDialogListener listener, final DecisionDialog.OnDecisionDialogListener onDecisionDialogListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.decision_dialog);
        dialog.setCancelable(cancelable);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("logout");
            }
        });
        FlatTextView textViewTitleWarning = (FlatTextView) dialog.findViewById(R.id.textViewWarningTitle);
        FlatTextView textViewWarning = (FlatTextView) dialog.findViewById(R.id.textViewWarning);
        FlatButton buttonOK = (FlatButton) dialog.findViewById(R.id.buttonDecisionOk);
        FlatButton buttonCancel = (FlatButton) dialog.findViewById(R.id.buttonDecisionCancel);

        textViewTitleWarning.getAttributes().setTheme(FlatUI.SNOW,context.getResources());
        textViewWarning.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonOK.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonCancel.getAttributes().setTheme(R.array.custom_gold_light, context.getResources());

        textViewTitleWarning.setText("Warning!");
        textViewWarning.setText(message);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDecisionDialogListener.onOKDecision();
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnDecisionDialogListener {
        void onOKDecision();
    }
}
