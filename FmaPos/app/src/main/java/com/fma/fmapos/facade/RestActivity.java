package com.fma.fmapos.facade;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fma.fmapos.R;
import com.fma.fmapos.helper.AppSingleton;
import com.fma.fmapos.helper.GsonRequest;
import com.fma.fmapos.model.ModelCustomer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;


/**
 * Created by fma on 7/30/2017.
 */

public class RestActivity extends BaseActivity {

    Button btnRestText;
    Button btnRestObject;
    Button btnPostObject;
    ProgressBar progressBar;
    ModelCustomer customer;
    Gson gson;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_rest, this.mainframe);

        btnRestText = (Button) findViewById(R.id.btnRestText);
        btnPostObject = (Button) findViewById(R.id.btnPostObject);
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
                testRestObject("http://10.0.2.1/customer/4");
            }
        });

        btnPostObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetPostObject("http://10.0.2.1/customer");
            }
        });

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-mm-dd hh:mm:ss");
        gson = gsonBuilder.create();

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
        GsonRequest<ModelCustomer> gsonReq = new GsonRequest<ModelCustomer>(url, ModelCustomer.class,
            new Response.Listener<ModelCustomer>() {
                @Override
                public void onResponse(ModelCustomer response) {
                    customer = response;
                    Toast.makeText(RestActivity.this, response.getName(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );


        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(gsonReq, REQUEST_TAG);
    }

    private void tetPostObject(String url){
        final String REQUEST_TAG = "com.fmapos.volleyObjectPost";
        if (customer == null) {
            Toast.makeText(this, "Customer = null", Toast.LENGTH_SHORT).show();
            return;
        }

        customer.setName("Edited From Android");

        progressBar.setVisibility(View.VISIBLE);
        GsonRequest<ModelCustomer> gsonReq = new GsonRequest<ModelCustomer>(url, customer,
            new Response.Listener<ModelCustomer>() {
                @Override
                public void onResponse(ModelCustomer response) {
                    Toast.makeText(RestActivity.this, response.getName(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(gsonReq, REQUEST_TAG);
    }


}

