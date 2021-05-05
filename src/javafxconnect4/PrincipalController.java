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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
public class PrincipalController implements Initializable {

    @FXML
    public Label nombreUsuario;
    @FXML
    private ImageView foto;

    private Stage actualStage;
    private String user;
    private Scene escenaActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param usr
     * @param passwd
     * @throws Connect4DAOException
     */
    public void initStage(Stage stage, String usr, String passwd) throws Connect4DAOException {
        actualStage = stage;
        escenaActual = stage.getScene();
        user = usr;
        Connect4 connect4 = Connect4.getSingletonConnect4();
        foto.imageProperty().setValue(connect4.getPlayer(user).getAvatar());
    }

    @FXML
    private void clickCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaLogin.fxml"));
            HBox root = (HBox) cargador.load();
            LoginController ventanaIni = cargador.<LoginController>getController();
            ventanaIni.initStage(actualStage);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void clickJuagarPVE(ActionEvent event) throws Connect4DAOException {
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPVE.fxml"));
            Pane root = (Pane) cargador.load();
            Player actualPlayer = connect4.getPlayer(user);
            PVEController ventanaJuegoPVE = cargador.<PVEController>getController();
            ventanaJuegoPVE.initStage(actualStage, actualPlayer);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (Connect4DAOException | IOException e) {
        }
    }

    @FXML
    private void clickJugarPVP(ActionEvent event) throws Connect4DAOException {
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundoLogin.fxml"));
            HBox root = (HBox) cargador.load();
            Player actualPlayer = connect4.getPlayer(user);
            SegundoLoginController ventanaIni = cargador.<SegundoLoginController>getController();
            ventanaIni.initStage(actualStage, actualPlayer);
            Scene scene = new Scene(root, 800, 500);
            actualStage.setScene(scene);
            actualStage.show();
        } catch (Connect4DAOException | IOException e) {
        }
    }
}