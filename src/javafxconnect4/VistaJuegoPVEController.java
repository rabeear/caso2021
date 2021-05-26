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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    private GridPane tableroGrid;
    @FXML
    private Label labelJugador;
    @FXML
    private Label labelPuntuacion;
    @FXML
    private Pane contenedorRaiz;
    @FXML
    private ToggleButton themeButton;
    @FXML
    private ImageView imagenTema;

    private Stage stageJuegoPVE;
    private Scene escenaJuegoPVE;
    private Player jugadorActual;
    private static boolean turno = true; // True -> Player / False -> Ordenador
    private MatrizDeTablero tableroIniciado;
    private final double TRANSLATE_Y = 68.5;
    private final double TRANSLATE_X = 66;
    private final int COL = 8;
    private final int RADIUS = 32;
    private Connect4 connect4;
    private SimpleObjectProperty<Theme> currentTheme;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connect4 = Connect4.getSingletonConnect4();
        } catch (Connect4DAOException ex) {
            Logger.getLogger(VistaJuegoPVEController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Cuando se pulse el botón, se cambia el modo de visualización.
        themeButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                currentTheme.set(Theme.DARK_THEME);
            } else {
                currentTheme.set(Theme.LIGTH_THEME);
            }
        });
    }

    /**
     * Iniciador para usar en el cambio de ventana.
     *
     * @param stage
     * @param seleccion
     * @param theme
     */
    public void initStage(Stage stage, Player seleccion, SimpleObjectProperty<Theme> theme) {
        stageJuegoPVE = stage;
        escenaJuegoPVE = stage.getScene();
        jugadorActual = seleccion;
        tableroIniciado = new MatrizDeTablero();
        labelJugador.setText(jugadorActual.getNickName());
        labelPuntuacion.setText("" + jugadorActual.getPoints());
        currentTheme = theme;
        setTheme();
    }

    private void setTheme() {
        // Cuando se cabie el modo de vsualización en otra ventana, se cambará en esta también.
        currentTheme.addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Theme.DARK_THEME)) {
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
                themeButton.setSelected(true);
            } else {
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
                themeButton.setSelected(false);
            }
        });

        switch (currentTheme.get()) {
            case DARK_THEME:
                contenedorRaiz.getStylesheets().add(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().remove(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/sol_tema.png", 21, 24, true, true));
                themeButton.setSelected(true);
                break;
            case LIGTH_THEME:
                contenedorRaiz.getStylesheets().remove(getClass().getResource("darkTheme.css").toExternalForm());
                contenedorRaiz.getStylesheets().add(getClass().getResource("ligthTheme.css").toExternalForm());
                imagenTema.setImage(new Image("/imagenes/luna_tema.png", 21, 24, true, true));
                themeButton.setSelected(false);
                break;
            default:
                throw new AssertionError(currentTheme.get().name());
        }
    }

    @FXML
    private void clickSalirPartida(ActionEvent event) {
        turno = true;
        stageJuegoPVE.setScene(escenaJuegoPVE);
    }

    @FXML
    private void clickReinicio(ActionEvent event) {
        // Limpiamos ambos tableros y añadimos los circulos blancos a la vista.
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
    private void clickJugar(MouseEvent event) throws InterruptedException, Connect4DAOException {
        // Cordenadas del click.
        int posX = posicionarX((int) event.getX());
        int posY = tableroIniciado.ultimaFicha(posX);
        ponerFichas(posX, posY);

        TranslateTransition animation = animation(fichaActual(), posX, posY);

        switcherTurno();

        if (tableroIniciado.comprobacionJuego()) {
            // falta registrar la partida.
            int n = Integer.parseInt(labelPuntuacion.getText()) + connect4.getPointsAlone();
            labelPuntuacion.setText("" + n);
            alertaVictoria(true);
            return; // Salir de la función.
        } else if (tableroIniciado.empate()) {
            alertaEmpate();
            return; // Salir de la función.
        }

        labelJugador.setText("ordenador");
        int[] pos = juegaMaquina();
        ponerFichas(pos[0], pos[1]);
        Circle ficha = fichaActual();

        // Una vez haya terminado la animación del jugador, hacemos la animación de la máquina.
        animation.setOnFinished((e) -> {
            animation(ficha, pos[0], pos[1]);
        });

        labelJugador.setText(jugadorActual.getNickName());
        if (tableroIniciado.comprobacionJuego()) {
            alertaVictoria(false);
            return; // Salir de la función.
        } else if (tableroIniciado.empate()) {
            alertaEmpate();
            return; // Salir de la función.
        }
        switcherTurno();
    }

    private Circle fichaActual() {
        // Creamos la ficha dependiendo del turno.
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

    // Turno de la máquina.
    private int[] juegaMaquina() {
        int posX;
        // Comprobamos que la columna no esté llena.
        do {
            posX = (int) (Math.random() * COL);
        } while (tableroIniciado.columnaLlena(posX));
        int posY = tableroIniciado.ultimaFicha(posX);

        return new int[]{posX, posY};
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

    private int posicionarX(int x) {
        // Saber que columna se clica, de 0 a 7.
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
            } else {
                return (x < 3 * medida) ? 2 : 3;
            }
        } else {
            if (x > 6 * medida) {
                return (x > 7 * medida) ? 7 : 6;
            } else {
                return (x < 5 * medida) ? 4 : 5;
            }
        }
    }

    private Bounds tamañoGrid() {
        // Obtener tamaño del gridTablero.
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
            sumaPuntos();

            tableroIniciado.clear();
            tableroGrid.getChildren().clear();
            añadirCirculos();
        } else {
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
            tableroIniciado.clear();
            tableroGrid.getChildren().clear();
            añadirCirculos();
        } else {
            stageJuegoPVE.setScene(escenaJuegoPVE);
        }
        turno = true;
    }

    /**
     * Suma los puntos que haya en la etiqueta y la limpia.
     *
     * @throws Connect4DAOException
     */
    public void sumaPuntos() throws Connect4DAOException {
        Connect4 connect4 = Connect4.getSingletonConnect4();
        jugadorActual = connect4.loginPlayer(jugadorActual.getNickName(), jugadorActual.getPassword());
        jugadorActual.plusPoints(connect4.getPointsAlone());
        labelPuntuacion.setText("" + jugadorActual.getPoints());
    }
}
