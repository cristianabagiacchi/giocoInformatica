package giocoInformatica;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Panello extends StackPane {

	//screen setting
	
	int tileOriginale=16;  //16x16
	int scala =3; // è 16 pixel ma sembra 48 sullo schermo (3x16)
	
	int tileSize= tileOriginale*scala; //48*48
	
	int colonne=48;
	int righe=48;
	
	int larghezzaSchermo=tileSize*colonne; //2304 pixels
	int altezzaSchermo=tileSize*righe; //2304 pixels
	
	public Panello() {
	
		// Carica l'immagine di sfondo
        Image principale = new Image(getClass().getResourceAsStream("dead forest.png"));

        // Crea un ImageView per visualizzare l'immagine
        ImageView imageView = new ImageView(principale);

        // Imposta le dimensioni dell'immagine
        imageView.setFitWidth(larghezzaSchermo);  // Imposta la larghezza dell'immagine
        imageView.setFitHeight(altezzaSchermo);   // Imposta l'altezza dell'immagine
        imageView.setPreserveRatio(true);         // Mantieni il rapporto di aspetto

        // Aggiungi l'immagine al pannello
        this.getChildren().add(imageView);

        // Layout del menu con un VBox
        VBox menuLayout = new VBox(20);  // 20px di spazio tra gli elementi
        menuLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
        
        // Imposta l'allineamento degli elementi al centro
        menuLayout.setAlignment(Pos.CENTER);

        // Titolo del gioco (label)
        Label gameTitle = new Label("The Revenge");
        gameTitle.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
        
        // Pulsante "Inizia Gioco"
        Button startButton = new Button("Inizia Gioco");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        startButton.setOnAction(e -> {
            // Gestisci l'azione per avviare il gioco
            // Cambia alla scena del gioco (questa logica sarà poi gestita da `Finestra`)
        });

        // Pulsante "Esci"
        Button exitButton = new Button("Esci");
        exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        exitButton.setOnAction(e -> {
            // Chiude l'applicazione
            System.exit(0);
        });

        // Aggiungi gli elementi (titolo e pulsanti) al layout del menu
        menuLayout.getChildren().addAll(gameTitle, startButton, exitButton);

        // Posiziona il menu sopra l'immagine e lo centra
        this.getChildren().add(menuLayout);  // Aggiunge il menu sopra l'immagine
    }
}
