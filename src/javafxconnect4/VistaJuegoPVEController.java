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
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaJuegoPVEController implements Initializable {

    private Stage stageJuegoPVE;
    private Scene escenaJuegoPVE;
    private Player jugadorActual;
    private boolean turno = true; //True -> Player / False -> Ordenador
    @FXML
    private Button btnReinicio;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initStage(Stage stage, Player seleccion) {
        stageJuegoPVE = stage;
        escenaJuegoPVE = stage.getScene();
        jugadorActual = seleccion;
    }

    @FXML
    private void clickSalirPartida(ActionEvent event) {
        stageJuegoPVE.setScene(escenaJuegoPVE);
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        
    }
    
    private boolean switcherTurno() {
        turno = !turno;
        return turno;
    }
}
