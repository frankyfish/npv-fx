package npv.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import npv.fx.FXMLDocumentController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by nick on 08.04.17.
 */

public class ProfitFlowController implements Initializable {

    @FXML private Button btClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleControlActions(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btClose)) {
            Stage stage = (Stage) btClose.getScene().getWindow();
            if (FXMLDocumentController.isNewWindowCreated()) {
                FXMLDocumentController.setNewWindowCreated(false);
            }
            stage.close();
        }
    }
}