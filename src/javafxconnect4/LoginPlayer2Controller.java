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
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class LoginPlayer2Controller implements Initializable {

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
    @FXML
    private Button iniciarButton;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;
    @FXML
    private HBox contenedorRaiz;

    private Stage actualStage;
    private Scene escenaActual;
    private Player player1, player2;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarButton.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return userCuadro.getText().split(" ").length == 0
                            && psswdCuadro.getText().split(" ").length == 0;
                }, userCuadro.textProperty(), psswdCuadro.textProperty()),
                Bindings.or(Bindings.isEmpty(userCuadro.textProperty()),
                        Bindings.isEmpty(psswdCuadro.textProperty()))));

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param player
     * @param theme
     */
    public void initStage(Stage stage, Player player, SimpleObjectProperty<Theme> theme) {
        actualStage = stage;
        escenaActual = stage.getScene();
        player1 = player;
        imagenJ1.imageProperty().setValue(player1.getAvatar());
        userJ1.setText(player.getNickName());
        pointsJ1.setText("" + player.getPoints());
        currentTheme = theme;
        setTheme();
    }

    private void setTheme() {
        // Cuando se cabie el modo de vsualización en otra ventana, se cambará en esta también.
        currentTheme.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Theme.DARK_THEME)) {
                setDark();
            } else {
                setLigth();
            }
        });

        switch (currentTheme.get()) {
            case DARK_THEME:
                setDark();
                break;
            case LIGTH_THEME:
                setLigth();
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }

    private void setLigth() {
        contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
        contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
        imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
        themeButton.setSelected(false);
    }

    private void setDark() {
        contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
        contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
        imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
        themeButton.setSelected(true);
    }

    @FXML
    private void clickInicioJ2(ActionEvent event) throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        // Comprobamos si el nombrede usuario introducido existe.
        if (!connect4.exitsNickName(userCuadro.getText()) || userCuadro.getText().equals(player1.getNickName())) {
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
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("JuegoPVP.fxml"));
                Parent root = cargador.load();
                JuegoPVPController ventanaIni = cargador.<JuegoPVPController>getController();
                ventanaIni.initStage(actualStage, player1, player2, currentTheme);
                Scene scene = new Scene(root, 800, 500);
                actualStage.setScene(scene);
                actualStage.show();
            }
        }
    }

    @FXML
    private void enterInicioJ2(KeyEvent event) throws Connect4DAOException, IOException {
        if (event.getCode().equals(KeyCode.ENTER) && !iniciarButton.isDisabled()) {
            clickInicioJ2(null);
        }
    }

    @FXML
    private void cancelarInicio(ActionEvent event) {
        actualStage.setScene(escenaActual);
    }
}
