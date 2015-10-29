package com.cjh.utils;


import com.cjh.cjh_sell.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;


public class LoadingDialog extends ProgressDialog {
    private Context context = null;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }
}
