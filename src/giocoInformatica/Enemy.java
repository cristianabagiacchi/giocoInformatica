package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Enemy {
	
	private long lastShotTime = 0;  // Memorizza il tempo (nanosecondi) dell'ultimo sparo effettuato
	private static final long SHOOT_INTERVAL = 2_000_000_000; // Intervallo minimo tra due spari (2 secondi in nanosecondi)
    private static final double VELOCITA_MOVIMENTO = 2.5;  // Velocità fissa con cui il nemico si muove a sinistra
    private double x, y;                                   // Coordinate correnti del nemico
    private ImageView enemyImageView;                      // Nodo grafico che rappresenta il nemico
    private Pane root;                                     // Contenitore grafico (non utilizzato al momento)

    public Enemy() {
        // Crea un generatore casuale per posizionare il nemico in modo random sulla schermata
        Random random = new Random();
        this.x = random.nextDouble() * 1350;  // Posizione casuale orizzontale (tra 0 e 1350)
        this.y = random.nextDouble() * 700;   // Posizione casuale verticale (tra 0 e 700)
 
        // Carica l'immagine del nemico dal file "edopanfo.png"
        Image enemy = new Image(getClass().getResourceAsStream("edopanfo.png"));  
        
        enemyImageView = new ImageView(enemy);    // Crea ImageView con l'immagine caricata
        enemyImageView.setX(x);                    // Posiziona il nemico sull'asse X
        enemyImageView.setY(y);                    // Posiziona il nemico sull'asse Y
        enemyImageView.setFitWidth(100);           // Imposta larghezza immagine nemico
        enemyImageView.setFitHeight(100);          // Imposta altezza immagine nemico
 
        root = new Pane();                         // Crea un nuovo Pane contenitore (non utilizzato nel gioco)
        root.getChildren().add(enemyImageView);  // Aggiunge il nemico al Pane root (ma root non è usato)
    }
    
    // Cambia l'immagine del nemico per il secondo livello, caricando da un percorso specifico
    public void setImageSecondoLivello(String imagePath) {
        Image imageSecondoLivello = new Image(getClass().getResourceAsStream(imagePath));
        enemyImageView.setImage(imageSecondoLivello);  // Aggiorna l'immagine del nemico
        System.out.println("immagine cambiata");
    }
    
    // Cambia l'immagine del nemico per il terzo livello, caricando da un percorso specifico
    public void setImageTerzoLivello(String imagePath) {
        Image imageTerzoLivello = new Image(getClass().getResourceAsStream(imagePath));
        enemyImageView.setImage(imageTerzoLivello);  // Aggiorna l'immagine del nemico
        System.out.println("immagine cambiata");
    }
    
    // Restituisce l'ImageView associato a questo nemico, per poterlo aggiungere alla scena
    public ImageView getImageView() {
        return enemyImageView;
    }

    // Aggiorna il movimento del nemico: si sposta da destra verso sinistra
    public void update() {
        x -= VELOCITA_MOVIMENTO;                   // Muove il nemico verso sinistra riducendo la coordinata X

        enemyImageView.setX(x);                     // Aggiorna la posizione grafica X
        enemyImageView.setY(y);                     // Aggiorna la posizione grafica Y (non cambia)

        // Se il nemico esce fuori dallo schermo a sinistra (oltre la larghezza dell'immagine)
        if (x < -enemyImageView.getFitWidth()) {
            x = 1350 + enemyImageView.getFitWidth();  // Riporta il nemico a destra fuori schermo
            y = new Random().nextDouble() * 750;       // Cambia posizione Y in modo casuale
        }
    }
        
    // Restituisce il nodo grafico rappresentante il nemico (da aggiungere alla scena)
    public ImageView getNode() {
        return enemyImageView;
    }
    
    // Metodo che crea e restituisce un colpo nemico sparato dal nemico stesso
    public ColpoNemico shoot() {
        double startX = enemyImageView.getX();                                   // Posizione X del nemico da cui parte il colpo
        double startY = enemyImageView.getY() + enemyImageView.getFitHeight() / 2; // Posizione Y al centro verticale del nemico
        // Il colpo viene sparato verso sinistra con velocità orizzontale negativa (-13)
        return new ColpoNemico(startX, startY, -13, 0);
    }
    
    // Verifica se è passato abbastanza tempo dall'ultimo sparo per poterne fare un altro
    public boolean canShoot(long now) {
        if (now - lastShotTime >= SHOOT_INTERVAL) {  // Controlla se sono passati almeno 2 secondi
            lastShotTime = now;                      // Aggiorna il tempo dell'ultimo sparo
            return true;                            // Può sparare
        }
        return false;                               // Non può ancora sparare
    }
    
    // Ritorna true se il nodo grafico del nemico è visibile (utile per gestire la logica di gioco)
    public boolean isVisible() {
        return this.getNode().isVisible();
    }
}
