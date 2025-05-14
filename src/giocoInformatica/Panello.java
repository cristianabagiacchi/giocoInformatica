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
        menuLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
        menuLayout.setAlignment(Pos.CENTER);

        Label titolo = new Label("The Revenge");
        titolo.setStyle("-fx-font-size: 150px; -fx-text-fill: white; -fx-font-weight: bold;");

        Button startButton = new Button("Inizia Gioco");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        startButton.setOnAction(e -> showPrimoLivello());

        Button exitButton = new Button("Esci");
        exitButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
        exitButton.setOnAction(e -> System.exit(0));

        menuLayout.getChildren().addAll(titolo, startButton, exitButton);
        this.getChildren().add(menuLayout);
    }

    public void showPrimoLivello() {
        PrimoLivello primoLivello = new PrimoLivello(primaryStage);
        Scene scene = new Scene(primoLivello, primoLivello.larghezzaSchermo, primoLivello.altezzaSchermo);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}