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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaContraseñaController implements Initializable {

    @FXML
    private Button okbutton;
    @FXML
    private Label campoPasswrd;

    private Stage ventanaPasswrd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void cerrar(ActionEvent event) {
        /*Cierra ventana actual*/
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    public void initStage(Stage stage, String passwrd) {
        /*Iniciador para usar en el cambio de ventana*/
        ventanaPasswrd = stage;
        ventanaPasswrd.setTitle("Contraseña");
        ventanaPasswrd.setResizable(false);
        // Enseñamos la contraseña por pantalla.
        campoPasswrd.setText(passwrd);
    }
}
