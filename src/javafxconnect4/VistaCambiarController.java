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
import javafx.fxml.Initializable;
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
    private SimpleObjectProperty<Theme> currentTheme;
    private VistaEditarPerfilController editarController;
    private VistaAñadirUsuarioController añadirController;
    private boolean añadir; // true -> vistaAñadirUser / false -> VistaEditarPerfil

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }

    @FXML
    private void clickImg1(ActionEvent event) throws IOException, Connect4DAOException {
        if (añadir) {
            añadirController.setImage(new Image("/avatars/avatar1.png"));
        } else {
            editarController.setImage(new Image("/avatars/avatar1.png"));
        }
        actualStage.close();
    }

    @FXML
    private void clickImg2(ActionEvent event) throws IOException, Connect4DAOException {
        if (añadir) {
            añadirController.setImage(new Image("/avatars/avatar2.png"));
        } else {
            editarController.setImage(new Image("/avatars/avatar2.png"));
        }
        actualStage.close();
    }

    @FXML
    private void clickImg3(ActionEvent event) throws IOException, Connect4DAOException {
        if (añadir) {
            añadirController.setImage(new Image("/avatars/avatar3.png"));
        } else {
            editarController.setImage(new Image("/avatars/avatar3.png"));
        }
        actualStage.close();
    }

    @FXML
    private void clickImg4(ActionEvent event) throws IOException, Connect4DAOException {
        if (añadir) {
            añadirController.setImage(new Image("/avatars/avatar4.png"));
        } else {
            editarController.setImage(new Image("/avatars/avatar4.png"));
        }
        actualStage.close();
    }

    @FXML
    private void clickImg5(ActionEvent event) throws IOException, Connect4DAOException {
        if (añadir) {
            añadirController.setImage(new Image("/avatars/default.png"));
        } else {
            editarController.setImage(new Image("/avatars/default.png"));
        }
        actualStage.close();
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        actualStage.close();
    }

    public void initStageEditar(Stage actual, Player j1, SimpleObjectProperty<Theme> theme, VistaEditarPerfilController controller) {
        actualStage = actual;
        añadir = false;
        editarController = controller;
        currentTheme = theme;
        setTheme();
    }

    public void initStageAñadir(Stage actual, SimpleObjectProperty<Theme> theme, VistaAñadirUsuarioController controller) {
        actualStage = actual;
        añadir = true;
        currentTheme = theme;
        añadirController = controller;
        setTheme();
    }

    private void setTheme() {
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
