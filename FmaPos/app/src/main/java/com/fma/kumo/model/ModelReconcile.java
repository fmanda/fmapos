package com.fma.kumo.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;
import java.util.List;

/**
 * Created by fma on 8/11/2017.
 */

public class ModelReconcile extends BaseModel {
    @TableField
    private String uid;
    @TableField
    private Integer company_id;
    @TableField
    private Integer unit_id;
    @TableField
    private Date transdate = new Date();
    @TableField
    private Double sales_amount = 0.0;
    @TableField
    private Double void_amount = 0.0;
    @TableField
    private Double cash_amount = 0.0;
    @TableField
    private Double card_amount = 0.0;
    @TableField
    private Double cash_in = 0.0;
    @TableField
    private Double cash_out = 0.0;
    @TableField
    private String notes;
    @TableField
    private Integer counter;
    @TableField
    private Integer total_order;
    @TableField
    private String user_name;
    @TableField
    private Integer uploaded;

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

    public Double getSales_amount() {
        return sales_amount;
    }

    public void setSales_amount(Double sales_amount) {
        this.sales_amount = sales_amount;
    }

    public Double getVoid_amount() {
        return void_amount;
    }

    public void setVoid_amount(Double void_amount) {
        this.void_amount = void_amount;
    }

    public Double getCash_amount() {
        return cash_amount;
    }

    public void setCash_amount(Double cash_amount) {
        this.cash_amount = cash_amount;
    }

    public Double getCard_amount() {
        return card_amount;
    }

    public void setCard_amount(Double card_amount) {
        this.card_amount = card_amount;
    }

    public Double getCash_in() {
        return cash_in;
    }

    public void setCash_in(Double cash_in) {
        this.cash_in = cash_in;
    }

    public Double getCash_out() {
        return cash_out;
    }

    public void setCash_out(Double cash_out) {
        this.cash_out = cash_out;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getTotal_order() {
        return total_order;
    }

    public void setTotal_order(Integer total_order) {
        this.total_order = total_order;
    }

    public void saveToDBAll(SQLiteDatabase db, List<ModelOrder> orderList, List<ModelCashTrans> cashTransList) {
        Log.d("DB","Reconcile.saveToDBAll");

        this.UpdateOrderBalance(orderList, cashTransList);

        db.beginTransaction();
        try {
            super.saveToDB(db ,true);

            for (ModelOrder modelOrder : orderList){
                modelOrder.setReconcile_id(this.id);
                modelOrder.saveToDB(db);
            }

            for (ModelCashTrans modelCashTrans : cashTransList){
                modelCashTrans.setReconcile_id(this.id);
                modelCashTrans.saveToDB(db);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void UpdateOrderBalance(List<ModelOrder> orderList, List<ModelCashTrans> cashTransList){
        for (ModelOrder modelOrder : orderList){
            if (modelOrder.getAmount() > 0){
                this.setSales_amount(this.getSales_amount() + modelOrder.getAmount());
            }else{
                this.setVoid_amount(this.getVoid_amount() + modelOrder.getAmount());
            }
        }

        for (ModelCashTrans modelCashTrans : cashTransList){
            if (modelCashTrans.getAmount() > 0){
                this.setCash_in(this.getCash_in() + modelCashTrans.getAmount());
            }else{
                this.setCash_out(this.getCash_out() + modelCashTrans.getAmount());
            }
        }
    }

    public Double getSysIncome(){
        return sales_amount + void_amount + cash_in + cash_out;
    }

    public Double getActIncome(){
        return cash_amount + card_amount;
    }

    public Double getVariant(){
        return getSysIncome() - getActIncome();
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }
}
