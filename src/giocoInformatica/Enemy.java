package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Enemy {
	
	private long lastShotTime = 0;
	private static final long SHOOT_INTERVAL = 2_000_000_000; // 2 secondi in nanosecondi
    private static final double VELOCITA_MOVIMENTO = 2.5;  // Velocità di movimento
    private double x, y;
    private ImageView enemyImageView;
    private Pane root;

    public Enemy() {
        // Imposta una posizione casuale per il nemico
        Random random = new Random();
        this.x = random.nextDouble() * 1350;  // Posizione casuale lungo la larghezza dello schermo
        this.y = random.nextDouble() * 700;  // Posizione casuale lungo l'altezza dello schermo
 
        // Carica l'immagine del nemico
        Image enemy = new Image(getClass().getResourceAsStream("edopanfo.png"));  // Sostituisci con il percorso dell'immagine
        
        
        
        
        enemyImageView = new ImageView(enemy);
        enemyImageView.setX(x);
        enemyImageView.setY(y);
        enemyImageView.setFitWidth(100);  // Imposta la larghezza dell'immagine del nemico
        enemyImageView.setFitHeight(100); // Imposta l'altezza dell'immagine del nemico
 
        root = new Pane();
        root.getChildren().add(enemyImageView);
    }
    
    
    public void setImage(String imagePath) {
        Image imageSecondoLivello = new Image(getClass().getResourceAsStream(imagePath));
        enemyImageView.setImage(imageSecondoLivello);
        System.out.println("immagine cambiata");
    }
    
    public ImageView getImageView() {
        return enemyImageView;
    }

    // Metodo per aggiornare il movimento del nemico
    public void update() {
        // Movimento da destra verso sinistra
        x -= VELOCITA_MOVIMENTO;

        // Aggiorna la posizione dell'immagine del nemico
        enemyImageView.setX(x);
        enemyImageView.setY(y);

        // Se il nemico esce dallo schermo a sinistra, lo rimetti a destra con una Y casuale
        if (x < -enemyImageView.getFitWidth()) {
            x = 1350 + enemyImageView.getFitWidth();  // fuori dallo schermo a destra
            y = new Random().nextDouble() * 750;     // nuova Y casuale
        }
    }
        
    // Restituisce il nodo che rappresenta il nemico
    public ImageView getNode() {
        return enemyImageView;
    }
    
    public ColpoNemico shoot() {
        double startX = enemyImageView.getX();
        double startY = enemyImageView.getY() + enemyImageView.getFitHeight() / 2;
        // Il colpo va verso sinistra (velocità negativa)
        return new ColpoNemico(startX, startY, -13, 0);
    }
    
    public boolean canShoot(long now) {
        if (now - lastShotTime >= SHOOT_INTERVAL) {
            lastShotTime = now;
            return true;
        }
        return false;
    }
    
    public boolean isVisible() {
        return this.getNode().isVisible();
    }
}
