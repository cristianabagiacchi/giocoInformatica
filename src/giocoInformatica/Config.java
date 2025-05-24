package giocoInformatica;

public class Config {

    // Dimensione base del singolo tile (in pixel)
    public static final int dimensioneTileOriginale = 16;

    // Fattore di scala per ingrandire i tile
    public static final int fattoreScala = 3;

    // Numero di colonne visibili sullo schermo
    public static final int numeroColonne = 45; // 1350 / (16*3) = 1350/48 = 28.125 ma per adattare puoi aumentare il numero di colonne

    // Numero di righe visibili sullo schermo
    public static final int numeroRighe = 15; // 750 / (16*3) = 750/48 = 15.625 quindi 15 righe

    // Calcolo della dimensione di un tile scalato (dimensione reale di un tile sullo schermo)
    public static final int dimensioneTile = dimensioneTileOriginale * fattoreScala;

    // Larghezza dello schermo in pixel (fissa)
    public static final int larghezzaSchermo = 1350;

    // Altezza dello schermo in pixel (fissa)
    public static final int altezzaSchermo = 750;
}
