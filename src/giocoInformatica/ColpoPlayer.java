package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ColpoPlayer {

    private double x, y;
    private double velocita = 7; // Velocità del colpo
    private Player.Direzione direzione;
    private ImageView imageView;

    public ColpoPlayer(double x, double y, Player.Direzione direzione) {
        this.x = x;
        this.y = y;
        this.direzione = direzione;
      
        System.out.println(x);
        System.out.println(y);
        
        // Crea un'immagine bianca di un cubetto o una linea
        Image colpo = new Image(getClass().getResourceAsStream("colpo player.png"));
        imageView = new ImageView(colpo);
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        imageView.setX(x);
        imageView.setY(y);

        
    }

    public void update() {
        // Aggiorna la posizione del colpo in base alla direzione
        switch (direzione) {
            case SU: y -= velocita; break;
            case GIU: y += velocita; break;
            case SINISTRA: x -= velocita; break;
            case DESTRA: x += velocita; break;
        }
        System.out.println(x);
        System.out.println(y);
        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getNode() {
        return imageView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}