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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaPrincipalController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private PasswordField passwd;
    @FXML
    private Label incorrecto;

    private Stage stagePrincipal;
    private Scene escenaActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void autentificacionClick(ActionEvent event) throws Connect4DAOException {
        autentificacion();
    }

    @FXML
    private void autentificacionEnter(KeyEvent event) throws Connect4DAOException {
        // Para poder iniciar sesión pulsando enter.
        if (event.getCode().equals(KeyCode.ENTER)) {
            autentificacion();
        }
    }

    private void autentificacion() throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        // Comprobamos que exista el nombre de usuario introducido.
        if (!connect4.exitsNickName(user.getText())) {
            incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
        } else {
            Player login = connect4.getPlayer(user.getText());
            // Comprobamos que la contraseña sea correcta.
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
                } catch (Connect4DAOException | IOException e) {
                }
            }
        }
    }

    @FXML
    private void passwdOlvidada(ActionEvent event) {
        // Cambio a ventana de password olvidada.
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPsswdOlvidada.fxml"));
            Parent root = cargador.load();
            cargador.<VistaPsswdOlvidadaController>getController().initStage(actual);
            Scene escena = new Scene(root, 650, 375);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
        } catch (IOException e) {
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
        } catch (IOException e) {
        }
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     */
    public void initStage(Stage stage) {
        stagePrincipal = stage;
        escenaActual = stage.getScene();
    }
}
