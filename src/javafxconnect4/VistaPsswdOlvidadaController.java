/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaPsswdOlvidadaController implements Initializable {

    @FXML
    private TextField campoCorreo;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField campoUser;
    @FXML
    private Label incorrecto;
    @FXML
    private Button btnEnviar;

    private Stage actualPasswd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnEnviar.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return campoCorreo.getText().trim().length() == 0
                            || campoUser.getText().trim().length() == 0;
                }, campoCorreo.textProperty(), campoUser.textProperty()),
                Bindings.or(Bindings.isEmpty(campoCorreo.textProperty()),
                        Bindings.isEmpty(campoUser.textProperty()))));
    }

    @FXML
    private void enviarCorreo(ActionEvent event) throws Connect4DAOException {
        // Compobamos si el nombre de usuario es correcto.
        Connect4 connect4 = Connect4.getSingletonConnect4();
        if (!connect4.exitsNickName(campoUser.getText())) {
            incorrecto.setText("Nombre de usuario incorrecto.");
        } else {
            if (!incorrecto.getText().equals("")) {
                incorrecto.setText("");
            }
            /*Cambio a ventana de password olvidada*/
            try {
                Stage actual = new Stage();
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCodigoRecuperacion.fxml"));
                Parent root = cargador.load();
                cargador.<VistaCodigoRecuperacionController>getController().initStage(actual, campoUser.getText(), actualPasswd);
                Scene escena = new Scene(root, 410, 225);
                actual.setScene(escena);
                actual.initModality(Modality.APPLICATION_MODAL);
                actual.show();
                /*Cierra ventana actual*/
                Node miNodo = (Node) event.getSource();
                miNodo.getScene().getWindow().hide();
                System.out.println("Cerrando PSSWDOlvidada...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void opcionCancelar(ActionEvent event) {
        /*Cierra ventana actual*/
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
        System.out.println("Cerrando PSSWDOlvidada...");
    }

    public void initStage(Stage stage) {
        /*Iniciador para usar en el cambio de ventana*/
        actualPasswd = stage;
        actualPasswd.setTitle("Recuperación de contraseña");
        actualPasswd.setResizable(false);
    }
}
