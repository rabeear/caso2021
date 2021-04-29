/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
public class VistaJuegoPVPController implements Initializable {

    @FXML
    private Label labelJugador;
    @FXML
    private Label labelPuntuacion;
    @FXML
    private GridPane tableroGrid;
    @FXML
    private Label labelPuntuacion2;

    private MatrizDeTablero tableroIniciado;
    private Stage stageActual;
    private Scene escenaActual;
    private Player j1, j2;
    private static boolean turno = true; //Controlar el turno, true -> j1, false -> j2
    private final double TRANSLATE_Y = 68.5;
    private final double TRANSLATE_X = 66;
    private final int COL = 8;
    private final int RADIUS = 32;
    private final int PUNTOS = 50;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initStage(Stage actualStage, Player player1, Player player2) {
        stageActual = actualStage;
        escenaActual = actualStage.getScene();
        j1 = player1;
        j2 = player2;
        labelPuntuacion.setText("" + j1.getPoints());
        labelPuntuacion2.setText("" + j2.getPoints());
        labelJugador.setText(j1.getNickName());
        tableroIniciado = new MatrizDeTablero();
    }

    @FXML
    private void clickSalir(ActionEvent event) {
        turno = true;
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
            HBox root = (HBox) cargador.load();

            VistaSegundaPrincipalController ventana2 = cargador.<VistaSegundaPrincipalController>getController();
            ventana2.initStage(stageActual, j1.getNickName(), j1.getPassword());
            ventana2.nombreUsuario.setText(j1.getNickName());
            Scene scene = new Scene(root, 800, 500);
            stageActual.setScene(scene);
            stageActual.show();
        } catch (Connect4DAOException | IOException e) {
        }
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        tableroIniciado.clear();
        tableroGrid.getChildren().clear();
        añadirCirculos();
        turno = true;
    }

    private void añadirCirculos() {
        // Añadir todos los circulos blancos.
        for (int r = 0; r < tableroGrid.getRowConstraints().size(); r++) {
            for (int c = 0; c < tableroGrid.getColumnConstraints().size(); c++) {
                Circle circulo = new Circle();
                circulo.setFill(javafx.scene.paint.Color.WHITE);
                circulo.setRadius(RADIUS - 1);
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
    private void clickJugar(MouseEvent event) throws Connect4DAOException {
        Circle ficha = fichaActual();
        // Obtenemos coordenadas del click
        int posicionX = posicionarX((int) event.getX());
        int posicionY = tableroIniciado.ultimaFicha(posicionX);

        // Añadimos la ficha a la matriz tablero.
        tableroIniciado.setNumero(posicionX, posicionY, turno);

        if (turno) { // Jugador 1.
            labelJugador.setText(j1.getNickName());
            labelJugador.setText(j2.getNickName());
        } else {
            labelJugador.setText(j1.getNickName());
        }
        animation(ficha, posicionX, posicionY);
        boolean finPartida = comprobacion();
        if (!finPartida) {
            switcherTurno();
        }
    }

    private boolean comprobacion() throws Connect4DAOException {
        if (tableroIniciado.comprobacionJuego()) {
            LocalDateTime time = LocalDateTime.now();
            Connect4 connect4 = Connect4.getSingletonConnect4();
            int n;

            if (turno) {
                n = Integer.parseInt(labelPuntuacion.getText()) + PUNTOS;
                labelPuntuacion.setText("" + n);
            } else {
                n = Integer.parseInt(labelPuntuacion2.getText()) + PUNTOS;
                labelPuntuacion2.setText("" + n);
            }
            // connect4.setPointsAlone(posicionY); Hay qu comprobar para qué es esto.
            connect4.regiterRound(time, j1, j2);
            alertaVictoria(turno);
            return true;
        } else if (tableroIniciado.empate()) {
            alertaEmpate();
            return true;
        }
        return false;
    }

    private Circle fichaActual() {
        Circle ficha = new Circle();
        if (turno) {
            ficha.setFill(javafx.scene.paint.Color.RED);
        } else {
            ficha.setFill(javafx.scene.paint.Color.YELLOW);
        }
        ficha.setRadius(RADIUS);
        ficha.setVisible(true);
        return ficha;
    }

    private void animation(Circle ficha, int posicionX, int posicionY) {
        tableroGrid.getChildren().add(ficha);
        ficha.setTranslateX(TRANSLATE_X * posicionX);
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), ficha);
        animation.setToY(TRANSLATE_Y * (6 - posicionY));
        animation.play();
    }

    private int posicionarX(int x) {
        int max, min, medida;
        Bounds tamaño = tamañoGrid();
        max = (int) tamaño.getMaxX();
        min = (int) tamaño.getMinX();
        if (min < 0) {
            min = 0;
        }
        medida = (max - min) / COL; // Cuánto mide cada columna.

        // Saber donde esta el click.
        if (x < 4 * medida) { // Si X esta por debajo de la mitad.
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
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        Player j;
        if (victoria) {
            alerta.setTitle("¡Ha ganado " + j1.getNickName() + "!");
            alerta.setHeaderText("¡ENORABUENA!");
            alerta.setContentText("¿Queréis volver a jugar?");
            j = j1;
        } else {
            alerta.setTitle("¡Ha ganado " + j2.getNickName() + "!");
            alerta.setHeaderText("¡ENORABUENA!");
            alerta.setContentText("¿Queréis volver a jugar?");
            j = j2;
        }

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.out.println("SEGUIR \n JUGANDO");
            sumaPuntos(j);

            tableroGrid.getChildren().clear();
            tableroIniciado.clear();
            añadirCirculos();
        } else {
            System.out.println("SALIR");
            stageActual.setScene(escenaActual);
            sumaPuntos(j);
        }
        turno = true;
    }

    private void alertaEmpate() throws Connect4DAOException {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
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
            stageActual.setScene(escenaActual);
        }
        turno = true;
    }

    public void sumaPuntos(Player jugadorActual) throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        int puntos = 50;

        jugadorActual = connect4.loginPlayer(jugadorActual.getNickName(), jugadorActual.getPassword());
        jugadorActual.plusPoints(puntos);
        if (jugadorActual.equals(j1)) {
            labelPuntuacion.setText("" + jugadorActual.getPoints());
        } else {
            labelPuntuacion.setText("" + jugadorActual.getPoints());
        }
    }
}
