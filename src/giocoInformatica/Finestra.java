package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finestra extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Crea un'istanza del pannello principale del gioco, passando il Stage principale
        Panello pan = new Panello(primaryStage);

        // Crea una nuova scena con il pannello 'pan' come root node
        Scene scene = new Scene(pan);

        // Aggiunge il foglio di stile CSS per personalizzare l'aspetto del pannello
        scene.getStylesheets().add(getClass().getResource("PanelloStile.css").toExternalForm());

        // Imposta il titolo della finestra principale
        primaryStage.setTitle("Gioco");
        
        // Imposta larghezza fissa della finestra a 1280 pixel
        primaryStage.setWidth(1280); 

        // Imposta altezza fissa della finestra a 720 pixel
        primaryStage.setHeight(720);

        // Disattiva la modalità schermo intero (fullscreen)
        primaryStage.setFullScreen(false);

        // Disabilita la possibilità di ridimensionare la finestra (opzionale)
        primaryStage.setResizable(false);

        // Centra la finestra sullo schermo
        primaryStage.centerOnScreen();
        
        // Imposta la scena appena creata come contenuto della finestra principale
        primaryStage.setScene(scene);

        // Mostra la finestra all'utente
        primaryStage.show();
    }

    // Metodo main che avvia l'applicazione JavaFX
    public static void main(String[] args) {
        launch(args);
    }
}
