package giocoInformatica;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimoLivello extends StackPane {


    int tileOriginale = 16;
    int scala = 3;
    int tileSize = tileOriginale * scala;
    int colonne = 48;
    int righe = 48;
    int larghezzaSchermo = tileSize * colonne;
    int altezzaSchermo = tileSize * righe;
    private Label storyLabel;
    private boolean spazioPremuto = false;

    // Costruttore che accetta Stage come parametro
    public PrimoLivello(Stage primaryStage) {
        // Crea un'immagine di sfondo
        Image image = new Image(getClass().getResourceAsStream("castle.png"));
        if (image.isError()) {
            System.out.println("Errore nel caricamento dell'immagine!");  // Aggiungi questa riga per il controllo dell'immagine
        } else {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(larghezzaSchermo);  // Adatta l'immagine alla larghezza dello schermo
            imageView.setFitHeight(altezzaSchermo);  // Adatta l'immagine all'altezza dello schermo
           // imageView.setPreserveRatio(true);  // Mantiene le proporzioni dell'immagine
            // Aggiungi l'immagine come sfondo (è già la base dello StackPane)
            this.getChildren().add(imageView);
        }

        // Aggiungi il pulsante "Esci" in alto a sinistra
        Button exitButton = new Button("Esci");
        exitButton.setStyle("-fx-font-size: 20px; -fx-background-color: #FF0000; -fx-text-fill: white; -fx-border-color: #FFFFFF; -fx-padding: 10px;");
        exitButton.setTranslateX(20); // Posiziona il pulsante a sinistra
        exitButton.setTranslateY(20); // Posiziona il pulsante in alto
        exitButton.setOnAction(e -> {
            System.out.println("Esci dal gioco!");
            Platform.exit(); // Chiude l'applicazione
        });

        // Aggiungi il pulsante alla scena
       // this.getChildren().add(exitButton);

        // Inizializza il testo della storia
        storyLabel = new Label("Benvenuto nel gioco! La tua avventura sta per cominciare...\n" +
                               "Un oscuro potere minaccia il regno e solo tu puoi fermarlo.\n" +
                               "Preparati a combattere e a scoprire la verità dietro questa maledizione.");
        storyLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.5);");

        // Posiziona il testo in basso
        storyLabel.setTranslateY(200);  // Spostiamo il testo verso il basso

        // Configura il testo per la visualizzazione in più righe
        storyLabel.setMaxWidth(larghezzaSchermo - 50);
        storyLabel.setWrapText(true);

        // Aggiungiamo il testo alla scena (questo deve venire dopo l'immagine)
        //this.getChildren().add(storyLabel);

        // Chiamata al metodo per mostrare la storia
        mostraStoria(primaryStage);
    }

    // Metodo per mostrare la storia con effetto macchina da scrivere
    public void mostraStoria(Stage primaryStage) {
        String storia = "Benvenuto nel gioco! La tua avventura sta per cominciare...\n" +
                        "Un oscuro potere minaccia il regno e solo tu puoi fermarlo.\n" +
                        "Preparati a combattere e a scoprire la verità dietro questa maledizione.";

        // La timeline per creare l'effetto macchina da scrivere
        Timeline timeline = new Timeline();
        StringBuilder testo = new StringBuilder();
        int i = 0;

        for (i = 0; i < storia.length(); i++) {
            int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(100 * i), e -> {
                testo.append(storia.charAt(index));
                storyLabel.setText(testo.toString());
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        // Quando l'animazione finisce, permette di iniziare il gioco con la pressione della barra spaziatrice
        KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(100 * i + 500), e -> {
            Platform.runLater(() -> {
                primaryStage.getScene().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.SPACE && !spazioPremuto) {
                        spazioPremuto = true;
                        System.out.println("Ora inizia il gioco!");
                        avviaGioco(primaryStage);
                    }
                });
            });
        });
        timeline.getKeyFrames().add(keyFrameEnd);

        timeline.play();
    }

    // Metodo per avviare il gioco
    public void avviaGioco(Stage primaryStage) {
        System.out.println("Avvio del gioco...");
        GiocoInformatica gioco = new GiocoInformatica();
        Scene scenaGioco = new Scene(gioco, larghezzaSchermo, altezzaSchermo);
        primaryStage.setScene(scenaGioco);
    }
}
