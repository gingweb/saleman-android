package com.akkaratanapat.altear.vrp_onboard.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.akkaratanapat.altear.vrp_onboard.R;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatRadioButton;
import com.cengalabs.flatui.views.FlatTextView;

/**
 * Created by aa on 8/11/2017.
 */

public class SubmitDialog {

    private static FlatEditText cause;

    public static void show(Context context, final int jobStatus, final int mode, boolean cancelable, final OnDismissDialogListener listener, final SubmitDialog.OnSubmitDialogListener onSubmitDialogListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.force_submit_dialog);
        dialog.setCancelable(cancelable);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null)
                    listener.onDismiss("logout");
            }
        });

        RelativeLayout backgroundSubmit = (RelativeLayout) dialog.findViewById(R.id.backgroundSubmit);
        FlatTextView textViewTitleWarning = (FlatTextView) dialog.findViewById(R.id.submitDialogTitle);
        FlatTextView textViewWarning = (FlatTextView) dialog.findViewById(R.id.submitDialogMessage);
        FlatTextView becauseText = (FlatTextView)dialog.findViewById(R.id.becauseText);
        FlatButton buttonOK = (FlatButton) dialog.findViewById(R.id.buttonSubmitOk);
        FlatButton buttonCancel = (FlatButton) dialog.findViewById(R.id.buttonSubmitCancel);

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.groupRadioSubmit);
        FlatRadioButton wrongJob = (FlatRadioButton) dialog.findViewById(R.id.radioAcceptWrongJob);
        FlatRadioButton incident = (FlatRadioButton) dialog.findViewById(R.id.radioIncident);
        FlatRadioButton other = (FlatRadioButton) dialog.findViewById(R.id.radioOther);

        cause = (FlatEditText) dialog.findViewById(R.id.causeEditText);

        textViewTitleWarning.getAttributes().setTheme(FlatUI.DEEP, context.getResources());
        textViewWarning.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        becauseText.getAttributes().setTheme(R.array.custom_gold_dark,context.getResources());
        buttonOK.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());
        buttonCancel.getAttributes().setTheme(R.array.custom_gold_light, context.getResources());
        cause.getAttributes().setTheme(R.array.custom_gold_dark, context.getResources());

        if (mode == 0) {
            textViewTitleWarning.setText("คืนงาน");
            backgroundSubmit.setBackgroundColor(context.getResources().getColor(R.color.grass_primary));
            wrongJob.getAttributes().setTheme(FlatUI.GRASS, context.getResources());
            incident.getAttributes().setTheme(FlatUI.GRASS, context.getResources());
            other.getAttributes().setTheme(FlatUI.GRASS, context.getResources());
        } else if (mode == 2) {
            textViewTitleWarning.setText("ข้ามไปรอรับผู้โดยสาร");
            backgroundSubmit.setBackgroundColor(context.getResources().getColor(R.color.sand_primary));
            wrongJob.getAttributes().setTheme(R.array.custom_sand, context.getResources());
            incident.getAttributes().setTheme(R.array.custom_sand, context.getResources());
            other.getAttributes().setTheme(R.array.custom_sand, context.getResources());
        }
        else if (mode == 4) {
            textViewTitleWarning.setText("ข้ามไปผู้โดยสารลงรถ");
            backgroundSubmit.setBackgroundColor(context.getResources().getColor(R.color.sand_primary));
            wrongJob.getAttributes().setTheme(R.array.custom_sand, context.getResources());
            incident.getAttributes().setTheme(R.array.custom_sand, context.getResources());
            other.getAttributes().setTheme(R.array.custom_sand, context.getResources());
        }
        else if (mode == 9) {
            textViewTitleWarning.setText("จบงาน");
            backgroundSubmit.setBackgroundColor(context.getResources().getColor(R.color.candy_primary));
            wrongJob.getAttributes().setTheme(FlatUI.CANDY, context.getResources());
            incident.getAttributes().setTheme(FlatUI.CANDY, context.getResources());
            other.getAttributes().setTheme(FlatUI.CANDY, context.getResources());
        }

        textViewWarning.setText("เมื่อกดตกลง การข้ามขั้นตอนงานจะถูกบันทึก");

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idCheck = radioGroup.getCheckedRadioButtonId();
                if(idCheck > -1){
                    View radioButton = radioGroup.findViewById(idCheck);
                    int index = radioGroup.indexOfChild(radioButton);
                    FlatRadioButton temp = (FlatRadioButton) radioGroup.getChildAt(index);
                    onSubmitDialogListener.onOKSubmit(jobStatus,mode, temp.getText().toString()
                            , cause.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public interface OnSubmitDialogListener {
        void onOKSubmit(int oldJobStatus,int newJobStatus, String text, String cause);
    }
}
