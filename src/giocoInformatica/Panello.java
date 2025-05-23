package giocoInformatica;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox; // Import HBox
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Panello extends StackPane {

    private Stage primaryStage;
    private Label impostazioniLabel; // Etichetta per mostrare le impostazioni
    private boolean impostazioniVisibili = false; // Flag per tracciare la visibilità

    public Panello(Stage primaryStage) {
        this.primaryStage = primaryStage;

        int tileOriginale = 16;
        int scala = 3;
        int tileSize = tileOriginale * scala;
        int colonne = 28;
        int righe = 22;
        int larghezzaSchermo = tileSize * colonne;
        int altezzaSchermo = tileSize * righe;

        Image sfondo = new Image(getClass().getResourceAsStream("dead forest.png"));
        ImageView imageView = new ImageView(sfondo);
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        imageView.setPreserveRatio(true);
        this.getChildren().add(imageView);

        VBox menuLayout = new VBox(20);
        menuLayout.getStyleClass().add("menu-layout"); // Applica la classe CSS
        menuLayout.setAlignment(Pos.CENTER);

        Label titolo = new Label("The Revenge");
        titolo.getStyleClass().add("game-title"); // Applica la classe CSS

        Button startButton = new Button("Inizia Gioco");
        startButton.getStyleClass().add("button"); // Applica la classe CSS
    
        startButton.setOnAction(e -> {
            PrimoLivello primoLivello = new PrimoLivello(primaryStage);
            Scene gameScene = new Scene(primoLivello, primoLivello.larghezzaSchermo, primoLivello.altezzaSchermo);
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });

        Button settingsButton = new Button("Indice Tasti"); // Nuovo pulsante
        settingsButton.getStyleClass().add("button");
        settingsButton.setOnAction(e -> mostraNascondiImpostazioni()); // Azione del pulsante

        // Crea l'etichetta per mostrare le impostazioni
        impostazioniLabel = new Label(
                "W: Muovi Su\n" +
                        "S: Muovi Giù\n" +
                        "A: Muovi a Sinistra\n" +
                        "D: Muovi a Destra\n" +
                        "SPACE: Attacco"
        );
        impostazioniLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;"); // Stile del testo
        impostazioniLabel.setVisible(false); // Inizialmente nascosta

        // Crea un HBox per il pulsante "Impostazioni" e l'etichetta
        HBox impostazioniLayout = new HBox(10); // Spazio tra il pulsante e l'etichetta
        impostazioniLayout.setAlignment(Pos.CENTER); // Allinea gli elementi al centro
        impostazioniLayout.getChildren().addAll(settingsButton, impostazioniLabel);

        Button exitButton = new Button("Esci");
        exitButton.getStyleClass().add("button"); // Applica la classe CSS
        exitButton.setOnAction(e -> System.exit(0));

        menuLayout.getChildren().addAll(titolo, startButton, impostazioniLayout, exitButton);
        this.getChildren().add(menuLayout);
    }
    
    

    public void showPrimoLivello() {
        PrimoLivello primoLivello = new PrimoLivello(primaryStage);
        //Scene scene = new Scene(primoLivello, primoLivello.larghezzaSchermo, primoLivello.altezzaSchermo);
        //scene.getStylesheets().add(getClass().getResource("PanelloStile.css").toExternalForm());
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }
    
    

    // Metodo per mostrare o nascondere le impostazioni
    private void mostraNascondiImpostazioni() {
        impostazioniVisibili = !impostazioniVisibili; // Inverti lo stato
        impostazioniLabel.setVisible(impostazioniVisibili); // Mostra o nascondi l'etichetta
    }
}