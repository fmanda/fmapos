package com.fma.kumo.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.helper.RandomString;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelOrderCategory;
import com.fma.kumo.model.ModelOrderItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerOrder {
    private Context context;

    public ControllerOrder(Context context) {
        this.context = context;
    }


    public List<ModelOrder> getOrderList(Boolean isHoldOrderOnly){
        ControllerCustomer controllerCustomer = new ControllerCustomer(context);
        List<ModelOrder> orders = new ArrayList<ModelOrder>();

        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String sql = "select * from orders";
        if (isHoldOrderOnly) sql = "select * from orders where status=0 ";
        sql += " order by orderdate desc limit 1000";

        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelOrder modelOrder = new ModelOrder();
            modelOrder.loadFromCursor(cursor);
            if (modelOrder.getCustomer_id()>0){
                modelOrder.setCustomer(controllerCustomer.getCustomer(modelOrder.getCustomer_id()));
            }
            orders.add(modelOrder);
        }
        return orders;
    }


    public String generateNewNumber(){
//        Date date = new Date();
//        Integer idt = (int) date.getTime();
//        Calendar c = Calendar.getInstance();
//        Integer idt = c.get(Calendar.DATE);

        RandomString gen = new RandomString(12, ThreadLocalRandom.current());
        return  gen.nextString();
    }

    public String generateCounter(){
        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String newNumber = "";

        Calendar c = Calendar.getInstance();   // this takes current date
        //c.set(Calendar.DAY_OF_MONTH, 1);
        Date d = c.getTime();

        int iorderno = 0;
        try {
            String sql = "select max(orderno) from orders where orderdate >= " + String.valueOf(d.getTime());
            Cursor cursor = rdb.rawQuery("select max(orderno) from orders ", null);
            if (cursor.moveToNext()) {
                String str = cursor.getString(0);
                if (!str.equals("")) iorderno = Integer.parseInt(str);
            }
        }
        catch(Exception e){
        }

        iorderno++;
        newNumber += String.format("%05d", iorderno);
        return newNumber;

    }

    public List<ModelOrderCategory> getOrderCategory(){
        List<ModelOrderCategory> cats = new ArrayList<ModelOrderCategory>();
        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String sql = "select * from ordercategory";

        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelOrderCategory modelOrderCategory = new ModelOrderCategory();
            modelOrderCategory.loadFromCursor(cursor);

            cats.add(modelOrderCategory);
        }
        return cats;
    }

    public void CreateVoid(ModelOrder modelOrder){
        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        SQLiteDatabase wdb = db.getWritableDatabase();

//        modelOrder.reLoadAll(rdb);

        //old order
        modelOrder.setStatus(2);
        modelOrder.setPayment(0.0);
        modelOrder.setCashpayment(0.0);
        modelOrder.setCardpayment(0.0);
        modelOrder.setChange(0.0);
        modelOrder.saveToDB(wdb);

        //void order
        modelOrder.reLoadAll(rdb);
        modelOrder.setId(0);
        modelOrder.setOrderdate(new Date());
        modelOrder.setAmount(modelOrder.getAmount() * -1);
        for (ModelOrderItem modelOrderItem : modelOrder.getItems()){
            modelOrderItem.setQty(modelOrderItem.getQty() * -1);
        }
        modelOrder.saveToDBAll(wdb);
    }
}
