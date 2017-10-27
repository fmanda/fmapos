package com.fma.fmapos.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.helper.GsonRequest;
import com.fma.fmapos.model.ModelCustomer;

import java.util.List;

/**
 * Created by fma on 10/26/2017.
 */

public class ControllerRest {
    private Context context;
    private DBHelper db;
    final String customer_get_url = "http://10.0.2.1/customerof/405";
    final String customer_post_url = "http://10.0.2.1/customer";
    private int company_id;
    private int unit_id;

    private AppSingleton appSingleton;
    private ControllerCustomer controllerCustomer;
    private Listener listener;

    public ControllerRest(Context context) {
        this.context = context;
        this.db = new DBHelper(context);
        this.appSingleton = AppSingleton.getInstance(context);
        this.controllerCustomer = new ControllerCustomer(context);
        this.company_id = new ControllerSetting(this.context).getCompanyID();
        this.unit_id = new ControllerSetting(this.context).getUnitID();
    }

    public void DownloadCustomers(){
        GsonRequest<ModelCustomer[]> gsonReq = new GsonRequest<ModelCustomer[]>(customer_get_url, ModelCustomer[].class,
            new Response.Listener<ModelCustomer[]>() {
                @Override
                public void onResponse(ModelCustomer[] response) {
                    try {
                        for (ModelCustomer cust : response) {
                            cust.setIDFromUID(db.getReadableDatabase(), cust.getUid());
                            cust.saveToDB(db.getWritableDatabase());
                            listener.onSuccess(cust.getName() + " updated");
                        }
                    }catch(Exception e){
                        listener.onError(e.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    listener.onError(error.toString());
                }
            }
        );
        this.appSingleton.addToRequestQueue(gsonReq,customer_get_url);
    }

    public void UploadCustomers(){
        ControllerCustomer controllerCustomer = new ControllerCustomer(this.context);
        List<ModelCustomer> customers = controllerCustomer.getCustomerList();
        for (final ModelCustomer cust : customers) {
            cust.setCompany_id(this.company_id);
            cust.setUnit_id(this.unit_id);

            //test
            GsonRequest<ModelCustomer> gsonReq = new GsonRequest<ModelCustomer>(customer_post_url, cust,
                new Response.Listener<ModelCustomer>() {
                    @Override
                    public void onResponse(ModelCustomer response) {
                        //update ID
                        try{
                            cust.setUid(response.getUid());
                            cust.saveToDB(db.getWritableDatabase());
                            listener.onSuccess(response.getName() + " updated");
                        }catch (Exception e){
                            listener.onError(e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                    }
                }
            );
            this.appSingleton.addToRequestQueue(gsonReq,customer_post_url);
        }

    }

    public interface Listener {
        void onSuccess(String msg);
        void onError(String msg);
    }

    public void setListener(ControllerRest.Listener listener) {
        this.listener = listener;
    }
}
