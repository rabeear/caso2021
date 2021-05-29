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
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaPrincipalController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private PasswordField passwd;
    @FXML
    private Label incorrecto;
    @FXML
    private Button loginButton;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private HBox contenedorRaiz;
    @FXML
    private ImageView imagenTema;

    private Stage stagePrincipal;
    private SimpleObjectProperty<Theme> currentTheme;
    @FXML
    private ToggleButton visibleButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTheme = new SimpleObjectProperty<>(Theme.LIGTH_THEME);
        // Si el campo del nickname o la contraseña están vacíos, deshabilitar el botón.
        loginButton.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return user.getText().split(" ").length == 0
                            && passwd.getText().split(" ").length == 0;
                }, user.textProperty(), passwd.textProperty()),
                Bindings.or(Bindings.isEmpty(user.textProperty()),
                        Bindings.isEmpty(passwd.textProperty()))));

        // Cuando se cabie el modo de vsualización en otra ventana, se cambará en esta también.
        currentTheme.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Theme.DARK_THEME)) {
                setDark();
            } else {
                setLigth();
            }
        });

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });
        
        visibleButton.selectedProperty().addListener((observable, oldValue, newValue) ->{
            if (newValue) {
                //cuando vuelva lo haago
            } else {
                //currentTheme.set(Theme.LIGTH_THEME);
            }
        });
    }

    @FXML
    private void autentificacionClick(ActionEvent event) throws Connect4DAOException, IOException {
        autentificacion();
    }

    @FXML
    private void autentificacionEnter(KeyEvent event) throws Connect4DAOException, IOException {
        // Para poder iniciar sesión pulsando enter.
        if (event.getCode().equals(KeyCode.ENTER) && !loginButton.isDisabled()) {
            autentificacion();
        }
    }

    private void autentificacion() throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        // Comprobamos que exista el nombre de usuario introducido.
        if (!connect4.exitsNickName(user.getText())) {
            incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
        } else {
            Player login = connect4.getPlayer(user.getText());
            // Comprobamos que la contraseña sea correcta.
            if (!login.getPassword().equals(passwd.getText())) {
                incorrecto.setText("Nombre de usuario o contraseña incorrectos.");
            } else {
                if (!incorrecto.getText().equals("")) {
                    incorrecto.setText("");
                }
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
                Parent root = cargador.load();
                VistaSegundaPrincipalController ventana2 = cargador.<VistaSegundaPrincipalController>getController();
                ventana2.initStage(stagePrincipal, user.getText(), currentTheme);
                ventana2.nombreUsuario.setText(user.getText());
                Scene scene = new Scene(root, 800, 500);
                stagePrincipal.setScene(scene);
                stagePrincipal.setTitle("Conecta4");
                stagePrincipal.show();
            }
        }
    }

    @FXML
    private void passwdOlvidada(ActionEvent event) throws IOException {
        // Cambio a ventana de password olvidada.
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPsswdOlvidada.fxml"));
        Parent root = cargador.load();
        cargador.<VistaPsswdOlvidadaController>getController().initStage(actual, user.getText(), currentTheme);
        Scene escena = new Scene(root, 650, 375);
        actual.setScene(escena);
        actual.setTitle("Recuperación de contraseña");
        actual.setResizable(false);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
    }

    @FXML
    private void irCrearCuenta(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaAñadirUsuario.fxml"));
        Parent root = cargador.load();
        cargador.<VistaAñadirUsuarioController>getController().initStage(actual, currentTheme);
        Scene escena = new Scene(root, 520, 540);
        actual.setScene(escena);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.setTitle("Registro");
        actual.setResizable(false);
        actual.show();
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param theme
     */
    public void initStage(Stage stage, SimpleObjectProperty<Theme> theme) {
        stagePrincipal = stage;
        if (theme != null) {
            currentTheme = theme;
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
}
