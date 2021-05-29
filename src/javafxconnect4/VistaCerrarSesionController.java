/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaCerrarSesionController implements Initializable {

    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    private Stage stageActual;
    private Scene escenaActual;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initStage(Stage stage, Player j1, Player j2) {
        btn1.setText(j1.getNickName());
        btn2.setText(j2.getNickName());
    }
    
}
