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

        //adding caption columns
        //Caption for queues
        TableColumn nameColumn = new TableColumn(("c"));
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(0).toString());
            }
        });
        tvQueue.getColumns().add(0, nameColumn);

        for (int i = 1; i < numberOfColumns + 1; i++) {
            final int id = i;
            TableColumn col = new TableColumn(String.valueOf(i-1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(id-1).toString());
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
//                ArrayList<Double> profits = planData.getMiniProjectNumberAndProfits();
                ArrayList<String> profits = planData.getStringMiniProjectNumberAndProfits();
                for (int i = 0; i < profits.size(); i++) {
                    if (profits.get(i).equals(PlanDataCounter.FAKE_MINIPROJECT_NUMBER_FOR_R.toString())) {
                        row.add(i, "R");
                    } else {
                        row.add(i, profits.get(i).toString());
                    }
                }
                data.add(row);
            }
        }






        tvQueue.refresh();
//        //caption for sum of R
//        TableColumn nameColumnR = new TableColumn(("c"));
//        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                return new SimpleStringProperty(param.getValue().get(0).toString());
//            }
//        });
//
//        tvRSum.getColumns().add(0, nameColumnR);
//        tvRSum.refresh();
//        //populating first column with rows
        List<Double> costs = PlanDataCounter.getMiniProjectsCostsPerQueue(plans);
//
        for (int i = 0; i < data.size(); i++) {
            ObservableList<String> row = data.get(i);
            if (row.contains("R")) {
                row.add(0, costs.remove(0).toString());
            }
//            else {
//                row.add(0, " ");
//            }
        }
        tvQueue.setItems(data);
        tvQueue.refresh();
        //table with sum of R
        ObservableList<String> rowOfRValues = FXCollections.observableArrayList();
        PlanData sumOfRPlanData = PlanDataCounter.getSumOfRFlow(plans);
        rowOfRValues.add(0, PlanDataCounter.getCostOfAllQueues(plans).toString());
        rowOfRValues.add(1, "R"); //because 0 is for cost
//        sumOfRPlanData.getProfitByMiniProject().forEach(value -> rowOfRValues.add(value.toString()));
        for (int i = 0; i < sumOfRPlanData.getProfitByMiniProject().size(); i++) {
            rowOfRValues.add(i+2, sumOfRPlanData.getProfitByMiniProject().get(i).toString());
            //+2 because 0&1 a reserved for cost & caption 'R'
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
