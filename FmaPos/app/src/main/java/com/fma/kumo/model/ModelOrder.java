package com.fma.kumo.model;

import android.database.Cursor;
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
//    @TableField
//    private String counterno;
    @TableField
    private int customer_id;

    private String customer_uid;
    private String reconcile_uid;

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
    private String uid;
    @TableField
    private int uploaded;
    @TableField
    private Integer company_id;
    @TableField
    private Integer unit_id;
    @TableField
    private int status = 0; //0:created/hold, 1:paid, 2:void
    @TableField
    private String customfield_1;
    @TableField
    private String customfield_2;
    @TableField
    private String customfield_3;
    @TableField
    private String order_category;
    @TableField
    private int reconcile_id = 0;
    @TableField
    private int day_counter = 0;

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

    public String getuid() {
        return uid;
    }

    public void setuid(String uuid) {
        this.uid = uuid;
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
    public String getCustomfield_1() {
        return customfield_1;
    }

    public void setCustomfield_1(String customfield_1) {
        this.customfield_1 = customfield_1;
    }

    public String getCustomfield_2() {
        return customfield_2;
    }

    public void setCustomfield_2(String customfield_2) {
        this.customfield_2 = customfield_2;
    }

    public String getCustomfield_3() {
        return customfield_3;
    }

    public void setCustomfield_3(String customfield_3) {
        this.customfield_3 = customfield_3;
    }

    public void reLoadAll(SQLiteDatabase db){
        this.items.clear();
        String sql = "select * from orderitem where order_id = " + String.valueOf(this.getId());

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            ModelOrderItem modelOrderItem = new ModelOrderItem();
            modelOrderItem.loadFromCursor(cursor);
            modelOrderItem.reLoadAll(db);
            this.addItem(modelOrderItem);
        }
    }

    public void prepareUpload(SQLiteDatabase db){
        if (customer_id > 0) {
            ModelCustomer modelCustomer = new ModelCustomer();
            modelCustomer.loadFromDB(db, this.customer_id);
            this.customer_uid = modelCustomer.getUid();
        }
        if (reconcile_id > 0) {
            ModelReconcile modelReconcile = new ModelReconcile();
            modelReconcile.loadFromDB(db, this.reconcile_id);
            this.reconcile_uid = modelReconcile.getUid();
        }

        this.reLoadAll(db);
        for (ModelOrderItem modelOrderItem : items) {
            modelOrderItem.prepareUpload(db);
        }

    }

    public String getOrder_category() {
        return order_category;
    }

    public void setOrder_category(String order_category) {
        this.order_category = order_category;
    }

    public String getStatusString(){
        if (this.getStatus() == 0){
            return "CREATED";
        }else if (this.getStatus() == 1){
            return "PAID";
        }else if (this.getStatus() == 2){
            return "VOID";
        }else{
            return "";
        }
    }

    public int getReconcile_id() {
        return reconcile_id;
    }

    public void setReconcile_id(int reconcile_id) {
        this.reconcile_id = reconcile_id;
    }

    public int getDay_counter() {
        return day_counter;
    }

    public void setDay_counter(int day_counter) {
        this.day_counter = day_counter;
    }
};


//known issue: order baru dengan customer baru, upload harus didahulukan customer,
// upload order tidak boleh masuk quee smpai customer mendapatkan uid
