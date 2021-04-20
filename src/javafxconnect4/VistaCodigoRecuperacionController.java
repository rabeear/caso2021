/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.stage.Modality;
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
    private Button cancelarButton;
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
        // Generamos un númeo de 6 cifras aleatorio para que sea el código de recuperación.
        int min = 100000;
        int max = 999999;
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
        textoCodigo.setText(random + "");
    }

    @FXML
    private void mostrarContraseña(ActionEvent event) throws Connect4DAOException {
        if (!codigo.getText().equals(textoCodigo.getText())) { // Comprobamos que el código sea el correcto.
            incorrecto.setText("Código de recuperación incorrecto.");
        } else {
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
            // Abrimos ventana con la contraseña solicitada.
            try {
                Stage actual = new Stage();
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaContraseña.fxml"));
                Parent root = cargador.load();
                System.out.println(passwrd);
                cargador.<VistaContraseñaController>getController().initStage(actual, passwrd);
                System.out.println(passwrd);
                Scene escena = new Scene(root, 275, 190);
                actual.setScene(escena);
                actual.initModality(Modality.APPLICATION_MODAL);
                actual.show();
                // Cerramos ventana actual.
                Node miNodo = (Node) event.getSource();
                miNodo.getScene().getWindow().hide();
            } catch (Exception e) {
                e.printStackTrace();
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

    public void initStage(Stage stage, String usuario, Stage ant) {
        /*Iniciador para usar en el cambio de ventana*/
        codigoRecu = stage;
        codigoRecu.setTitle("Código de recuperación");
        codigoRecu.setResizable(false);
        user = usuario;
        ventanaAnt = ant;
    }
}
