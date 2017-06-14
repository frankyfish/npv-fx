/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 *
 * @author nima0814
 */
public class GUIConstants implements Initializable {

    //Layouts
    public static final String FXML_ROOT_LAYOUT = String.valueOf("/npv/fx/layouts/MainMenu.fxml");
    public static final String FXML_NPV_LAYOUT = String.valueOf("/npv/fx/layouts/NPV.fxml");
    public static final String FXML_QUEUES_LAYOUT = String.valueOf("/npv/fx/layouts/Queues.fxml");
    public static final String FXML_PROFIT_FLOW_LAYOUT = String.valueOf("/npv/fx/layouts/ProfitFlow.fxml");
    public static final String FXML_ABOUT_WINDOW_LAYOUT = String.valueOf("/npv/fx/layouts/About.fxml");
    public static final String FXML_HELP_WINDOW_LAYOUT = String.valueOf("/npv/fx/layouts/Help.fxml");

    public static final String CSS_GANTT_CHART = String.valueOf("/npv/fx/styles/ganttchart.css");

    public static final String APP_ICON = String.valueOf("icons/tree-leave.png");

    //Queues.fxml
    //choicebox values
    public static final String ALGORITHM_I = "Algorithm I";
    public static final String ALGORITHM_II = "Algorithm II";
    public static final String ALGORITHM_III = "Algorithm III";

    //NPV GUI properties
    public static int numberOfRows;

    public static int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
   
}
