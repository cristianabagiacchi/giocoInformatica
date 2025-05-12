package giocoInformatica;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Panello extends StackPane {

    private Stage primaryStage;

    public Panello(Stage primaryStage) {
        this.primaryStage = primaryStage;

        int tileOriginale = 16;
        int scala = 3;
        int tileSize = tileOriginale * scala;
        int colonne = 48;
        int righe = 48;
        int larghezzaSchermo = tileSize * colonne; //2304
        int altezzaSchermo = tileSize * righe;  //2304

        // Immagine di sfondo
        Image principale = new Image(getClass().getResourceAsStream("dead forest.png"));
        ImageView imageView = new ImageView(principale);
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        imageView.setPreserveRatio(true);
        this.getChildren().add(imageView);

        // Layout del menu
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
        menuLayout.setAlignment(Pos.CENTER);

        // Titolo del gioco
        Label gameTitle = new Label("The Revenge");
        gameTitle.setStyle("-fx-font-size: 150px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Pulsante "Inizia Gioco"
        Button startButton = new Button("Inizia Gioco");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        startButton.setOnAction(e -> {
            System.out.println("Pulsante 'Inizia Gioco' cliccato");  // Verifica che il pulsante sia cliccato
            showPrimoLivello();  // Cambia scena al primo livello
        });

        // Pulsante "Esci"
        Button exitButton = new Button("Esci");
        exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        exitButton.setOnAction(e -> {
            System.exit(0);
        });

        menuLayout.getChildren().addAll(gameTitle, startButton, exitButton);
        this.getChildren().add(menuLayout);
    }

    // Metodo per passare al primo livello
    public void showPrimoLivello() {
        System.out.println("Cambio scena al primo livello");  // Debug per vedere se questo metodo viene invocato

        // Crea il primo livello
        PrimoLivello primoLivello = new PrimoLivello(primaryStage);
        
        // Crea una nuova scena per il primo livello
        Scene primoLivelloScene = new Scene(primoLivello, primoLivello.larghezzaSchermo, primoLivello.altezzaSchermo);
        
        // Imposta la scena sul primaryStage
        primaryStage.setScene(primoLivelloScene);
        
        // Mostra la nuova scena
        primaryStage.show();
    }
}