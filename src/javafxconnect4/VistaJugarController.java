/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class VistaJugarController implements Initializable {

    private Stage actualStage;
    private String user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initStage(Stage actual, String usuario) {
        actualStage = actual;
        user = usuario;
    }

    @FXML
    private void jugarPVE(ActionEvent event) throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJuegoPVE.fxml"));
        Pane root = (Pane) cargador.load();
        Player actualPlayer = connect4.getPlayer(user);
        VistaJuegoPVEController ventanaJuegoPVE = cargador.<VistaJuegoPVEController>getController();
        ventanaJuegoPVE.initStage(actualStage, actualPlayer);
        Scene scene = new Scene(root, 800, 500);
        actualStage.setScene(scene);
        actualStage.show();
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    @FXML
    private void jugarPVP(ActionEvent event) throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaInicioSesionSegundoJugador.fxml"));
        HBox root = (HBox) cargador.load();
        Player actualPlayer = connect4.getPlayer(user);
        VistaInicioSesionSegundoJugadorController ventanaIni = cargador.<VistaInicioSesionSegundoJugadorController>getController();
        ventanaIni.initStage(actualStage, actualPlayer);
        Scene scene = new Scene(root, 800, 500);
        actualStage.setScene(scene);
        actualStage.show();
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Falta implementarlo
    }
}
