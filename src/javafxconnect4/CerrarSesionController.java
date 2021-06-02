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
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class CerrarSesionController implements Initializable {

    @FXML
    private Button btn1;
    @FXML
    private Button btn2;
    @FXML
    private VBox contenedorRaiz;

    private Stage stageActual;
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

    public void initStage(Stage stage, Player j1, Player j2, SimpleObjectProperty<Theme> theme) {
        btn1.setText(j1.getNickName());
        btn2.setText(j2.getNickName());
        stageActual = stage;
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
    private void clickBoton(ActionEvent event) throws IOException, Connect4DAOException {
        Node n = (Node) event.getSource();
        String id = n.getId();
        Connect4 connect4 = Connect4.getSingletonConnect4();
        Stage actual = new Stage();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("Principal.fxml"));
        Parent root = cargador.load();
        
        if (id.equals("btn2")) {
            cargador.<PrincipalController>getController().initStage(actual, btn1.getText(), currentTheme);
        } else {
            cargador.<PrincipalController>getController().initStage(actual, btn2.getText(), currentTheme);
        }
        
        Scene escena = new Scene(root);
        actual.setScene(escena);
        actual.initModality(Modality.APPLICATION_MODAL);
        actual.show();
        stageActual.hide();
        
    }
}
