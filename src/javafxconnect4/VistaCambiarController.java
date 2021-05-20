/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;

/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaCambiarController implements Initializable {

    
    private Stage actualStage;
    private Scene actualScene;
    private Player player;
    private boolean anterior; //Si true vistaAñadirUser / false -> VistaEditarPerfil
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickImg1(ActionEvent event) throws IOException {
        if (anterior) {
            devolver(event, 1);
        } else {
            
        }
    }

    @FXML
    private void clickImg2(ActionEvent event) throws IOException {
        if (anterior) {
            devolver(event, 2);
        } else {
            
        }
    }

    @FXML
    private void clickImg3(ActionEvent event) throws IOException {
        if (anterior) {
            devolver(event, 3);
        } else {
            
        }
    }

    @FXML
    private void clickImg4(ActionEvent event) throws IOException {
        if (anterior) {
            devolver(event, 4);
        } else {
            
        }
    }

    @FXML
    private void clickImg5(ActionEvent event) throws IOException {
        if (anterior) {
            devolver(event, 5);
        } else {
            
        }
    }

    @FXML
    private void clickCancelar(ActionEvent event) {
        Node nodo = (Node) event.getSource();
        nodo.getScene().getWindow().hide();
    }

    void initStage(Stage actual, Player j1) {
        actualStage = actual;
        actualScene = actual.getScene();
        player = j1;
        anterior = false;
    }

    void initStage(Stage actual) {
        actualStage = actual;
        actualScene = actual.getScene();
        anterior = true;
    }
    
    void devolver(ActionEvent event, int numeroDeFoto) throws IOException {
        Stage actual = new Stage();
        String img = "";
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaAñadirUsuario.fxml"));
        Parent root = cargador.load();
        
        switch (numeroDeFoto) {
            case 1:
                img = "../avatars/avatar1.png";
                break;
            case 2:
                img = "../avatars/avatar2.png";
                break;
            case 3:
                img = "../avatars/avatar3.png";
                break;
            case 4:
                img = "../avatars/avatar4.png";
                break;
            case 5:
                img = "../avatars/default.png";
                break;
            default:
                break;
        }
        cargador.<VistaAñadirUsuarioController>getController().initStage(actual, img);
        Scene escena = new Scene(root);
        actual.setScene(escena);
        actual.show();
        actualStage.close();
    }
    
}
