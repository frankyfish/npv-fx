package npv.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    @FXML private Button btAbout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception, IOException {
        Stage currentStage = null;
        Parent layout = null;
        if (event.getSource().equals(queues)) {
            layout = loadLayout(getClass().getResource(GUIConstants.FXML_QUEUES_LAYOUT));
            currentStage = (Stage) queues.getScene().getWindow();
            ControllerUtils.goToNewScene(layout, currentStage);
        } else if (event.getSource().equals(npv)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GUIConstants.FXML_NPV_LAYOUT));
            NPVController controller = new NPVController();
            loader.setController(controller);
            AnchorPane pane = loader.load();
            currentStage = (Stage) npv.getScene().getWindow();
            ControllerUtils.goToNewScene(pane, currentStage);
        } else if (event.getSource().equals(btAbout)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GUIConstants.FXML_ABOUT_WINDOW_LAYOUT));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            Scene aboutScene = new Scene(pane);
            stage.setScene(aboutScene);
            stage.show();
        }
    }
}
