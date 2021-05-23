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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaSegundaPrincipalController implements Initializable {

    @FXML
    public Label nombreUsuario;
    @FXML
    private ImageView foto;
    @FXML
    private HBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;

    private Stage actualStage;
    private String user;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cuando se pulse el botón del cambio de visualización, cambiamos el estilo de la vista.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (themeButton.isSelected()) {
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                currentTheme.set(Theme.DARK_THEME);
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
            } else {
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                currentTheme.set(Theme.LIGTH_THEME);
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
            }
        });
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param usr
     * @param theme
     * @throws Connect4DAOException
     */
    public void initStage(Stage stage, String usr, SimpleObjectProperty<Theme> theme) throws Connect4DAOException {
        actualStage = stage;
        user = usr;
        Connect4 connect4 = Connect4.getSingletonConnect4();
        foto.imageProperty().setValue(connect4.getPlayer(user).getAvatar());
        currentTheme = theme;
        switch (currentTheme.get()) {
            case DARK_THEME:
                themeButton.setSelected(true);
                break;
            case LIGTH_THEME:
                themeButton.setSelected(false);
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }

    @FXML
    private void clickCerrarSesion(ActionEvent event) throws IOException {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPrincipal.fxml"));
        Parent root = cargador.load();
        VistaPrincipalController ventanaIni = cargador.<VistaPrincipalController>getController();
        ventanaIni.initStage(actualStage, currentTheme);
        Scene scene = new Scene(root, 800, 500);
        actualStage.setScene(scene);
        actualStage.show();
    }

    @FXML
    private void jugar(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaJugar.fxml"));
        Parent root = cargador.load();
        cargador.<VistaJugarController>getController().initStage(actualStage, user);
        Scene escena = new Scene(root, 420, 190);
        actual.setScene(escena);
        actual.setTitle("Oponente");
        actual.setResizable(false);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
    }

    @FXML
    private void editarPerfil(ActionEvent event) throws IOException, Connect4DAOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaEditarPerfil.fxml"));
        Parent root = cargador.load();
        cargador.<VistaEditarPerfilController>getController().initStage(actual, user);
        Scene escena = new Scene(root, 600, 400);
        actual.setScene(escena);
        actual.setTitle("Editar perfil");
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
        actualStage.close();
    }

    @FXML
    private void verRanking(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaRanking.fxml"));
        Parent root = cargador.load();
        Scene escena = new Scene(root, 345, 400);
        actual.setResizable(false);
        actual.setScene(escena);
        actual.setTitle("Ranking");
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
    }

    @FXML
    private void verHistorial(ActionEvent event) throws IOException {
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaHistorial.fxml"));
        Parent root = cargador.load();
        cargador.<VistaHistorialController>getController().initStage(user);
        Scene escena = new Scene(root);
        actual.setResizable(false);
        actual.setScene(escena);
        actual.setTitle("Historial");
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
    }
}
