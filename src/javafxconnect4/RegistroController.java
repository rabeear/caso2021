/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class RegistroController implements Initializable {

    @FXML
    private Button btnAvatar;
    private Stage stageActual;
    private Scene escenaPrincipal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clickAvatar(ActionEvent event) {
        // Llevar a zona de cambio de avatar.
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCambiarAvatar.fxml"));
            Parent root = cargador.load();
            cargador.<CambiarAvatarController>getController().initStage(actual);
            Scene escena = new Scene(root, 650, 375);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
        } catch (IOException e) {
        }
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     */
    public void initStage(Stage stage) {
        stageActual = stage;
        escenaPrincipal = stage.getScene();
    }
}