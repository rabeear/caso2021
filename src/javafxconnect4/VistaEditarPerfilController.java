/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

    @FXML
    private Label labelUsuario;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private TextField cuadroPswd;
    @FXML
    private TextField cuadroMail;
    @FXML
    private Button btnConfirmar;
    @FXML
    private HBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;
    @FXML
    private DatePicker date;
    @FXML
    private Label psswdError;
    @FXML
    private Label emailError;
    @FXML
    private Label dateError;
    @FXML
    private PasswordField confirmarPsswd;

    private Stage actualStage;
    private Player player;
    private SimpleObjectProperty<Theme> currentTheme;
    private Connect4 connect4;
    private VistaEditarPerfilController controllerActual;
    private VistaSegundaPrincipalController controllerVentanaAnt;
    private boolean avatarChanged;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        avatarChanged = false;

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });

        try {
            connect4 = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaEditarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void clickCambiar(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCambiar.fxml"));
        Parent root = cargador.load();
        cargador.<VistaCambiarController>getController().initStageEditar(actual, player, currentTheme, controllerActual);
        Scene escena = new Scene(root);
        actual.setResizable(false);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.setScene(escena);
        actual.show();
    }

    @FXML
    private void clickConfirmar(ActionEvent event) throws Connect4DAOException {
        psswdError.setText("");
        emailError.setText("");
        dateError.setText("");
        boolean datosOK = true;
        if (!cuadroPswd.getText().isEmpty()) {
            if (!Player.checkPassword(cuadroPswd.getText())) {
                psswdError.setText("Contaseña no válida.");
                datosOK = false;
            } else if (!cuadroPswd.getText().equals(confirmarPsswd.getText())) {
                psswdError.setText("Las contraseñas no coinciden.");
                datosOK = false;
            } else {
                player.setPassword(cuadroPswd.getText());
            }
        }

        if (!cuadroMail.getText().isEmpty()) {
            if (!Player.checkEmail(cuadroMail.getText())) {
                emailError.setText("Correo electónico no válido.");
                datosOK = false;
            } else if (datosOK) {
                player.setPassword(cuadroPswd.getText());
            }
        }

        if (!date.getValue().equals(player.getBirthdate())) {
            if (date.getValue().plusYears(12).isAfter(LocalDate.now())) {
                dateError.setText("Es necesario tener 12 años mínimo.");
                datosOK = false;
            } else if (datosOK) {
                player.setBirthdate(date.getValue());
            }
        }

        if (datosOK) {
            if (avatarChanged) {
                player.setAvatar(imgAvatar.getImage());
                controllerVentanaAnt.setImage(imgAvatar.getImage());
            }
            actualStage.close();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) throws IOException, Connect4DAOException {
        actualStage.close();
    }

    public void initStage(Stage stage, String user, SimpleObjectProperty<Theme> theme, VistaEditarPerfilController controller, VistaSegundaPrincipalController controllerAnt) throws Connect4DAOException {
        actualStage = stage;
        labelUsuario.setText(user);
        player = connect4.getPlayer(user);
        imgAvatar.imageProperty().setValue(player.getAvatar());
        cuadroMail.setPromptText(player.getEmail());
        date.setValue(player.getBirthdate());

        // no consigo que se pueda confirmar solo cambiando la foto
        BooleanBinding noChanges = cuadroPswd.textProperty().isEmpty().and(
                cuadroMail.textProperty().isEmpty()).and(
                Bindings.createBooleanBinding(() -> date.getValue().equals(player.getBirthdate()),
                        date.valueProperty()));
        btnConfirmar.disableProperty().bind(noChanges);

        controllerActual = controller;
        controllerVentanaAnt = controllerAnt;
        currentTheme = theme;
        setTheme();
    }

    public void setImage(Image avatar) {
        avatarChanged = true;
        imgAvatar.setImage(avatar);
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
}
