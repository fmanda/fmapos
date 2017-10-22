package com.fma.fmapos.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fma on 7/30/2017.
 */

public class ModelCustomer extends BaseModel implements Serializable{
    @TableField
    private String uid;
    @TableField
    private String name;
    @TableField
    private String code;
    @TableField
    private Date date_modified;

    public ModelCustomer(){
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String guid) {
        this.uid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @TableField
    private String address;
    @TableField
    private String category;


    @Override
    public String getTableName() {
        return super.getTableName();
    }


}
