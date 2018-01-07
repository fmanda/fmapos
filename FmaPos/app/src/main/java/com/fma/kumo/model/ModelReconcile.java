package com.fma.kumo.model;

import java.util.Date;

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
    private Double sales_amount;
    @TableField
    private Double void_amount;
    @TableField
    private Double cash_amount;
    @TableField
    private Double card_amount;
    @TableField
    private Double cash_in;
    @TableField
    private Double cash_out;
    @TableField
    private String notes;
    @TableField
    private Integer counter;
    @TableField
    private String user_name;

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
}
