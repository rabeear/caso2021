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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaCambiarController implements Initializable {

    @FXML
    private VBox contenedorRaiz;

    private Stage actualStage;
    private Player player;
    private Image img;
    private boolean anterior; //Si true vistaAñadirUser / false -> VistaEditarPerfil
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTheme = new SimpleObjectProperty<>();
    }

    @FXML
    private void clickImg1(ActionEvent event) throws IOException, Connect4DAOException {
        if (anterior) {
            devolver(event, 1);
        } else {
            img = new Image("/avatars/avatar1.png");
            player.setAvatar(img);
            volver();
        }
    }

    @FXML
    private void clickImg2(ActionEvent event) throws IOException, Connect4DAOException {
        if (anterior) {
            devolver(event, 2);
        } else {
            img = new Image("/avatars/avatar2.png");
            player.setAvatar(img);
            volver();
        }
    }

    @FXML
    private void clickImg3(ActionEvent event) throws IOException, Connect4DAOException {
        if (anterior) {
            devolver(event, 3);
        } else {
            img = new Image("/avatars/avatar3.png");
            player.setAvatar(img);
            volver();
        }
    }

    @FXML
    private void clickImg4(ActionEvent event) throws IOException, Connect4DAOException {
        if (anterior) {
            devolver(event, 4);
        } else {
            img = new Image("/avatars/avatar4.png");
            player.setAvatar(img);
            volver();
        }
    }

    @FXML
    private void clickImg5(ActionEvent event) throws IOException, Connect4DAOException {
        if (anterior) {
            devolver(event, 5);
        } else {
            img = new Image("/avatars/default.png");
            player.setAvatar(img);
            volver();
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        Node nodo = (Node) event.getSource();
        nodo.getScene().getWindow().hide();
    }

    public void initStage(Stage actual, Player j1) {
        actualStage = actual;
        player = j1;
        anterior = false;
        actualStage.setResizable(false);
    }

    public void initStage(Stage actual, SimpleObjectProperty<Theme> theme) {
        actualStage = actual;
        anterior = true;
        actualStage.setResizable(false);
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

    private void devolver(ActionEvent event, int numeroDeFoto) throws IOException {
        Stage actual = new Stage();
        String image = "";
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaAñadirUsuario.fxml"));
        Parent root = cargador.load();

        switch (numeroDeFoto) {
            case 1:
                image = "/avatars/avatar1.png";
                break;
            case 2:
                image = "/avatars/avatar2.png";
                break;
            case 3:
                image = "/avatars/avatar3.png";
                break;
            case 4:
                image = "/avatars/avatar4.png";
                break;
            case 5:
                image = "/avatars/default.png";
                break;
            default:
                break;
        }
        cargador.<VistaAñadirUsuarioController>getController().initStage(actual, image);
        Scene escena = new Scene(root);
        actual.setScene(escena);
        actual.show();

        actualStage.close();
    }

    private void volver() throws IOException, Connect4DAOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaEditarPerfil.fxml"));
        Parent root = cargador.load();
        cargador.<VistaEditarPerfilController>getController().initStage(actual, player.getNickName());
        Scene escena = new Scene(root, 340, 620);
        actual.setScene(escena);
        actual.show();

        actualStage.close();
    }
}
