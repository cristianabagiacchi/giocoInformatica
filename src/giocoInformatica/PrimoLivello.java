package giocoInformatica;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class PrimoLivello extends StackPane {

    int tileOriginale = 16;
    int scala = 3;
    int tileSize = tileOriginale * scala;
    int colonne = 48;
    int righe = 48;
    public int larghezzaSchermo = tileSize * colonne;
    public int altezzaSchermo = tileSize * righe;

    private ImageView imageView;
    private String[] immaginiSfondo = {"castle.png", "forest.png", "desert.png"};
    private int currentImageIndex = 0;

    private Player player;

    public PrimoLivello(Stage primaryStage) {
        Pane root = new Pane();
        this.getChildren().add(root);

        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);

        player = new Player(100, 100);
        root.getChildren().add(player.getNode());

        // Gestisce gli eventi della tastiera
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        scene.setOnKeyPressed(event -> player.handleKeyPress(event));
        scene.setOnKeyReleased(event -> player.handleKeyRelease(event));  // Aggiunta gestione key release

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
}
