/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.fx.controllers;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.scene.layout.AnchorPane;
import npv.fx.GUIConstants;
import npv.fx.controllers.NavigationController;
import npv.fx.controllers.utils.ControllerUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import npv.data.*;
import npv.data.algorithms.Algorithm;
import npv.data.algorithms.AlgorithmI;
import npv.data.algorithms.AlgorithmII;
import npv.data.algorithms.AlgorithmIII;
import npv.data.utils.QueueDataUtils;
import npv.fx.notifications.UINotification;
import npv.importer.XlsImporter;

import static npv.fx.GUIConstants.ALGORITHM_I;
import static npv.fx.GUIConstants.ALGORITHM_II;
import static npv.fx.GUIConstants.ALGORITHM_III;

/**
 * @author nima0814
 */
public class QueuesController extends NavigationController implements Initializable {

    //mb delete this fields
    private Parent rootFxml;
    private Stage rootStage;
    private Scene rootScene;

    //queues buttons etc
    @FXML private TextField tfT;
    @FXML private TextField tfD;
    @FXML private TextField tfC;
    @FXML private Button btnAdd;
    @FXML private Button btnCountFactorK;
    @FXML private TextField tfPercentQueue;
    @FXML private TextArea taQueues;
    @FXML private TextArea taQueuesProfit;
    @FXML private ChoiceBox cbAlgorithmSelection;
    @FXML private Button btShowInNewWindow;
    @FXML private Button countNPV;
    //for limiting new windows creation
    static boolean isNewWindowCreated = false;
    //table for queues
    int rowNum = 0;
    @FXML private TableView<MiniProjectData> miniProjectTable;
    @FXML private TableColumn<MiniProjectData, Integer> periodI;
    @FXML private TableColumn<MiniProjectData, Integer> time;
    @FXML private TableColumn<MiniProjectData, Integer> income;
    @FXML private TableColumn<MiniProjectData, Integer> gain;
    @FXML private TableColumn<MiniProjectData, Double> directExpense;
    @FXML private TableColumn<MiniProjectData, Double> factorK;
    ObservableList miniProjectData = FXCollections.observableArrayList();
    @FXML Button testBtn;

    private LinkedHashMap<Integer, ArrayList<PlanData>> plans;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            periodI.setCellValueFactory(new PropertyValueFactory<>("periodI"));
            time.setCellValueFactory(new PropertyValueFactory<>("time"));
            income.setCellValueFactory(new PropertyValueFactory<>("income"));
            gain.setCellValueFactory(new PropertyValueFactory<>("gain"));
            directExpense.setCellValueFactory(new PropertyValueFactory<>("directExpense"));
            factorK.setCellValueFactory(new PropertyValueFactory<>("factorK"));
            ControllerUtils.setTableColumnsDraggableFalse(miniProjectTable);
            miniProjectTable.setItems(miniProjectData);

