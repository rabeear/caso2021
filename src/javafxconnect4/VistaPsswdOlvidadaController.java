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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaPsswdOlvidadaController implements Initializable {

    @FXML
    private TextField campoCorreo;
    @FXML
    private TextField campoUser;
    @FXML
    private Label incorrecto;
    @FXML
    private Button btnEnviar;
    @FXML
    private VBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;

    private Stage ventanaActual;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTheme = new SimpleObjectProperty<>();
        btnEnviar.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return campoCorreo.getText().trim().length() == 0
                            || campoUser.getText().trim().length() == 0;
                }, campoCorreo.textProperty(), campoUser.textProperty()),
                Bindings.or(Bindings.isEmpty(campoCorreo.textProperty()),
                        Bindings.isEmpty(campoUser.textProperty()))));

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (themeButton.isSelected()) {
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                currentTheme.set(Theme.DARK_THEME);
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
            } else {
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                currentTheme.set(Theme.LIGTH_THEME);
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
            }
        });
    }

    @FXML
    private void enviarCorreo(ActionEvent event) throws Connect4DAOException {
        // Compobamos si el nombre de usuario es correcto.
        Connect4 connect4 = Connect4.getSingletonConnect4();
        if (!connect4.exitsNickName(campoUser.getText())) {
            incorrecto.setText("Nombre de usuario incorrecto.");
        } else {
            Player jugador = connect4.getPlayer(campoUser.getText());
            if (!jugador.getEmail().equals(campoCorreo.getText())) {
                incorrecto.setText("Correo electrónico incorrecto.");
            } else {
                if (!incorrecto.getText().equals("")) {
                    incorrecto.setText("");
                }
                /*Cambio a ventana de password olvidada*/
                try {
                    Stage actual = new Stage();
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCodigoRecuperacion.fxml"));
                    Parent root = cargador.load();
                    cargador.<VistaCodigoRecuperacionController>getController().initStage(campoUser.getText(), ventanaActual);
                    Scene escena = new Scene(root, 410, 225);
                    actual.setScene(escena);
                    actual.setTitle("Código de recuperación");
                    actual.setResizable(false);
                    actual.initModality(Modality.APPLICATION_MODAL);
                    actual.show();
                    /*Cierra ventana actual*/
                    Node miNodo = (Node) event.getSource();
                    miNodo.getScene().getWindow().hide();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Iniciador para usar en el cambio de ventana
     *
     * @param stage
     * @param usuario
     * @param theme
     */
    public void initStage(Stage stage, String usuario, SimpleObjectProperty<Theme> theme) {
        ventanaActual = stage;
        campoUser.setText(usuario);
        currentTheme = theme;
        switch (currentTheme.get()) {
            case DARK_THEME:
                themeButton.setSelected(true);
                break;
            case LIGTH_THEME:
                themeButton.setSelected(false);
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }

    @FXML
    private void cerrar(ActionEvent event) {
        // Cierra ventana actual
        Node miNodo = (Node) event.getSource();
        ((Stage) miNodo.getScene().getWindow()).close();
    }
}
