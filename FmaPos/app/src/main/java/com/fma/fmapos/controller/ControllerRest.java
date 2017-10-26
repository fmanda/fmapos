package com.fma.fmapos.controller;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fma.fmapos.facade.RestActivity;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.helper.GsonRequest;
import com.fma.fmapos.model.ModelCustomer;

/**
 * Created by fma on 10/26/2017.
 */

public class ControllerRest {
    private Context context;
    private DBHelper db;
    final String customer_get_url = "http://10.0.2.1/customerof/405";
    private AppSingleton appSingleton;
    private ControllerCustomer controllerCustomer;

    public ControllerRest(Context context) {
        this.context = context;
        this.db = new DBHelper(context);
        this.appSingleton = AppSingleton.getInstance(context);
        this.controllerCustomer = new ControllerCustomer(context);
    }

    public void UpdateCustomers(){
        GsonRequest<ModelCustomer[]> gsonReq = new GsonRequest<ModelCustomer[]>(customer_get_url, ModelCustomer[].class,
            new Response.Listener<ModelCustomer[]>() {
                @Override
                public void onResponse(ModelCustomer[] response) {

                    for (ModelCustomer cust : response){
                        cust.setIDFromUID(db.getReadableDatabase(), cust.getUID());
                        cust.saveToDB(db.getWritableDatabase());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        );
        this.appSingleton.addToRequestQueue(gsonReq,customer_get_url);

    }

}
