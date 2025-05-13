
package giocoInformatica;

import javafx.animation.AnimationTimer;
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
    public int larghezzaSchermo = tileSize * colonne; // Modificato per renderlo pubblico
    public int altezzaSchermo = tileSize * righe;     // Modificato per renderlo pubblico

    private ImageView imageView;
    private String[] immaginiSfondo = {"castle.png", "forest.png", "desert.png"}; // Aggiungi qui i nomi delle immagini che vuoi cambiare
    private int currentImageIndex = 0; // Indice per l'immagine corrente

    public PrimoLivello(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, larghezzaSchermo, altezzaSchermo);

        // Crea un ImageView per l'immagine di sfondo
        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);

        // Avvia il timer per cambiare immagine
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                aggiornaImmagine();  // Cambia l'immagine ogni tick del timer
            }
        };
        timer.start(); // Inizia il timer

        // Imposta la scena
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void aggiornaImmagine() {
        // Carica l'immagine successiva
        if (currentImageIndex >= immaginiSfondo.length) {
            currentImageIndex = 0; // Torna alla prima immagine quando arriva alla fine dell'array
        }

        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
        imageView.setImage(image);

        currentImageIndex++; // Passa alla prossima immagine
    }
}