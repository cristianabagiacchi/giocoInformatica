package giocoInformatica;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox; // Importazione per contenitore orizzontale
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Panello extends StackPane {

    private Stage primaryStage; // Riferimento alla finestra principale
    private Label impostazioniLabel; // Etichetta per mostrare le istruzioni tasti
    private boolean impostazioniVisibili = false; // Stato di visibilità delle istruzioni

    public Panello(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Dimensioni originali e scala per il gioco
        int tileOriginale = 16;
        int scala = 3;
        int tileSize = tileOriginale * scala; // Dimensione di un singolo tile ingrandito
        int colonne = 28;  // Numero colonne
        int righe = 22;    // Numero righe
        int larghezzaSchermo = tileSize * colonne; // Calcola larghezza totale
        int altezzaSchermo = tileSize * righe;     // Calcola altezza totale

        // Carica l'immagine di sfondo del menu principale
        Image sfondo = new Image(getClass().getResourceAsStream("dead forest.png"));
        ImageView imageView = new ImageView(sfondo);
        imageView.setFitWidth(larghezzaSchermo);   // Imposta larghezza immagine di sfondo
        imageView.setFitHeight(altezzaSchermo);    // Imposta altezza immagine di sfondo
        imageView.setPreserveRatio(true);           // Mantiene proporzioni originali
        this.getChildren().add(imageView);          // Aggiunge sfondo al pannello

        // Crea un layout verticale per il menu con spaziatura di 20 pixel
        VBox menuLayout = new VBox(20);
        menuLayout.getStyleClass().add("menu-layout"); // Applica stile CSS definito
        menuLayout.setAlignment(Pos.CENTER);            // Centra contenuti verticalmente e orizzontalmente

        // Crea il titolo del gioco
        Label titolo = new Label("The Revenge");
        titolo.getStyleClass().add("game-title");       // Applica stile CSS per il titolo

        // Pulsante per iniziare il gioco
        Button startButton = new Button("Inizia Gioco");
        startButton.getStyleClass().add("button");      // Applica stile CSS al pulsante

        // Gestore evento click sul pulsante start
        startButton.setOnAction(e -> {
            // Crea il primo livello e cambia scena a quella del gioco
            PrimoLivello primoLivello = new PrimoLivello(primaryStage);
            Scene gameScene = new Scene(primoLivello, primoLivello.larghezzaSchermo, primoLivello.altezzaSchermo);
            primaryStage.setScene(gameScene);
            primaryStage.show();
        });

        // Pulsante per mostrare/nascondere indice dei tasti
        Button settingsButton = new Button("Indice Tasti");
        settingsButton.getStyleClass().add("button");   // Stile CSS
        settingsButton.setOnAction(e -> mostraNascondiImpostazioni()); // Cambia visibilità istruzioni

        // Etichetta con istruzioni dei tasti, testo multilinea con escape \n
        impostazioniLabel = new Label(
                "W: Muovi Su\n" +
                "S: Muovi Giù\n" +
                "A: Muovi a Sinistra\n" +
                "D: Muovi a Destra\n" +
                "SPACE: Attacco"
        );
        // Stile testo bianco e dimensione 16px
        impostazioniLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        impostazioniLabel.setVisible(false);  // Di default l'etichetta è nascosta

        // HBox per contenere orizzontalmente il pulsante e l'etichetta istruzioni
        HBox impostazioniLayout = new HBox(10);  // Spaziatura 10px fra elementi
        impostazioniLayout.setAlignment(Pos.CENTER); // Centra elementi orizzontalmente
        impostazioniLayout.getChildren().addAll(settingsButton, impostazioniLabel); // Aggiunge pulsante e etichetta

        // Pulsante per uscire dal gioco
        Button exitButton = new Button("Esci");
        exitButton.getStyleClass().add("button"); // Applica stile CSS
        exitButton.setOnAction(e -> System.exit(0)); // Esce dall'applicazione

        // Aggiunge tutti i nodi al layout verticale
        menuLayout.getChildren().addAll(titolo, startButton, impostazioniLayout, exitButton);

        // Aggiunge il layout menu al pannello principale (StackPane)
        this.getChildren().add(menuLayout);
    }
    
    // Metodo non usato, lasciato forse per futuri sviluppi
    public void showPrimoLivello() {
        PrimoLivello primoLivello = new PrimoLivello(primaryStage);
        // Il codice commentato qui sotto mostrerebbe il primo livello, 
        // ma è disabilitato per ora.
    }
    
    // Metodo per alternare visibilità delle istruzioni tasti
    private void mostraNascondiImpostazioni() {
        impostazioniVisibili = !impostazioniVisibili; // Inverte lo stato
        impostazioniLabel.setVisible(impostazioniVisibili); // Mostra o nasconde l'etichetta
    }
}
