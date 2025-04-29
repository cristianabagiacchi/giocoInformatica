package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finestra extends Application{

	  public void start(Stage primaryStage) {
		  Group g = new Group();
		  Scene scene = new Scene(g, 1760, 990);

		  primaryStage.setTitle("Gioco");
		  primaryStage.setScene(scene);
		  primaryStage.show();

		  Panello a = new Panello();
		  System.out.println(a);	
	
	  }  
	  public static void main(String[] args) {
			launch(args);
		}
}
