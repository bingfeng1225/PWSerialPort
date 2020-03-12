package cn.haier.bio.medical.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class DTEConfigDialog extends Dialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener {
    public DTEConfigDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_dte_config);
        this.setCancelable(false);
        this.setOnDismissListener(this);
        this.setOnShowListener(this);
    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }
}
