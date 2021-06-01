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
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
public class VistaAñadirUsuarioController implements Initializable {

    @FXML
    private TextField user;
    @FXML
    private Label userError;
    @FXML
    private Label psswdError;
    @FXML
    private Label psswd2Error;
    @FXML
    private TextField email;
    @FXML
    private Label emailError;
    @FXML
    private DatePicker date;
    @FXML
    private Button regButton;
    @FXML
    private ImageView imagenAvatar;
    @FXML
    private VBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;
    @FXML
    private Button ayuda;
    @FXML
    private Label dateError;
    @FXML
    private PasswordField psswd;
    @FXML
    private PasswordField psswd2;
    @FXML
    private AnchorPane ayudaPane;

    private Stage stageActual;
    private Image auxiliarFoto;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ayudaPane.visibleProperty().bind(ayuda.hoverProperty());

        // revisar esto porque algo esta fallando pero no se el que
        BooleanBinding vacios = user.textProperty().isEmpty().or(
                psswd.textProperty().isEmpty()).or(psswd2.textProperty().isEmpty()).or(
                email.textProperty().isEmpty());

        BooleanBinding espacios = Bindings.createBooleanBinding(() -> {
            return user.getText().split(" ").length == 0
                    && psswd.getText().split(" ").length == 0
                    && psswd2.getText().split(" ").length == 0
                    && email.getText().split(" ").length == 0;
        }, user.textProperty(), psswd.textProperty(), psswd2.textProperty(), email.textProperty());

        regButton.disableProperty().bind(Bindings.or(espacios, vacios));

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

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });
    }

    @FXML
    private void clickAvatar(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaCambiar.fxml"));
        Parent root = cargador.load();
        cargador.<VistaCambiarController>getController().initStageAñadir(actual, currentTheme, this);
        Scene escena = new Scene(root, 800, 400);
        actual.setScene(escena);
        actual.setResizable(false);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.setTitle("Avatar");
        actual.show();
    }

    public void setImage(Image avatar) {
        imagenAvatar.setImage(avatar);
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage Stage de la ventana actual.
     * @param theme Propiedad del modo de visualización de la ventana anterior.
     */
    public void initStage(Stage stage, SimpleObjectProperty<Theme> theme) {
        stageActual = stage;
        Image avatar = new Image("/avatars/default.png");
        auxiliarFoto = avatar;
        imagenAvatar.imageProperty().setValue(avatar);
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

    public void initStage(Stage stage, String foto) {
        stageActual = stage;
        Image avatar = new Image(foto);
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
        } else if (!psswd.getText().equals(psswd2.getText())) { // Comprobamos si se ha confirmado de manera correcta la contraseña.
            psswd2Error.setText("Las contraseñas no coinciden.");
            registrar = false;
        }
        if (!Player.checkEmail(email.getText())) { // Comprobamos si el email introducido es válido.
            emailError.setText("Correo electónico no válido.");
            registrar = false;
        }
        if (date.getValue().plusYears(12).isAfter(LocalDate.now())) { // Comprobamos si tiene más de 12 años.
            dateError.setText("Es necesario tener 12 años mínimo.");
            registrar = false;
        }
        // Si no ha habido ningún error en los datos introducidos, entonces registramos al jugador.
        if (registrar) {
            // Falta añadir foto elegida o por defecto.
            connect4.registerPlayer(user.getText(), email.getText(), psswd.getText(), auxiliarFoto, date.getValue(), 0);
            // Cerramos la ventana y volvemos a enseñar la anteior.
            Node miNodo = (Node) event.getSource();
            miNodo.getScene().getWindow().hide();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cerramos la ventana y volvemos a enseñar la anteior.
        stageActual.close();
    }
}
