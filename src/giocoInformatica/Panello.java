package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Panello extends StackPane {

	//screen setting
	
	int tileOriginale=16;  //16x16
	int scala =3; // è 16 pixel ma sembra 48 sullo schermo (3x16)
	
	int tileSize= tileOriginale*scala; //48*48
	
	int colonne=16;
	int righe=16;
	
	int larghezzaSchermo=tileSize*colonne; //768 pixels
	int altezzaSchermo=tileSize*righe; //576 pixels
	
	public Panello() {
	
		// Caricare l'immagine
		Image ImmagineSfondo = new Image("file:percorso/dell/immagine.png"); 
		ImageView immagine = new ImageView(ImmagineSfondo);

		// Imposta dimensioni dell'immagine
		immagine.setFitWidth(100);  // Imposta la larghezza dell'immagine
		immagine.setFitHeight(100); // Imposta l'altezza dell'immagine

		// Aggiungi l'immagine al pannello
		this.getChildren().add(immagine);

	}
}
