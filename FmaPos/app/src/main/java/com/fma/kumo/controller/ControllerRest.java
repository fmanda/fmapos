package com.fma.kumo.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.helper.GsonRequest;
import com.fma.kumo.helper.ImageHelper;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelOrderCategory;
import com.fma.kumo.model.ModelProduct;
import com.fma.kumo.model.ModelReconcile;

import org.json.JSONObject;

import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

/**
 * Created by fma on 10/26/2017.
 */

public class ControllerRest {
    private Context context;
    private DBHelper db;
    private ControllerSetting controllerSetting;

    public String base_url;

//    private static ControllerRest mInstance;
//
//    public static synchronized ControllerRest getInstance(Context context) {
//        if (mInstance == null) {
//            mInstance = new ControllerRest(context.getApplicationContext());
//        }
//        return mInstance;
//    }

//    final String customer_get_url = "http://10.0.2.1/customerof/405";
//    final String customer_post_url = "http://10.0.2.1/customer";
//    final String product_get_url = "http://10.0.2.1/productof/405/1";
//    final String product_post_url = "http://10.0.2.1/productclient";
//    final String ordercategory_get_url = "http://10.0.2.1/ordercategoryof/405";


    private int company_id;
    private int unit_id;

    private ControllerRequest controllerRequest;
    private ControllerCustomer controllerCustomer;
    private Listener listener;

    public String getURLCompanyByUser(){
        return (base_url + "companybyuser");
    }

    public String getURLUnits(){
        return base_url + "unitsof";
    }

    public String customer_get_url(){
        return base_url + "customerof/" + this.company_id;
    }

    public String customer_post_url(){
        return base_url + "customer";
    }

    public String product_get_url(){
        return base_url + "productof/" + this.company_id + "/" + this.unit_id;
    }

    public String ordercategory_get_url(){
        return base_url + "ordercategoryof/" + this.company_id ;
    }

    public String order_post_url(){
        return base_url + "orderfromclient";
    }

    public String cashtrans_post_url(){
        return base_url + "cashtransfromclient";
    }

    public String reconcile_post_url(){
        return base_url + "reconcilefromclient";
    }
    public void DownloadAll(){
        this.DownloadProducts();
        this.DownloadCustomers();
        this.DownloadOrderCategory();
    }

    public String image_get_url(String uid){
        return base_url + "upload/" + uid + ".jpg";
    }

    public void UploadAll(){
        //mater first;
//        this.UploadProducts(); //product tidak di upload
        this.UploadReconcile();
        this.UploadCashTrans();
        this.UploadCustomers();
        this.UploadOrders();
    }

    public void SyncData(){
        //reconcile
        this.UploadReconcile();
        this.UploadCashTrans();

        //master
        this.DownloadProducts();
        this.DownloadOrderCategory();
        this.UploadCustomers(); //modified customer only
        this.DownloadCustomers();
        this.UploadOrders();
    }



    public interface Listener {
        void onSuccess(String msg);
        void onError(String msg);
    }

    public void setListener(ControllerRest.Listener listener) {
        this.listener = listener;
    }

    public ControllerRest(Context context) {
        this.context = context;
        this.db = DBHelper.getInstance(context);
        this.controllerRequest = ControllerRequest.getInstance(context);
        this.controllerCustomer = new ControllerCustomer(context);
        this.company_id = new ControllerSetting(this.context).getCompanyID();
        this.unit_id = new ControllerSetting(this.context).getUnitID();
        this.controllerSetting = new ControllerSetting((this.context));
        this.base_url = "http://" + controllerSetting.getSettingStr("rest_url") + "/";
    }

    //CUSTOMER

