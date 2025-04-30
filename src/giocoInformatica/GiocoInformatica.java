package giocoInformatica;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GiocoInformatica extends StackPane {

    public GiocoInformatica() {
        // Esempio semplice di gioco
        Text giocoText = new Text("Benvenuto nel Gioco!");
        this.getChildren().add(giocoText);
    }
}
