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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Connect4;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaSegundaPrincipalController implements Initializable {

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
    private void clickCerrarSesion(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
        HBox root = (HBox) cargador.load();
        VistaPrincipalController ventanaIni = cargador.<VistaPrincipalController>getController();
        ventanaIni.initStage(actualStage);
        Scene scene = new Scene(root, 800, 500);
        actualStage.setScene(scene);
        actualStage.show();
    }

    @FXML
    private void jugar(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJugar.fxml"));
        Parent root = cargador.load();
        cargador.<VistaJugarController>getController().initStage(actualStage, user);
        Scene escena = new Scene(root, 420, 145);
        actual.setScene(escena);
        actual.show();
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    @FXML
    private void editarPerfil(ActionEvent event) {
    }

    @FXML
    private void verRanking(ActionEvent event) {
    }
}
