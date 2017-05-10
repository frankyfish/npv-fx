/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author nick
 */
public class MiniProjectDataCounter {
    
    private final TableView<MiniProjectData> tableView;
    private final Double alpha;
    private final int rowNum;
    
    Integer[] time;
    Double[] S;
    Integer [] D;
    Double[] C;
    Double[] factorK;
    
    public MiniProjectDataCounter(TableView<MiniProjectData> mpdTable,
            Double alphaVal, int rowN) {
        this.tableView = mpdTable;
        this.alpha = alphaVal/100;
        this.rowNum = rowN;
        
        time = new Integer[rowNum];
        S = new Double[rowNum];
        D = new Integer[rowNum];
        C = new Double[rowNum];
        factorK = new Double[rowNum];
    }
    
    public void count() {
        TableColumn<MiniProjectData, ?> gainCol // C
                = tableView.getColumns().get(GAIN_COL_NUMBER);
        
        for (int i=0; i<rowNum; i++) {
           C[i] = (Double) gainCol.getCellData(i);
        }
        
        TableColumn<MiniProjectData, ?> timeCol 
                = tableView.getColumns().get(TIME_COL_NUMER);
        
        for (int i=0; i<rowNum; i++) {
            time[i] = (Integer) timeCol.getCellData(i);
        }
        
        for (int i=0; i<time.length; i++) {
            S[i] = (Math.pow(1+alpha, time[i]) - 1) / alpha;
        }
        
        TableColumn<MiniProjectData, ?> incomeCol 
                = tableView.getColumns().get(INCOME_COL_NUMBER);
        
        for (int i=0; i<rowNum; i++) {
            D[i] = (Integer) incomeCol.getCellData(i);
        }
        Double[] dDouble = new Double[D.length];
        
        //converting from Integer to double \_(ツ)_/¯
        for (int i=0; i<rowNum; i++) {
            dDouble[i] = D[i].doubleValue();
        }
        
        for (int i=0; i<rowNum; i++) {
            factorK[i] = dDouble[i] / S[i];
        }
    }
    
    public Double[] getS() {
        return S;
    }
    
    public Double[] getFactorK() {
        return factorK;
    }
    
    public Integer[] getTime() {
        return time;
    }
    
    public Integer[] getD() {
        return D;
    }
    
    public Double[] getC() {
        return C;
    }
    
    //indexes of columns
    private final int TIME_COL_NUMER = 1;
    private final int INCOME_COL_NUMBER = 2;
    private final int GAIN_COL_NUMBER = 3;
}
