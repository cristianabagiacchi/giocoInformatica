package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class Player {

    public enum Azione { IDLE, CORSA }
    public enum Direzione { SU, GIU, SINISTRA, DESTRA }

    private Azione azioneCorrente = Azione.IDLE;
    private Direzione direzioneCorrente = Direzione.GIU;

    private int frame = 0;
    private int frameDelay = 100;
    private int frameCounter = 0;

    private Image[][] idleFrames = new Image[4][4];
    private Image[][] corsaFrames = new Image[4][8];

    private double x, y;
    public double velocita = 2.5;

    private ImageView imageView;

    private boolean muoviSu = false, muoviGiu = false, muoviSinistra = false, muoviDestra = false;

    private double scala = 4;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        imageView = new ImageView();
        caricaAnimazioni();
        imageView.setX(x);
        imageView.setY(y);
        imageView.setImage(getImmagineAttuale());
        imageView.setFitWidth(imageView.getImage().getWidth() * scala);
        imageView.setFitHeight(imageView.getImage().getHeight() * scala);
    }

    private void caricaAnimazioni() {
        for (int dir = 0; dir < 4; dir++) {
            for (int i = 0; i < 4; i++) {
                idleFrames[dir][i] = new Image(this.getClass().getResourceAsStream("personaggio/idle/idle_" + dir + "_" + i + ".png"));
            }
            for (int i = 0; i < 8; i++) {
                corsaFrames[dir][i] = new Image(this.getClass().getResourceAsStream("personaggio/corsa/corsa_" + dir + "_" + i + ".png"));
            }
        }
    }

    public ImageView getNode() {
        return imageView;
    }
    public ImageView getImageView() {
        return imageView;
    }
    
    public void update() {
        double nuovaX = x;
        double nuovaY = y;

        // Movimento
        if (muoviSu) {
            nuovaY -= velocita;
            direzioneCorrente = Direzione.GIU;
            setAzione(Azione.CORSA);
        }
        if (muoviGiu) {
            nuovaY += velocita;
            direzioneCorrente = Direzione.SU;
            setAzione(Azione.CORSA);
        }
        if (muoviSinistra) {
            nuovaX -= velocita;
            direzioneCorrente = Direzione.SINISTRA;
            setAzione(Azione.CORSA);
        }
        if (muoviDestra) {
            nuovaX += velocita;
            direzioneCorrente = Direzione.DESTRA;
            setAzione(Azione.CORSA);
        }

        // Calcolo dimensioni effettive del personaggio
        double playerWidth = imageView.getFitWidth();
        double playerHeight = imageView.getFitHeight();

        // Limiti della finestra
        double screenWidth = 1350;  // larghezza finestra
        double screenHeight = 750; // altezza finestra

        // GESTIONE COLLISIONI CON I BORDI
        if (nuovaX < 0) {
            nuovaX = 0;
        }
        if (nuovaX + playerWidth > screenWidth) {
            nuovaX = screenWidth - playerWidth;
        }
        if (nuovaY < 0) {
            nuovaY = 0;
        }
        if (nuovaY + playerHeight > screenHeight) {
            nuovaY = screenHeight - playerHeight;
        }

        // Aggiorna posizione
        x = nuovaX;
        y = nuovaY;

        // Se non si sta muovendo, torna a idle
        if (!muoviSu && !muoviGiu && !muoviSinistra && !muoviDestra) {
            setAzione(Azione.IDLE);
        }

        // Animazione
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            frame++;
            if (frame >= getNumeroFrameAttuale()) {
                frame = 0;
            }
        }

        // Aggiorna immagine e posizione
        imageView.setImage(getImmagineAttuale());
        imageView.setX(x);
        imageView.setY(y);
    }

    private int getNumeroFrameAttuale() {
        switch (azioneCorrente) {
            case CORSA: return 8;
            case IDLE:
            default: return 4;
        }
    }

    public void setAzione(Azione azione) {
        azioneCorrente = azione;
        frame = 0;
    }

    private Image getImmagineAttuale() {
        int dir = direzioneCorrente.ordinal();
        switch (azioneCorrente) {
            case CORSA: return corsaFrames[dir][frame];
            case IDLE:
            default: return idleFrames[dir][frame];
        }
    }

    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W: muoviSu = true; break;
            case S: muoviGiu = true; break;
            case A: muoviSinistra = true; break;
            case D: muoviDestra = true; break;
            default: break;
        }
    }

    public void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case W: muoviSu = false; break;
            case S: muoviGiu = false; break;
            case A: muoviSinistra = false; break;
            case D: muoviDestra = false; break;
            default: break;
        }
    }

    // Metodi aggiunti per supportare movimento da PrimoLivello
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move(double moveX, double moveY) {
        x += moveX;
        y += moveY;
    }
}