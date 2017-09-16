package com.fma.fmapos.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fma.fmapos.model.BaseModel;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelModifier;
import com.fma.fmapos.model.ModelOrder;
import com.fma.fmapos.model.ModelOrderItem;
import com.fma.fmapos.model.ModelOrderModifier;
import com.fma.fmapos.model.ModelProduct;
import com.fma.fmapos.model.ModelSetting;

/**
 * Created by fmanda on 08/01/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "fmaPOS";
    private static final int DATABASE_VERSION = 1;

    private static DBHelper mInstance;

    public DBHelper(Context context, String name) {
        super(context, name, null, 0);
        this.DATABASE_NAME = name;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(new ModelProduct().generateMetaData());
        db.execSQL(new ModelCustomer().generateMetaData());
        db.execSQL(new ModelModifier().generateMetaData());
        db.execSQL(new ModelOrder().generateMetaData());
        db.execSQL(new ModelOrderItem().generateMetaData());
        db.execSQL(new ModelOrderModifier().generateMetaData());
        db.execSQL(new ModelSetting().generateMetaData());

        //init
        ModelSetting.initMetaData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db);
    }


    public void dummyProduct(SQLiteDatabase db){
        for (int i=1; i<=20; i++){
            ModelProduct prod = new ModelProduct();
            prod.setName("Sample Product " + String.valueOf(i));
            prod.setSku("Kode " + String.valueOf(i));
            prod.setCategory("makanan");
            prod.setPrice((double) Math.round(Math.random()*10000.0));

            if (i%2==0){
                prod.setCategory("minuman");
            }

            for (int j=1; j<=5; j++){
                ModelModifier item = new ModelModifier();
                item.setName("Sample Modifier " + String.valueOf(j));
                item.setPrice((double) Math.round(Math.random()*1000.0));
                prod.addItem(item);
            }
            prod.setTax(10);
            prod.saveToDBAll(db);

            ModelCustomer cust = new ModelCustomer();
            cust.setName("Sample Customer " + String.valueOf(i));
            cust.setCode("Code " + String.valueOf(i));
            cust.setAddress("Sample Customer " + String.valueOf(i));
            cust.setCategory("Customer");
            cust.saveToDB(db);
        };

    }

    public void dropAllTables(SQLiteDatabase db){
        db.execSQL(new ModelProduct().generateDropMetaData());
        db.execSQL(new ModelCustomer().generateDropMetaData());
        db.execSQL(new ModelModifier().generateDropMetaData());
        db.execSQL(new ModelOrder().generateDropMetaData());
        db.execSQL(new ModelOrderItem().generateDropMetaData());
        db.execSQL(new ModelOrderModifier().generateDropMetaData());
        db.execSQL(new ModelSetting().generateDropMetaData());
    }

    public void resetDatabase(SQLiteDatabase db){
        dropAllTables(db);
        onCreate(db);
    }



}
