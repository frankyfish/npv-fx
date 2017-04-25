package npv.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import npv.fx.GUIConstants;
import npv.fx.GUIManager;
import npv.fx.controllers.utils.ControllerUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by nick on 25.04.17.
 */
public class MainMenuController extends NavigationController implements Initializable {

    @FXML private Button queues;
    @FXML private Button npv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception, IOException {
        Stage currentStage = null;
        Parent layout = null;
        //todo change button1 block to another, useful one
        if (event.getSource().equals(queues)) {
            layout = loadLayout(getClass().getResource(GUIConstants.FXML_QUEUES_LAYOUT));
            currentStage = (Stage) queues.getScene().getWindow();
            ControllerUtils.goToNewScene(layout, currentStage);
        } else if (event.getSource().equals(npv)) {
            layout = loadLayout(getClass().getResource(GUIConstants.FXML_NPV_LAYOUT));
            currentStage = (Stage) npv.getScene().getWindow();
            ControllerUtils.goToNewScene(layout, currentStage);
        }
    }
}
