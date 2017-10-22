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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fma.fmapos.R;
import com.fma.fmapos.helper.AppSingleton;

import org.json.JSONObject;


/**
 * Created by fma on 7/30/2017.
 */

public class RestActivity extends BaseActivity {

    Button btnRestText;
    Button btnRestObject;
    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rest, this.mainframe);

        btnRestText = (Button) findViewById(R.id.btnRestText);
        btnRestObject = (Button) findViewById(R.id.btnRestObject);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnRestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRestText("http://10.0.2.1/company");
            }
        });

        btnRestObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRestObject("http://10.0.2.1/company/405");
            }
        });

    }

    private void testRestText(String url) {
        final String REQUEST_TAG = "com.fmapos.volleyStringRequest";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(RestActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(REQUEST_TAG, error.toString());
                    Toast.makeText(RestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }

    private void testRestObject(String url) {
        final String REQUEST_TAG = "com.fmapos.volleyObjectRequest";
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonReq = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(RestActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(REQUEST_TAG, error.toString());
                    Toast.makeText(RestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        );

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonReq, REQUEST_TAG);
    }



}

