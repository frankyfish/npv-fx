package npv.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController extends NavigationController implements Initializable {

    @FXML private Button btClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

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
