/*package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    public double velocita = 4.0; // Velocità di movimento

    private ImageView imageView;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        imageView = new ImageView(); // Crea un ImageView per il giocatore
        caricaAnimazioni(); // Carica le immagini
        imageView.setX(x);  // Imposta la posizione iniziale
        imageView.setY(y);
        imageView.setImage(getImmagineAttuale()); // Imposta l'immagine iniziale
    }

    private void caricaAnimazioni() {
        // Carica le immagini da risorse per ogni direzione e tipo di animazione
        for (int dir = 0; dir < 4; dir++) {
            for (int i = 0; i < 4; i++) {
                String nome = "personaggio/idle/idle_" + dir + "_" + i + ".png";
                idleFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
            for (int i = 0; i < 8; i++) {
                String nome = "personaggio/corsa/corsa_" + dir + "_" + i + ".png";
                corsaFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
            for (int i = 0; i < 12; i++) {
                String nome = "personaggio/attacco/attacco_" + dir + "_" + i + ".png";
                attaccoFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
        }
    }

    public ImageView getNode() {
        // Restituisci il nodo ImageView del giocatore
        return imageView;
    }

    public void update() {
        // Aggiorna il frame corrente
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
        imageView.setImage(getImmagineAttuale()); // Aggiorna l'immagine del giocatore
    }

    public void muovi(double dx, double dy) {
        // Movimento del giocatore e gestione direzione
        if (dx > 0) {
            direzioneCorrente = Direzione.DESTRA;
        } else if (dx < 0) {
            direzioneCorrente = Direzione.SINISTRA;
        } else if (dy > 0) {
            direzioneCorrente = Direzione.GIU;
        } else if (dy < 0) {
            direzioneCorrente = Direzione.SU;
        }

        x += dx * velocita;
        y += dy * velocita;
        imageView.setX(x);  // Aggiorna la posizione dell'immagine
        imageView.setY(y);  // Aggiorna la posizione dell'immagine
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
        frame = 0;  // Reset dei frame quando cambia l'azione
    }

    private Image getImmagineAttuale() {
        // Restituisce l'immagine corrente in base all'azione e alla direzione
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
}*/