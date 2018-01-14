package com.fma.kumo.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

/**
 * Created by fma on 8/11/2017.
 */

public class ModelCashTrans extends BaseModel {
    @TableField
    private String uid;
    @TableField
    private Integer company_id;
    @TableField
    private Integer unit_id;
    @TableField
    private Date transdate = new Date();
    @TableField
    private Double amount ;
    @TableField
    private String notes;
    @TableField
    private Integer reconcile_id;
    @TableField
    private Integer uploaded;

    private String reconcile_uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Date getTransdate() {
        return transdate;
    }

    public void setTransdate(Date transdate) {
        this.transdate = transdate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getReconcile_id() {
        return reconcile_id;
    }

    public void setReconcile_id(Integer reconcile_id) {
        this.reconcile_id = reconcile_id;
    }

    public void prepareUpload(SQLiteDatabase db){
        if (reconcile_id > 0) {
            ModelReconcile modelReconcile = new ModelReconcile();
            modelReconcile.loadFromDB(db, this.reconcile_id);
            this.reconcile_uid = modelReconcile.getUid();
        }

    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }
}
