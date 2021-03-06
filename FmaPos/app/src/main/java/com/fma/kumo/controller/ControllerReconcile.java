package com.fma.kumo.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.model.ModelCashTrans;
import com.fma.kumo.model.ModelCustomer;
import com.fma.kumo.model.ModelOrder;
import com.fma.kumo.model.ModelReconcile;
import com.fma.kumo.model.ModelSetting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerReconcile {
    private Context context;

    public ControllerReconcile(Context context) {
        this.context = context;
    }

    public ModelCashTrans getCashTrans(int id){
        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cursor = rdb.rawQuery("select * from cashtrans where id = " + String.valueOf(id), null);
        if (cursor.moveToNext()){
            ModelCashTrans cashTrans = new ModelCashTrans();
            cashTrans.loadFromCursor(cursor);
            return cashTrans;
        }
        return null;
    }

    public ModelCashTrans getCashTrans(String uid){
        DBHelper db = DBHelper.getInstance(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cursor = rdb.rawQuery("select * from cashtrans where uid = " + String.valueOf(uid), null);
        if (cursor.moveToNext()){
            ModelCashTrans cashTrans = new ModelCashTrans();
            cashTrans.loadFromCursor(cursor);
            return cashTrans;
        }
        return null;
    }

    public List<ModelCashTrans> getCashTransList(int reconcile_id){
        List<ModelCashTrans> cashtrans = new ArrayList<ModelCashTrans>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String sql = "select * from cashtrans where reconcile_id = " + String.valueOf(reconcile_id);

        if (reconcile_id == 0) {
            sql += " or reconcile_id is null";
        }

        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelCashTrans cashTrans = new ModelCashTrans();
            cashTrans.loadFromCursor(cursor);
            cashtrans.add(cashTrans);
        }
        return cashtrans;
    }

    public List<ModelReconcile> getReconcileList(){
        List<ModelReconcile> reconcileList = new ArrayList<ModelReconcile>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();

        String sql = "select * from reconcile order by id desc limit 10";
        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelReconcile modelReconcile  = new ModelReconcile();
            modelReconcile.loadFromCursor(cursor);
            reconcileList.add(modelReconcile);
        }
        return reconcileList;
    }

    public List<ModelOrder> getOrderList(){
        List<ModelOrder> orders = new ArrayList<ModelOrder>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();

        String sql = "select * from orders where reconcile_id is null or reconcile_id = 0";
        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelOrder modelOrder  = new ModelOrder();
            modelOrder.loadFromCursor(cursor);
            orders.add(modelOrder);
        }
        return orders;
    }

    public List<ModelReconcile> getModifiedReconcile(){
        List<ModelReconcile> reconcileList = new ArrayList<ModelReconcile>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();

        String sql = "select * from reconcile where uploaded is null or uploaded = 0";
        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelReconcile modelReconcile  = new ModelReconcile();
            modelReconcile.loadFromCursor(cursor);
            reconcileList.add(modelReconcile);
        }
        return reconcileList;
    }

    public List<ModelCashTrans> getModifiedCashTrans(){
        List<ModelCashTrans> cashtrans = new ArrayList<ModelCashTrans>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String sql = "select * from cashtrans where uploaded is null or uploaded = 0";
        Cursor cursor = rdb.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelCashTrans cashTrans = new ModelCashTrans();
            cashTrans.loadFromCursor(cursor);
            cashtrans.add(cashTrans);
        }
        return cashtrans;
    }

}
