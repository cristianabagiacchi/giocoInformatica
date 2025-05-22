package giocoInformatica;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PrimoLivello extends StackPane {

    int tileOriginale = 16;
    int scala = 3;
    int tileSize = tileOriginale * scala;
    int colonne = 28;
    int righe = 22;
    public int larghezzaSchermo = 1350;
    public int altezzaSchermo = 750;
    private ArrayList<Enemy> enemies;  // Lista per gestire i nemici

    private ImageView imageView;
    private String[] immaginiSfondo = {"castle.png"};
    private int currentImageIndex = 0;

    private Player player;
    private Pane root;  // Definisci la variabile root

    private ProgressBar barraVita;
    private static final double MAX_HEALTH = 100.0;
    private double health = MAX_HEALTH;

    public PrimoLivello(Stage primaryStage) {
    	
        
        // Crea il root Pane
        root = new Pane();
        this.getChildren().add(root);


        // Crea e configura l'ImageView per lo sfondo
        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);

        // Aggiorna l'immagine iniziale dello sfondo
        aggiornaImmagine();

        // Crea il giocatore e aggiungilo al root
        player = new Player(400, 400);
        root.getChildren().add(player.getNode());

        // Crea e configura la barra della vita
        barraVita = new ProgressBar();
        barraVita.setProgress(1.0);
        barraVita.setPrefWidth(200);
        barraVita.setPrefHeight(20);
        barraVita.setStyle("-fx-accent: red;");
        barraVita.setTranslateX(10);
        barraVita.setTranslateY(10);
        root.getChildren().add(barraVita);
        
     // Crea la lista di nemici
        enemies = new ArrayList<>();

        // Aggiungi nemici al gioco (puoi aggiungerne quanti vuoi)
        for (int i = 0; i < 20; i++) {  // Per esempio, 5 nemici
        	double x = Math.random() * larghezzaSchermo;  // Posizione casuale sull'asse X (0 - 1350)
            double y = Math.random() * altezzaSchermo;   // Posizione casuale sull'asse Y (0 - 750)
            Enemy enemy = new Enemy();  // Crea un nemico
            System.out.println("Nemico creato: x=" + x + ", y=" + y);
            enemies.add(enemy);  // Aggiungi il nemico alla lista
            root.getChildren().add(enemy.getNode());  // Aggiungi il nemico alla scena
            System.out.println("Nemico aggiunto alla scena: " + enemy);
        }

        // Imposta la scena e aggiungi il root
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        primaryStage.setScene(scene);

        // Aggiungi il focus per ricevere gli eventi da tastiera
        this.setFocusTraversable(true);
        this.requestFocus();

        // Gestione degli eventi da tastiera
        scene.setOnKeyPressed(event -> {
            player.handleKeyPress(event);
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                sparoPlayer();  // Aggiungi il colpo quando si preme SPACE
            }
        });

        scene.setOnKeyReleased(event -> {
            player.handleKeyRelease(event);
        });

        // Crea un timer per animazioni e aggiornamenti
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Aggiorna la posizione del giocatore
                player.update();

                // Aggiorna la posizione dei nemici
                for (Enemy enemy : enemies) {
                    enemy.update();  // Aggiorna il movimento e il comportamento del nemico
                }

                // Ottieni la posizione del giocatore (usando getLayoutX() e getLayoutY())
                double playerX = player.getNode().getLayoutX();
                double playerY = player.getNode().getLayoutY();

                // Ottieni la larghezza e l'altezza del giocatore usando getBoundsInParent()
                double playerWidth = player.getNode().getBoundsInParent().getWidth();
                double playerHeight = player.getNode().getBoundsInParent().getHeight();

                // Limiti di movimento per il giocatore
                if (playerX < 0) playerX = 0;
                if (playerX + playerWidth > larghezzaSchermo) playerX = larghezzaSchermo - playerWidth;
                if (playerY < 0) playerY = 0;
                if (playerY + playerHeight > altezzaSchermo) playerY = altezzaSchermo - playerHeight;

                // Aggiorna la posizione del giocatore (usando setLayoutX() e setLayoutY())
                player.getNode().setLayoutX(playerX);
                player.getNode().setLayoutY(playerY);

                // Aggiorna la barra della vita
                updateHealthBar();
            }
        };
        timer.start();
    }

    // Metodo per aggiornare l'immagine di sfondo
    private void aggiornaImmagine() {
        if (currentImageIndex >= immaginiSfondo.length) {
            currentImageIndex = 0;
        }

        // Carica l'immagine da un file
        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
        imageView.setImage(image);
        currentImageIndex++;
    }

    // Metodo per aggiornare la barra della vita
    private void updateHealthBar() {
        barraVita.setProgress(health / MAX_HEALTH);
    }

    // Metodo per ridurre la vita del giocatore (prendere danno)
    public void takeDamage(double damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        updateHealthBar();
    }

    // Metodo per guarire il giocatore
    public void heal(double healAmount) {
        health += healAmount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
        updateHealthBar();
    }

    // Metodo per sparare il colpo del giocatore
    private void sparoPlayer() {
        System.out.println("colpo sparato");

        ImageView playerImage = player.getImageView();

        // Calcolo posizione di partenza del colpo più precisa
        double colpoX = playerImage.getX() + playerImage.getFitWidth()/3; // appena dopo il bordo destro
        double colpoY = playerImage.getY() + playerImage.getFitHeight() / 2; // centro verticale del personaggio - metà altezza colpo

        ColpoPlayer colpo = new ColpoPlayer(colpoX, colpoY, Player.Direzione.DESTRA);

        root.getChildren().add(colpo.getNode());

        AnimationTimer colpoTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                colpo.update();
                if (colpo.getX() > larghezzaSchermo) {
                    root.getChildren().remove(colpo.getNode());
                    stop();
                }
            }
        };
        colpoTimer.start();
    }
}