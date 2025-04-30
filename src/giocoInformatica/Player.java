package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {

    public enum Azione { IDLE, CORSA, ATTACCO }
    public enum Direzione { SU, GIU, SINISTRA, DESTRA }

    private Azione azioneCorrente = Azione.IDLE;
    private Direzione direzioneCorrente = Direzione.GIU;

    private int frame = 0;
    private int frameDelay = 6;
    private int frameCounter = 0;

    // Immagini per le animazioni: [direzione][frame]
    private Image[][] idleFrames = new Image[4][4];     // 4 direzioni, 4 frame per direzione
    private Image[][] corsaFrames = new Image[4][8];    // 4 direzioni, 8 frame per direzione
    private Image[][] attaccoFrames = new Image[4][12]; // 4 direzioni, 12 frame per direzione

    private double x, y; // Posizione
    private double velocita = 4.0; // Velocità di movimento

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        caricaAnimazioni(); // Carica le immagini
    }

    private void caricaAnimazioni() {
        // Carica le immagini da risorse per ogni direzione e tipo di animazione
        for (int dir = 0; dir < 4; dir++) {
            for (int i = 0; i < 4; i++) {
                idleFrames[dir][i] = new Image("idle_" + dir + "_" + i + ".png");
            }
            for (int i = 0; i < 8; i++) {
                corsaFrames[dir][i] = new Image("corsa_" + dir + "_" + i + ".png");
            }
            for (int i = 0; i < 12; i++) {
                attaccoFrames[dir][i] = new Image("attacco_" + dir + "_" + i + ".png");
            }
        }
    }

    public void aggiorna() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            frame++;
            int maxFrame = getNumeroFrameAttuale();
            if (frame >= maxFrame) {
                frame = 0;
                if (azioneCorrente == Azione.ATTACCO) {
                    setAzione(Azione.IDLE); // Ritorna a idle dopo l'attacco
                }
            }
        }
    }

    public void disegna(GraphicsContext gc) {
        Image img = getImmagineAttuale();
        gc.drawImage(img, x, y);
    }

    private Image getImmagineAttuale() {
        int dir = direzioneCorrente.ordinal();
        switch (azioneCorrente) {
            case CORSA:
                return corsaFrames[dir][frame];
            case ATTACCO:
                return attaccoFrames[dir][frame];
            case IDLE:
            default:
                return idleFrames[dir][frame];
        }
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
        if (azioneCorrente != azione) {
            azioneCorrente = azione;
            frame = 0;
        }
    }

    public void setDirezione(Direzione direzione) {
        if (direzioneCorrente != direzione) {
            direzioneCorrente = direzione;
            frame = 0;
        }
    }

    // Funzione per il movimento (WASD)
    public void muovi(double dx, double dy) {
        x += dx * velocita;
        y += dy * velocita;
        setAzione(Azione.CORSA);

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) setDirezione(Direzione.DESTRA);
            else setDirezione(Direzione.SINISTRA);
        } else {
            if (dy > 0) setDirezione(Direzione.GIU);
            else setDirezione(Direzione.SU);
        }
    }

    public void attacca() {
        setAzione(Azione.ATTACCO);
    }

    public void ferma() {
        setAzione(Azione.IDLE);
    }

    public double getX() { return x; }
    public double getY() { return y; }
}