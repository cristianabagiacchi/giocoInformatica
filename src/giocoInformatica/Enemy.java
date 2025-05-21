package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Enemy {
    private double x, y;
    private double velocita = 1.5;
    private ImageView imageView;
    private double scala = 4;
    private Random rand = new Random();

    // Riferimento al personaggio
    private Player player;

    // Per sparare proiettili
    private Timeline sparoTimeline;

    // Costruttore
    public Enemy(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;

        imageView = new ImageView();
        imageView.setX(x);
        imageView.setY(y);

        // Carica l'immagine del nemico
        imageView.setImage(new Image(this.getClass().getResourceAsStream("edopanfo.png")));
        imageView.setFitWidth(imageView.getImage().getWidth() * scala);
        imageView.setFitHeight(imageView.getImage().getHeight() * scala);

        // Movimento casuale del nemico
        movimentoCasuale();

        // Gestione sparo
        gestisciSparo();
    }

    public ImageView getNode() {
        return imageView;
    }

    public void update() {
        // Movimento casuale
        movimentoCasuale();

        // Sparo verso il personaggio
        if (rand.nextInt(100) < 1) { // 1% di probabilità di sparare ogni frame
            sparaVersoPersonaggio();
        }
    }

    private void movimentoCasuale() {
        // Movimento casuale del nemico sulla mappa
        int direzione = rand.nextInt(4); // 0 = su, 1 = giù, 2 = sinistra, 3 = destra
        switch (direzione) {
            case 0: // Su
                y -= velocita;
                break;
            case 1: // Giù
                y += velocita;
                break;
            case 2: // Sinistra
                x -= velocita;
                break;
            case 3: // Destra
                x += velocita;
                break;
        }

        // Limiti della finestra
        double screenWidth = 1350;  // larghezza finestra
        double screenHeight = 750; // altezza finestra

        // Prevenire il nemico di uscire dai bordi
        if (x < 0) x = 0;
        if (x > screenWidth - imageView.getFitWidth()) x = screenWidth - imageView.getFitWidth();
        if (y < 0) y = 0;
        if (y > screenHeight - imageView.getFitHeight()) y = screenHeight - imageView.getFitHeight();

        // Aggiorna la posizione del nemico
        imageView.setX(x);
        imageView.setY(y);
    }

    private void gestisciSparo() {
        // Ogni 3 secondi, il nemico prova a sparare
        sparoTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            sparaVersoPersonaggio();
        }));
        sparoTimeline.setCycleCount(Timeline.INDEFINITE);
        sparoTimeline.play();
    }

    private void sparaVersoPersonaggio() {
        double dx = player.getX() - x;
        double dy = player.getY() - y;

        double distanza = Math.sqrt(dx * dx + dy * dy);
        double velocitaProiettile = 5.0;

        // Normalizza la direzione
        double direzioneX = dx / distanza;
        double direzioneY = dy / distanza;

        // Crea e lancia il proiettile
        ColpoNemico proiettile = new ColpoNemico(x, y, direzioneX * velocitaProiettile, direzioneY * velocitaProiettile);
        // Aggiungi il proiettile alla scena (per esempio se hai una lista di proiettili)
        // scena.getChildren().add(proiettile.getNode());
    }

    // Metodo per fermare il nemico
    public void fermati() {
        sparoTimeline.stop();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}