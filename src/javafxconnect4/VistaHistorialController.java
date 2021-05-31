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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
    private BorderPane contenedorRaiz;
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
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;
    @FXML
    private Label errorBusqueda;

    private Connect4 connect4;
    private String user;
    private SimpleObjectProperty<Theme> currentTheme;
    private TreeMap<LocalDate, List<Round>> roundsPerDay;
    private final ObservableList<Round> dataList = FXCollections.observableArrayList();
    private boolean errorEscrito; // Indica si hay algo en la label errorBusqueda.

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorEscrito = false; // Inicializado a false.
        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });

        try {
            connect4 = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaHistorialController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Programamos el calendario de manera que no se pueda elegir una fecha
        // fuera del rango de la fecha de la primera y útima partidas realizadas
        // en el sistema.
        roundsPerDay = connect4.getRoundsPerDay();
        LocalDate iniMaxDate = roundsPerDay.lastKey();
        LocalDate iniMinDate = roundsPerDay.firstKey();
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

        fin.setValue(iniMaxDate);
        inicio.setValue(iniMinDate);

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

        // Iniciamos la vista enseñando todas las partidas registradas en el sistema.
        partidasButton.setSelected(true);
        tablaPartidas(null);
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param usr: nombre del usuario que ha iniciado sesión.
     * @param theme
     */
    public void initStage(String usr, SimpleObjectProperty<Theme> theme) {
        user = usr;
        currentTheme = theme;
        setTheme();
    }

    private void setTheme() {
        // Cuando se cabie el modo de vsualización en otra ventana, se cambará en esta también.
        currentTheme.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Theme.DARK_THEME)) {
                setDark();
            } else {
                setLigth();
            }
        });

        switch (currentTheme.get()) {
            case DARK_THEME:
                setDark();
                break;
            case LIGTH_THEME:
                setLigth();
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }

    private void setLigth() {
        contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
        contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
        imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
        themeButton.setSelected(false);
    }

    private void setDark() {
        contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
        contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
        imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
        themeButton.setSelected(true);
    }

    private void ponerTabla() {
        if (!contenedorRaiz.getCenter().equals(tabla)) {
            contenedorRaiz.setCenter(tabla);
        }
    }

    private List<Round> acotarPartidas(ArrayList<Round> datos) {
        // Cojemos solo los datos en el intervalo de tiempo requerido.
        int indiceIni = 0;
        int indiceFin = datos.size() - 1;
        LocalDateTime primeraFecha = datos.get(indiceIni).getLocalDate().atStartOfDay();
        LocalDateTime ultimaFecha = datos.get(indiceFin).getLocalDate().atStartOfDay();
        if (!((primeraFecha.isAfter(inicio.getValue().atStartOfDay())
                || primeraFecha.isEqual(inicio.getValue().atStartOfDay())))) {
            for (Round ronda : datos) {
                if (ronda.getLocalDate().atStartOfDay().isAfter(inicio.getValue().atStartOfDay())
                        || ronda.getLocalDate().atStartOfDay().isEqual(inicio.getValue().atStartOfDay())) {
                    indiceIni = datos.indexOf(ronda);
                    break;
                }
            }
        }

        if (!((ultimaFecha.isBefore(fin.getValue().atStartOfDay())
                || ultimaFecha.isEqual(fin.getValue().atStartOfDay())))) {
            for (Round ronda : datos) {
                if (ronda.getLocalDate().atStartOfDay().isAfter(fin.getValue().atStartOfDay())) {
                    indiceFin = datos.indexOf(ronda) - 1;
                    break;
                }
            }
        }
        return datos.subList(indiceIni, indiceFin + 1);
    }

    @FXML
    private void tablaPartidas(ActionEvent event) {
        ponerTabla();
        // Ya tenemos creado el TreeMap con los datos
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
        comprobarBarraBusqueda();
        if (!connect4.exitsNickName(jugador.getText())) {
            errorBusqueda.setText("El usuario no existe");
            errorEscrito = true;
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
        comprobarBarraBusqueda();
        if (!connect4.exitsNickName(jugador.getText())) {
            errorBusqueda.setText("El usuario no existe");
            errorEscrito = true;
        } else {
            ponerTabla();
            jugador.setDisable(false);
            ArrayList<Round> roundsPlayer = connect4.getWinnedRoundsPlayer(connect4.getPlayer(jugador.getText()));
            dataList.clear();

            if (!roundsPlayer.isEmpty()) {
                dataList.addAll(acotarPartidas(roundsPlayer));
                tabla.setItems(dataList);
            }
        }
    }

    @FXML
    private void tablaPerdidas(ActionEvent event) {
        comprobarBarraBusqueda();
        if (!connect4.exitsNickName(jugador.getText())) {
            errorBusqueda.setText("El usuario no existe");
            errorEscrito = true;
        } else {
            ponerTabla();
            jugador.setDisable(false);
            ArrayList<Round> roundsPlayer = connect4.getLostRoundsPlayer(connect4.getPlayer(jugador.getText()));
            dataList.clear();

            if (!roundsPlayer.isEmpty()) {
                dataList.addAll(acotarPartidas(roundsPlayer));
                tabla.setItems(dataList);
            }
        }
    }

    @FXML
    private void graficaPartidas(ActionEvent event) {
        comprobarBarraBusqueda();
        jugador.setDisable(true);
        TreeMap<LocalDate, Integer> numPartidas = connect4.getRoundCountsPerDay();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Número partidas");

        ObservableList<XYChart.Data<String, Number>> lineChartData
                = FXCollections.observableArrayList();
        numPartidas.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach(
                (date, rounds) -> {
                    lineChartData.add(new XYChart.Data(date.getDayOfMonth() + "-" + date.getMonthValue(), rounds));
                });

        // Rellenar la serie con la ObservableList creada anteriormente.
        XYChart.Series serie = new XYChart.Series(lineChartData);
        serie.setName("Partidas diarias");

        LineChart<String, Number> grafica = new LineChart<>(xAxis, yAxis);
        grafica.setTitle("Nº partidas totales");
        grafica.setLegendVisible(false);
        grafica.getData().add(serie);
        contenedorRaiz.setCenter(grafica);
    }

    @FXML
    private void graficaJugador(ActionEvent event) {
        comprobarBarraBusqueda();
        jugador.setDisable(false);
        TreeMap<LocalDate, DayRank> dataPlayer = connect4.getDayRanksPlayer(connect4.getPlayer(jugador.getText()));

        ObservableList<XYChart.Data<String, Number>> numGanadas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<String, Number>> numPerdidas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<String, Number>> numOponentes = FXCollections.observableArrayList();

        // Creo que tambien hay que añadir lo de comprobar la fecha antes.
        dataPlayer.subMap(inicio.getValue(), fin.getValue().plusDays(1)).forEach((date, rank) -> {
            numGanadas.add(new XYChart.Data(date.getDayOfMonth() + "-" + date.getMonthValue(), rank.getWinnedGames()));
            numPerdidas.add(new XYChart.Data(date.getDayOfMonth() + "-" + date.getMonthValue(), rank.getLostGames()));
            numOponentes.add(new XYChart.Data(date.getDayOfMonth() + "-" + date.getMonthValue(), rank.getOponents()));
        });

        // Gráfica de barras apiladas con las partidas ganadas y perdidas de cada día.
        CategoryAxis xAxisPartidas = new CategoryAxis();
        NumberAxis yAxisPartidas = new NumberAxis();
        yAxisPartidas.setLabel("Número partidas");
        StackedBarChart<String, Number> partidas = new StackedBarChart<>(xAxisPartidas, yAxisPartidas);

        XYChart.Series serieGanadas = new XYChart.Series(numGanadas);
        serieGanadas.setName("Partidas ganadas");

        XYChart.Series seriePerdidas = new XYChart.Series(numPerdidas);
        seriePerdidas.setName("Partidas perdidas");

        partidas.getData().addAll(serieGanadas, seriePerdidas);

        // Gráfica de barras con el nº de jugadores diferentes a los que se ha
        // enfrentado el jugador cada día.
        CategoryAxis xAxisJugadores = new CategoryAxis();
        NumberAxis yAxisJugadores = new NumberAxis();
        yAxisJugadores.setLabel("Número partidas");
        BarChart<String, Number> jugadores = new BarChart<>(xAxisJugadores, yAxisJugadores);
        jugadores.setLegendVisible(false);

        XYChart.Series serieOponentes = new XYChart.Series(numOponentes);
        serieOponentes.setName("Oponentes");

        jugadores.getData().addAll(serieOponentes);

        VBox fondo = new VBox();
        fondo.setAlignment(Pos.CENTER);
        fondo.setSpacing(10);
        fondo.getChildren().addAll(partidas, jugadores);
        contenedorRaiz.setCenter(fondo);
    }

    private void comprobarBarraBusqueda() {
        if (jugador.getText().isEmpty() || jugador.getText().split(" ").length == 0
                || errorEscrito) {
            jugador.setText(user);
            if (errorEscrito) {
                errorBusqueda.setText("");
                errorEscrito = false;
            }
        }
    }

    @FXML
    private void buscar(ActionEvent event) {
        if (!connect4.exitsNickName(jugador.getText())) {
            errorBusqueda.setText("El usuario no existe");
            errorEscrito = true;
        } else if (partidasButton.isSelected()) {
            tablaPartidas(null);
        } else if (realizadasButton.isSelected()) {
            tablaRealizadas(null);
        } else if (perdidasButton.isSelected()) {
            tablaPerdidas(null);
        } else if (ganadasButton.isSelected()) {
            tablaGanadas(null);
        } else if (numPartidasButton.isSelected()) {
            graficaPartidas(null);
        } else if (numJugadorButton.isSelected()) {
            graficaJugador(null);
        }
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Node miNodo = (Node) event.getSource();
        ((Stage) miNodo.getScene().getWindow()).close();
    }
}
