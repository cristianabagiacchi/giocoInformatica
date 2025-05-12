package giocoInformatica;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PrimoLivello extends StackPane {

    int tileOriginale = 16;
    int scala = 3;
    int tileSize = tileOriginale * scala;
    int colonne = 48;
    int righe = 48;
    int larghezzaSchermo = tileSize * colonne;
    int altezzaSchermo = tileSize * righe;

    private Player player;

    // Costruttore che accetta Stage come parametro
    public PrimoLivello(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, larghezzaSchermo, altezzaSchermo);

        // Carica l'immagine di sfondo
        Image image = new Image(getClass().getResourceAsStream("castle.png"));
        if (image.isError()) {
            System.out.println("Errore nel caricamento dell'immagine!");
        } else {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(larghezzaSchermo);  // Adatta l'immagine alla larghezza dello schermo
            imageView.setFitHeight(altezzaSchermo);  // Adatta l'immagine all'altezza dello schermo
            root.getChildren().add(imageView);
        }

        // Crea il giocatore e lo aggiungi alla scena
        player = new Player(100, 100);  // Posiziona il player a (100, 100)
        root.getChildren().add(player.getNode());  // Aggiungi il nodo del giocatore (l'immagine) al root

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Movimento del giocatore (puoi implementare il movimento qui)
                player.muovi(0, 0);  // Muovi il giocatore (gestibile con i tasti)
            }
        };
        timer.start();

        // Aggiungi la scena al primaryStage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}