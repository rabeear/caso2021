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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private TableColumn<Player, Image> avatarCol;
    @FXML
    private TableColumn<Player, String> playerCol;
    @FXML
    private TableColumn<Player, Integer> pointCol;
    @FXML
    private TextField jugador;
    @FXML
    private VBox contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;

    private ArrayList ranking;
    private final ObservableList<Player> dataList = FXCollections.observableArrayList();
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentTheme = new SimpleObjectProperty<>();
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

        // Obtenemos la lista con el ranking.
        try {
            Connect4 connect4 = Connect4.getSingletonConnect4();
            ranking = connect4.getConnect4Ranking();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaRankingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dataList.addAll(ranking);
        // Rellenamos la tabla con dicha lista.
        hacerTabla();
        FilteredList<Player> filteredData = new FilteredList<>(dataList, b -> true);
        jugador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(player -> {
                // Si el la barra de búsqueda está vacía, mostrar todo el ranking.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Comparamos el texto introducido en la barra de búsqueda con los nombres de los jugadores.
                String lowerCaseFilter = newValue.toLowerCase();
                return player.getNickName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Player> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(sortedData);
    }

    private void hacerTabla() {
        playerCol.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        avatarCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<Image>(cellData.getValue().getAvatar()));
        avatarCol.setCellFactory((columna) -> {
            return new TableCell<Player, Image>() {
                private final ImageView view = new ImageView();

                @Override
                protected void updateItem(Image item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        view.setFitHeight(60);
                        view.setFitWidth(50);
                        view.setImage(item);
                        setGraphic(view);
                    }
                }
            };
        });
        pointCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        tabla.setItems(dataList);
    }

    @FXML
    private void cerrar(ActionEvent event) throws IOException, Connect4DAOException {
        // Cierra ventana actual
        Node miNodo = (Node) event.getSource();
        ((Stage) miNodo.getScene().getWindow()).close();
    }

    public void setTheme(SimpleObjectProperty<Theme> theme) {
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
}
