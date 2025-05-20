package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ColpoNemico {
    private double x, y;
    private double velocitaX, velocitaY;
    private ImageView imageView;

    public ColpoNemico(double x, double y, double velocitaX, double velocitaY) {
        this.x = x;
        this.y = y;
        this.velocitaX = velocitaX;
        this.velocitaY = velocitaY;

        imageView = new ImageView(new Image(this.getClass().getResourceAsStream("proiettile.png")));
        imageView.setX(x);
        imageView.setY(y);
    }

    public void update() {
        x += velocitaX;
        y += velocitaY;

        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getNode() {
        return imageView;
    }
}