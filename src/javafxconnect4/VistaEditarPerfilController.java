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
import javafx.scene.image.ImageView;
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

    private Stage actualStage;
    private Scene escenaActual;
    private Player player;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BooleanBinding noDatos = cuadroPswd.textProperty().isEmpty().and(cuadroMail.textProperty().isEmpty());

        btnConfirmar.disableProperty().bind(noDatos);

        //Esto hay que cambiarlo.
        currentTheme = new SimpleObjectProperty<>();
        currentTheme.set(Theme.LIGTH_THEME);
    }

    @FXML
    private void clickCambiar(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCambiar.fxml"));
        Parent root = cargador.load();
        cargador.<VistaCambiarController>getController().initStage(actual, player);
        Scene escena = new Scene(root);
        actual.setScene(escena);
        actual.show();

        actualStage.close();

    }

    @FXML
    private void clickConfirmar(ActionEvent event) throws Connect4DAOException {
        boolean pswd = false;
        Connect4 connect4 = Connect4.getSingletonConnect4();

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

    void initStage(Stage stage, String user) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();

        actualStage = stage;
        escenaActual = stage.getScene();
        labelUsuario.setText(user);
        player = connect4.getPlayer(user);
        imgAvatar.imageProperty().setValue(player.getAvatar());
        cuadroPswd.setPromptText(player.getPassword());
        cuadroMail.setPromptText(player.getEmail());
    }
}
