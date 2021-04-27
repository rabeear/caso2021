/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import DBAccess.Connect4DAOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
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
import model.Connect4;
import model.Player;
import model.Round;

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
    private Label indicadorPruebas;
    @FXML
    private Button btnReinicio;
    @FXML
    private GridPane tableroGrid;
    @FXML
    private Label labelPuntuacion2;
    private MatrizDeTablero tableroIniciado;
    private Stage stageActual;
    private Scene escenaActual;
    private Player j1, j2;
    private static boolean turno = true; //Controlar el turno, true -> j1, false -> j2
    private Round partida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        tableroIniciado.clear();
        Node node = tableroGrid.getChildren().get(0);
        tableroGrid.getChildren().clear();
        tableroGrid.getChildren().add(0, node);
    }

    @FXML
    private void clickJugar(MouseEvent event) throws InterruptedException, Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        Circle ficha = new Circle();
        LocalDateTime t = LocalDateTime.now();
        int posicionX = posicionarX((int) event.getX());
        int posicionY = tableroIniciado.ultimaFicha(posicionX);
        // indicadorPruebas.setText(posicionX + "," + posicionY);
        tableroIniciado.setNumero(posicionX, posicionY, turno);

        if (turno) { // Jugador 1.
            labelJugador.setText(j1.getNickName());
            ficha.setFill(javafx.scene.paint.Color.RED);
            ficha.setRadius(20);
            ficha.setVisible(true);

            tableroGrid.add(ficha, posicionX, 6 - posicionY);
            Thread.sleep(500);
            switcherTurno();
            labelJugador.setText(j2.getNickName());
            if (tableroIniciado.comprobacionJuego()) {
                int n = Integer.parseInt(labelPuntuacion.getText()) + 50;
                labelPuntuacion.setText("" + n);
                partida = connect4.regiterRound(t, j1, j2);
                alertaVictoria(true);
                return; // Salir de la función.
            }
        } else {
            ficha.setFill(javafx.scene.paint.Color.YELLOW);
            ficha.setRadius(20);
            ficha.setVisible(true);

            tableroGrid.add(ficha, posicionX, 6 - posicionY);
            Thread.sleep(500);
            switcherTurno();
            labelJugador.setText(j1.getNickName());
            if (tableroIniciado.comprobacionJuego()) {
                int n = Integer.parseInt(labelPuntuacion2.getText()) + 50;
                labelPuntuacion2.setText("" + n);
                partida = connect4.regiterRound(t, j1, j2);
                connect4.setPointsAlone(posicionY);
                alertaVictoria(false);
                return; // Salir de la función.
            }
        }
    }

    void initStage(Stage actualStage, Player player1, Player player2) {
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
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("VistaSegundaPrincipal.fxml"));
            HBox root = (HBox) cargador.load();

            VistaSegundaPrincipalController ventana2 = cargador.<VistaSegundaPrincipalController>getController();
            ventana2.initStage(stageActual, j1.getNickName(), j1.getPassword());
            ventana2.nombreUsuario.setText(j1.getNickName());
            Scene scene = new Scene(root, 800, 500);
            stageActual.setScene(scene);
            stageActual.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int posicionarX(int x) {
        int max, min, medida;
        Bounds tamaño = tamañoGrid();
        max = (int) tamaño.getMaxX();
        min = (int) tamaño.getMinX();
        if (min < 0) {
            min = 0;
        }
        medida = (max - min) / 8; //cuanto mide cada columna

        //saber donde esta el click:
        if (x < 4 * medida) { //si X esta por debajo de la mitad
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
            tableroIniciado.clear();
            sumaPuntos(j);

            Node node = tableroGrid.getChildren().get(0);
            tableroGrid.getChildren().clear();
            tableroGrid.getChildren().add(0, node);
        } else {
            System.out.println("SALIR");
            stageActual.setScene(escenaActual);
            sumaPuntos(j);
        }
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

    private boolean switcherTurno() {
        turno = !turno;
        return turno;
    }
}
