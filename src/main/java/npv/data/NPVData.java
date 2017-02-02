/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty; 
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author nima0814
 * This class represents java bean of the NPV table entity(row)
 * used in UI
 */
public class NPVData {
    
    private SimpleIntegerProperty periodId = new SimpleIntegerProperty();
    private SimpleDoubleProperty fundPerPeriod = new SimpleDoubleProperty();
    private SimpleDoubleProperty fundWithAlpha = new SimpleDoubleProperty();
    private SimpleDoubleProperty discountRateAlpha = new SimpleDoubleProperty();
    private SimpleDoubleProperty netPresentValue = new SimpleDoubleProperty();
    
    public NPVData () {}
    
    public NPVData(Integer i, Double ri) {
        this.periodId= new SimpleIntegerProperty(i);
        this.fundPerPeriod = new SimpleDoubleProperty(ri);
        this.fundWithAlpha = new SimpleDoubleProperty(0.0);
        this.discountRateAlpha = new SimpleDoubleProperty(0.0);
        this.netPresentValue = new SimpleDoubleProperty(0.0);
    }
    
    public NPVData(Integer i, Double ri, Double r, Double a, Double npv) {
        this.periodId= new SimpleIntegerProperty(i);
        this.fundPerPeriod = new SimpleDoubleProperty(ri);
        this.fundWithAlpha = new SimpleDoubleProperty(r);
        this.discountRateAlpha = new SimpleDoubleProperty(a);
        this.netPresentValue = new SimpleDoubleProperty(npv);
    }
    
    public Integer getPeriodId() {
        return periodId.get();
    }

    public Double getFundPerPeriod() {
        return fundPerPeriod.get();
    }

    public Double getFundWithAlpha() {
        return fundWithAlpha.get();
    }
    
    public Double getDiscountRateAlpha() {
        return discountRateAlpha.get();
    }
    
    public Double getNetPresentValue() {
        return netPresentValue.get();
    }
    
    public void setPeriodId (Integer value) {
        periodId.set(value);
    }
    
    public void setFundPerPeriod(Integer value) {
        fundPerPeriod.set(value);
    }
    
    public void setFundWithAlpha(Integer value) {
        fundWithAlpha.set(value);
    }

    public void setDiscountRateAlpha(Integer value) {
        discountRateAlpha.set(value);
    }

    public void setNetPresentValue(Integer value) {
        netPresentValue.set(value);
    }

}
