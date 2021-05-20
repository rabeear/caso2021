/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
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
import model.DayRank;
import model.Player;
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
    private ToggleButton partidasButton;
    @FXML
    private ToggleButton realizadasButton;
    @FXML
    private ToggleButton ganadasButton;
    @FXML
    private ToggleButton perdidasButton;
    @FXML
    private ToggleButton numPartidasButton;

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
        // Para que no se pueda elegir una fecha anterior a la de incio.
        fin.setDayCellFactory(date -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(iniMaxDate) || item.isBefore(inicio.getValue()));
            }
        });

        // Indicamos cómo queremos que se muestren los datos en las tablas.
        fechaCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getLocalDate().toString()));
        horaCol.setCellValueFactory((cellData) -> new SimpleObjectProperty<String>(
                cellData.getValue().getTimeStamp().toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString()));
        ganadorCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getWinner().getNickName()));
        perdedorCol.setCellValueFactory((cellData)
                -> new SimpleObjectProperty<String>(cellData.getValue().getLoser().getNickName()));

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
     * @param usr: nombre del usuario que ha iniciado sesión.
     */
    public void initStage(String usr) {
        user = usr;
    }

    @FXML
    private void tablaPartidas(ActionEvent event) {
        if (!borderPane.getCenter().equals(tabla)) {
            borderPane.setCenter(tabla);
        }
        TreeMap<LocalDate, List<Round>> roundsPerDay = connect4.getRoundsPerDay();
        dataList.clear();
        jugador.setDisable(true);

        roundsPerDay.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach(
                (LocalDate date, List<Round> rounds) -> {
                    dataList.addAll(rounds);
                });
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaRealizadas(ActionEvent event) {
        if (!borderPane.getCenter().equals(tabla)) {
            borderPane.setCenter(tabla);
        }
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente (Enseñar alerta ERROR)
        }
        ArrayList<Round> roundsPlayer = connect4.getRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();

        // Cojemos solo los datos en el intervalo de tiempo requerido.
        int indiceIni = 0;
        int indiceFin = roundsPlayer.size() - 1;

        if (roundsPlayer.get(0).getLocalDate().compareTo(inicio.getValue()) < 0) {
            for (Round ronda : roundsPlayer) {
                if (ronda.getLocalDate().compareTo(inicio.getValue()) >= 0) {
                    indiceIni = roundsPlayer.indexOf(ronda);
                    break;
                }
            }
        }

        if (roundsPlayer.get(roundsPlayer.size() - 1).getLocalDate().compareTo(fin.getValue()) > 0) {
            for (Round ronda : roundsPlayer) {
                if (ronda.getLocalDate().compareTo(fin.getValue()) >= 0) {
                    indiceFin = roundsPlayer.indexOf(ronda);
                    break;
                }
            }
        }

        dataList.addAll(roundsPlayer.subList(indiceIni, indiceFin));
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaGanadas(ActionEvent event) {
        if (!borderPane.getCenter().equals(tabla)) {
            borderPane.setCenter(tabla);
        }
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente.
        }
        ArrayList<Round> roundsPlayer = connect4.getWinnedRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();
        // Falta que coja solo las rondas en el intervalo de tiempo requerido.
        dataList.addAll(roundsPlayer);
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaPerdidas(ActionEvent event) {
        if (!borderPane.getCenter().equals(tabla)) {
            borderPane.setCenter(tabla);
        }
        jugador.setDisable(false);
        jugador.setText(user);
        if (!connect4.exitsNickName(jugador.getText())) {
            // Error jugador no existente.
        }
        ArrayList<Round> roundsPlayer = connect4.getLostRoundsPlayer(connect4.getPlayer(jugador.getText()));
        dataList.clear();
        // Falta que coja solo las rondas en el intervalo de tiempo requerido.
        dataList.addAll(roundsPlayer);
        tabla.setItems(dataList);
    }

    @FXML
    private void graficaPartidas(ActionEvent event) {
        jugador.setDisable(true);
        TreeMap<LocalDate, Integer> numPartidas = connect4.getRoundCountsPerDay();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Fecha");
        yAxis.setLabel("Número partidas");

        ObservableList<XYChart.Data<String, Number>> lineChartData
                = FXCollections.observableArrayList();
        numPartidas.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach(
                (date, rounds) -> {
                    lineChartData.add(new XYChart.Data(date.toString(), rounds));
                });

        // Rellenar la serie con la ObservableList creada anteriormente.
        XYChart.Series serie = new XYChart.Series(lineChartData);

        LineChart<String, Number> grafica = new LineChart<>(xAxis, yAxis);
        grafica.setTitle("Nº partidas totales");
        grafica.getData().add(serie);
        borderPane.setCenter(grafica);

    }

    @FXML
    private void graficaJugador(ActionEvent event) {
        jugador.setDisable(false);
        jugador.setText(user);
        TreeMap<LocalDate, DayRank> dataPlayer = connect4.getDayRanksPlayer(connect4.getPlayer(user));
        CategoryAxis yAxis = new CategoryAxis();
        NumberAxis xAxis = new NumberAxis();

        // Gráfica de barras apiladas con las partidas ganadas y perdidas de cada día.
        StackedBarChart<Number, Number> partidas;

        // Gráfica de barras con el nº de jugadores diferentes a los que se ha
        // enfrentado el jugador cada día.
        BarChart<Number, String> jugadores = new BarChart<>(xAxis, yAxis);
    }

    @FXML
    private void buscar(ActionEvent event) {
    }
}
