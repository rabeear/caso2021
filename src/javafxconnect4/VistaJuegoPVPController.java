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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaJuegoPVPController implements Initializable {

    @FXML
    private Label labelJugador;
    @FXML
    private Label labelPuntuacion;
    @FXML
    private Label indicadorPruebas;
    @FXML
    private Button btnReinicio;
    @FXML
    private GridPane tableroGrid;
    @FXML
    private Label labelPuntuacion2;

    private Stage stageActual;
    private Scene escenaActual;
    private Player j1, j2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
    }

    @FXML
    private void clickJugar(MouseEvent event) {
    }

    void initStage(Stage actualStage, Player player1, Player player2) {
        stageActual = actualStage;
        escenaActual = actualStage.getScene();
        j1 = player1;
        j2 = player2;
    }

    @FXML
    private void clickSalir(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
            HBox root = (HBox) cargador.load();

            VistaSegundaPrincipalController ventana2 = cargador.<VistaSegundaPrincipalController>getController();
            ventana2.initStage(stageActual, j1.getNickName(), j1.getPassword());
            ventana2.nombreUsuario.setText(j1.getNickName());
            Scene scene = new Scene(root, 800, 500);
            stageActual.setScene(scene);
            stageActual.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
