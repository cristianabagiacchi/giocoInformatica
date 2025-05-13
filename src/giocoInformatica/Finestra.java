package giocoInformatica;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Finestra extends Application {
    @Override
    public void start(Stage primaryStage) {
        Panello pan = new Panello(primaryStage);
        Scene scene = new Scene(pan);
        scene.getStylesheets().add(getClass().getResource("PanelloStile.css").toExternalForm());

        primaryStage.setTitle("Gioco");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}