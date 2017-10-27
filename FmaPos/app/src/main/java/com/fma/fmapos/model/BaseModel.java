package com.fma.fmapos.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fma.fmapos.helper.ModelHelper;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by fmanda on 08/02/17.
 */


public class BaseModel implements Serializable {
    @TableField
    protected int id = 0;
    public boolean restclient = true; //always true, for post rest data

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName(){
        String str = this.getClass().getSimpleName().toLowerCase();
        str = str.replace("model", "");
        return str;
    }

    public String generateSQL(){
        String str = ModelHelper.generateSQL(this);
        return str;
    }

    public String generateSQLDelete(){
        return generateSQLDelete("where id = " + String.valueOf(this.id) );
    }

    public String generateSQLDelete(String filter){
        return "delete from " + this.getTableName() + " " + filter + ";";
    }

    public String generateMetaData(){
        String str = ModelHelper.generateMetaData(this);
        return str;
    }

    public String generateDropMetaData(){
        String str = ModelHelper.generateDropMetaData(this);
        return str;
    }

    public static String generateUID(){
        return UUID.randomUUID().toString();
    }

    public boolean loadFromCursor(Cursor cursor){
        return ModelHelper.loadFromCursor(this, cursor);
    }

    public void copyObject(BaseModel source){
        ModelHelper.copyObject(source, this);
    }

    public String debugToString(){
        return ModelHelper.debugToString(this);
    }

    public void saveToDB(SQLiteDatabase db, Boolean isSetID){
        this.saveToDB(db);
        if (id == 0 && isSetID){
            Cursor cur = db.rawQuery("SELECT last_insert_rowid()", null);
            if (cur.moveToNext()) setId(cur.getInt(0));
        }
    }

    public void setIDFromUID(SQLiteDatabase db, String uid){
        String sql = "select id from " + this.getTableName() + " where uid = '"  + uid + "'";
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToNext()) {
            this.setId(cur.getInt(0));
        }else{
            this.setId(0);
        }

    }

    public void saveToDB(SQLiteDatabase db){
        db.execSQL(this.generateSQL());
    }

    public void deleteFromDB(SQLiteDatabase db)
    {
        db.execSQL(this.generateSQLDelete());
    }

}
