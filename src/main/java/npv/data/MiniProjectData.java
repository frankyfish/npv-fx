/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author nick
 */
public class MiniProjectData {

    private SimpleIntegerProperty periodI = new SimpleIntegerProperty();//i
    private SimpleIntegerProperty time = new SimpleIntegerProperty();//t
    private SimpleIntegerProperty income = new SimpleIntegerProperty();//D
    private SimpleDoubleProperty gain = new SimpleDoubleProperty();//C
    private SimpleDoubleProperty directExpense = new SimpleDoubleProperty();//S
    private SimpleDoubleProperty factorK = new SimpleDoubleProperty();//k

    public MiniProjectData(){}

    public MiniProjectData(Integer numId, Integer t, Integer D, Double C) {
        this.periodI = new SimpleIntegerProperty(numId+1); //+1 for UI view
        this.time = new SimpleIntegerProperty(t);
        this.income = new SimpleIntegerProperty(D);
        this.gain = new SimpleDoubleProperty(C);
        this.directExpense = new SimpleDoubleProperty(0);
        this.factorK = new SimpleDoubleProperty(0);
    }

    public MiniProjectData(Integer numId, Integer t, Integer D, Double C, Double S, Double K) {
        this.periodI = new SimpleIntegerProperty(numId+1); //+1 for UI view
        this.time = new SimpleIntegerProperty(t);
        this.income = new SimpleIntegerProperty(D);
        this.gain = new SimpleDoubleProperty(C);
        this.directExpense = new SimpleDoubleProperty(S);
        this.factorK = new SimpleDoubleProperty(K);
    }

    public Integer getPeriodI() {
        return this.periodI.get();
    }

    public Integer getTime() {
        return this.time.get();
    }

    public Integer getIncome() {
        return this.income.get();
    }

    public Double getGain() {
        return this.gain.get();
    }

    public Double getDirectExpense() {
        return this.directExpense.get();
    }

    public Double getFactorK() {
        return this.factorK.get();
    }

    public void setPeriodI(Integer value) {
        this.periodI.set(value);
    }

    public void setTime(Integer value) {
        this.time.set(value);
    }

    public void setIncome(Integer value) {
        this.income.set(value);
    }

    public void setGain(Integer value) {
        this.gain.set(value);
    }

    public void setDirectExpense(Double value) {
        this.directExpense.set(value);
    }

    public void setFactorK(Double value) {
        this.factorK.set(value);
    }
}
