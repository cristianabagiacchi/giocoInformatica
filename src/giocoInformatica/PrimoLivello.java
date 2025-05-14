package giocoInformatica;

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
    int larghezzaSchermo = tileSize * colonne;
    int altezzaSchermo = tileSize * righe;

    private ImageView imageView;
    private String[] immaginiSfondo = {"castle.png"};
    private int currentImageIndex = 0;

    private Player player;

    // Aggiungi la barra della vita
    private ProgressBar barraVita;
    private static final double MAX_HEALTH = 100.0;  // Salute massima
    private double health = MAX_HEALTH;

    public PrimoLivello(Stage primaryStage) {
        Pane root = new Pane();
        this.getChildren().add(root);

        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);

        player = new Player(400, 400);
        root.getChildren().add(player.getNode());

        // Barra della vita
        barraVita = new ProgressBar();
        barraVita.setProgress(1.0); // Imposta la barra inizialmente piena
        barraVita.setPrefWidth(200);
        barraVita.setPrefHeight(20);
        barraVita.setStyle("-fx-accent: red;");

        // Posiziona la barra della vita in alto a sinistra
        barraVita.setTranslateX(10);
        barraVita.setTranslateY(10);
        root.getChildren().add(barraVita);

        // Gestisce gli eventi della tastiera
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        scene.setOnKeyPressed(event -> player.handleKeyPress(event));
        scene.setOnKeyReleased(event -> player.handleKeyRelease(event));

        // Aggiornamento del gioco
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate > 2_000_000_000_000L) {
                    aggiornaImmagine();
                    lastUpdate = now;
                }
                player.update();

                // Aggiorna la barra della vita in base alla salute del giocatore
                updateHealthBar();
            }
        };
        timer.start();

        primaryStage.setScene(scene);
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

    // Metodo per aggiornare la barra della vita
    private void updateHealthBar() {
        barraVita.setProgress(health / MAX_HEALTH);
    }

    // Metodo per danneggiare il giocatore (ad esempio quando viene colpito)
    public void takeDamage(double damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        updateHealthBar();
    }

    // Metodo per curare il giocatore (ad esempio quando prende una pozione)
    public void heal(double healAmount) {
        health += healAmount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
        updateHealthBar();
    }
}