    public void DownloadCustomers(){
        GsonRequest<ModelCustomer[]> gsonReq = new GsonRequest<ModelCustomer[]>(customer_get_url(), ModelCustomer[].class,
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
        this.controllerRequest.addToRequestQueue(gsonReq,customer_get_url());
    }

    public void UploadCustomers(){
        ControllerCustomer controllerCustomer = new ControllerCustomer(this.context);
        List<ModelCustomer> customers = controllerCustomer.getModifiedCustomerList();
        for (final ModelCustomer cust : customers) {
            cust.setCompany_id(this.company_id);
            cust.setUnit_id(this.unit_id);

            //test
            GsonRequest<ModelCustomer> gsonReq = new GsonRequest<ModelCustomer>(customer_post_url(), cust,
                new Response.Listener<ModelCustomer>() {
                    @Override
                    public void onResponse(ModelCustomer response) {
                        //update ID
                        try{
                            cust.setUid(response.getUid());
                            cust.setIs_modified(0);
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
            this.controllerRequest.addToRequestQueue(gsonReq,customer_post_url());
        }

    }

    public void DownloadOrderCategory(){
        GsonRequest<ModelOrderCategory[]> gsonReq = new GsonRequest<ModelOrderCategory[]>(ordercategory_get_url(), ModelOrderCategory[].class,
            new Response.Listener<ModelOrderCategory[]>() {
                @Override
                public void onResponse(ModelOrderCategory[] response) {
                    ControllerOrder controllerOrder = new ControllerOrder(context);
                    try {
                        for (ModelOrderCategory cat : response) {
                            cat.setIDFromUID(db.getReadableDatabase(), cat.getUid());
                            cat.saveToDB(db.getWritableDatabase());
                            listener.onSuccess(cat.getName() + " updated");
                        }

                        //delete others
                        List<ModelOrderCategory> existingCategories = controllerOrder.getOrderCategory();


                        for (ModelOrderCategory existingCat : existingCategories){
                            Boolean isfound = false;
                            for (ModelOrderCategory cat : response) {
                                isfound = cat.getUid().equals(existingCat.getUid());
                                if (isfound) break;
                            }
                            if (!isfound)
                                existingCat.deleteFromDB(db.getWritableDatabase());
                        }
                    }catch(Exception e){
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
        this.controllerRequest.addToRequestQueue(gsonReq,ordercategory_get_url());
    }

    //PRODUCT
    public void DownloadProducts(){
        GsonRequest<ModelProduct[]> gsonReq = new GsonRequest<ModelProduct[]>(product_get_url(), ModelProduct[].class,
            new Response.Listener<ModelProduct[]>() {
                @Override
                public void onResponse(ModelProduct[] response) {
                    try {
                        ImageHelper img = new ImageHelper(context);
                        for (ModelProduct prod : response) {
                            prod.setIDFromUID(db.getReadableDatabase(), prod.getUid());
                            prod.setImg(prod.getImg().replace(".jpg","")); //server contain .jpg
                            prod.saveToDBAll(db.getWritableDatabase());

                            //replace format to png
                            if (!prod.getImg().equals("")) {
                                img.setFileName(prod.getImg());
                                if (!img.checFileExist()) {
                                    DownloadProductImage(prod.getImg());
                                }
                            }
                            listener.onSuccess(prod.getName() + " updated");
                        }
                    }catch(Exception e){
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
        this.controllerRequest.addToRequestQueue(gsonReq,product_get_url());
    }


    public void UploadOrders(){
        ControllerOrder controllerorder = new ControllerOrder(this.context);
        List<ModelOrder> orders = controllerorder.getModifiedOrders();
        final SQLiteDatabase db = DBHelper.getInstance(this.context).getReadableDatabase();
        final SQLiteDatabase dbw = DBHelper.getInstance(this.context).getWritableDatabase();

        //tambahkan yg diedit saja

//        GsonBuilder gsonBuilder;
//        Gson gson;
//        gsonBuilder = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss");
//        gson = gsonBuilder.create();

        for (final ModelOrder modelOrder : orders) {
            modelOrder.prepareUpload(db);
            modelOrder.setCompany_id(this.company_id);
            modelOrder.setUnit_id(this.unit_id);
//            Toast.makeText(context, debug, Toast.LENGTH_SHORT).show();

            GsonRequest<ModelOrder> gsonReq = new GsonRequest<ModelOrder>(order_post_url(), modelOrder,
                new Response.Listener<ModelOrder>() {
                    @Override
                    public void onResponse(ModelOrder response) {
                        //update ID
                        try{
                            modelOrder.setuid(response.getuid());
                            modelOrder.setUploaded(1);
                            modelOrder.saveToDB(dbw);
                            listener.onSuccess(response.getOrderno() + " updated");
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
            this.controllerRequest.addToRequestQueue(gsonReq,customer_post_url());
        }

        //test


    }


    public void UploadReconcile(){
        ControllerReconcile controllerReconcile = new ControllerReconcile(this.context);
        List<ModelReconcile> reconcileList = controllerReconcile.getModifiedReconcile();
        final SQLiteDatabase db = DBHelper.getInstance(this.context).getReadableDatabase();
        final SQLiteDatabase dbw = DBHelper.getInstance(this.context).getWritableDatabase();

        for (final ModelReconcile modelReconcile : reconcileList) {
            modelReconcile.setCompany_id(this.company_id);
            modelReconcile.setUnit_id(this.unit_id);
//            Toast.makeText(context, debug, Toast.LENGTH_SHORT).show();

            GsonRequest<ModelReconcile> gsonReq = new GsonRequest<ModelReconcile>(reconcile_post_url(), modelReconcile,
                    new Response.Listener<ModelReconcile>() {
                        @Override
                        public void onResponse(ModelReconcile response) {
                            //update ID
                            try{
                                modelReconcile.setUid(response.getUid());
                                modelReconcile.setUploaded(1);
                                modelReconcile.saveToDB(dbw);
                                listener.onSuccess(response.getUid() + " updated");
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
            this.controllerRequest.addToRequestQueue(gsonReq,customer_post_url());
        }

        //test


    }


    public void UploadCashTrans(){
        ControllerReconcile controllerReconcile = new ControllerReconcile(this.context);
        List<ModelCashTrans> cashTransList = controllerReconcile.getModifiedCashTrans();
        final SQLiteDatabase db = DBHelper.getInstance(this.context).getReadableDatabase();
        final SQLiteDatabase dbw = DBHelper.getInstance(this.context).getWritableDatabase();

        for (final ModelCashTrans modelCashTrans : cashTransList) {
            modelCashTrans.setCompany_id(this.company_id);
            modelCashTrans.setUnit_id(this.unit_id);
            modelCashTrans.prepareUpload(db);
//            Toast.makeText(context, debug, Toast.LENGTH_SHORT).show();

            GsonRequest<ModelCashTrans> gsonReq = new GsonRequest<ModelCashTrans>(cashtrans_post_url(), modelCashTrans,
                    new Response.Listener<ModelCashTrans>() {
                        @Override
                        public void onResponse(ModelCashTrans response) {
                            //update ID
                            try{
                                modelCashTrans.setUid(response.getUid());
                                modelCashTrans.setUploaded(1);
                                modelCashTrans.saveToDB(dbw);
                                listener.onSuccess(response.getUid() + " updated");
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
            this.controllerRequest.addToRequestQueue(gsonReq,customer_post_url());
        }

        //test


    }


    public void DownloadProductImage(final String img_name){
        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        String get_url = image_get_url(img_name);

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                get_url, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        ImageHelper img = new ImageHelper(context);
                        img.setFileName(img_name);
                        img.save(response);
                    }
                },
                0, // Image width
                0, // Image height
                CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        listener.onError(error.toString());
//                        error.printStackTrace();
                    }
                }
        );

        this.controllerRequest.addToRequestQueue(imageRequest,image_get_url(img_name));
    }
}
