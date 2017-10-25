package com.fma.fmapos.model;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fma on 7/30/2017.
 */

public class ModelOrder extends BaseModel implements Serializable{
    @TableField
    private String orderno = "<blank>";
    @TableField
    private int customer_id;
    @TableField
    private Date orderdate = new Date();
    @TableField
    private Double amount = 0.0;
    @TableField
    private Double payment = 0.0;
    @TableField
    private Double cashpayment = 0.0;
    @TableField
    private Double cardpayment = 0.0;
    @TableField
    private Double change = 0.0;
    @TableField
    private String uuid;
    @TableField
    private int uploaded;
    @TableField
    private int status = 0; //0:created/hold, 1:paid, 2:void

    private List<ModelOrderItem> items = new ArrayList<ModelOrderItem>();
    private ModelCustomer customer = new ModelCustomer();

    @Override
    public String getTableName() {
        return "orders";
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public void addItem(ModelOrderItem item){
        this.items.add(item);
    }

    public void addItem(ModelProduct prod, int inc, String notes){
        ModelOrderItem item;
        ModelOrderModifier ordermodifier;

        if (inc>0){
            //find prod without attribute
            item = findItemByProductID(prod.getId(), true); //doesnt care, they have attribute or not
            if (item == null || prod.selectedModifiers()>0 || notes != "") {
                item = new ModelOrderItem(prod, inc);
                for (ModelModifier modifier : prod.modifiers) {
                    if (modifier.checked) {
                        ordermodifier = new ModelOrderModifier(modifier);
                        item.addModifier(ordermodifier);
                    }
                }
                addItem(item);
                item.setNotes(notes);
            }else{
                item.setQty(item.getQty() + inc);
            }
        }else{
            item = findItemByProductID(prod.getId(), false); //doesnt care, they have attribute or not
            item.setQty(item.getQty() + inc);
        }

        if (item != null) {
            if (item.getQty() <= 0){
                removeItem(item);
            }
            item.calculate();
        }

    }

    public void removeItem(ModelOrderItem item){
        this.items.remove(item);
    }

    public void clearItem(){
        this.items.clear();
    }

    public ModelOrderItem getItem(int i){
        return this.items.get(i);
    }

    public int itemCount(){
        return this.items.size();
    }

    public ModelOrderItem findItemByProductID(int productID, Boolean hasNoModifiers){
        ModelOrderItem aItem = null;
        for (int i=0; i<this.itemCount(); i++){
            if (getItem(i).getProduct().getId() == productID) {
                if (hasNoModifiers){
                    if (!getItem(i).hasModifiers()) aItem = this.getItem(i);
                }else {
                    aItem = this.getItem(i);
                }
            }
        }
        return aItem;
    }

    public Double getSummary(){
        return getSubTotal() + getTax();
    }

    public Double getSubTotal(){
        Double sum = 0.0;
        for (ModelOrderItem item : items) {
            sum += (item.getQty() * item.getPrice());
        }
        return sum;
    }

    public Double getTax(){
        Double sum = 0.0;
        for (ModelOrderItem item : items) {
            sum += (item.getQty() * item.getPrice() * item.getTax() / 100);
        }
        return sum;
    }

    public List<ModelOrderItem> getItems(){
        return this.items;
    }

    public ModelCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(ModelCustomer customer) {
        this.customer_id = 0;
        this.customer = customer;

        if (this.customer != null)  this.customer_id = customer.getId();
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getCashpayment() {
        return cashpayment;
    }

    public void setCashpayment(Double cashpayment) {
        this.cashpayment = cashpayment;
    }

    public Double getCardpayment() {
        return cardpayment;
    }

    public void setCardpayment(Double cardpayment) {
        this.cardpayment = cardpayment;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public void saveToDBAll(SQLiteDatabase db) {
        Log.d("DB","Order.saveToDBAll");
        refreshAmount();
        db.beginTransaction();
        try {
            super.saveToDB(db ,true);
            ModelOrderItem tmp = new ModelOrderItem();
            db.execSQL(tmp.generateSQLDelete("where order_id = " + String.valueOf(getId())));

            for (ModelOrderItem modelOrderItem : items) {
                modelOrderItem.setId(0); //force insert
                modelOrderItem.setOrder_id(this.getId());
                modelOrderItem.saveToDBAll(db);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void refreshAmount(){
        this.amount = this.getSummary();
    }

    public Double getTotalCustPayment(){
        return (this.cashpayment + this.cardpayment);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
};


