package com.parthacreation.jbmatrix.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.parthacreation.jbmatrix.activities.MainActivity;

public class MyProgressDialog {
    Context context;
    View baseView ;
    ProgressDialog progressDialog;

    public MyProgressDialog(View baseView) {
        this.context = baseView.getContext();
        this.baseView = baseView;
    }

    public void show(){
        baseView.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Set a message
        progressDialog.setCancelable(false); // Prevent dismiss on touch outside
        progressDialog.show();
    }
    public void dismiss(){
        progressDialog.dismiss();
        baseView.setVisibility(View.VISIBLE);
    }


}
