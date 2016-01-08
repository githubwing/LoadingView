package com.wingsofts.loadingview;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
public class MainActivity extends Activity {
    private LoadingView mLoadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void showDialog(View v){
        new AlertDialog.Builder(this).setView(R.layout.layout).setNegativeButton("OK",null).create().show();

    }
}
