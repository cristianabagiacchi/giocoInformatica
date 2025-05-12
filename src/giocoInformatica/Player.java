package giocoInformatica;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

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

    private Rectangle hitbox;
    private final double width = 64, height = 64; // dimensioni sprite

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        caricaAnimazioni(); // Carica le immagini
        hitbox = new Rectangle(x, y, width, height); // Inizializza la hitbox
    }

    private void caricaAnimazioni() {
        // Carica le immagini da risorse per ogni direzione e tipo di animazione
        for (int dir = 0; dir < 4; dir++) {
            for (int i = 0; i < 4; i++) {
                String nome = "personaggio/idle/idle_" + dir + "_" + i + ".png";
                System.out.println(nome);
                idleFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
            for (int i = 0; i < 8; i++) {
                String nome = "personaggio/corsa/corsa_" + dir + "_" + i + ".png";
                System.out.println(nome);
                corsaFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
            for (int i = 0; i < 12; i++) {
                String nome = "personaggio/attacco/attacco_" + dir + "_" + i + ".png";
                System.out.println(nome);
                attaccoFrames[dir][i] = new Image(this.getClass().getResourceAsStream(nome));
            }
        }
    }

    private void aggiornaHitbox() {
        hitbox.setX(x);
        hitbox.setY(y);
    }

    // Metodo per aggiornare lo stato del personaggio (animazioni, etc.)
    public void update(double deltaTime) {
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

        aggiornaHitbox(); // Sempre aggiorna la hitbox
    }

    // Controllo collisione con un ostacolo
    public boolean controllaCollisione(Rectangle ostacolo) {
        return hitbox.getBoundsInParent().intersects(ostacolo.getBoundsInParent());
    }

    // Funzione per il movimento (con controllo collisioni)
    public void muovi(double dx, double dy, Rectangle[] ostacoli) {
        double nuovoX = x + dx * velocita;
        double nuovoY = y + dy * velocita;

        Rectangle nextHitbox = new Rectangle(nuovoX, nuovoY, width, height);

        boolean collisione = false;
        for (Rectangle o : ostacoli) {
            if (nextHitbox.getBoundsInParent().intersects(o.getBoundsInParent())) {
                collisione = true;
                break;
            }
        }

        if (!collisione) {
            x = nuovoX;
            y = nuovoY;
            aggiornaHitbox();
        }

        setAzione(Azione.CORSA);

        if (Math.abs(dx) > Math.abs(dy)) {
            setDirezione(dx > 0 ? Direzione.DESTRA : Direzione.SINISTRA);
        } else if (dy != 0) {
            setDirezione(dy > 0 ? Direzione.GIU : Direzione.SU);
        }
    }

    // Metodo per disegnare il personaggio sul canvas
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
            case CORSA:
                return 8;
            case ATTACCO:
                return 12;
            case IDLE:
            default:
                return 4;
        }
    }

    // Imposta l'azione del personaggio
    public void setAzione(Azione azione) {
        if (azioneCorrente != azione) {
            azioneCorrente = azione;
            frame = 0;
        }
    }

    // Imposta la direzione del personaggio
    public void setDirezione(Direzione direzione) {
        if (direzioneCorrente != direzione) {
            direzioneCorrente = direzione;
            frame = 0;
        }
    }

    // Funzione per far partire l'attacco
    public void attacca() {
        setAzione(Azione.ATTACCO);
    }

    // Ferma il personaggio (ritorna in stato IDLE)
    public void ferma() {
        setAzione(Azione.IDLE);
    }

    // Getter per la posizione del personaggio
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Getter per la hitbox
    public Rectangle getHitbox() {
        return hitbox;
    }
}