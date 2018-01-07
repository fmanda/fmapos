package com.fma.kumo.facade;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fma.kumo.R;
import com.fma.kumo.controller.ControllerRest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by fma on 7/30/2017.
 */

public class RestActivity extends BaseActivity {

    Button btnDownload;
    Button btnUpload;
    Button btnSync;
    ProgressBar progressBar;
    TextView txtLog;
    Gson gson;
    ControllerRest controllerRest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rest, this.mainframe);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnSync = (Button) findViewById(R.id.btnSync);
        txtLog = (TextView) findViewById(R.id.txtLog);
        txtLog.setText("");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        controllerRest = new ControllerRest(this.getApplicationContext());

        controllerRest.setListener(new ControllerRest.Listener() {
            @Override
            public void onSuccess(String msg) {
                progressBar.setProgress(progressBar.getProgress()+1);
                txtLog.append(msg + "\n");
            }

            @Override
            public void onError(String msg) {
                progressBar.setProgress(progressBar.getProgress()+1);
                txtLog.append(msg + "\n");
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLog.setText("");
                controllerRest.DownloadAll();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setMax(20);
                progressBar.setProgress(0);
                txtLog.setText("");
                controllerRest.UploadAll();
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controllerRest.SyncData();
            }
        });

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gson = gsonBuilder.create();

    }

}

