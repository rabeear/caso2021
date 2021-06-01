/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class InsertarCodController implements Initializable {

    @FXML
    private VBox contenedorRaiz;
    @FXML
    private Button enviarButton;
    @FXML
    private Label incorrecto;
    @FXML
    private TextField inputCodigo;

    private Stage ventanaPsswdOlvidada;
    private Stage ventanaActual;
    private Stage ventanaAnt;
    private String user;
    private String codigo;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        // Si no hay nada escrito en ambos campos o son espacios desabilitamos el botón de enviar.
        enviarButton.disableProperty().bind(Bindings.or(Bindings.createBooleanBinding(() -> {
            return inputCodigo.getText().split(" ").length == 0;
        }, inputCodigo.textProperty()),
                Bindings.isEmpty(inputCodigo.textProperty())));
         */
    }

    @FXML
    private void mostrarContraseñaEnter(KeyEvent event) throws Connect4DAOException {
        if (event.getCode().equals(KeyCode.ENTER) && !enviarButton.isDisabled()) {
            mostrarContraseña(null);
        }
    }

    @FXML
    private void mostrarContraseña(ActionEvent event) throws Connect4DAOException {
        // Comprobamos que el código introducido sea el correcto.
        if (!inputCodigo.getText().equals(codigo)) {
            incorrecto.setText("Código de recuperación incorrecto.");
        } else {
            // Limpiamos la etiqueta si tiene algo escrito.
            if (!incorrecto.getText().equals("")) {
                incorrecto.setText("");
            }
            // Guardamos la contraseña del usuario
            Connect4 connect4 = Connect4.getSingletonConnect4();
            Player player = connect4.getPlayer(user);

            // Abrimos ventana con la contraseña solicitada usando un diálogo de información.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Contraseña");
            alert.setHeaderText("Su contraseña");
            alert.setContentText(player.getPassword());

            // Le ponemos el modo de visualización correcto.
            DialogPane dialog = alert.getDialogPane();
            switch (currentTheme.get()) {
                case DARK_THEME:
                    dialog.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                    break;
                case LIGTH_THEME:
                    dialog.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                    break;
                default:
                    throw new AssertionError(currentTheme.get().name());
            }

            // Cerramos ventana actual.
            ventanaActual.close();
            ventanaPsswdOlvidada.close();
            ventanaAnt.close();

            // Mostramos diálogo.
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ventanaAnt.close();
        ventanaActual.close();
        ventanaPsswdOlvidada.show();
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param usuario
     * @param PsswdOlvidada
     * @param ant
     * @param theme
     * @param actual
     * @param cod
     */
    public void initStage(String usuario, Stage PsswdOlvidada, Stage ant, Stage actual, SimpleObjectProperty<Theme> theme, String cod) {
        user = usuario;
        ventanaPsswdOlvidada = PsswdOlvidada;
        ventanaActual = actual;
        ventanaAnt = ant;
        codigo = cod;
        currentTheme = theme;
        switch (currentTheme.get()) {
            case DARK_THEME:
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                break;
            case LIGTH_THEME:
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }
}
