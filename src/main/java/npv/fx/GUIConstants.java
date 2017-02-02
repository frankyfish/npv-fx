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
    public static final String FXML_ROOT_LAYOUT = String.valueOf("FXMLDocument");
    public static final String FXML_NPV_LAYOUT = String.valueOf("NPV");
    public static final String FXML_QUEUES_LAYOUT = String.valueOf("Queues");

    //NPV GUI properties
    public static int numberOfRows;

    public static int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
   
}
