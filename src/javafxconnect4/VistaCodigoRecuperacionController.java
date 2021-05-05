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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    private Stage codigoRecu;
    private Stage ventanaAnt;
    private String user;
    private String passwrd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            // Pasamos a String los datos del jugador y lo dividimos por espacios en un array,
            // de esa manera la contraseña estará en el índice 1 de dicho array.
            String[] playerString = player.toString().split("\n");
            passwrd = playerString[1].split(" ")[1];
            System.out.println(passwrd);
            // Abrimos ventana con la contraseña solicitada usando un diálogo de información.
            try {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Contraseña");
                alert.setHeaderText("Su contraseña");
                alert.setContentText(passwrd);
                // Cerramos ventana actual.
                Node miNodo = (Node) event.getSource();
                miNodo.getScene().getWindow().hide();
                // Mostramos diálogo.
                alert.showAndWait();
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Cerramos la ventana y volvemos a enseñar la anteior.
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
        ventanaAnt.show();
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param usuario
     * @param ant
     */
    public void initStage(Stage stage, String usuario, Stage ant) {
        codigoRecu = stage;
        codigoRecu.setTitle("Código de recuperación");
        codigoRecu.setResizable(false);
        user = usuario;
        ventanaAnt = ant;
    }
}
