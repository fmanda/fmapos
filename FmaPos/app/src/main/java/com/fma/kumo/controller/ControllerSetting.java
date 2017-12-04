package com.fma.kumo.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.fma.kumo.helper.DBHelper;
import com.fma.kumo.model.ModelMoneyPreset;
import com.fma.kumo.model.ModelOrderPreset;
import com.fma.kumo.model.ModelSetting;

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
        try {
            List<ModelSetting> settings = new ArrayList<ModelSetting>();

            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cursor = rdb.rawQuery("select * from setting", null);
            while (cursor.moveToNext()) {
                ModelSetting setting = new ModelSetting();
                setting.loadFromCursor(cursor);
                settings.add(setting);
            }
            return settings;
        }catch(Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public List<ModelMoneyPreset> getMoneyPreset(Double greaterThan){
        try {
            List<ModelMoneyPreset> presets = new ArrayList<ModelMoneyPreset>();

            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();

            Cursor cursor = rdb.rawQuery("select * from MoneyPreset", null);
            while (cursor.moveToNext()) {
                ModelMoneyPreset preset = new ModelMoneyPreset(0);
                preset.loadFromCursor(cursor);
                if (preset.getMoneyval() > greaterThan) presets.add(preset);
            }
            return presets;
        }catch(Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public String getCashierPrinter(){
        return getSettingStr("cashier_printer");
    }
    public String getKitchenPrinter(){
        return getSettingStr("kitchen_printer");
    }
    public Boolean IsPrintToKitchen(){
        return Boolean.parseBoolean(getSettingStr( "print_to_kitchen"));
    }
    public Boolean IsPrintToCashier(){
        return Boolean.parseBoolean(getSettingStr( "print_to_kitchen"));
    }

    public void updateSetting(String varname, String varvalue){

        ModelSetting setting = getSetting(varname);
        if (setting != null) setting.setVarvalue(varvalue);
        setting.saveToDB(DBHelper.getInstance(this.context).getWritableDatabase());
    }

    public String getSettingStr(String varname){
        ModelSetting setting = getSetting(varname);
        if (setting != null) {
            return setting.getVarvalue();
        }else{
            return "";
        }
    }

    public Integer getCompanyID(){
        ModelSetting setting = getSetting("company_id");
        if (setting.getVarvalue() != "")  {
            return Integer.parseInt(setting.getVarvalue());
        }else{
            return 0;
        }
    }

    public Integer getUnitID(){
        ModelSetting setting = getSetting( "unit_id");
        if (setting.getVarvalue() != "")  {
            return Integer.parseInt(setting.getVarvalue());
        }else{
            return 0;
        }
    }

    public String getUserName(){
        ModelSetting setting = getSetting( "user_name");
        if (setting.getVarvalue() != "")  {
            return setting.getVarvalue();
        }else{
            return "";
        }
    }

    public String getCompanyName(){
        ModelSetting setting = getSetting( "company_name");
        if (setting.getVarvalue() != "")  {
            return setting.getVarvalue();
        }else{
            return "Unknown Company";
        }
    }

    public String getUnitName(){
        ModelSetting setting = getSetting( "unit_name");
        if (setting.getVarvalue() != "")  {
            return setting.getVarvalue();
        }else{
            return "Unknown Unit";
        }
    }

    public ModelSetting getSetting(String varname){
        try {
            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cursor = rdb.rawQuery("select * from setting where varname = '" + varname + "'", null);
            ModelSetting modelSetting = new ModelSetting(varname, "");
            if (cursor.moveToNext()) {
                modelSetting.loadFromCursor(cursor);
                return modelSetting;
            }
        }catch(Exception ex){
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }
        return new ModelSetting(varname,"");
    }

//    public ModelSetting getSetting(List<ModelSetting> settings, String varname){
//        try {
//            for (ModelSetting setting : settings) {
//                if (setting.getVarname().equals(varname))
//                    return setting;
//            }
//        }catch(Exception ex){
//            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
//        }
//        return new ModelSetting(varname,"");
//    }

    public List<ModelOrderPreset> getOrderPreset(){
        try {
            List<ModelOrderPreset> presets = new ArrayList<ModelOrderPreset>();

            DBHelper db = DBHelper.getInstance(context);
            SQLiteDatabase rdb = db.getReadableDatabase();

            Cursor cursor = rdb.rawQuery("select * from OrderPreset", null);
            while (cursor.moveToNext()) {
                ModelOrderPreset preset = new ModelOrderPreset();
                preset.loadFromCursor(cursor);
                presets.add(preset);
            }
            return presets;
        }catch(Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}

