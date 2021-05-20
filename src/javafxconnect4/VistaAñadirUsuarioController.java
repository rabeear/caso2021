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
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaAñadirUsuarioController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private Label userError;
    @FXML
    private TextField psswd;
    @FXML
    private Label psswdError;
    @FXML
    private TextField psswd2;
    @FXML
    private Label psswd2Error;
    @FXML
    private TextField email;
    @FXML
    private Label emailError;
    @FXML
    private Label hayFoto;
    @FXML
    private DatePicker date;
    @FXML
    private Button regButton;

    private Stage stageActual;
    private Scene escenaPrincipal;
    private Image auxiliarFoto;
    @FXML
    private ImageView imagenAvatar;
    @FXML
    private Text labelFoto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // revisar esto porque algo esta fallando pero no se el que
        regButton.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return user.getText().split(" ").length == 0
                            && psswd.getText().split(" ").length == 0
                            && psswd2.getText().split(" ").length == 0
                            && email.getText().split(" ").length == 0
                            && date.getValue().toString().split(" ").length == 0;
                }, user.textProperty(), psswd.textProperty(), psswd2.textProperty(), email.textProperty()),
                Bindings.createBooleanBinding(() -> {
                    return user.getText().isEmpty()
                            || psswd.getText().isEmpty()
                            || psswd2.getText().isEmpty()
                            || email.getText().isEmpty()
                            || date.getValue().toString().isEmpty();
                }, user.textProperty(), psswd.textProperty(), psswd2.textProperty(), email.textProperty())));

        // Nos aseguramos de que la fecha de nacimiento no pueda ser mayor a la fecha actual.
        date.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) > 0);
                }
            };
        });
    }

    @FXML
    private void clickAvatar(ActionEvent event) {
        // Llevar a zona de cambio de avatar.
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCambiar.fxml"));
            Parent root = cargador.load();
            cargador.<VistaCambiarController>getController().initStage(actual);
            Scene escena = new Scene(root, 650, 375);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.showAndWait();
            stageActual.close();
        } catch (IOException e) {
        }
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage: ventana actual
     */
    public void initStage(Stage stage) {
        stageActual = stage;
        escenaPrincipal = stage.getScene();
        Image avatar = new Image("/avatars/default.png");
        auxiliarFoto = avatar;
        imagenAvatar.imageProperty().setValue(avatar);
    }

    @FXML
    private void registrar(ActionEvent event) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        boolean registrar = true;
        if (connect4.exitsNickName(user.getText())) { // Comprobamos si el usuario ya existe.
            userError.setText("El nombre de usuario ya existe.");
            registrar = false;
        } else if (!Player.checkNickName(user.getText())) { // Comprobamos si el usuario es válido.
            userError.setText("Nombre de usuario no válido.");
            registrar = false;
        }
        if (!Player.checkPassword(psswd.getText())) { // Comprobamos si la contraseña es válida.
            psswdError.setText("Contaseña no válida.");
            registrar = false;
        } else if (!psswd.getText().equals(psswd2.getText())) { // Comprobamos si se ha confirmado de manera correcta la contraseña
            psswd2Error.setText("Las contraseñas no coinciden.");
            registrar = false;
        }
        if (!Player.checkEmail(email.getText())) { // Comprobamos si el email introducido es válido.
            emailError.setText("Correo electónico no válido.");
            registrar = false;
        }
        // Si no ha habido ningún error en los datos introducidos, entonces registramos al jugador.
        if (registrar) {
            // Falta añadir foto elegida o por defecto.
            connect4.registerPlayer(user.getText(), email.getText(), psswd.getText(),auxiliarFoto ,date.getValue(), 0);
            // Cerramos la ventana y volvemos a enseñar la anteior.
            Node miNodo = (Node) event.getSource();
            miNodo.getScene().getWindow().hide();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cerramos la ventana y volvemos a enseñar la anteior.
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }
    
    void initStage(Stage stage, String foto) { //enProceso
     stageActual = stage;
     escenaPrincipal = stage.getScene();
     Image avatar = new Image(foto);
     auxiliarFoto = avatar;
     imagenAvatar.imageProperty().setValue(avatar);
     
    }
}
