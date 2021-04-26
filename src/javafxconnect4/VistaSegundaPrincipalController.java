/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaSegundaPrincipalController implements Initializable {

    private Stage actualStage;
    private Scene escenaActual;
    @FXML
    private Button btnUnJugador;
    @FXML
    private Button btnDosJugadores;
    @FXML
    private Button btnCerrarSesion;
    private String user, password;
    @FXML
    public Label nombreUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void initStage(Stage stage, String usr, String passwd) {
        actualStage = stage;
        escenaActual = stage.getScene();
        user = usr;
        password = passwd;
    }

    @FXML
    private void clickCerrarSesion(ActionEvent event) {
        try {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
            HBox root = (HBox) cargador.load();
            VistaPrincipalController ventanaIni = cargador.<VistaPrincipalController>getController();
            ventanaIni.initStage(actualStage);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickJuagarPVE(ActionEvent event) throws Connect4DAOException {
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();

            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJuegoPVE.fxml"));
            Pane root = (Pane) cargador.load();
            Player actualPlayer = connect4.getPlayer(user);
            VistaJuegoPVEController ventanaJuegoPVE = cargador.<VistaJuegoPVEController>getController();
            ventanaJuegoPVE.initStage(actualStage, actualPlayer);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clickJugarPVP(ActionEvent event) throws Connect4DAOException {
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();

            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaInicioSesionSegundoJugador.fxml"));
            HBox root = (HBox) cargador.load();
            Player actualPlayer = connect4.getPlayer(user);
            VistaInicioSesionSegundoJugadorController ventanaIni = cargador.<VistaInicioSesionSegundoJugadorController>getController();
            ventanaIni.initStage(actualStage, actualPlayer);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
