package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Panello extends Main {

	//screen setting
	
	int tileOriginale=16;  //16x16
	int scala =3; // è 16 pixel ma sembra 48 sullo schermo (3x16)
	
	int tileSize= tileOriginale*scala; //48*48
	
	int colonne=16;
	int righe=16;
	
	int larghezzaSchermo=tileSize*colonne; //768 pixels
	int altezzaSchermo=tileSize*righe; //576 pixels
	
	public Panello() {
		
		// Crea un StackPane per aggiungere l'immagine di sfondo
        StackPane g = new StackPane();

        // Carica l'immagine di sfondo
        Image imagineSfondo = new Image("sfondo1.jpg"); 
        ImageView sfondo = new ImageView(imagineSfondo);

        // Imposta la dimensione dell'immagine di sfondo per adattarsi alla finestra
        sfondo.setFitWidth(larghezzaSchermo);
        sfondo.setFitHeight(altezzaSchermo);

        // Aggiungi l'immagine come sfondo
        g.getChildren().add(sfondo);

        // Crea la scena con la larghezza e l'altezza specificata
        Scene scene = new Scene(g, larghezzaSchermo, altezzaSchermo);

	}
}
