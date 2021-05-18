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
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label userJ1;
    @FXML
    private Label pointsJ1;
    @FXML
    private TextField userCuadro;
    @FXML
    private PasswordField psswdCuadro;
    @FXML
    private ImageView imagenJ1;
    @FXML
    private Label incorrecto;

    private Stage actualStage;
    private Scene escenaActual;
    private Player player1, player2;
    @FXML
    private Button iniciarButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        iniciarButton.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return userCuadro.getText().split(" ").length == 0
                            && psswdCuadro.getText().split(" ").length == 0;
                }, userCuadro.textProperty(), psswdCuadro.textProperty()),
                Bindings.or(Bindings.isEmpty(userCuadro.textProperty()),
                        Bindings.isEmpty(psswdCuadro.textProperty()))));
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param player
     */
    public void initStage(Stage stage, Player player) {
        actualStage = stage;
        escenaActual = stage.getScene();
        player1 = player;
        imagenJ1.imageProperty().setValue(player1.getAvatar());
        userJ1.setText(player.getNickName());
        pointsJ1.setText("" + player.getPoints());
    }

    @FXML
    private void clickInicioJ2(ActionEvent event) throws Connect4DAOException {
        inicioJ2();
    }

    @FXML
    private void enterInicioJ2(KeyEvent event) throws Connect4DAOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            inicioJ2();
        }
    }

    private void inicioJ2() throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        // Comprobamos si el nombrede usuario introducido existe.
        if (!connect4.exitsNickName(userCuadro.getText())) {
            incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
        } else {
            Player login = connect4.getPlayer(userCuadro.getText());
            // Comprobamos si la contraseña es correcta.
            if (!login.getPassword().equals(psswdCuadro.getText())) {
                incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
            } else {
                // Limpiamos la etiqueta si hay algo escrito.
                if (!incorrecto.getText().equals("")) {
                    incorrecto.setText("");
                }
                player2 = login;
                // Cargamos la ventana del juego.
                try {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJuegoPVP.fxml"));
                    Parent root = cargador.load();
                    VistaJuegoPVPController ventanaIni = cargador.<VistaJuegoPVPController>getController();
                    ventanaIni.initStage(actualStage, player1, player2);
                    Scene scene = new Scene(root, 800, 500);
                    actualStage.setScene(scene);
                    actualStage.show();
                } catch (IOException e) {
                }
            }
        }
    }

    @FXML
    private void cancelarInicio(ActionEvent event) {
        actualStage.setScene(escenaActual);
    }
}
