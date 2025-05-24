package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ColpoPlayer {

    private double x, y;                      // Coordinate attuali del colpo del giocatore
    private double velocita = 7;              // Velocità fissa con cui il colpo si muove
    private Player.Direzione direzione;       // Direzione in cui il colpo si muove (enum definito nella classe Player)
    private ImageView imageView;              // Nodo grafico che rappresenta il colpo nell'interfaccia JavaFX

    // Costruttore: inizializza posizione, direzione e immagine del colpo
    public ColpoPlayer(double x, double y, Player.Direzione direzione) {
        this.x = x;                          // Imposta la posizione iniziale asse X
        this.y = y;                          // Imposta la posizione iniziale asse Y
        this.direzione = direzione;          // Imposta la direzione di movimento del colpo

        // Debug: stampa le coordinate iniziali del colpo
        System.out.println(x);
        System.out.println(y);

        // Carica l'immagine del colpo del giocatore da risorsa (file "colpo player.png")
        Image colpo = new Image(getClass().getResourceAsStream("colpo player.png"));
        imageView = new ImageView(colpo);
        imageView.setFitWidth(30);          // Imposta la larghezza dell'immagine del colpo
        imageView.setFitHeight(30);         // Imposta l'altezza dell'immagine del colpo
        imageView.setX(x);                  // Posiziona il colpo sulla coordinata X iniziale
        imageView.setY(y);                  // Posiziona il colpo sulla coordinata Y iniziale
    }

    // Metodo chiamato ad ogni frame per aggiornare la posizione del colpo in base alla direzione
    public void update() {
        switch (direzione) {
            case SU: 
                y -= velocita;              // Muove il colpo verso l'alto sottraendo alla coordinata Y
                break;
            case GIU: 
                y += velocita;              // Muove il colpo verso il basso aggiungendo alla coordinata Y
                break;
            case SINISTRA: 
                x -= velocita;              // Muove il colpo verso sinistra sottraendo alla coordinata X
                break;
            case DESTRA: 
                x += velocita;              // Muove il colpo verso destra aggiungendo alla coordinata X
                break;
        }
        // Debug: stampa le nuove coordinate ad ogni aggiornamento
        System.out.println(x);
        System.out.println(y);

        // Aggiorna la posizione grafica dell'immagine con le nuove coordinate
        imageView.setX(x);
        imageView.setY(y);
    }

    // Metodo per ottenere il nodo grafico (ImageView) del colpo da inserire nella scena
    public ImageView getNode() {
        return imageView;
    }

    // Getter per la coordinata X del colpo (utile per controlli di collisione o fuori schermo)
    public double getX() {
        return x;
    }

    // Getter per la coordinata Y del colpo (utile per controlli di collisione o fuori schermo)
    public double getY() {
        return y;
    }

}
