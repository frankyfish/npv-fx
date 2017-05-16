package npv.fx.controllers.utils;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by nick on 24.04.17.
 */
public class ControllerUtils {

    public static void setTableColumnsDraggableFalse(TableView tableView) {
        tableView.widthProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
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

    public static void clearTableBeforeShowCountResults(TableView table) {
        ObservableList tableDataForErase = table.getItems();
        tableDataForErase.clear();
        table.setItems(tableDataForErase);
    }

    public static void goToNewScene(Parent fxml, Stage stage) {
        Scene scene = new Scene(fxml);
        stage.setScene(scene);
        stage.show();
    }

    public static void goToNewScene(Pane pane, Stage stage) {
        stage.setScene(new Scene(pane));
        stage.show();
    }

}
