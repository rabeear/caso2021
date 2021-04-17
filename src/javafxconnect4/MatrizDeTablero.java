/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxconnect4;

/**
 *
 * @author belen
 */
public class MatrizDeTablero {
    private int[][] matrizPrincipal;
    //se va a llenar con 0 y 1, 0 para el usuario y 1 para el ordenador. null cuando no haya nada
    public MatrizDeTablero() {
        matrizPrincipal = new int[8][7];
        for (int i=0;i<8;i++){
            for(int j=0;j<7;j++){
                matrizPrincipal[i][j] = -1;
            }
        }
    }
    
    public void setNumero(int x, int y,boolean jugador) {
        if (jugador) {
            matrizPrincipal[x][y] = 0; //usuario
        } else {
            matrizPrincipal[x][y] = 1; //máquina
        }
    }
    
    public int getNumero(int x, int y) {
        return matrizPrincipal[x][y];
    }
    
    public int ultimaFicha(int x) {
        int res = 0;
        while (res < 7 && matrizPrincipal[x][res] != -1) {
            res += 1;
        }
        
        return res;
    }
    
    public void clear() {
        for (int i=0;i<8;i++){
            for(int j=0;j<7;j++){
                matrizPrincipal[i][j] = -1;
            }
        }
    }
    
    public boolean linea() {
        for (int j=0;j<7;j++) { //j es la Y, las filas
            for (int i = 0;i<=4;i++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i+1][j] && 
                        matrizPrincipal[i+1][j] == matrizPrincipal[i + 2][j] &&
                        matrizPrincipal[i +2][j] == matrizPrincipal[i + 3][j]) {
                    return true;
                }
            }
        }
        return false;
            
        }
    
    public boolean columna() {
        for (int i=0;i<8;i++) { //j es la Y, las filas
            for (int j = 0;j<=3;j++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i][j+1] && 
                        matrizPrincipal[i][j+1] == matrizPrincipal[i][j + 2] &&
                        matrizPrincipal[i][j + 2] == matrizPrincipal[i][j + 3]) {
                    return true;
                }
            }
        
         }
        return false;
        }
    
    public boolean diagonal() {
        boolean res = false;
        for (int i=0;i<=4;i++) { //j es la Y, las filas
            for (int j = 0;j<=3;j++) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i+1][j+1] && 
                        matrizPrincipal[i+1][j+1] == matrizPrincipal[i + 2][j + 2] &&
                        matrizPrincipal[i + 2][j + 2] == matrizPrincipal[i + 3][j + 3]) {
                    res = true;
                    break;
                }
            }
        }
        for (int i = 7;i>=3;i--) {
            for (int j=6;j>=3;j--) {
                if (matrizPrincipal[i][j] != -1 && matrizPrincipal[i][j] == matrizPrincipal[i-1][j-1] && 
                        matrizPrincipal[i-1][j-1] == matrizPrincipal[i - 2][j - 2] &&
                        matrizPrincipal[i - 2][j - 2] == matrizPrincipal[i - 3][j - 3]) {
                    res = true;
                    break;
                }
            }
        }
        return res;
    }
    
    public boolean comprobacionJuego() {
        return this.columna() || this.linea() || this.diagonal();
    }
    }

