/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Rafa BA, Raquel RR
 */
public class JavaFxConnect4 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        VistaPrincipalController controladorPrincipal = cargador.<VistaPrincipalController>getController();
        controladorPrincipal.initStage(stage);
        stage.setScene(scene);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
