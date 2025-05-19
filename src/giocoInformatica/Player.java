package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class Player {

    public enum Azione { IDLE, CORSA, ATTACCO }
    public enum Direzione { SU, GIU, SINISTRA, DESTRA }

    private Azione azioneCorrente = Azione.IDLE;
    private Direzione direzioneCorrente = Direzione.GIU;

    private int frame = 0;
    private int frameDelay = 100;
    private int frameCounter = 0;

    private Image[][] idleFrames = new Image[4][4];
    private Image[][] corsaFrames = new Image[4][8];
    private Image[][] attaccoFrames = new Image[4][12];

    private double x, y;
    public double velocita = 2.5;

    private ImageView imageView;

    private boolean muoviSu = false, muoviGiu = false, muoviSinistra = false, muoviDestra = false;
    private boolean eseguiAttacco = false;

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
            for (int i = 0; i < 12; i++) {
                attaccoFrames[dir][i] = new Image(this.getClass().getResourceAsStream("personaggio/attacco/attacco_" + dir + "_" + i + ".png"));
            }
        }
    }

    public ImageView getNode() {
        return imageView;
    }

    public void update() {
        double nuovaX = x;
        double nuovaY = y;

        if (muoviSu) {
            nuovaY -= velocita;
            direzioneCorrente = Direzione.SU;
            setAzione(Azione.CORSA);
        }
        if (muoviGiu) {
            nuovaY += velocita;
            direzioneCorrente = Direzione.GIU;
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

        if (eseguiAttacco) {
            setAzione(Azione.ATTACCO);
        }

        // Calcolo dimensioni effettive del personaggio
        double playerWidth = imageView.getFitWidth();
        double playerHeight = imageView.getFitHeight();

        // Dimensioni dello schermo (usa Config se disponibile)
        double schermoLarghezza = Config.LARGHEZZA_SCHERMO;
        double schermoAltezza = Config.ALTEZZA_SCHERMO;

        // GESTIONE COLLISIONI CON I BORDI
        if (nuovaX < 0) nuovaX = 0;
        if (nuovaX + playerWidth > schermoLarghezza) nuovaX = schermoLarghezza - playerWidth;

        if (nuovaY < 0) nuovaY = 0;
        if (nuovaY + playerHeight > schermoAltezza) nuovaY = schermoAltezza - playerHeight;

        // Aggiorna posizione interna
        x = nuovaX;
        y = nuovaY;

        // Se non si sta muovendo o attaccando, torna a idle
        if (!muoviSu && !muoviGiu && !muoviSinistra && !muoviDestra && !eseguiAttacco) {
            setAzione(Azione.IDLE);
        }

        // Animazione
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            frame++;
            if (frame >= getNumeroFrameAttuale()) {
                frame = 0;
                if (azioneCorrente == Azione.ATTACCO) {
                    eseguiAttacco = false;
                    setAzione(Azione.IDLE);
                }
            }
        }

        // Aggiorna immagine e posizione grafica
        imageView.setImage(getImmagineAttuale());
        imageView.setX(x);
        imageView.setY(y);
    }

    private int getNumeroFrameAttuale() {
        switch (azioneCorrente) {
            case CORSA: return 8;
            case ATTACCO: return 12;
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
            case ATTACCO: return attaccoFrames[dir][frame];
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
            case SPACE:
                if (azioneCorrente != Azione.ATTACCO && !eseguiAttacco) {
                    eseguiAttacco = true;
                }
                break;
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

    // ➕ Metodi aggiunti per supportare movimento da PrimoLivello
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

