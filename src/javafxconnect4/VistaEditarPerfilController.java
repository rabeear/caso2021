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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label labelError;
    @FXML
    private HBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;

    private Stage actualStage;
    private Scene escenaActual;
    private Player player;
    private SimpleObjectProperty<Theme> currentTheme;
    private Connect4 connect4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BooleanBinding noDatos = cuadroPswd.textProperty().isEmpty().and(cuadroMail.textProperty().isEmpty());

        btnConfirmar.disableProperty().bind(noDatos);

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
        cargador.<VistaCambiarController>getController().initStage(actual, player);
        Scene escena = new Scene(root);
        actual.setScene(escena);
        actualStage.hide();
        actual.showAndWait();
        actualStage.show();
        

    }

    @FXML
    private void clickConfirmar(ActionEvent event) throws Connect4DAOException {
        boolean pswd = false;

        if (!cuadroPswd.getText().isEmpty()) {
            if (Player.checkPassword(cuadroPswd.getText())) {
                player.setPassword(cuadroPswd.getText());
            } else {
                labelError.setText("Contraseña no valida!");
                pswd = true;
            }
        }
        if (!cuadroMail.getText().isEmpty()) {
            if (Player.checkEmail(cuadroMail.getText())) {
                player.setEmail(cuadroMail.getText());
            } else {
                if (pswd) {
                    labelError.setText("Contraseña y correo no validos!");
                } else {
                    labelError.setText("Correo no valido!");
                }
            }
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) throws IOException, Connect4DAOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
        Parent root = cargador.load();
        cargador.<VistaSegundaPrincipalController>getController().initStage(actual, labelUsuario.getText(), currentTheme);
        Scene escena = new Scene(root, 800, 500);
        actual.setScene(escena);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();

        actualStage.close();
    }

    public void initStage(Stage stage, String user, SimpleObjectProperty<Theme> theme) throws Connect4DAOException {
        actualStage = stage;
        escenaActual = stage.getScene();
        labelUsuario.setText(user);
        player = connect4.getPlayer(user);
        imgAvatar.imageProperty().setValue(player.getAvatar());
        cuadroMail.setPromptText(player.getEmail());
        currentTheme = theme;
        setTheme();
    }

    private void setTheme() {
        // Cuando se cabie el modo de vsualización en otra ventana, se cambará en esta también.
        currentTheme.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Theme.DARK_THEME)) {
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
                themeButton.setSelected(true);
            } else {
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
                themeButton.setSelected(false);
            }
        });

        switch (currentTheme.get()) {
            case DARK_THEME:
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
                themeButton.setSelected(true);
                break;
            case LIGTH_THEME:
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
                themeButton.setSelected(false);
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }
}
