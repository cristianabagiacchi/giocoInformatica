package giocoInformatica;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class Player {

    // Enum per definire lo stato del player: fermo (IDLE) o in corsa (CORSA)
    public enum Azione { IDLE, CORSA }
    
    // Enum per definire la direzione del player
    public enum Direzione { SU, GIU, SINISTRA, DESTRA }

    private Azione azioneCorrente = Azione.IDLE;          // Stato attuale del player
    private Direzione direzioneCorrente = Direzione.GIU;  // Direzione attuale (inizialmente verso il basso)

    private int frameCorrente = 0;    // Frame corrente dell'animazione
    private int frameDelay = 100;     // Ritardo tra i frame (più alto = animazione più lenta)
    private int contatoreFrame = 0;   // Contatore per gestire il frameDelay

    // Array 2D: 4 direzioni, 4 frame per l'animazione idle
    private Image[][] idleFrames = new Image[4][4];
    
    // Array 2D: 4 direzioni, 8 frame per l'animazione corsa
    private Image[][] corsaFrames = new Image[4][8];

    private double posizioneX, posizioneY;  // Coordinate attuali del player
    public double velocita = 3.5;            // Velocità di movimento

    private ImageView imageView;             // Nodo grafico per mostrare il player

    // Booleani per tracciare quali tasti di movimento sono premuti
    private boolean muoviSu = false, muoviGiu = false, muoviSinistra = false, muoviDestra = false;

    private double scala = 4;  // Scala per ingrandire l'immagine del player

    // Costruttore che inizializza posizione e carica le animazioni
    public Player(double posizioneX, double posizioneY) {
        this.posizioneX = posizioneX;
        this.posizioneY = posizioneY;
        imageView = new ImageView();
        caricaAnimazioni(); // carica tutte le immagini per idle e corsa
        imageView.setX(posizioneX);
        imageView.setY(posizioneY);
        imageView.setImage(getImmagineCorrente()); // imposta l'immagine iniziale (idle verso il basso)
        imageView.setFitWidth(imageView.getImage().getWidth() * scala);   // scala immagine in larghezza
        imageView.setFitHeight(imageView.getImage().getHeight() * scala); // scala immagine in altezza
    }

    // Carica immagini per ogni animazione e direzione dal filesystem risorse
    private void caricaAnimazioni() {
        for (int direzione = 0; direzione < 4; direzione++) {
            // Carica 4 frame per animazione idle per ogni direzione
            for (int i = 0; i < 4; i++) {
                idleFrames[direzione][i] = new Image(this.getClass().getResourceAsStream("personaggio/idle/idle_" + direzione + "_" + i + ".png"));
            }
            // Carica 8 frame per animazione corsa per ogni direzione
            for (int i = 0; i < 8; i++) {
                corsaFrames[direzione][i] = new Image(this.getClass().getResourceAsStream("personaggio/corsa/corsa_" + direzione + "_" + i + ".png"));
            }
        }
    }

    // Restituisce il nodo ImageView per poter aggiungere il player alla scena
    public ImageView getNode() {
        return imageView;
    }
    
    // Getter per l'ImageView
    public ImageView getImageView() {
        return imageView;
    }

    // Aggiorna la posizione, la direzione e il frame di animazione del player ad ogni ciclo di gioco
    public void update() {
        double nuovaX = posizioneX;
        double nuovaY = posizioneY;

        // Controlla quale tasto movimento è premuto e aggiorna posizione/direzione
        if (muoviSu) {
            nuovaY -= velocita;               // muovi verso l'alto
            direzioneCorrente = Direzione.GIU;  // ATTENZIONE: sembra invertito (forse bug)
            setAzione(Azione.CORSA);          // cambia stato in corsa
        }
        if (muoviGiu) {
            nuovaY += velocita;               // muovi verso il basso
            direzioneCorrente = Direzione.SU; // ATTENZIONE: anche qui sembra invertito
            setAzione(Azione.CORSA);
        }
        if (muoviSinistra) {
            nuovaX -= velocita;               // muovi verso sinistra
            direzioneCorrente = Direzione.SINISTRA;
            setAzione(Azione.CORSA);
        }
        if (muoviDestra) {
            nuovaX += velocita;               // muovi verso destra
            direzioneCorrente = Direzione.DESTRA;
            setAzione(Azione.CORSA);
        }

        // Calcola dimensioni immagine player per limiti schermo
        double larghezzaPlayer = imageView.getFitWidth();
        double altezzaPlayer = imageView.getFitHeight();

        // Dimensioni fisse dello schermo di gioco
        double larghezzaSchermo = 1350;
        double altezzaSchermo = 750;

        // Limita movimento per non uscire dallo schermo
        if (nuovaX < 0) nuovaX = 0;
        if (nuovaX + larghezzaPlayer > larghezzaSchermo) nuovaX = larghezzaSchermo - larghezzaPlayer;
        if (nuovaY < 0) nuovaY = 0;
        if (nuovaY + altezzaPlayer > altezzaSchermo) nuovaY = altezzaSchermo - altezzaPlayer;

        // Aggiorna posizione reale del player
        posizioneX = nuovaX;
        posizioneY = nuovaY;

        // Se nessun tasto premuto, torna allo stato idle
        if (!muoviSu && !muoviGiu && !muoviSinistra && !muoviDestra) {
            setAzione(Azione.IDLE);
        }

        // Gestione animazione: aggiorna frame in base a frameDelay
        contatoreFrame++;
        if (contatoreFrame >= frameDelay) {
            contatoreFrame = 0;
            frameCorrente++;
            // Se finito ultimo frame, torna al primo
            if (frameCorrente >= getNumeroFrameCorrente()) {
                frameCorrente = 0;
            }
        }

        // Aggiorna immagine mostrata e posizione ImageView
        imageView.setImage(getImmagineCorrente());
        imageView.setX(posizioneX);
        imageView.setY(posizioneY);
    }

    // Ritorna numero di frame correnti basato sull'azione (idle=4, corsa=8)
    private int getNumeroFrameCorrente() {
        switch (azioneCorrente) {
            case CORSA: return 8;
            case IDLE:
            default: return 4;
        }
    }

    // Imposta l'azione corrente e resetta il frame animazione
    public void setAzione(Azione nuovaAzione) {
        azioneCorrente = nuovaAzione;
        frameCorrente = 0;
    }

    // Ritorna l'immagine corrente in base a direzione e azione
    private Image getImmagineCorrente() {
        int indiceDirezione = direzioneCorrente.ordinal();
        switch (azioneCorrente) {
            case CORSA: return corsaFrames[indiceDirezione][frameCorrente];
            case IDLE:
            default: return idleFrames[indiceDirezione][frameCorrente];
        }
    }

    // Gestione pressione tasti: abilita i movimenti corrispondenti
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W: muoviSu = true; break;
            case S: muoviGiu = true; break;
            case A: muoviSinistra = true; break;
            case D: muoviDestra = true; break;
            default: break;
        }
    }

    // Gestione rilascio tasti: disabilita i movimenti corrispondenti
    public void handleKeyRelease(KeyEvent event) {
        switch (event.getCode()) {
            case W: muoviSu = false; break;
            case S: muoviGiu = false; break;
            case A: muoviSinistra = false; break;
            case D: muoviDestra = false; break;
            default: break;
        }
    }

    // Getter per la posizione X attuale
    public double getX() {
        return posizioneX;
    }

    // Getter per la posizione Y attuale
    public double getY() {
        return posizioneY;
    }

    // Metodo per spostare il player aggiungendo delta alle coordinate
    public void move(double deltaX, double deltaY) {
        posizioneX += deltaX;
        posizioneY += deltaY;
    }
}
