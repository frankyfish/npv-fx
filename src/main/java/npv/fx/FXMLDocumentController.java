/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.fx;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import npv.data.MiniProjectData;
import npv.data.MiniProjectDataCounter;
import npv.data.NPVData;
import npv.data.NPVDataCounter;
import npv.data.algorithms.Algorithm;
import npv.data.algorithms.AlgorithmI;
import npv.importer.XlsImporter;

/**
 *
 * @author nima0814
 */
public class FXMLDocumentController extends GUIManager implements Initializable {

    //mb delete this fields
    private Parent rootFxml;
    private Stage rootStage;
    private Scene rootScene;

    @FXML private Button queues;
    @FXML private Button npv;
    //control panel
    @FXML private Button returnToRoot;
    @FXML private Button importFromExcell;
    //npv buttons for count\control
    @FXML private Spinner spinnerPeriods;
    @FXML private Label testLabel;
    @FXML private TextField textPeriods;
    @FXML private Button btnCount;
    @FXML private TextField tfNewFund;
    @FXML private TextField tfAlpha;
    //table NPV
    int rowNumber = 0;
    @FXML private TableView<NPVData> npvTable;
    @FXML private TableColumn<NPVData, Integer> periodId;
    @FXML private TableColumn<NPVData, Double> fundPerPeriod;
    @FXML private TableColumn<NPVData, Double> fundWithAlpha;
    @FXML private TableColumn<NPVData, Double> discountRateAlpha;
    @FXML private TableColumn<NPVData, Double> netPresentValue;
    @FXML private Button addRow;
    ObservableList data = FXCollections.observableArrayList();
    //queues buttons etc
    @FXML private TextField tfT;
    @FXML private TextField tfD;
    @FXML private TextField tfC;
    @FXML private Button btnAdd;
    @FXML private Button btnCountFactorK;
    @FXML private TextField tfPercentQueue;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String path = url.getPath();
        if (path.contains(GUIConstants.FXML_NPV_LAYOUT+".fxml")) { //TODO change this part (change architecture)
            periodId.setCellValueFactory(new PropertyValueFactory<>("periodId"));
            fundPerPeriod.setCellValueFactory(new PropertyValueFactory<>("fundPerPeriod"));
            fundWithAlpha.setCellValueFactory(new PropertyValueFactory<>("fundWithAlpha"));
            discountRateAlpha.setCellValueFactory(new PropertyValueFactory<>("discountRateAlpha"));
            netPresentValue.setCellValueFactory(new PropertyValueFactory<>("netPresentValue"));
            setTableColumnsDraggableFalse(npvTable);//for moving cells
            npvTable.setItems(data);
        }
        if (path.contains(GUIConstants.FXML_QUEUES_LAYOUT+".fxml")) {
            periodI.setCellValueFactory(new PropertyValueFactory<>("periodI"));
            time.setCellValueFactory(new PropertyValueFactory<>("time"));
            income.setCellValueFactory(new PropertyValueFactory<>("income"));
            gain.setCellValueFactory(new PropertyValueFactory<>("gain"));
            directExpense.setCellValueFactory(new PropertyValueFactory<>("directExpense"));
            factorK.setCellValueFactory(new PropertyValueFactory<>("factorK"));
            setTableColumnsDraggableFalse(miniProjectTable);
            miniProjectTable.setItems(miniProjectData);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception, IOException {
        rootFxml = GUIManager.getRoot();
        rootStage = (Stage) npv.getScene().getWindow();
        rootScene = npv.getScene();

        Stage currentStage = null;
        Parent layout = null;
        //todo change button1 block to another, useful one
        if (event.getSource().equals(queues)) {
            layout = loadLayout(GUIConstants.FXML_QUEUES_LAYOUT);
            currentStage = (Stage) queues.getScene().getWindow();
            goToNewScene(layout, currentStage);
        } else if (event.getSource().equals(npv)) {
            layout = loadLayout(GUIConstants.FXML_NPV_LAYOUT);
            currentStage = (Stage) npv.getScene().getWindow();
            goToNewScene(layout, currentStage);
        }
    }

    @FXML
    public void handleControlButtonAction(ActionEvent event)
            throws Exception, IOException {
        if (event.getSource().equals(returnToRoot)) {
            //TODO figure out how to save root,scene,stage from the 
            //default window (to save the date from it) mb use map instead for
            // saving the data itself
            /*if (rootFxml != null && rootStage != null 
                    && rootScene != null) {           
                rootStage.show();*/
            Parent fxml = loadLayout(GUIConstants.FXML_ROOT_LAYOUT);
            Stage stage = (Stage) returnToRoot.getScene().getWindow();
            goToNewScene(fxml, stage);
        }
    }

    @FXML
    public void handleNpvActions(ActionEvent event) throws Exception, IOException {
        NPVData npvdat = null;
        if (event.getSource().equals(textPeriods)) {
            String text = textPeriods.getText();
            System.out.println("Text =" + text);
            rowNumber = Integer.parseInt(text); //TODO mb change this move count to another class
            for (int i=0;i<rowNumber;i++) { //TODO change this part\ factory?!
             npvdat = new NPVData(i,2.0,3.0,4.0,5.0);   
             data.add(npvdat);
            }            
        } else if (event.getSource().equals(addRow)) {            
            Double newFund = Double.parseDouble(tfNewFund.getText());
            tfNewFund.clear();
            npvdat = new NPVData(rowNumber,newFund);
            rowNumber++;
            data.add(npvdat);
        } else if (event.getSource().equals(btnCount)) {
            //TODO: fix the problem
            //alpha value only getted when it is like 1.0
            Double alpha = Double.valueOf(tfAlpha.getText());
            if (validateAlpha()) {
                NPVDataCounter nDC =
                    new NPVDataCounter(npvTable, rowNumber, alpha);
                nDC.countNpv();
                ArrayList<NPVData> npvDat = new ArrayList<>();
                //clearing the table before filling it
                ObservableList<NPVData> tableDat = npvTable.getItems();
                tableDat.clear(); //TODO mb change this code
                npvTable.setItems(tableDat);
            
                for (int i=0;i<rowNumber;i++) {
                    npvDat.add(
                    new NPVData(i,nDC.getFunds()[i],nDC.getR()[i],
                            nDC.getA()[i],nDC.getNpv()[i]));
                }
                for (NPVData npvData:npvDat) {
                    data.add(npvData);
                }
            }
        } else if (event.getSource().equals(importFromExcell)){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Excell File");
            System.out.println("Opening excell file");
            Stage s = super.getStage();
            File file = fileChooser.showOpenDialog(s);
            
            XlsImporter xlsImporter = new XlsImporter(file, "#Ri");
            Double[] importedData = xlsImporter.importXls();
            for (int i=0; i< importedData.length; i++) {
             npvdat = new NPVData(rowNumber,importedData[i]);
             rowNumber++;
             data.add(npvdat);   
            }            
        }      
    }
    
    @FXML
    private void handleQueueActions (ActionEvent actionEvent) {        
        MiniProjectData miniPrjDat = null;
        if (actionEvent.getSource().equals(btnAdd)) {
            if (tfT != null && tfD != null && tfC != null) {
                Integer t = Integer.valueOf(tfT.getText());
                Integer d = Integer.valueOf(tfD.getText());
                Integer c = Integer.valueOf(tfC.getText());
                miniPrjDat 
                        = new MiniProjectData(rowNum, t, d, c);
                rowNum++;
                miniProjectData.add(miniPrjDat);
                tfT.clear();
                tfD.clear();
                tfC.clear();
            }
        } else if (actionEvent.getSource().equals(btnCountFactorK)) {
            Double alpha = Double.valueOf(tfPercentQueue.getText());
            MiniProjectDataCounter mpDataCounter
                    = new MiniProjectDataCounter(miniProjectTable, alpha, rowNum);
            mpDataCounter.count();
            //clearing table before filling
            ObservableList<MiniProjectData> currentTableData
                    = miniProjectTable.getItems();
            currentTableData.clear();
            miniProjectTable.setItems(currentTableData);
            
            ArrayList<MiniProjectData> newMiniProjectData = new ArrayList<>();
            for (int i=0; i<rowNum; i++) {
                newMiniProjectData.add(
                        new MiniProjectData(i,
                                            mpDataCounter.getTime()[i],
                                            mpDataCounter.getD()[i],
                                            mpDataCounter.getC()[i],
                                            mpDataCounter.getS()[i],
                                            mpDataCounter.getFactorK()[i])
                );
            }
            
            for (MiniProjectData newMiniProjectDat:newMiniProjectData) {
                miniProjectData.add(newMiniProjectDat);
            }
            Algorithm<AlgorithmI> newAlgorithm = new AlgorithmI();
            //newAlgorithm.sort(newMiniProjectData);
            newAlgorithm.sort(newMiniProjectData);
        } else if (actionEvent.getSource().equals(testBtn)) { //todo delete this is for dev purposes
            miniProjectData.add(new MiniProjectData(0, 10, 2, 25));
            miniProjectData.add(new MiniProjectData(1, 9, 5, 15));
            miniProjectData.add(new MiniProjectData(2, 8, 6, 15));
            miniProjectData.add(new MiniProjectData(3, 7, 3, 15));
            miniProjectData.add(new MiniProjectData(4, 6, 8, 10));
            miniProjectData.add(new MiniProjectData(5, 5, 4, 10));
            miniProjectData.add(new MiniProjectData(6, 4, 3, 15));
            miniProjectData.add(new MiniProjectData(7, 4, 5, 14));
            miniProjectData.add(new MiniProjectData(8, 3, 3, 25));
            miniProjectData.add(new MiniProjectData(9, 3, 3, 13));
            miniProjectData.add(new MiniProjectData(10, 2, 2, 20));
            miniProjectData.add(new MiniProjectData(11, 2, 2, 10));
            miniProjectTable.setItems(miniProjectData);
        }
    }

    private Parent loadLayout(String layoutName) throws IOException {
        layoutName = "layouts/" + layoutName + ".fxml";
        Parent layout = FXMLLoader.load(getClass().getResource(layoutName));
        return layout;
    }

    private void goToNewScene(Parent fxml, Stage stage) {
        Scene scene = new Scene(fxml);
        stage.setScene(scene);
        stage.show();
    }
    
    private void setTableColumnsDraggableFalse(TableView tableView) {
        tableView.widthProperty().addListener(
                new ChangeListener<Number>(){
                    @Override
                    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth){
                        TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
                        header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            header.setReordering(false);
                            }
                        });
                    }
                });
    }
    
    private boolean validateAlpha() {
        if (!this.tfAlpha.getText().contains(".")) {           
            return false;
        }
        return true;
    }
}