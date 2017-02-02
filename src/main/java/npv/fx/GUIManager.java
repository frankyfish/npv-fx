/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nima0814
 */
public class GUIManager extends Application {

    private static Parent root; //todo do i need static here?! What for? Refactor!
    private static Stage stage;
    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        buildRootScene();
    }

    private void buildRootScene() throws IOException {
        root = FXMLLoader.load(getClass().getResource("layouts/FXMLDocument.fxml"));
        //root = FXMLLoader.load(getClass().getResource("layouts/NPV.fxml"));
        scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public static Scene getScene() {
        return scene;
    }

    public static Parent getRoot() {
        return root;
    }
}
