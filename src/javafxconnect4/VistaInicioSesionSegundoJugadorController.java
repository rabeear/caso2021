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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaInicioSesionSegundoJugadorController implements Initializable {

    @FXML
    private Text labelJ1;
    @FXML
    private Label userJ1;
    @FXML
    private Label pointsJ1;
    @FXML
    private TextField userCuadro;
    @FXML
    private PasswordField psswdCuadro;

    private Stage actualStage;
    private Scene escenaActual;
    private Player player1, player2;
    @FXML
    private Label labelIndicador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initStage(Stage stage, Player player) throws Connect4DAOException {
        actualStage = stage;
        escenaActual = stage.getScene();
        player1 = player;
        userJ1.setText(player.getNickName());
        pointsJ1.setText("" + player.getPoints());

    }

    @FXML
    private void clickInicioJ2(ActionEvent event) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        try {
            Player loginTest = connect4.getPlayer(userCuadro.getText());

            if (loginTest.getPassword().equals(psswdCuadro.getText()) && loginTest != player1) {
                player2 = loginTest;
                try {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJuegoPVP.fxml"));
                    Pane root = (Pane) cargador.load();
                    VistaJuegoPVPController ventanaIni = cargador.<VistaJuegoPVPController>getController();
                    ventanaIni.initStage(actualStage, player1, player2);
                    Scene scene = new Scene(root, 800, 500);
                    actualStage.setScene(scene);
                    actualStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            labelIndicador.setText("Usuario o contraseña incorrecto");
        }
    }

    @FXML
    private void cancelarInicio(ActionEvent event) {
        actualStage.setScene(escenaActual);
    }
}
