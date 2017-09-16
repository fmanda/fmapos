package com.fma.fmapos.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fma.fmapos.helper.DBHelper;
import com.fma.fmapos.model.ModelCustomer;
import com.fma.fmapos.model.ModelSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fmanda on 07/31/17.
 */

public class ControllerSetting {
    private Context context;

    public ControllerSetting(Context context) {
        this.context = context;
    }


    public List<ModelSetting> getSettings(){
        List<ModelSetting> settings = new ArrayList<ModelSetting>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor cursor = rdb.rawQuery("select * from setting", null);
        while (cursor.moveToNext()){
            ModelSetting setting = new ModelSetting();
            setting.loadFromCursor(cursor);
            settings.add(setting);
        }
        return settings;
    }

    public String getCashierPrinter(){
        String str = "";
        List<ModelSetting> settings = this.getSettings();
        for (ModelSetting setting : settings) {
            if (setting.getVarname().equals("cashier_printer")){
                str = setting.getVarvalue();
            }
        }
        return str;
    }
}
