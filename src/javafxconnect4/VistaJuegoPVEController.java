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
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
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
import javafx.util.Duration;
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
    private Label labelJugador;
    @FXML
    private Label labelPuntuacion;

    private Stage stageJuegoPVE;
    private Scene escenaJuegoPVE;
    private Player jugadorActual;
    private static boolean turno = true; // True -> Player / False -> Ordenador
    private MatrizDeTablero tableroIniciado;
    private final double TRANSLATE_Y = 68.5;
    private final double TRANSLATE_X = 66;
    private final int COL = 7;

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
        turno = true;
        stageJuegoPVE.setScene(escenaJuegoPVE);
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        tableroIniciado.clear();
        tableroGrid.getChildren().clear();
        añadirCirculos();
        turno = true;
    }

    private void añadirCirculos() {
        // añadir todos los circulos blancos.
        for (int r = 0; r < tableroGrid.getRowConstraints().size(); r++) {
            for (int c = 0; c < tableroGrid.getColumnConstraints().size(); c++) {
                Circle circulo = new Circle();
                circulo.setFill(javafx.scene.paint.Color.WHITE);
                circulo.setRadius(31);
                circulo.setVisible(true);
                tableroGrid.add(circulo, c, r);
            }
        }
    }

    private boolean switcherTurno() {
        turno = !turno;
        return turno;
    }

    @FXML
    private void clickJugar(MouseEvent event) throws InterruptedException, Connect4DAOException {
        // Cordenadas del click.
        int posX = posicionarX((int) event.getX());
        int posY = tableroIniciado.ultimaFicha(posX);

        ponerFichas(posX, posY);
        TranslateTransition animation = animation(fichaActual(), posX, posY);

        switcherTurno();

        if (tableroIniciado.comprobacionJuego()) {
            int n = Integer.parseInt(labelPuntuacion.getText()) + 20;
            labelPuntuacion.setText("" + n);
            alertaVictoria(true);
            return; // Salir de la función.
        } else if (tableroIniciado.empate()) {
            alertaEmpate();
        }

        labelJugador.setText("ordenador");
        int[] coord = juegaMaquina();
        ponerFichas(coord[0], coord[1]);
        Circle ficha = fichaActual();
        animation.setOnFinished((e) -> {
            animation(ficha, coord[0], coord[1]);
        });

        labelJugador.setText(jugadorActual.getNickName());
        if (tableroIniciado.comprobacionJuego()) {
            alertaVictoria(false);
            return; // Salir de la función.
        } else if (tableroIniciado.empate()) {
            alertaEmpate();
            return;
        }
        switcherTurno();
    }

    private Circle fichaActual() {
        Circle ficha = new Circle();
        if (turno) {
            ficha.setFill(javafx.scene.paint.Color.RED);
        } else {
            ficha.setFill(javafx.scene.paint.Color.YELLOW);
        }
        ficha.setRadius(32);
        ficha.setVisible(true);
        return ficha;
    }

    // Turno de la máquina.
    private int[] juegaMaquina() {
        int x;
        do {
            x = (int) (Math.random() * (COL + 1));
        } while (tableroIniciado.columnaLlena(x));
        int y = tableroIniciado.ultimaFicha(x);

        int[] coord = new int[]{x, y};
        return coord;
    }

    private void ponerFichas(int posX, int posY) throws InterruptedException {
        tableroIniciado.setNumero(posX, posY, turno);
    }

    private TranslateTransition animation(Circle ficha, int posicionX, int posicionY) {
        tableroGrid.getChildren().add(ficha);
        ficha.setTranslateX(TRANSLATE_X * posicionX);
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), ficha);
        animation.setToY(TRANSLATE_Y * (6 - posicionY));
        animation.play();
        return animation;
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

    // Obtener tamaño del gridTablero.
    private Bounds tamañoGrid() {
        Bounds tamaño = tableroGrid.getBoundsInLocal();
        return tamaño;
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
            sumaPuntos();

            tableroIniciado.clear();
            tableroGrid.getChildren().clear();
            añadirCirculos();
        } else {
            System.out.println("SALIR");
            stageJuegoPVE.setScene(escenaJuegoPVE);
            sumaPuntos();
        }
        turno = true;
    }

    private void alertaEmpate() throws Connect4DAOException {
        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("¡Empate!");
        alerta.setHeaderText("¡Habéis empatado!");
        alerta.setContentText("¿Quieres volver a jugar?");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.out.println("SEGUIR \n JUGANDO");

            tableroIniciado.clear();
            tableroGrid.getChildren().clear();
            añadirCirculos();
        } else {
            System.out.println("SALIR");
            stageJuegoPVE.setScene(escenaJuegoPVE);
        }
        turno = true;
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
