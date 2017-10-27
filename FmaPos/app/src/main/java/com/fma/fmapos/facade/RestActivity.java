package com.fma.fmapos.facade;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fma.fmapos.R;
import com.fma.fmapos.controller.AppSingleton;
import com.fma.fmapos.controller.ControllerRest;
import com.fma.fmapos.helper.GsonRequest;
import com.fma.fmapos.model.ModelCustomer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ResourceBundle;


/**
 * Created by fma on 7/30/2017.
 */

public class RestActivity extends BaseActivity {

    Button btnDownload;
    Button btnUpload;
    ProgressBar progressBar;
    TextView txtLog;
    Gson gson;
    ControllerRest controllerRest;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rest, this.mainframe);

        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        txtLog = (TextView) findViewById(R.id.txtLog);
        txtLog.setText("");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        controllerRest = new ControllerRest(this.getApplicationContext());

        controllerRest.setListener(new ControllerRest.Listener() {
            @Override
            public void onSuccess(String msg) {
                txtLog.append(msg + "\n");
            }

            @Override
            public void onError(String msg) {
                txtLog.append(msg + "\n");
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLog.setText("");
                controllerRest.DownloadCustomers();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLog.setText("");
                controllerRest.UploadCustomers();
            }
        });

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd hh:mm:ss");
        gson = gsonBuilder.create();

    }

}

