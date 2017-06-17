package npv.fx.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import npv.fx.GUIConstants;
import npv.fx.GUIManager;
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
import npv.fx.notifications.UINotification;
import npv.importer.XlsImporter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static npv.fx.GUIConstants.APP_ICON;

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
    @FXML private TextField tfDecimalPlaces;
    @FXML private Button btHelp;
    private Double preInitializeAlpaValue;
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

    public NPVController(){}

    public NPVController(int numberOfRows, List<? extends NPVData> newData) {
        this.rowNumber = numberOfRows;
        this.data = FXCollections.observableArrayList(newData);
    }

    public NPVController(int numberOfRows, List<? extends NPVData> newData, Double percentage) {
        this.rowNumber = numberOfRows;
        this.data = FXCollections.observableArrayList(newData);
        if (percentage != null) {
            preInitializeAlpaValue = percentage;
        }
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
            if (tfAlpha.getText().isEmpty()) {
                new UINotification(UINotification.Type.ERROR, "Alpha value is not specified", "Alpha");
                return;
            }
            validateDecimalPlaces();
            Double alpha = Double.valueOf(tfAlpha.getText());

            NPVDataCounter nDC =
                    new NPVDataCounter(npvTable, rowNumber, alpha);
            nDC.countNpv();
            ArrayList<NPVData> npvDat = new ArrayList<>();
            ControllerUtils.clearTableBeforeShowCountResults(npvTable);
            nDC.round(Integer.valueOf(tfDecimalPlaces.getText()));
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
        } else if (event.getSource().equals(btHelp)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(GUIConstants.FXML_HELP_NPV_WINDOW_LAYOUT));
                HelpQueuesController controller = new HelpQueuesController();
                loader.setController(controller);
                AnchorPane pane = loader.load();
                Stage helpStage = new Stage();
                Scene helpScene = new Scene(pane);
                helpStage.setScene(helpScene);
                helpStage.setTitle("Help");
                helpStage.getIcons().add(new Image(GUIManager.class.getResourceAsStream(APP_ICON)));
                helpStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void validateDecimalPlaces() {
        if (tfDecimalPlaces.getText().isEmpty()) {
            tfDecimalPlaces.setText("2");//setting default value
        } else if (Integer.valueOf(tfDecimalPlaces.getText()) > 12){
            new UINotification(UINotification.Type.INFO, "The maximum accuracy is reached.",
                    "accuracy will be ignored, highest possible will be used");
        }
    }

    private boolean validateAlpha() {
        if (this.tfAlpha.getText().isEmpty()) {
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

            if (preInitializeAlpaValue != null) {
                tfAlpha.setText(preInitializeAlpaValue.toString());
            }
    }
}
