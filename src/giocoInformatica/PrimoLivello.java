package giocoInformatica;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class PrimoLivello extends StackPane {

    public int larghezzaSchermo = 1350;
    public int altezzaSchermo = 750;
    private ArrayList<Enemy> enemies;  // Lista per gestire i nemici
    private ArrayList<ColpoNemico> enemyBullets = new ArrayList<>();  // Lista dei proiettili nemici attivi
    private ArrayList<ColpoPlayer> playerBullets = new ArrayList<>(); // Lista dei proiettili player attivi

    private ImageView imageView;  // Nodo per visualizzare l'immagine di sfondo
    private String[] immaginiSfondo = {"castle.png"};  // Array con le immagini di sfondo disponibili
    private int currentImageIndex = 0;  // Indice per cambiare immagine di sfondo (utile se ci fossero più immagini)

    private Player player;  // Riferimento al player controllato dall'utente
    private Pane root;      // Pane principale dove vengono aggiunti tutti i nodi di gioco (player, nemici, proiettili...)

    private ProgressBar barraVita;  // Barra della vita visiva del giocatore
    private static final double MAX_HEALTH = 100.0;  // Vita massima del player
    private double health = MAX_HEALTH;  // Vita attuale

    private Text gameOverText;  // Testo "Hai perso"
    private Button backToMenuButton;  // Bottone per uscire dal gioco
    private Stage primaryStage;  // Riferimento alla finestra principale
    private VBox gameOverBox;    // Contenitore verticale per mostrare messaggio game over e bottone
    private AnimationTimer timer; // Timer principale che gestisce aggiornamenti e logica di gioco

    // Costruttore che riceve lo Stage principale
    public PrimoLivello(Stage primaryStage) {
        this.primaryStage = primaryStage;  // Salva il riferimento allo stage principale

        // Creo un Pane root, che sarà il contenitore principale di tutti gli elementi grafici del livello
        root = new Pane();
        this.getChildren().add(root);  // Aggiungo il root al StackPane (questa classe estende StackPane)

        // Creo un overlay StackPane che starà sopra il root per contenere UI come messaggi di game over
        StackPane overlay = new StackPane();
        overlay.setPickOnBounds(false);  // Permette eventi mouse/passaggio a nodi sottostanti
        this.getChildren().add(overlay);  // Lo aggiungo sopra il root

        // VBox per mostrare il messaggio "HAI PERSO" e il bottone di uscita
        gameOverBox = new VBox(20);  // Spaziatura verticale 20 px
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setVisible(false); // Inizialmente nascosto (mostreremo solo quando il giocatore perde)

        // Testo grande rosso per il game over
        gameOverText = new Text("HAI PERSO");
        gameOverText.getStyleClass().add("game-over-text");  // Classe CSS per styling
        gameOverText.setFont(Font.font("Arial", 80));
        gameOverText.setFill(Color.RED);

        // Bottone per uscire dal gioco
        backToMenuButton = new Button("Esci");
        backToMenuButton.getStyleClass().add("game-over-button");
        backToMenuButton.setOnAction(e -> System.exit(0)); // Chiude l'applicazione alla pressione

        // Aggiungo testo e bottone alla VBox gameOverBox e la aggiungo all'overlay
        gameOverBox.getChildren().addAll(gameOverText, backToMenuButton);
        overlay.getChildren().add(gameOverBox);

        // Creo ImageView per lo sfondo e lo dimensiono alle dimensioni della finestra
        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);
        imageView.setFitHeight(altezzaSchermo);
        root.getChildren().add(imageView);  // Lo aggiungo al root (sfondo sempre sotto)

        // Imposto l'immagine iniziale dello sfondo
        aggiornaImmagine();

        // Creo il player e lo aggiungo al root
        player = new Player(400, 400);  // Posizione iniziale fissa
        root.getChildren().add(player.getNode());

        // Creo la barra della vita, la configuro e la aggiungo al root
        barraVita = new ProgressBar();
        barraVita.setProgress(1.0);  // Piena all'inizio
        barraVita.setPrefWidth(200);
        barraVita.setPrefHeight(20);
        barraVita.setStyle("-fx-accent: red;");  // Barra rossa
        barraVita.setTranslateX(10);
        barraVita.setTranslateY(10);
        root.getChildren().add(barraVita);

        // Inizializzo la lista dei nemici
        enemies = new ArrayList<>();

        // Creo 5 nemici posizionati casualmente nel livello
        for (int i = 0; i < 5; i++) {
            double x = Math.random() * larghezzaSchermo;  // Posizione casuale X
            double y = Math.random() * altezzaSchermo;   // Posizione casuale Y
            Enemy enemy = new Enemy();  // Creo un nemico (posizione da settare internamente)
            System.out.println("Nemico creato: x=" + x + ", y=" + y);
            enemies.add(enemy);         // Lo aggiungo alla lista di gestione
            root.getChildren().add(enemy.getNode());  // Lo aggiungo al root per visualizzazione
            System.out.println("Nemico aggiunto alla scena: " + enemy);
        }

        // Creo la scena con larghezza e altezza definite, e setto questa classe (StackPane) come root della scena
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        primaryStage.setScene(scene);  // Imposto la scena nello stage principale

        // Aggiungo il file CSS per styling personalizzato
        scene.getStylesheets().add(getClass().getResource("PrimoLivelloStile.css").toExternalForm());

        // Rendo il nodo principale focussabile e chiedo subito il focus per ricevere eventi tastiera
        this.setFocusTraversable(true);
        this.requestFocus();

        // Gestione eventi tastiera: quando viene premuto un tasto
        scene.setOnKeyPressed(event -> {
            player.handleKeyPress(event);  // Comunico al player quale tasto è stato premuto
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                sparoPlayer();  // Se spazio premuto, sparo un colpo
            }
        });

        // Gestione eventi tastiera: quando il tasto viene rilasciato
        scene.setOnKeyReleased(event -> {
            player.handleKeyRelease(event);
        });

        // Creo un AnimationTimer principale per aggiornare logica e animazioni ad ogni frame
        timer = new AnimationTimer() {
            private long lastUpdate = 0;  // Per gestione temporale, se serve

            @Override
            public void handle(long now) {
                // Aggiorno posizione e stato del giocatore
                player.update();

                // Aggiorno nemici e controllo se possono sparare
                for (Enemy enemy : enemies) {
                    enemy.update();

                    // Se il nemico può sparare in questo momento (basato su timer interno)
                    if (enemy.canShoot(now)) {
                        ColpoNemico colpo = enemy.shoot();  // Creo un colpo
                        enemyBullets.add(colpo);             // Lo aggiungo alla lista proiettili nemici
                        root.getChildren().add(colpo.getNode()); // Lo aggiungo alla scena per visualizzazione
                    }
                }

                // Aggiorno proiettili nemici, controllo collisioni e rimozioni
                ArrayList<ColpoNemico> toRemove = new ArrayList<>();
                for (ColpoNemico colpo : enemyBullets) {
                    colpo.update();

                    // Controllo se colpisce il player (collisione bounding box)
                    if (player.getNode().getBoundsInParent().intersects(colpo.getNode().getBoundsInParent())) {
                        takeDamage(10);  // Player perde 10 punti vita
                        root.getChildren().remove(colpo.getNode());  // Rimuovo il proiettile dalla scena
                        toRemove.add(colpo);  // Segno il proiettile da rimuovere dalla lista
                        continue;  // Passo al prossimo proiettile
                    }

                    // Se il proiettile è uscito dallo schermo (a sinistra)
                    if (colpo.getNode().getX() < -50) {
                        root.getChildren().remove(colpo.getNode());
                        toRemove.add(colpo);
                    }
                }
                enemyBullets.removeAll(toRemove);  // Rimuovo i proiettili fuori o colpiti

                // Aggiorno nuovamente la posizione e stato dei nemici (puoi valutare di unificare con precedente)
                for (Enemy enemy : enemies) {
                    enemy.update();
                }

                // Liste temporanee per rimuovere nemici e colpi colpiti
                ArrayList<ColpoPlayer> colpiDaRimuovere = new ArrayList<>();
                ArrayList<Enemy> nemiciDaRimuovere = new ArrayList<>();

                // Controllo collisioni tra colpi player e nemici
                for (ColpoPlayer colpo : playerBullets) {
                    for (Enemy enemy : enemies) {
                        if (colpo.getNode().getBoundsInParent().intersects(enemy.getNode().getBoundsInParent())) {
                            nemiciDaRimuovere.add(enemy);  // Nemico colpito
                            colpiDaRimuovere.add(colpo);   // Colpo colpito
                            root.getChildren().remove(enemy.getNode()); // Rimuovo nemico dalla scena
                            root.getChildren().remove(colpo.getNode()); // Rimuovo colpo dalla scena
                            break;  // Esco dal ciclo nemici per questo colpo
                        }
                    }
                }

                // Rimuovo nemici e colpi colpiti dalle rispettive liste
                enemies.removeAll(nemiciDaRimuovere);
                playerBullets.removeAll(colpiDaRimuovere);

                // Se tutti i nemici sono stati eliminati, passo al secondo livello
                if (enemies.isEmpty()) {
                    passaggioAlSecondoLivello();
                    return; // Esco dall'handle per fermare aggiornamenti
                }

                // Limito il movimento del giocatore ai confini dello schermo
                double playerX = player.getNode().getLayoutX();
                double playerY = player.getNode().getLayoutY();

                double playerWidth = player.getNode().getBoundsInParent().getWidth();
                double playerHeight = player.getNode().getBoundsInParent().getHeight();

                if (playerX < 0) playerX = 0;
                if (playerX + playerWidth > larghezzaSchermo) playerX = larghezzaSchermo - playerWidth;
                if (playerY < 0) playerY = 0;
                if (playerY + playerHeight > altezzaSchermo) playerY = altezzaSchermo - playerHeight;

                player.getNode().setLayoutX(playerX);
                player.getNode().setLayoutY(playerY);

                // Aggiorno la barra della vita in base alla salute attuale
                updateHealthBar();
            }
        };
        timer.start();  // Avvio il timer principale per far partire il gioco
    }

    // Metodo che aggiorna l'immagine di sfondo, utile se si vuole fare uno slideshow o cambiare scena
    private void aggiornaImmagine() {
        if (currentImageIndex >= immaginiSfondo.length) {
            currentImageIndex = 0;  // Riparte dall'inizio se supera la lunghezza
        }

        // Carico l'immagine dallo stream di risorse (cartella risorse)
        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
        imageView.setImage(image);  // Aggiorno l'immagine di sfondo
        currentImageIndex++;        // Passo all'immagine successiva per prossima chiamata
    }

    // Aggiorna la barra della vita visuale in base al valore corrente di health
    private void updateHealthBar() {
        barraVita.setProgress(health / MAX_HEALTH);
        if (health <= 0) {
            gameOverBox.setVisible(true);  // Mostro messaggio game over
            root.setDisable(true);          // Disabilito interazioni col gioco per fermare tutto
            timer.stop();           // FERMA il ciclo principale di gioco!
        }
    }

    // Metodo per far subire danni al player, decrementando la vita e aggiornando la barra
    public void takeDamage(double damage) {
        System.out.println("takeDamage chiamato con danno: " + damage + ", health prima: " + health);
        health -= damage;
        if (health < 0) {
            health = 0;
        }
        System.out.println("health dopo: " + health);
        updateHealthBar();  // Aggiorno la barra della vita
    }

    // Metodo per curare il player aumentando la vita fino al massimo
    public void heal(double healAmount) {
        health += healAmount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;
        }
        updateHealthBar();
    }

    // Metodo per sparare un colpo dal player quando preme SPAZIO
    private void sparoPlayer() {
        System.out.println("colpo sparato");

        ImageView playerImage = player.getImageView();

        // Calcolo posizione iniziale del colpo, basandomi sulla posizione del player e dimensioni immagine
        double colpoX = playerImage.getX() + playerImage.getFitWidth()/3;
        double colpoY = playerImage.getY() + playerImage.getFitHeight() / 2;

        // Creo un nuovo colpo in direzione destra
        ColpoPlayer colpo = new ColpoPlayer(colpoX, colpoY, Player.Direzione.DESTRA);

        playerBullets.add(colpo);            // Lo aggiungo alla lista dei proiettili del player
        root.getChildren().add(colpo.getNode());  // Lo aggiungo alla scena per vederlo

        // Creo un AnimationTimer per aggiornare il singolo colpo finché non esce dallo schermo
        AnimationTimer colpoTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                colpo.update();  // Aggiorno la posizione del colpo

                // Se il colpo esce oltre la larghezza dello schermo lo rimuovo
                if (colpo.getX() > larghezzaSchermo) {
                    root.getChildren().remove(colpo.getNode());
                    playerBullets.remove(colpo);  // Rimuovo dalla lista colpi player
                    stop();  // Stoppo questo timer per il colpo
                }
            }
        };
        colpoTimer.start();
    }

    // Metodo per passare al secondo livello quando tutti i nemici sono eliminati
    private void passaggioAlSecondoLivello() {
        heal(0);     // Eventuale cura o reset vita (qui a 0, quindi no)
        cleanup();   // Pulisce elementi e liste per liberare risorse
        timer.stop(); // Ferma il timer principale del primo livello

        // Creo il secondo livello e imposto la sua root nella scena
        SecondoLivello secondoLivello = new SecondoLivello(primaryStage);
        primaryStage.getScene().setRoot(secondoLivello);
    }

    // Metodo per pulire risorse e nodi alla fine del livello
    private void cleanup() {
        root.getChildren().clear();  // Rimuovo tutti i nodi dal pane root
        enemies.clear();             // Svuoto la lista nemici
        playerBullets.clear();       // Svuoto lista proiettili player
        enemyBullets.clear();        // Svuoto lista proiettili nemici
    }
}
