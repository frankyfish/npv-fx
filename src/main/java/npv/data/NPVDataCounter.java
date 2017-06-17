/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author nick
 */
public class NPVDataCounter {

    private final int rowNumber;
    private final TableView<NPVData> tableNpv;
    private final Double alpha;

    Double[] funds;
    Double[] r;
    Double[] a;
    Double[] npv;

    public NPVDataCounter(TableView<NPVData> table, int rN, Double alphaValue) {
        this.tableNpv = table;
        this.rowNumber = rN;
        this.alpha = alphaValue;

        this.funds = new Double[rowNumber];
        this.r = new Double[rowNumber];
        this.a = new Double[rowNumber];
        this.npv = new Double[rowNumber];
    }

    public void countNpv() {

        TableColumn<NPVData, ?> fundsPerPeriodColumn
                = tableNpv.getColumns().get(FUNDS_PER_PERIOD_COL_NUM);

        for (int i = 0; i < rowNumber; i++) {
            funds[i] = (Double) fundsPerPeriodColumn.getCellData(i);
        }

        for (int i = 0; i < r.length; i++) {
            r[i] = (1 / Math.pow(1 + (alpha / 100), i));
        }

        for (int i = 0; i < a.length; i++) {
            a[i] = funds[i] * r[i];
        }

        for (int i = 0; i < npv.length; i++) {
            if (i == 0) {
                npv[i] = a[i];
            } else {
                npv[i] = npv[i - 1] + a[i];
            }
        }

        System.out.println("NPV=" + npv[npv.length - 1]);
    }

    public void round(int places) {
        for (int i = 0; i < this.funds.length; i++) {
//            funds[i] = round(funds[i], places);
            r[i] = round(r[i], places);
            a[i] = round(a[i], places);
            npv[i] = round(npv[i], places);
        }
    }

    public static Double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return Double.valueOf(bd.doubleValue());
    }

    public Double[] getFunds() {
        return funds;
    }

    public Double[] getR() {
        return r;
    }

    public Double[] getA() {
        return a;
    }

    public Double[] getNpv() {
        return npv;
    }

    public Double getNPVValue() {
        return npv[npv.length - 1];
    }

    private final int FUNDS_PER_PERIOD_COL_NUM = 1;
}
