package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player extends ImageView {
    private double velocita = 5;  // Velocità di movimento
    private double xIniziale, yIniziale;
    
    // Immagini per le 4 direzioni
    private Image imgSu;
    private Image imgGiù;
    private Image imgSinistra;
    private Image imgDestra;

    public Player(double x, double y) {
        // Carica le immagini per ogni direzione
        imgSu = new Image("file:personaggio_su.png");      // Immagine movimento su
        imgGiù = new Image("file:personaggio_giu.png");    // Immagine movimento giù
        imgSinistra = new Image("file:personaggio_sinistra.png");  // Immagine movimento sinistra
        imgDestra = new Image("file:personaggio_destra.png");  // Immagine movimento destra

        // Imposta l'immagine iniziale (faccio partire il personaggio guardando verso il basso)
        setImage(imgGiù);
        setFitWidth(48);  // Imposta la larghezza
        setFitHeight(48); // Imposta l'altezza

        xIniziale = x;
        yIniziale = y;
        setX(xIniziale);
        setY(yIniziale);
    }

    // Metodo per muovere il personaggio con W, A, S, D
    public void muovi(KeyEvent event) {
        if (event.getCode() == KeyCode.W) {
            setY(getY() - velocita);  // Muovi verso l'alto
            setImage(imgSu);          // Cambia l'immagine per il movimento verso l'alto
        } else if (event.getCode() == KeyCode.S) {
            setY(getY() + velocita);  // Muovi verso il basso
            setImage(imgGiù);         // Cambia l'immagine per il movimento verso il basso
        } else if (event.getCode() == KeyCode.A) {
            setX(getX() - velocita);  // Muovi verso sinistra
            setImage(imgSinistra);     // Cambia l'immagine per il movimento verso sinistra
        } else if (event.getCode() == KeyCode.D) {
            setX(getX() + velocita);  // Muovi verso destra
            setImage(imgDestra);       // Cambia l'immagine per il movimento verso destra
        }
    }
}
