/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Player;
/**
 * FXML Controller class
 *
 * @author belen
 */
public class VistaJuegoPVEController implements Initializable {

    private Stage stageJuegoPVE;
    private Scene escenaJuegoPVE;
    private Player jugadorActual;
    private static boolean turno = true; //True -> Player / False -> Ordenador
    private MatrizDeTablero tableroIniciado;
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
        tableroGrid.getChildren().add(0,node);
    }
    
    private boolean switcherTurno() {
        turno = !turno;
        return turno;
    }

    @FXML
    private void clickJugar(MouseEvent event) throws InterruptedException {
        //Crear ficha (Objeto (nodo) Circulo);
            Circle ficha = new Circle();
        
            //Cordenadas del click
            int posicionX = posicionarX((int) event.getX());
            int posicionY = tableroIniciado.ultimaFicha(posicionX);
            indicadorPruebas.setText(posicionX + "," + posicionY);
            tableroIniciado.setNumero(posicionX, posicionY, turno);
            //switcherTurno();
            
            // ejemplo para pintar de rojo -> ficha.setFill(javafx.scene.paint.Color.RED);
            ficha.setFill(javafx.scene.paint.Color.RED);
            ficha.setRadius(20);
            ficha.setVisible(true);
            
            tableroGrid.add(ficha, posicionX,6 - posicionY);
            Thread.sleep(500);
            switcherTurno();
            if (tableroIniciado.comprobacionJuego()) {
                int n = Integer.parseInt(labelPuntuacion.getText()) + 20;
                labelPuntuacion.setText("" + n);
                indicadorPruebas.setText("Se ganó"); // cambiar por autoreinicio o alerta + 2 botones con reinicio o salir.
            }
            juegaMaquina();
            
    }
    
    //Saber que columna se clica, de 0 a 7;
    private int posicionarX(int x) {
        int max, min, res, medida;
        Bounds tamaño = tamañoGrid();
        max = (int) tamaño.getMaxX();
        min = (int) tamaño.getMinX();
        if (min < 0) { min = 0; }
        medida = (max - min) / 8; //cuanto mide cada columna
        
        //saber donde esta el click:
        if (x < 4*medida) { //si X esta por debajo de la mitad
            if (x < 2*medida) {
                if (x < medida) {
                    return 0;
                } else { return 1; }
            } if (x < 3*medida) {
                return 2;
            } else { return 3; }
        } else {
            if (x > 6*medida) {
                if (x > 7*medida) {
                    return 7;
                } else { return 6; }
            } else {
                if (x < 5*medida) {
                    return 4;
                } else {
                    return 5;
                }
            }
        }
    }
    //Saber en que Y se tiene que posicionar el nodo circulo nuevo;
    private int fichasEnColumna(int col) {
        int max, min, res, media;
        return 0;
    }
    //obtener tamaño del gridTablero;
    private Bounds tamañoGrid() {
        Bounds tamaño = tableroGrid.getBoundsInLocal();
        return tamaño;
    }
    
    
    //Turno de la máquina
    private void juegaMaquina() {
        int x = (int)(Math.random()*7);
        int y = tableroIniciado.ultimaFicha(x);;
        tableroIniciado.setNumero(x, y, turno);
        Circle ficha = new Circle();
        
        ficha.setFill(javafx.scene.paint.Color.YELLOW);
        ficha.setRadius(20);
        ficha.setVisible(true);
            
        tableroGrid.add(ficha, x, 6 - y);
        switcherTurno();
    }
}
