package npv.data;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;

import java.util.ArrayList;

/**
 * Created by nick on 3/27/2017.
 */
public class PlanData {
    private Integer miniProjectNumber;
    private ArrayList<Integer> profitByMiniProject;

    public PlanData(Integer miniProjectN) {
        this.miniProjectNumber = miniProjectN;
        this.profitByMiniProject= new ArrayList<>();
    }

    public PlanData(Integer miniProjectN, ArrayList<Integer> profit) {
        this.miniProjectNumber = miniProjectN;
        this.profitByMiniProject = profit;
    }

    public void addToMiniProjectProfit(@NotNull Integer value) throws Exception {
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

    public ArrayList<Integer> getProfitByMiniProject() {
        return profitByMiniProject;
    }

    public void setProfitByMiniProject(ArrayList<Integer> profitByMiniProject) {
        this.profitByMiniProject = profitByMiniProject;
    }

    public ArrayList<Integer> getMiniProjectNumberAndProfits() {
        ArrayList<Integer> result = new ArrayList<>(profitByMiniProject);
        result.add(0, miniProjectNumber);
        return result;
    }
}
