package npv.fx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import npv.data.PlanData;
import npv.data.PlanDataCounter;

import java.net.URL;
import java.util.*;

/**
 * Created by nick on 08.04.17.
 */

public class ProfitFlowController extends NavigationController implements Initializable {

    @FXML private Button btClose;
    @FXML private TableView tvQueue;
    @FXML private TableView tvRSum;

    private LinkedHashMap<Integer, ArrayList<PlanData>> plans;

    public ProfitFlowController(LinkedHashMap<Integer, ArrayList<PlanData>> calendarPlans) {
        this.plans = calendarPlans;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //table with miniprojects profit flows
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        ObservableList<ObservableList> sumOfRData = FXCollections.observableArrayList();

        int numberOfColumns = plans.get(0).get(0).getMiniProjectNumberAndProfits().size();
        for (int i = 0; i < numberOfColumns; i++) {
            final int id = i;
            TableColumn col = new TableColumn(String.valueOf(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(id).toString());
                }
            });
            tvQueue.getColumns().add(col);
            TableColumn columnsForSumOfR = new TableColumn(String.valueOf(i));
            columnsForSumOfR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(id).toString());
                }
            });
            tvRSum.getColumns().add(columnsForSumOfR);//same amount of columns for RSum
        }

        for (Map.Entry<Integer, ArrayList<PlanData>> plan : plans.entrySet()) {
            for (PlanData planData : plan.getValue()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                ArrayList<Integer> profits = planData.getMiniProjectNumberAndProfits();
                for (int i = 0; i < profits.size(); i++) {
                    if (profits.get(i) == PlanDataCounter.FAKE_MINIPROJECT_NUMBER_FOR_R) {
                        row.add(i, "R");
                    } else {
                        row.add(i, profits.get(i).toString());
                    }
                }
                data.add(row);
            }
        }
        for (ObservableList<String> row : data) {

        }
        tvQueue.setItems(data);

        //table with sum of R
        ObservableList<String> rowOfRValues = FXCollections.observableArrayList();
        PlanData sumOfRPlanData = PlanDataCounter.getSumOfRFlow(plans);
        rowOfRValues.add("R");
//        sumOfRPlanData.getProfitByMiniProject().forEach(value -> rowOfRValues.add(value.toString()));
        for (int i = 0; i < sumOfRPlanData.getProfitByMiniProject().size(); i++) {
            rowOfRValues.add(i+1, sumOfRPlanData.getProfitByMiniProject().get(i).toString());
        }
        sumOfRData.add(rowOfRValues);
        tvRSum.setItems(sumOfRData);
    }

    @FXML
    private void handleControlActions(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btClose)) {
            Stage stage = (Stage) btClose.getScene().getWindow();
            if (QueuesController.isNewWindowCreated()) {
                QueuesController.setNewWindowCreated(false);
            }
            stage.close();
        }
    }
}
