package npv.fx.controllers;

import javafx.beans.binding.IntegerBinding;
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

        int numberOfColumns = plans.get(0).get(0).getMiniProjectCostAndProfits().size();
        Integer indexOfNameColumn = numberOfColumns;

        //adding caption columns
        //Caption for queues names
        TableColumn nameColumn = new TableColumn("N"/*(indexOfNameColumn.toString())*/);
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(indexOfNameColumn).toString());
            }
        });

        //caption for sum of R
        TableColumn nameColumnR = new TableColumn(("N"));
        nameColumnR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                return new SimpleStringProperty(param.getValue().get(numberOfColumns).toString());
            }
        });

        Collection<TableColumn> columns = new ArrayList<TableColumn>();
        Collection<TableColumn> columnsForR = new ArrayList<TableColumn>();
        columns.add(nameColumn);
        columnsForR.add(nameColumnR);
        for (int i = 0; i < numberOfColumns; i++) {
            final int id = i;
            TableColumn col = new TableColumn(String.valueOf(id));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(id).toString());
                }
            });
            columns.add(col);
            TableColumn columnsForSumOfR = new TableColumn(String.valueOf(i));
            columnsForSumOfR.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(id).toString());
                }
            });
            columnsForR.add(columnsForSumOfR);
        }


        columns.forEach(column -> tvQueue.getColumns().add(column));
        columnsForR.forEach(columnR -> tvRSum.getColumns().add(columnR));

        List<Double> costs = PlanDataCounter.getMiniProjectsCostsPerQueue(plans);

        for (Map.Entry<Integer, ArrayList<PlanData>> plan : plans.entrySet()) {
            for (PlanData planData : plan.getValue()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                ArrayList<String> profits = planData.getStringMiniProjectCostAndProfits();
                for (int i = 0; i < profits.size(); i++) {
                    row.add(i, profits.get(i).toString());
                }
                if(PlanDataCounter.FAKE_MINIPROJECT_NUMBER_FOR_R.equals(planData.getMiniProjectNumber())) {
                    row.add(numberOfColumns, "R");
                } else {
                    row.add(numberOfColumns, planData.getMiniProjectNumber().toString());
                }
                data.add(row);
            }
        }

        tvQueue.setItems(data);

        //table with sum of R
        ObservableList<String> rowOfRValues = FXCollections.observableArrayList();
        PlanData sumOfRPlanData = PlanDataCounter.getSumOfRFlow(plans);
        for (int i = 0; i < sumOfRPlanData.getProfitByMiniProject().size(); i++) {
            rowOfRValues.add(i, sumOfRPlanData.getProfitByMiniProject().get(i).toString());
        }
        rowOfRValues.add(0, PlanDataCounter.getCostOfAllQueues(plans).toString());
        rowOfRValues.add(numberOfColumns, "R");
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