            btShowInNewWindow.setVisible(false);
            cbAlgorithmSelection.
                    setItems(FXCollections.observableArrayList(ALGORITHM_I, ALGORITHM_II, ALGORITHM_III));
    }


    @FXML
    private void handleQueueActions(ActionEvent actionEvent) {
        MiniProjectData miniPrjDat = null;
        if (actionEvent.getSource().equals(btnAdd)) {
            if (tfT != null && tfD != null && tfC != null) {
                Integer t = Integer.valueOf(tfT.getText());
                Integer d = Integer.valueOf(tfD.getText());
                Double c = Double.valueOf(tfC.getText());
                miniPrjDat
                        = new MiniProjectData(rowNum, t, d, c);
                rowNum++;
                miniProjectData.add(miniPrjDat);
                tfT.clear();
                tfD.clear();
                tfC.clear();
            }
        } else if (actionEvent.getSource().equals(btnCountFactorK)) {
            try {
                if (tfPercentQueue.getText().isEmpty()) {
                    new UINotification(UINotification.Type.ERROR, "", "aльфа");
                    return;
                }
                Double alpha = Double.valueOf(tfPercentQueue.getText());
                MiniProjectDataCounter mpDataCounter
                        = new MiniProjectDataCounter(miniProjectTable, alpha, rowNum);
                mpDataCounter.count();
                //clearing table before filling
                ControllerUtils.clearTableBeforeShowCountResults(miniProjectTable);

                ArrayList<MiniProjectData> newMiniProjectData = new ArrayList<>();
                for (int i = 0; i < rowNum; i++) {
                    newMiniProjectData.add(
                            new MiniProjectData(i,
                                    mpDataCounter.getTime()[i],
                                    mpDataCounter.getD()[i],
                                    mpDataCounter.getC()[i],
                                    mpDataCounter.getS()[i],
                                    mpDataCounter.getFactorK()[i])
                    );
                }

                for (MiniProjectData newMiniProjectDat : newMiniProjectData) {
                    miniProjectData.add(newMiniProjectDat);
                }
                Algorithm selectedAlgorithm;
                String choiceBoxValue = (String) cbAlgorithmSelection.getValue();
                if (choiceBoxValue != null) {
                    PlanDataCounter planDataCounter = null;
                    switch (cbAlgorithmSelection.getValue().toString()) {
                        case ALGORITHM_I:
                            selectedAlgorithm = new AlgorithmI();
                            taQueues.setText(QueueDataUtils
                                    .getStringOfQueueData(selectedAlgorithm.sort(newMiniProjectData)));
                            planDataCounter = new PlanDataCounter(selectedAlgorithm.getQueues());
                            planDataCounter.count();
                            break;
                        case ALGORITHM_II:
                            selectedAlgorithm = new AlgorithmII();
                            taQueues.setText(QueueDataUtils
                                    .getStringOfQueueData(selectedAlgorithm.sort(newMiniProjectData)));
                            planDataCounter = new PlanDataCounter(selectedAlgorithm.getQueues());
                            planDataCounter.count();
                            break;
                        case ALGORITHM_III:
                            selectedAlgorithm = new AlgorithmIII();
                            taQueues.setText(QueueDataUtils
                                    .getStringOfQueueData(selectedAlgorithm.sort(newMiniProjectData)));
                            planDataCounter = new PlanDataCounter(selectedAlgorithm.getQueues());
                            planDataCounter.count();
                            break;
                    }
                    if (planDataCounter != null) {
                        plans = planDataCounter.getPlans();
                        taQueuesProfit.setText(planDataCounter.getStringRepresentation());
                        btShowInNewWindow.setVisible(true);
                    }
                } else {
                    new UINotification(UINotification.Type.ERROR, "", "algorithm type");
                }
            } catch (Exception e) {
                e.printStackTrace();
                new UINotification(UINotification.Type.ERROR, "Fatal Error", e.getMessage());
            }
        } else if (actionEvent.getSource().equals(testBtn)) {
            miniProjectData.add(new MiniProjectData(0, 10, 2, 10.0));
            miniProjectData.add(new MiniProjectData(1, 9, 5, 13.0));
            miniProjectData.add(new MiniProjectData(2, 8, 6, 25.0));
            miniProjectData.add(new MiniProjectData(3, 7, 3, 20.0));
            miniProjectData.add(new MiniProjectData(4, 6, 8, 25.0));
            miniProjectData.add(new MiniProjectData(5, 5, 4, 15.0));
            miniProjectData.add(new MiniProjectData(6, 4, 3, 14.0));
            miniProjectData.add(new MiniProjectData(7, 4, 5, 15.0));
            miniProjectData.add(new MiniProjectData(8, 3, 3, 10.0));
            miniProjectData.add(new MiniProjectData(9, 3, 3, 10.0));
            miniProjectData.add(new MiniProjectData(10, 2, 2, 15.0));
            miniProjectData.add(new MiniProjectData(11, 2, 2, 15.0));
            rowNum = miniProjectData.size();
            tfPercentQueue.setText("2");
            miniProjectTable.setItems(miniProjectData);
        } else if (actionEvent.getSource().equals(btShowInNewWindow)) {
            if (!isNewWindowCreated) {
                try {
                    isNewWindowCreated = true;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(GUIConstants.FXML_PROFIT_FLOW_LAYOUT));
                    ProfitFlowController controller = new ProfitFlowController(plans);
                    loader.setController(controller);
                    AnchorPane pane = loader.load();

                    Stage queuesStage = new Stage();
                    Scene queuesScene = new Scene(pane);
                    queuesStage.setScene(queuesScene);
                    queuesStage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (actionEvent.getSource().equals(countNPV)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(GUIConstants.FXML_NPV_LAYOUT));
                List<NPVData> npvDatas = convertToNPVData();
                NPVController controller = new NPVController(npvDatas.size(), npvDatas);
                loader.setController(controller);
                AnchorPane pane = loader.load();

                Stage queuesStage = new Stage();
                Scene queuesScene = new Scene(pane);
                queuesStage.setScene(queuesScene);
                queuesStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<NPVData> convertToNPVData() {
        List<NPVData> result = new ArrayList<>();
        PlanData sumOfR = PlanDataCounter.getSumOfRFlow(plans);
        for (int i = 0; i < sumOfR.getProfitByMiniProject().size(); i++) {
            result.add(new NPVData(i, sumOfR.getProfitByMiniProject().get(i)));
        }
        return result;
    }

    public static boolean isNewWindowCreated() {
        return isNewWindowCreated;
    }

    public static void setNewWindowCreated(boolean newWindowCreated) {
        isNewWindowCreated = newWindowCreated;
    }
}