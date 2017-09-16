package com.fma.fmapos.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.model.ModelOrder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerOrder {
    private Context context;

    public ControllerOrder(Context context) {
        this.context = context;
    }


    public List<ModelOrder> getOrderList(){
        List<ModelOrder> orders = new ArrayList<ModelOrder>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cursor = rdb.rawQuery("select * from orders limit 100", null);
        while (cursor.moveToNext()){
            ModelOrder modelOrder = new ModelOrder();
            modelOrder.loadFromCursor(cursor);
            orders.add(modelOrder);
        }
        return orders;
    }

    public String generateNewNumber(){
        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        String newNumber = "";

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date d = c.getTime();

        int iorderno = 0;

        newNumber += String.valueOf(c.get(Calendar.YEAR));
        newNumber = newNumber.substring(newNumber.length()-2);
        newNumber += String.valueOf(c.get(Calendar.MONTH));

        try {
            String sql = "select max(orderno) from orders where orderdate >= " + String.valueOf(d.getTime());
            Cursor cursor = rdb.rawQuery("select max(orderno) from orders ", null);
            if (cursor.moveToNext()) {
                String str = cursor.getString(0);
                //str = str.replace(newNumber,"");
                str = str.substring(str.length()-4);
                if (!str.equals("")) iorderno = Integer.parseInt(str);
            }
        }
        catch(Exception e){
        }

        iorderno++;
        newNumber += String.format("%04d", iorderno);
        return newNumber;

    }
}
