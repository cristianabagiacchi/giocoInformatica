package giocoInformatica;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class GiocoInformatica extends StackPane {

    public GiocoInformatica() {
        // Crea un testo di benvenuto per il gioco
        Text testoBenvenuto = new Text("Benvenuto nel Gioco!");

        // Aggiunge il testo alla lista dei figli di questo StackPane
        this.getChildren().add(testoBenvenuto);
    }
}
