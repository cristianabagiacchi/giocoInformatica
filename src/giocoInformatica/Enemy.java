package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Enemy {
    private static final double VELOCITA_MOVIMENTO = 1.0;  // Velocità di movimento
    private double x, y;
    private ImageView enemyImageView;
    private Pane root;

    public Enemy() {
        // Imposta una posizione casuale per il nemico
        Random random = new Random();
        this.x = random.nextDouble() * 800;  // Posizione casuale lungo la larghezza dello schermo
        this.y = random.nextDouble() * 600;  // Posizione casuale lungo l'altezza dello schermo

     // Verifica del percorso dell'immagine
        System.out.println(getClass().getResource("edopanfo.png"));
        
        // Carica l'immagine del nemico
        Image enemy = new Image(getClass().getResourceAsStream("edopanfo.png"));  // Sostituisci con il percorso dell'immagine
        
        // Verifica se l'immagine è stata caricata correttamente
        if (enemy.isError()) {
            System.out.println("Errore nel caricamento dell'immagine.");
        }else {
            System.out.println("Immagine caricata correttamente!");
        }
        
        enemyImageView = new ImageView(enemy);
        enemyImageView.setX(x);
        enemyImageView.setY(y);
        enemyImageView.setFitWidth(250);  // Imposta la larghezza dell'immagine del nemico
        enemyImageView.setFitHeight(250); // Imposta l'altezza dell'immagine del nemico
        
       
        
        

        root = new Pane();
        root.getChildren().add(enemyImageView);
    }

    // Metodo per aggiornare il movimento del nemico
    public void update() {
        // Muove il nemico in modo casuale
        Random random = new Random();
        x += random.nextDouble() * VELOCITA_MOVIMENTO - VELOCITA_MOVIMENTO / 2;  // Movimento casuale lungo l'asse X
        y += random.nextDouble() * VELOCITA_MOVIMENTO - VELOCITA_MOVIMENTO / 2;  // Movimento casuale lungo l'asse Y

        // Aggiorna la posizione dell'immagine del nemico
        enemyImageView.setX(x);
        enemyImageView.setY(y);
      

        // Se il nemico esce dai bordi dello schermo, lo riposiziona casualmente all'interno dello schermo
        if (x < 0) x = 0;
        if (x > 800) x = 800;  // Limita il movimento all'interno dello schermo (larghezza)
        if (y < 0) y = 0;
        if (y > 600) y = 600;  // Limita il movimento all'interno dello schermo (altezza)
    }

    // Restituisce il nodo che rappresenta il nemico
    public Rectangle getNode() {
        return new Rectangle(x, y, 50, 50);  // Puoi anche restituire il nodo dell'immagine
    }
}
