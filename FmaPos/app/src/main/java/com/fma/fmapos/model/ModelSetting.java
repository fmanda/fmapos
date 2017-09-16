package com.fma.fmapos.model;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;

/**
 * Created by fma on 8/11/2017.
 */

public class ModelSetting extends BaseModel {
    @TableField
    private String varname;
    @TableField
    private String varvalue;

    public ModelSetting(String varname, String varvalue){
        this.varname = varname;
        this.varvalue = varvalue;
    }

    public ModelSetting() {

    }

    public String getVarname() {
        return varname;
    }

    public void setVarname(String varname) {
        this.varname = varname;
    }

    public String getVarvalue() {
        return varvalue;
    }

    public void setVarvalue(String varvalue) {
        this.varvalue = varvalue;
    }

    public static void initMetaData(SQLiteDatabase db) {
        new ModelSetting("cashier_printer","").saveToDB(db);
    }
}
