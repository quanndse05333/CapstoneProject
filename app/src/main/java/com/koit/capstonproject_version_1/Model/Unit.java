package com.koit.capstonproject_version_1.Model;


import java.io.Serializable;

public class Unit implements Serializable {
    private String unitId;
    private String unitName;
    private long convertRate, unitPrice, unitQuantity;

    public Unit(String unitName, long convertRate, long unitPrice, long unitQuantity) {
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public Unit(String unitId, String unitName, long convertRate, long unitPrice, long unitQuantity) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.convertRate = convertRate;
        this.unitPrice = unitPrice;
        this.unitQuantity = unitQuantity;
    }

    public Unit() {
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public long getConvertRate() {
        return convertRate;
    }

    public void setConvertRate(long convertRate) {
        this.convertRate = convertRate;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(long unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    @Override
    public String toString() {
        return unitName;
    }
}
