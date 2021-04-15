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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 *
 * @author belen
 */
public class VistaPrincipalController implements Initializable {

    @FXML
    private Button botonIniciar;
    @FXML
    private Button btnNewAcc;
    @FXML
    private Button btnPasswd;
    @FXML
    private TextField user;
    @FXML
    private PasswordField passwd;

    private Stage stagePrincipal;
    @FXML
    private Label incorrecto;
    @FXML
    private CheckBox checkRecuerdame;
    private Scene escenaActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void autentificacionClick(ActionEvent event) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4(); // Necesario para usar la libreria.
        if (!connect4.exitsNickName(user.getText())) {
            incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
        } else {
            Player login = connect4.getPlayer(user.getText());
            if (!login.getPassword().equals(passwd.getText())) {
                incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
            } else {
                if (!incorrecto.getText().equals("")) {
                    incorrecto.setText("");
                }
                try {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
                    HBox root = (HBox) cargador.load();

                    VistaSegundaPrincipalController ventana2 = cargador.<VistaSegundaPrincipalController>getController();
                    ventana2.initStage(stagePrincipal, user.getText(), passwd.getText());
                    ventana2.nombreUsuario.setText(user.getText());
                    Scene scene = new Scene(root, 800, 500);
                    stagePrincipal.setScene(scene);
                    stagePrincipal.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void cambioColorBoton(MouseEvent event) {
        /* cambio de color en los botones al pasar por encima(Ni idea de como hacerlo de momento)*/

    }

    @FXML
    private void passwdOlvidada(ActionEvent event) {
        /*Cambio a ventana de password olvidada*/
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPsswdOlvidada.fxml"));
            Parent root = cargador.load();
            cargador.<VistaPsswdOlvidadaController>getController().initStage(actual);
            Scene escena = new Scene(root, 650, 375);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irCrearCuenta(ActionEvent event) {
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaAñadirUsuario.fxml"));
            Parent root = cargador.load();
            cargador.<VistaAñadirUsuarioController>getController().initStage(actual);
            Scene escena = new Scene(root, 800, 500);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
            stagePrincipal.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initStage(Stage stage) {
        stagePrincipal = stage;
        escenaActual = stage.getScene();
    }

    @FXML
    private void clickRecuerdo(ActionEvent event) {
        if (checkRecuerdame.isSelected()) {
            //función recuerdame
        } else {

        }
    }
}
