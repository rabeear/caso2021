/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Connect4;
import model.Player;

/**
 * FXML Controller class
 *
 * @author Rafa BA, Raquel RR
 */
public class VistaJuegoPVEController implements Initializable {

    @FXML
    private Button btnReinicio;
    @FXML
    private GridPane tableroGrid;
    @FXML
    private Label indicadorPruebas;
    @FXML
    private Label labelJugador;
    @FXML
    private Label labelPuntuacion;

    private Stage stageJuegoPVE;
    private Scene escenaJuegoPVE;
    private Player jugadorActual;
    private static boolean turno = true; // True -> Player / False -> Ordenador
    private MatrizDeTablero tableroIniciado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initStage(Stage stage, Player seleccion) {
        stageJuegoPVE = stage;
        escenaJuegoPVE = stage.getScene();
        jugadorActual = seleccion;
        tableroIniciado = new MatrizDeTablero();
        labelJugador.setText(jugadorActual.getNickName());
        labelPuntuacion.setText("" + jugadorActual.getPoints());
    }

    @FXML
    private void clickSalirPartida(ActionEvent event) {
        stageJuegoPVE.setScene(escenaJuegoPVE);
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        tableroIniciado.clear();
        Node node = tableroGrid.getChildren().get(0);
        tableroGrid.getChildren().clear();
        tableroGrid.getChildren().add(0, node);
    }

    private void añadirCirculos() {
        // añadir todos los circulos blancos.
    }

    private boolean switcherTurno() {
        turno = !turno;
        return turno;
    }

    @FXML
    private void clickJugar(MouseEvent event) throws InterruptedException, Connect4DAOException {
        // Crear ficha (Objeto (nodo) Circulo).
        Circle ficha = new Circle();

        // Cordenadas del click.
        int posicionX = posicionarX((int) event.getX());
        int posicionY = tableroIniciado.ultimaFicha(posicionX);
        indicadorPruebas.setText(posicionX + "," + posicionY);
        tableroIniciado.setNumero(posicionX, posicionY, turno);

        // Ejemplo para pintar de rojo -> ficha.setFill(javafx.scene.paint.Color.RED);
        ficha.setFill(javafx.scene.paint.Color.RED);
        ficha.setRadius(32);
        ficha.setVisible(true);

        tableroGrid.add(ficha, posicionX, 6 - posicionY);
        Thread.sleep(500);
        switcherTurno();

        if (tableroIniciado.comprobacionJuego()) {
            int n = Integer.parseInt(labelPuntuacion.getText()) + 20;
            labelPuntuacion.setText("" + n);
            indicadorPruebas.setText("Gana " + jugadorActual.getNickName()); // Cambiar por autoreinicio o alerta + 2 botones con reinicio o salir.
            alertaVictoria(true);
            return; // Salir de la función.
        }

        labelJugador.setText("ordenador");
        juegaMaquina();
        labelJugador.setText(jugadorActual.getNickName());

        if (tableroIniciado.comprobacionJuego()) {
            indicadorPruebas.setText("Gana ordenador");
            alertaVictoria(false);
            return; // Salir de la función.
        }
        switcherTurno();
    }

    // Saber que columna se clica, de 0 a 7.
    private int posicionarX(int x) {
        int max, min, medida;
        Bounds tamaño = tamañoGrid();
        max = (int) tamaño.getMaxX();
        min = (int) tamaño.getMinX();
        if (min < 0) {
            min = 0;
        }
        medida = (max - min) / 8; // Cuanto mide cada columna.

        // Saber donde esta el click.
        if (x < 4 * medida) { // si X esta por debajo de la mitad.
            if (x < 2 * medida) {
                return (x < medida) ? 0 : 1;
            }
            return (x < 3 * medida) ? 2 : 3;
        } else {
            if (x > 6 * medida) {
                return (x > 7 * medida) ? 7 : 6;
            } else {
                return (x < 5 * medida) ? 4 : 5;
            }
        }
    }

    // Saber en que Y se tiene que posicionar el nodo circulo nuevo.
    private int fichasEnColumna(int col) {
        int max, min, res, media;
        return 0;
    }

    // Obtener tamaño del gridTablero.
    private Bounds tamañoGrid() {
        Bounds tamaño = tableroGrid.getBoundsInLocal();
        return tamaño;
    }

    // Turno de la máquina.
    private void juegaMaquina() {
        int x = (int) (Math.random() * 7);
        int y = tableroIniciado.ultimaFicha(x);;
        tableroIniciado.setNumero(x, y, turno);
        Circle ficha = new Circle();

        ficha.setFill(javafx.scene.paint.Color.YELLOW);
        ficha.setRadius(32);
        ficha.setVisible(true);
        tableroGrid.add(ficha, x, 6 - y);
    }

    public void alertaVictoria(boolean victoria) throws Connect4DAOException {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        if (victoria) {
            alerta.setTitle("¡Victoria!");
            alerta.setHeaderText("¡Has ganado!");
            alerta.setContentText("¿Quieres volver a jugar?");
        } else {
            alerta.setTitle("Derrota");
            alerta.setHeaderText("Has perdido...");
            alerta.setContentText("¿Quieres volver a jugar?");
        }

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.out.println("SEGUIR \n JUGANDO");
            tableroIniciado.clear();
            sumaPuntos();

            Node node = tableroGrid.getChildren().get(0);
            tableroGrid.getChildren().clear();
            tableroGrid.getChildren().add(0, node);
        } else {
            System.out.println("SALIR");
            stageJuegoPVE.setScene(escenaJuegoPVE);
            sumaPuntos();
        }
    }

    // Suma los puntos que haya en el label puntos y limpia.
    public void sumaPuntos() throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        int puntos = 20;

        jugadorActual = connect4.loginPlayer(jugadorActual.getNickName(), jugadorActual.getPassword());
        jugadorActual.plusPoints(puntos);
        labelPuntuacion.setText("" + jugadorActual.getPoints());
    }
}
