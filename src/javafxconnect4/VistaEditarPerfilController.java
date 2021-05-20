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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

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
    private Player player;

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
    private void clickCancelar(ActionEvent event) throws IOException, Connect4DAOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
        Parent root = cargador.load();
        cargador.<VistaSegundaPrincipalController>getController().initStage(actual, labelUsuario.getText());
        Scene escena = new Scene(root, 800, 500);
        actual.setScene(escena);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
        actualStage.close();
    }
    
    void initStage(Stage stage, String user) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        
        actualStage = stage;
        escenaActual = stage.getScene();
        labelUsuario.setText(user);
        player = connect4.getPlayer(user);
        imgAvatar.imageProperty().setValue(player.getAvatar());
    }
}
