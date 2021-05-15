/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import model.Connect4;
import model.Round;

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
    private DatePicker inicio;
    @FXML
    private DatePicker fin;
    @FXML
    private TableView<Round> tabla;
    @FXML
    private TableColumn<Round, String> fechaCol;
    @FXML
    private TableColumn<Round, String> ganadorCol;
    @FXML
    private TableColumn<Round, String> perdedorCol;
    @FXML
    private Button buscarButton;
    @FXML
    private ToggleGroup botones;
    @FXML
    private ToggleButton numPartidas;
    @FXML
    private ToggleButton partidasButton;
    @FXML
    private ToggleButton realizadasButton;
    @FXML
    private ToggleButton ganadasButton;
    @FXML
    private ToggleButton perdidasButton;
    @FXML
    private ToggleButton numPartidasButton;

    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private Connect4 connect4;
    private final ObservableList<Round> datesList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connect4 = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
        }
        partidasButton.setSelected(true);
        fin.setValue(LocalDate.now());
        inicio.setValue(LocalDate.of(fin.getValue().getYear(),
                fin.getValue().getMonthValue() - 1, fin.getValue().getDayOfMonth()));
        tablaPartidas(null);
    }

    @FXML
    private void tablaPartidas(ActionEvent event) {
        TreeMap<LocalDate, List<Round>> roundsPerDay = connect4.getRoundsPerDay();

        roundsPerDay.subMap(inicio.getValue(), fin.getValue()).forEach(
                (LocalDate date, List<Round> rounds) -> {
                    datesList.addAll(rounds);
                });
        fechaCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<String>(cellData.getValue().getLocalDate().toString()));
        ganadorCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<String>(cellData.getValue().getWinner().getNickName()));
        perdedorCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<String>(cellData.getValue().getLoser().getNickName()));
        tabla.setItems(datesList);
    }

    @FXML
    private void tablaRealizadas(ActionEvent event) {
    }

    @FXML
    private void tablaGanadas(ActionEvent event) {
    }

    @FXML
    private void tablaPerdidas(ActionEvent event) {
    }

    @FXML
    private void graficaPartidas(ActionEvent event) {
    }

    @FXML
    private void graficaJugador(ActionEvent event) {
    }
}
