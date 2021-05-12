/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class VistaHistorialController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField jugador;
    @FXML
    private ToggleGroup botones;
    @FXML
    private TableView<?> tabla;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
