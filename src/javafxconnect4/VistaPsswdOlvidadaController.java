/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaPsswdOlvidadaController implements Initializable {

    @FXML
    private TextField campoCorreo;
    @FXML
    private Button btnEvniar;
    @FXML
    private Button btnCancelar;

    private Stage actualPasswd;
    @FXML
    private TextField campoUser;

    public void initStage(Stage stage) {
        /*Iniciador para usar en el cambio de ventana*/
        actualPasswd = stage;
        actualPasswd.setTitle("Recuperación de contraseña");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void enviarCorreo(ActionEvent event) {
        /*No se si se usara como tal asi que de momento nada*/
    }

    @FXML
    private void opcionCancelar(ActionEvent event) {
        /*Cierra ventana actual*/
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
        System.out.println("Cerrando PSSWDOlvidada...");
    }
}
