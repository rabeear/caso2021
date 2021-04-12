/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaSegundaPrincipalController implements Initializable {
    private Stage actualStage;
    private Scene escenaActual;
    @FXML
    private Button btnUnJugador;
    @FXML
    private Button btnDosJugadores;
    @FXML
    private Button btnCerrarSesion;
    private String user, password;
    @FXML
    public Label nombreUsuario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void initStage(Stage stage, String usr, String passwd) {
        actualStage = stage;
        escenaActual = stage.getScene();
        user = usr;
        password = passwd;
    }

    @FXML
    private void clickCerrarSesion(ActionEvent event) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        Player actual = connect4.loginPlayer(user, password);
        
        
        
    }
    
}
