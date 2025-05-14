package giocoInformatica;

public class Config {

    // Dimensione base del singolo tile (in pixel)
    public static final int TILE_ORIGINALE = 16;

    // Fattore di scala per ingrandire i tile
    public static final int SCALA = 3;

    // Numero di colonne e righe visibili sullo schermo
    public static final int COLONNE = 40;
    public static final int RIGHE = 22;

    // Calcolo automatico delle dimensioni dello schermo in pixel
    public static final int TILE_SIZE = TILE_ORIGINALE * SCALA;
    public static final int LARGHEZZA_SCHERMO = TILE_SIZE * COLONNE;
    public static final int ALTEZZA_SCHERMO = TILE_SIZE * RIGHE;
}

