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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaEditarPerfilController implements Initializable {

    private Stage actualStage;
    private Scene escenaActual;
    @FXML
    private Label labelUsuario;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private TextField cuadroPswd;
    @FXML
    private TextField cuadroMail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickCambiar(ActionEvent event) {
    }

    @FXML
    private void clickConfirmar(ActionEvent event) {
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
    }
    
    void initStage(Stage stage) {
        actualStage = stage;
        escenaActual = stage.getScene();
    }
}
