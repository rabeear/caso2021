/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
    private TableColumn<Round, String> horaCol;
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
    private final ObservableList<Round> dataList = FXCollections.observableArrayList();
    private String user;

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
        // Como los usuarios creados como demo data se crean haciendo que hayan
        // jugado durante un mes hacemos que no se pueda elegir una fecha anterior
        // a un mes.
        LocalDate iniMaxDate = LocalDate.now();
        LocalDate iniMinDate = iniMaxDate.minusMonths(1);
        inicio.setDayCellFactory(date -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(iniMinDate) || item.isAfter(iniMaxDate));
            }
        });
        // Esto hay que cambiarlo para que no se pueda elegir una fecha anterior a la de incio.
        fin.setDayCellFactory(date -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(iniMaxDate) || item.isBefore(iniMinDate));
            }
        });
        // Iniciamos la vista enseñando todas las partidas registradas en el sistema.
        partidasButton.setSelected(true);
        fin.setValue(LocalDate.now());
        inicio.setValue(LocalDate.of(fin.getValue().getYear(),
                fin.getValue().getMonthValue() - 1, fin.getValue().getDayOfMonth()));
        tablaPartidas(null);
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param usr
     */
    public void initStage(String usr) {
        user = usr;
    }

    private void rellenarCol() {
        fechaCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getLocalDate().toString()));
        horaCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<String>(
                cellData.getValue().getTimeStamp().toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString()));
        ganadorCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getWinner().getNickName()));
        perdedorCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getLoser().getNickName()));
    }

    @FXML
    private void tablaPartidas(ActionEvent event) {
        TreeMap<LocalDate, List<Round>> roundsPerDay = connect4.getRoundsPerDay();
        dataList.clear();
        jugador.setDisable(true);

        roundsPerDay.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach(
                (LocalDate date, List<Round> rounds) -> {
                    dataList.addAll(rounds);
                });
        rellenarCol();
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaRealizadas(ActionEvent event) {
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente.
        }
        ArrayList<Round> roundsPlayer = connect4.getRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();
        // Falta que coja solo las rondas en el intervalo de tiempo requerido.
        dataList.addAll(roundsPlayer);
        rellenarCol();
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaGanadas(ActionEvent event) {
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente.
        }
        ArrayList<Round> roundsPlayer = connect4.getWinnedRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();
        // Falta que coja solo las rondas en el intervalo de tiempo requerido.
        dataList.addAll(roundsPlayer);
        rellenarCol();
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaPerdidas(ActionEvent event) {
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente.
        }
        ArrayList<Round> roundsPlayer = connect4.getLostRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();
        // Falta que coja solo las rondas en el intervalo de tiempo requerido.
        dataList.addAll(roundsPlayer);
        rellenarCol();
        tabla.setItems(dataList);
    }

    @FXML
    private void graficaPartidas(ActionEvent event) {
    }

    @FXML
    private void graficaJugador(ActionEvent event) {
    }
}
