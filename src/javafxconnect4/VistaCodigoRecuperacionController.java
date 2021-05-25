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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaCodigoRecuperacionController implements Initializable {

    @FXML
    private Label textoCodigo;
    @FXML
    private TextField codigo;
    @FXML
    private Button enviarButton;
    @FXML
    private Label incorrecto;
    @FXML
    private VBox contenedorRaiz;

    private Stage ventanaAnt;
    private String user;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTheme = new SimpleObjectProperty<>();
        // Si no hay nada escrito en ambos campos o son espacios desabilitamos el botón de enviar.
        enviarButton.disableProperty().bind(Bindings.or(
                Bindings.createBooleanBinding(() -> {
                    return codigo.getText().split(" ").length == 0;
                }, codigo.textProperty()),
                Bindings.isEmpty(codigo.textProperty())));
        // Generamos un númeo de 4 cifras aleatorio para que sea el código de recuperación.
        int min = 1000;
        int max = 9999;
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
        textoCodigo.setText(random + "");
    }

    @FXML
    private void mostrarContraseña(ActionEvent event) throws Connect4DAOException {
        // Comprobamos que el código introducido sea el correcto.
        if (!codigo.getText().equals(textoCodigo.getText())) {
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
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Contraseña");
            alert.setHeaderText("Su contraseña");
            alert.setContentText(player.getPassword());

            // Cerramos ventana actual.
            Node miNodo = (Node) event.getSource();
            ((Stage) miNodo.getScene().getWindow()).close();

            // Mostramos diálogo.
            alert.showAndWait();

            ventanaAnt.close();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cerramos la ventana y volvemos a enseñar la anteior.
        Node miNodo = (Node) event.getSource();
        ((Stage) miNodo.getScene().getWindow()).close();
        ventanaAnt.show();
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param usuario
     * @param ant
     * @param theme
     */
    public void initStage(String usuario, Stage ant, SimpleObjectProperty<Theme> theme) {
        user = usuario;
        ventanaAnt = ant;
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
