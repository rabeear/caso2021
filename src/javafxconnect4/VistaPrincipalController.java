/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 *
 * @author belen
 */
public class VistaPrincipalController implements Initializable {

    @FXML
    private Button botonIniciar;
    @FXML
    private Button btnNewAcc;
    @FXML
    private Button btnPasswd;
    private Stage stagePrincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void autentificacionClick(ActionEvent event) {
        System.out.println("Prueba"); //si que funciona :D
    }
    
    @FXML
    private void cambioColorBoton(MouseEvent event) { /* cambio de color en los botones al pasar por encima(Ni idea de como hacerlo de momento)*/
        
    } 
    
    @FXML
    private void passwdOlvidada(ActionEvent event) { /*Cambio a ventana de password olvidada*/
        try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaPsswdOlvidada.fxml"));
            Parent root = cargador.load();
            cargador.<VistaPsswdOlvidadaController>getController().initStage(actual);
            Scene escena = new Scene(root, 650, 375);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irCrearCuenta(ActionEvent event) {
            try {
            Stage actual = new Stage();
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaAñadirUsuario.fxml"));
            Parent root = cargador.load();
            cargador.<VistaAñadirUsuarioController>getController().initStage(actual);
            Scene escena = new Scene(root, 800, 500);
            actual.setScene(escena);
            actual.initModality(Modality.APPLICATION_MODAL);
            actual.show();
            stagePrincipal.hide();
        } catch (Exception e) {
            e.printStackTrace();
        }
            
           
    }
    
    public void initStage(Stage stage) {
        stagePrincipal = stage;
    }
    
    
}
