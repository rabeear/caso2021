/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Raquel
 */
public class VistaRankingController implements Initializable {

    @FXML
    private TableView<Player> tabla;
    @FXML
    private TableColumn<Player, String> avatarCol;
    @FXML
    private TableColumn<Player, String> playerCol;
    @FXML
    private TableColumn<Player, Integer> pointCol;

    private ArrayList ranking;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();
            ranking = connect4.getConnect4Ranking();
            hacerTabla(ranking);
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaRankingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void hacerTabla(ArrayList ranking) {
        playerCol.setCellValueFactory(new PropertyValueFactory<Player, String>("nickName"));
        // avatarCol.setCellValueFactory((cellData) -> cellData.getValue().getAvatar());
        avatarCol.setCellFactory(columna -> {
            return new TableCell<Player, String>() {
                private ImageView view = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        Image image = new Image(VistaRankingController.class.getResourceAsStream(item), 40, 40, true, true);
                        view.setImage(image);
                        setGraphic(view);
                    }
                }
            };
        });
        pointCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        tabla.setItems(FXCollections.observableArrayList(ranking));
    }

    @FXML
    private void cerrar(ActionEvent event) throws IOException, Connect4DAOException {
        // Cierra ventana actual
        Node miNodo = (Node) event.getSource();
        miNodo.getScene().getWindow().hide();
    }
}
