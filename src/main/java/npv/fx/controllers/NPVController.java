package npv.fx.controllers;

import javafx.collections.FXCollections;
import npv.fx.controllers.utils.ControllerUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import npv.data.NPVData;
import npv.data.NPVDataCounter;
import npv.importer.XlsImporter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by nick on 24.04.17.
 */
public class NPVController extends NavigationController implements Initializable {
    @FXML private Button importFromExcell;
    //npv buttons for count\control
    @FXML private TextField tfResultNPV;
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

    public NPVController(int numberOfRows, List<? extends NPVData> newData) {
        this.rowNumber = numberOfRows;
        this.data = FXCollections.observableArrayList(newData);
    }

    @FXML
    public void handleNpvActions(ActionEvent event) throws Exception, IOException {
        NPVData npvdat = null;
//        if (event.getSource().equals(textPeriods)) {
//            String text = textPeriods.getText();
//            System.out.println("Text =" + text);
//            rowNumber = Integer.parseInt(text); //TODO mb change this move count to another class
//            for (int i = 0; i < rowNumber; i++) { //TODO change this part\ factory?!
//                npvdat = new NPVData(i, 2.0, 3.0, 4.0, 5.0);
//                data.add(npvdat);
//            }
//        } else
        if (event.getSource().equals(addRow)) {
            Double newFund = Double.parseDouble(tfNewFund.getText());
            tfNewFund.clear();
            npvdat = new NPVData(rowNumber, newFund);
            rowNumber++;
            data.add(npvdat);
        } else if (event.getSource().equals(btnCount)) {
            Double alpha = Double.valueOf(tfAlpha.getText());
            NPVDataCounter nDC =
                    new NPVDataCounter(npvTable, rowNumber, alpha);
            nDC.countNpv();
            ArrayList<NPVData> npvDat = new ArrayList<>();
            ControllerUtils.clearTableBeforeShowCountResults(npvTable);
            for (int i = 0; i < rowNumber; i++) {
                npvDat.add(
                        new NPVData(i, nDC.getFunds()[i], nDC.getR()[i],
                                nDC.getA()[i], nDC.getNpv()[i]));
            }
            for (NPVData npvData : npvDat) {
                data.add(npvData);
            }
            StringBuilder resultNpv = new StringBuilder();
            resultNpv.append(Math.round(nDC.getNPVValue()));
            tfResultNPV.setText(resultNpv.toString());
        } else if (event.getSource().equals(importFromExcell)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Excell File");
            System.out.println("Opening excell file");
//            Stage s = super.getStage();
            Stage s = (Stage) importFromExcell.getScene().getWindow();
            File file = fileChooser.showOpenDialog(s);

            XlsImporter xlsImporter = new XlsImporter(file, "#Ri");
            Double[] importedData = xlsImporter.importXls();
            for (int i = 0; i < importedData.length; i++) {
                npvdat = new NPVData(rowNumber, importedData[i]);
                rowNumber++;
                data.add(npvdat);
            }
        }
    }

    private boolean validateAlpha() {
        if (!this.tfAlpha.getText().contains(".")) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            periodId.setCellValueFactory(new PropertyValueFactory<>("periodId"));
            fundPerPeriod.setCellValueFactory(new PropertyValueFactory<>("fundPerPeriod"));
            fundWithAlpha.setCellValueFactory(new PropertyValueFactory<>("fundWithAlpha"));
            discountRateAlpha.setCellValueFactory(new PropertyValueFactory<>("discountRateAlpha"));
            netPresentValue.setCellValueFactory(new PropertyValueFactory<>("netPresentValue"));
            ControllerUtils.setTableColumnsDraggableFalse(npvTable);//for moving cells
            npvTable.setItems(data);
    }
}
