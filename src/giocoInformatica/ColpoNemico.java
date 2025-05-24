package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ColpoNemico {
    private double x, y;               // Coordinate attuali del colpo (posizione sullo schermo)
    private double velocitaX, velocitaY;  // Velocità del colpo lungo gli assi X e Y (direzione e velocità)
    private ImageView imageView;       // Nodo grafico che rappresenta il colpo nell'interfaccia JavaFX

    // Costruttore: inizializza posizione, velocità e l'immagine del colpo
    public ColpoNemico(double x, double y, double velocitaX, double velocitaY) {
        this.x = x;                   // Posizione iniziale asse X
        this.y = y;                   // Posizione iniziale asse Y
        this.velocitaX = velocitaX;   // Velocità movimento asse X
        this.velocitaY = velocitaY;   // Velocità movimento asse Y

        // Crea un ImageView con l'immagine del colpo nemico caricata da risorsa
        imageView = new ImageView(new Image(this.getClass().getResourceAsStream("colponemico.png")));
        imageView.setFitWidth(30);    // Imposta la larghezza del colpo a 30 pixel
        imageView.setFitHeight(30);   // Imposta l'altezza del colpo a 30 pixel
        imageView.setX(x);            // Posiziona il colpo sulla coordinata X iniziale
        imageView.setY(y);            // Posiziona il colpo sulla coordinata Y iniziale
    }

    // Metodo chiamato ad ogni frame per aggiornare la posizione del colpo
    public void update() {
        x += velocitaX;               // Aggiorna la posizione X in base alla velocità
        y += velocitaY;               // Aggiorna la posizione Y in base alla velocità
        imageView.setX(x);            // Aggiorna la posizione grafica X
        imageView.setY(y);            // Aggiorna la posizione grafica Y
    }

    // Ritorna il nodo grafico (ImageView) associato al colpo, da aggiungere alla scena
    public ImageView getNode() {
        return imageView;
    }
}
