package npv.data;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

import java.util.ArrayList;

/**
 * Created by nick on 3/27/2017.
 */
public class PlanData {
    private Integer miniProjectNumber;
    private Double miniProjectCost;
    private ArrayList<Double> profitByMiniProject;

    public PlanData(Integer miniProjectN) {
        this.miniProjectNumber = miniProjectN;
        this.profitByMiniProject= new ArrayList<>();
    }

    public PlanData(Integer miniProjectN, Double cost) {
        this.miniProjectNumber = miniProjectN;
        this.miniProjectCost = cost;
        this.profitByMiniProject= new ArrayList<>();
    }

    public PlanData(Integer miniProjectN, ArrayList<Double> profit) {
        this.miniProjectNumber = miniProjectN;
        this.profitByMiniProject = profit;
    }

    public void addToMiniProjectProfit(@NotNull Double value) throws Exception {
        if (null == value) {
            throw new Exception("Null value is not allowed here");
        }
        this.profitByMiniProject.add(value);
    }

    public Integer getMiniProjectNumber() {
        return miniProjectNumber;
    }

    public void setMiniProjectNumber(Integer miniProjectNumber) {
        this.miniProjectNumber = miniProjectNumber;
    }

    public ArrayList<Double> getProfitByMiniProject() {
        return profitByMiniProject;
    }

    public Double getMiniProjectCost() {
        return miniProjectCost;
    }

    public void setMiniProjectCost(Double miniProjectCost) {
        this.miniProjectCost = miniProjectCost;
    }

    public void setProfitByMiniProject(ArrayList<Double> profitByMiniProject) {
        this.profitByMiniProject = profitByMiniProject;
    }

    public ArrayList<Double> getMiniProjectNumberAndProfits() {
        ArrayList<Double> result = new ArrayList<>(profitByMiniProject);
        result.add(0, miniProjectNumber.doubleValue());
        return result;
    }

    public ArrayList<Double> getMiniProjectCostAndProfits() {
        ArrayList<Double> result = new ArrayList<>(profitByMiniProject);
        result.add(0, miniProjectCost);
        return result;
    }

    public ArrayList<Double> getMiniProjectNumberCostAndProfits() {
        ArrayList<Double> result = new ArrayList<>(profitByMiniProject);
        result.add(0, miniProjectNumber.doubleValue());
        result.add(1, miniProjectCost);
        return result;
    }

    public ArrayList<String> getStringMiniProjectNumberAndProfits() {
        ArrayList<String> result = new ArrayList<>(profitByMiniProject.size());
        result.add(0, miniProjectNumber.toString());
        profitByMiniProject.forEach(profit -> result.add(profit.toString()));
        return result;
    }

    public ArrayList<String> getStringMiniProjectCostAndProfits() {
        ArrayList<String> result = new ArrayList<>(profitByMiniProject.size());
        result.add(0, miniProjectCost.toString());
        profitByMiniProject.forEach(profit -> result.add(profit.toString()));
        return result;
    }
}
