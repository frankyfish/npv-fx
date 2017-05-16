package npv.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import npv.data.PlanData;
import npv.fx.GUIManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

/**
 * Created by nick on 25.04.17.
 */
public class NavigationController extends GUIManager implements Initializable {

    private LinkedHashMap<Integer, ArrayList<PlanData>> calendarPlans = new LinkedHashMap<>();
    //control panel
    @FXML private Button returnToRoot;

    public Parent loadLayout(URL url) throws IOException {
        Parent layout = FXMLLoader.load(url);
        return layout;
    }

    @FXML
    public void handleControlButtonAction(ActionEvent event)
            throws Exception, IOException {
        if (event.getSource().equals(returnToRoot)) {
//            URL url = returnToRoot.getParent().getClass().getResource("MainMenu.fxml");
//            Parent fxml = loadLayout(url);
//            Stage stage = (Stage) returnToRoot.getScene().getWindow();
//            ControllerUtils.goToNewScene(getRoot(), getStage());
            Stage rootStage = getStage();
            rootStage.setScene(getScene());
            rootStage.show();
        }
    }



    protected void setCalendarPlans(LinkedHashMap<Integer, ArrayList<PlanData>> calendarPlans) {
        this.calendarPlans.putAll(calendarPlans);
    }

    protected LinkedHashMap<Integer, ArrayList<PlanData>> getCalendarPlans() {
        return calendarPlans;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
