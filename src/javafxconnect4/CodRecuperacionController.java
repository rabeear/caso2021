/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class CodRecuperacionController implements Initializable {

    @FXML
    private Label textoCodigo;
    @FXML
    private VBox contenedorRaiz;

    private Stage ventanaAnt;
    private Stage ventanaActual;
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
        // Generamos un númeo de 4 cifras aleatorio para que sea el código de recuperación.
        int min = 1000;
        int max = 9999;
        int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
        textoCodigo.setText(random + "");
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
     * @param actual
     */
    public void initStage(String usuario, Stage ant, SimpleObjectProperty<Theme> theme, Stage actual) {
        user = usuario;
        ventanaAnt = ant;
        ventanaActual = actual;
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

    @FXML
    private void introducir(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("InsertarCod.fxml"));
        Parent root = cargador.load();
        cargador.<InsertarCodController>getController().initStage(user, ventanaAnt, ventanaActual, actual, currentTheme, textoCodigo.getText());
        Scene escena = new Scene(root, 352, 222);
        actual.setScene(escena);
        actual.setTitle("Recuperar Contraseña");
        actual.setResizable(false);

        // Colocamos las ventanas de manera que una no tape a la otra y el usuario
        // pueda ver el código de recuperación como si estuviera leyendo el correo.
        ventanaActual.setX(ventanaActual.getX() - ventanaActual.getX() / 2);
        actual.setX(ventanaActual.getX() * 3);
        actual.setY(ventanaActual.getY());

        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
    }
}
