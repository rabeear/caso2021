/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

/**
 *
 * @author Rafa BA, Raquel RR
 */
public class MatrizDeTablero {

    private final int[][] matrizPrincipal;
    private final int COL = 8;
    private final int ROW = 7;

    /**
     * Método constructor para llenar con 0 para el usuario, 1 para el ordenador
     * u otro usuario y -1 cuando no haya nada.
     */
    public MatrizDeTablero() {
        matrizPrincipal = new int[COL][ROW];
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                matrizPrincipal[i][j] = -1;
            }
        }
    }

    /**
     * Añade una "ficha" a la matriz.
     *
     * @param x
     * @param y
     * @param jugador
     */
    public void setNumero(int x, int y, boolean jugador) {
        if (jugador) {
            matrizPrincipal[x][y] = 0; // usuario1
        } else {
            matrizPrincipal[x][y] = 1; // máquina/usuario2
        }
    }

    /**
     * Devuelve la "ficha" que hay en la matriz en esa posición.
     *
     * @param x
     * @param y
     * @return
     */
    public int getNumero(int x, int y) {
        return matrizPrincipal[x][y];
    }

    public int ultimaFicha(int x) {
        int res = 0;
        while (res < ROW && matrizPrincipal[x][res] != -1) {
            res += 1;
        }
        return res;
    }

    /**
     * Vacía la matriz.
     */
    public void clear() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                matrizPrincipal[i][j] = -1;
            }
        }
    }

    private boolean linea() {
        for (int j = 0; j < ROW; j++) { //j es la Y, las filas
            for (int i = 0; i <= 4; i++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i + 1][j]
                        && matrizPrincipal[i + 1][j] == matrizPrincipal[i + 2][j]
                        && matrizPrincipal[i + 2][j] == matrizPrincipal[i + 3][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean columna() {
        for (int i = 0; i < COL; i++) { //j es la Y, las filas
            for (int j = 0; j <= 3; j++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i][j + 1]
                        && matrizPrincipal[i][j + 1] == matrizPrincipal[i][j + 2]
                        && matrizPrincipal[i][j + 2] == matrizPrincipal[i][j + 3]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean diagonal() {
        boolean res = false;
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 3; j++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i + 1][j + 1]
                        && matrizPrincipal[i + 1][j + 1] == matrizPrincipal[i + 2][j + 2]
                        && matrizPrincipal[i + 2][j + 2] == matrizPrincipal[i + 3][j + 3]) {
                    res = true;
                    break;
                }
            }
        }
        for (int i = 7; i >= 3; i--) {
            for (int j = 6; j >= 3; j--) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i - 1][j - 1]
                        && matrizPrincipal[i - 1][j - 1] == matrizPrincipal[i - 2][j - 2]
                        && matrizPrincipal[i - 2][j - 2] == matrizPrincipal[i - 3][j - 3]) {
                    res = true;
                    break;
                }
            }
        }
        for (int j = 6; j >= 3; j--) { //j es la Y, las filas
            for (int i = 0; i <= 4; i++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i + 1][j - 1]
                        && matrizPrincipal[i + 1][j - 1] == matrizPrincipal[i + 2][j - 2]
                        && matrizPrincipal[i + 2][j - 2] == matrizPrincipal[i + 3][j - 3]) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Comrpueba si ha ganado alguien.
     *
     * @return
     */
    public boolean comprobacionJuego() {
        return this.columna() || this.linea() || this.diagonal();
    }

    /**
     * Comprueba si la columna esta llena.
     *
     * @param columna
     * @return
     */
    public boolean columnaLlena(int columna) {
        return !(matrizPrincipal[columna][ROW - 1] == -1);
    }

    /**
     * Comprueba si hay un empate.
     *
     * @return
     */
    public boolean empate() {
        for (int i = 0; i < COL; i++) {
            if (!columnaLlena(i)) {
                return false;
            }
        }
        return true;
    }
}
