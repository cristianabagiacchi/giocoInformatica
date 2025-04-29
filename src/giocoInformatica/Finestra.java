package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finestra extends Application{

	  public void start(Stage primaryStage) {
		  
		  Panello a = new Panello();
		  
		
		  
		  Scene scene = new Scene(a, 1760, 990);

		  primaryStage.setTitle("Gioco");
		  primaryStage.setScene(scene);
		  primaryStage.show();


	       // Aggiungi un debug per la larghezza e altezza dello schermo
	       System.out.println("Larghezza Schermo: " + a.larghezzaSchermo);
	       System.out.println("Altezza Schermo: " + a.altezzaSchermo);	
	
	  }  
	  public static void main(String[] args) {
			launch(args);
		}
}
