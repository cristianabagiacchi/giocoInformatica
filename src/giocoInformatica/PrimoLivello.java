package giocoInformatica;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

public class PrimoLivello extends StackPane {

    int tileOriginale = 16;
    int scala = 3;
    int tileSize = tileOriginale * scala;
    int colonne = 28;
    int righe = 22;
    public int larghezzaSchermo = tileSize * colonne;
    public int altezzaSchermo = tileSize * righe;

    private ImageView imageView;
    private String[] immaginiSfondo = {"castle.png"};
    private int currentImageIndex = 0;

    private Player player;

    private ProgressBar barraVita;
    private static final double MAX_HEALTH = 100.0;
    private double health = MAX_HEALTH;

    public PrimoLivello(Stage primaryStage) {
        Pane root = new Pane();
        this.getChildren().add(root);

        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);

        aggiornaImmagine();  // carica l'immagine iniziale

        player = new Player(400, 400);
        root.getChildren().add(player.getNode());

        barraVita = new ProgressBar();
        barraVita.setProgress(1.0);
        barraVita.setPrefWidth(200);
        barraVita.setPrefHeight(20);
        barraVita.setStyle("-fx-accent: red;");
        barraVita.setTranslateX(10);
        barraVita.setTranslateY(10);
        root.getChildren().add(barraVita);

        // Imposta la scena su questo StackPane
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        primaryStage.setScene(scene);

        // Focus necessario per ricevere gli eventi da tastiera
        this.setFocusTraversable(true);
        this.requestFocus();

        // Eventi tastiera
        scene.setOnKeyPressed(event -> {
            player.handleKeyPress(event);
        });

        scene.setOnKeyReleased(event -> {
            player.handleKeyRelease(event);
        });

        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate > 10_000_000_000L) { // ogni 10 secondi cambia sfondo (opzionale)
                    aggiornaImmagine();
                    lastUpdate = now;
                }

                // Aggiorna animazioni e posizione del giocatore
                player.update();

                // Ottieni la posizione del giocatore
                double playerX = player.getX();
                double playerY = player.getY();
                double playerWidth = player.getNode().getImage().getWidth() * 3;
                double playerHeight = player.getNode().getImage().getHeight() * 3;

                // Limiti di movimento
                if (playerX < 0) playerX = 0;
                if (playerX + playerWidth > larghezzaSchermo) playerX = larghezzaSchermo - playerWidth;

                if (playerY < 0) playerY = 0;
                if (playerY + playerHeight > altezzaSchermo) playerY = altezzaSchermo - playerHeight;

                // Aggiorna la posizione del giocatore
                player.getNode().setX(playerX);
                player.getNode().setY(playerY);

                updateHealthBar();
            }
        };
        timer.start();

        primaryStage.show();
    }

    private void aggiornaImmagine() {
        if (currentImageIndex >= immaginiSfondo.length) {
            currentImageIndex = 0;
        }

        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
        imageView.setImage(image);
        currentImageIndex++;
    }

    private void updateHealthBar() {
        barraVita.setProgress(health / MAX_HEALTH);
    }

    public void takeDamage(double damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        updateHealthBar();
    }

    public void heal(double healAmount) {
        health += healAmount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
        updateHealthBar();
    }
}