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
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Connect4;
import model.DayRank;
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
    @FXML
    private ToggleButton numJugadorButton;

    private Connect4 connect4;
    private final ObservableList<Round> dataList = FXCollections.observableArrayList();
    private String user;
    private final Alert errorJugador = new Alert(Alert.AlertType.ERROR);

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

        // Cuando se cambie la fecha, cambiar la tabla directamente.
        inicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            buscar(null);
        });

        fin.valueProperty().addListener((observable, oldValue, newValue) -> {
            buscar(null);
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

        // Inicializamos el diálogo del error para mostrarlo cuando sea necesario.
        errorJugador.setTitle("ERROR");
        errorJugador.setHeaderText("Jugador incorrecto.");
        errorJugador.setContentText("Por favor, introduzca un nickname correcto.");

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

    private void ponerTabla() {
        if (!borderPane.getCenter().equals(tabla)) {
            borderPane.setCenter(tabla);
        }
    }

    private List<Round> acotarPartidas(ArrayList<Round> datos) {
        // Cojemos solo los datos en el intervalo de tiempo requerido.
        int indiceIni = 0;
        int indiceFin = datos.size() - 1;

        if (datos.get(0).getLocalDate().compareTo(inicio.getValue()) < 0) {
            for (Round ronda : datos) {
                if (ronda.getLocalDate().compareTo(inicio.getValue()) >= 0) {
                    indiceIni = datos.indexOf(ronda);
                    break;
                }
            }
        }

        if (datos.get(datos.size() - 1).getLocalDate().compareTo(fin.getValue()) > 0) {
            for (Round ronda : datos) {
                if (ronda.getLocalDate().compareTo(fin.getValue()) >= 0) {
                    indiceFin = datos.indexOf(ronda);
                    break;
                }
            }
        }
        return datos.subList(indiceIni, indiceFin);
    }

    @FXML
    private void tablaPartidas(ActionEvent event) {
        ponerTabla();
        TreeMap<LocalDate, List<Round>> roundsPerDay = connect4.getRoundsPerDay();
        dataList.clear();
        jugador.setDisable(true);
        // Añadir diferenciacion entre coger inicio/fin o el primero y el ultimo, yo me entiendo xd
        roundsPerDay.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach(
                (LocalDate date, List<Round> rounds) -> {
                    dataList.addAll(rounds);
                });
        tabla.setItems(dataList);
    }

    @FXML
    private void tablaRealizadas(ActionEvent event) {
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0) {
            jugador.setText(user);
        }
        if (!connect4.exitsNickName(jugador.getText())) {
            errorJugador.showAndWait();
        } else {
            ponerTabla();
            jugador.setDisable(false);

            ArrayList<Round> roundsPlayer = connect4.getRoundsPlayer(connect4.getPlayer(jugador.getText()));
            dataList.clear();

            dataList.addAll(acotarPartidas(roundsPlayer));
            tabla.setItems(dataList);
        }
    }

    @FXML
    private void tablaGanadas(ActionEvent event) {
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0) {
            jugador.setText(user);
        }
        if (!connect4.exitsNickName(jugador.getText())) {
            errorJugador.showAndWait();
        } else {
            ponerTabla();
            jugador.setDisable(false);
            ArrayList<Round> roundsPlayer = connect4.getWinnedRoundsPlayer(connect4.getPlayer(jugador.getText()));
            dataList.clear();

            dataList.addAll(acotarPartidas(roundsPlayer));
            tabla.setItems(dataList);
        }
    }

    @FXML
    private void tablaPerdidas(ActionEvent event) {
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0) {
            jugador.setText(user);
        }
        if (!connect4.exitsNickName(jugador.getText())) {
            errorJugador.showAndWait();
        } else {
            ponerTabla();
            jugador.setDisable(false);
            ArrayList<Round> roundsPlayer = connect4.getLostRoundsPlayer(connect4.getPlayer(jugador.getText()));
            dataList.clear();

            dataList.addAll(acotarPartidas(roundsPlayer));
            tabla.setItems(dataList);
        }
    }

    @FXML
    private void graficaPartidas(ActionEvent event) {
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0) {
            jugador.setText(user);
        }
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
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0) {
            jugador.setText(user);
        }
        jugador.setDisable(false);
        TreeMap<LocalDate, DayRank> dataPlayer = connect4.getDayRanksPlayer(connect4.getPlayer(user));

        ObservableList<XYChart.Data<String, Number>> numGanadas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<String, Number>> numPerdidas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<String, Number>> numOponentes = FXCollections.observableArrayList();

        // Creo que tambien hay que añadir lo de comprobar la fecha antes.
        dataPlayer.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach((date, rank) -> {
            numGanadas.add(new XYChart.Data(date.toString(), rank.getWinnedGames()));
            numPerdidas.add(new XYChart.Data(date.toString(), rank.getLostGames()));
            numOponentes.add(new XYChart.Data(date.toString(), rank.getOponents()));
        });

        // Gráfica de barras apiladas con las partidas ganadas y perdidas de cada día.
        CategoryAxis xAxisPartidas = new CategoryAxis();
        NumberAxis yAxisPartidas = new NumberAxis();
        xAxisPartidas.setLabel("Fecha");
        yAxisPartidas.setLabel("Número partidas");
        StackedBarChart<String, Number> partidas = new StackedBarChart<>(xAxisPartidas, yAxisPartidas);

        // algo esta falando
        XYChart.Series serieGanadas = new XYChart.Series(numGanadas);
        serieGanadas.setName("Partidas ganadas");

        XYChart.Series seriePerdidas = new XYChart.Series(numPerdidas);
        seriePerdidas.setName("Partidas perdidas");

        partidas.getData().addAll(serieGanadas, seriePerdidas);

        // Gráfica de barras con el nº de jugadores diferentes a los que se ha
        // enfrentado el jugador cada día.
        CategoryAxis xAxisJugadores = new CategoryAxis();
        NumberAxis yAxisJugadores = new NumberAxis();
        xAxisJugadores.setLabel("Fecha");
        yAxisJugadores.setLabel("Número partidas");
        BarChart<String, Number> jugadores = new BarChart<>(xAxisJugadores, yAxisJugadores);

        XYChart.Series serieOponentes = new XYChart.Series(numOponentes);
        serieOponentes.setName("Oponentes");

        jugadores.getData().addAll(serieOponentes);

        VBox fondo = new VBox();
        fondo.setAlignment(Pos.CENTER);
        fondo.setSpacing(10);
        fondo.getChildren().addAll(partidas, jugadores);
        borderPane.setCenter(fondo);
    }

    @FXML
    private void buscar(ActionEvent event) {
        if (partidasButton.isSelected()) {
            tablaPartidas(null);
        } else if (realizadasButton.isSelected()) {
            tablaRealizadas(null);
        } else if (perdidasButton.isSelected()) {
            tablaPartidas(null);
        } else if (ganadasButton.isSelected()) {
            tablaGanadas(null);
        } else if (numPartidasButton.isSelected()) {
            graficaPartidas(null);
        } else if (numJugadorButton.isSelected()) {
            graficaJugador(null);
        }
    }
}
