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
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class VistaJugarController implements Initializable {

    @FXML
    private VBox contenedorRaiz;

    private Stage ventanaInicio;
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
        // TODO
    }

    public void initStage(Stage actual, String usuario, SimpleObjectProperty<Theme> theme) {
        ventanaInicio = actual;
        user = usuario;
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
    private void jugarPVE(ActionEvent event) throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJuegoPVE.fxml"));
        Pane root = (Pane) cargador.load();
        Player actualPlayer = connect4.getPlayer(user);
        VistaJuegoPVEController ventanaJuegoPVE = cargador.<VistaJuegoPVEController>getController();
        ventanaJuegoPVE.initStage(ventanaInicio, actualPlayer, currentTheme);
        Scene scene = new Scene(root, 800, 500);
        ventanaInicio.setScene(scene);
        ventanaInicio.show();
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    @FXML
    private void jugarPVP(ActionEvent event) throws Connect4DAOException, IOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaInicioSesionSegundoJugador.fxml"));
        HBox root = (HBox) cargador.load();
        Player actualPlayer = connect4.getPlayer(user);
        VistaInicioSesionSegundoJugadorController ventanaIni = cargador.<VistaInicioSesionSegundoJugadorController>getController();
        ventanaIni.initStage(ventanaInicio, actualPlayer, currentTheme);
        Scene scene = new Scene(root, 800, 500);
        ventanaInicio.setScene(scene);
        ventanaInicio.show();
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Node miNodo = (Node) event.getSource();
        ((Stage) miNodo.getScene().getWindow()).close();
    }
}
