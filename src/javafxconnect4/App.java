/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Connect4;

/**
 *
 * @author Rafa BA, Raquel RR
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = cargador.load();
        Scene scene = new Scene(root);

        LoginController controladorPrincipal = cargador.<LoginController>getController();
        controladorPrincipal.initStage(stage, null);
        stage.setScene(scene);
        stage.setMinHeight(480);
        stage.setMinWidth(635);
        stage.setTitle("Conecta4");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();
            connect4.removeAllData();
            connect4.createDemoData(50, 4, 30);
            connect4.createDemoData(5, 4, 10);
            connect4.createDemoData(10, 5, 15);
        } catch (Connect4DAOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        launch(args);
    }
}
