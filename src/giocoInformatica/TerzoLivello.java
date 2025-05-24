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

public class TerzoLivello extends StackPane{

    // Dimensioni della finestra di gioco
    public int larghezzaSchermo = 1350;
    public int altezzaSchermo = 750;
    
    // Liste per gestire oggetti dinamici di gioco: nemici e proiettili
    private ArrayList<Enemy> enemies;  // Lista dei nemici presenti
    private ArrayList<ColpoNemico> enemyBullets = new ArrayList<>();  // Proiettili nemici
    private ArrayList<ColpoPlayer> playerBullets = new ArrayList<>();  // Proiettili giocatore

    // Visualizzazione dello sfondo
    private ImageView imageView;
    private String[] immaginiSfondo = {"throne room.png"};  // Immagini sfondo, si può espandere
    private int currentImageIndex = 0;  // Indice per cambiare sfondo (anche se ora c'è una sola immagine)

    private Player player;  // Riferimento al giocatore
    private Pane root;      // Pane principale contenitore degli elementi di gioco

    private ProgressBar barraVita;    // Barra della vita del giocatore
    private static final double MAX_HEALTH = 100.0;  // Vita massima giocatore
    private double health = MAX_HEALTH;  // Vita attuale

    private Text gameOverText;     // Testo "Game Over" o "Hai vinto"
    private Button backToMenuButton;  // Bottone per uscire dal gioco
    private Stage primaryStage;    // Riferimento allo stage principale per settare la scena
    private VBox gameOverBox;      // Box contenente gameOverText e bottone, per mostrare il risultato
    private AnimationTimer timer;  // Timer principale per il ciclo di gioco (aggiornamenti e rendering)

    // Costruttore principale
    public TerzoLivello(Stage primaryStage) {
        this.primaryStage = primaryStage;  // Salvo il riferimento allo stage per settare la scena

        // Creazione del root Pane che conterrà tutti gli elementi grafici
        root = new Pane();
        this.getChildren().add(root);  // Aggiungo root al StackPane corrente (questa classe estende StackPane)
        System.out.println("Root children count: " + root.getChildren().size());

        // Creazione di un overlay StackPane per elementi come game over, sopra al gioco
        StackPane overlay = new StackPane();
        overlay.setPickOnBounds(false); // Non cattura eventi mouse fuori dai suoi figli
        this.getChildren().add(overlay);

        // Configuro il VBox per il messaggio di fine gioco (sia vittoria che sconfitta)
        gameOverBox = new VBox(20);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setVisible(false); // Lo nascondo all'avvio del gioco

        // Testo di game over iniziale (può essere cambiato in vittoria)
        gameOverText = new Text("HAI PERSO");
        gameOverText.getStyleClass().add("game-over-text");
        gameOverText.setFont(Font.font("Arial", 80));
        gameOverText.setFill(Color.RED);

        // Bottone per uscire dal gioco, chiude il programma alla pressione
        backToMenuButton = new Button("Esci");
        backToMenuButton.getStyleClass().add("game-over-button");
        backToMenuButton.setOnAction(e -> System.exit(0));

        // Aggiungo testo e bottone al box di game over e aggiungo overlay al contenitore principale
        gameOverBox.getChildren().addAll(gameOverText, backToMenuButton);
        overlay.getChildren().add(gameOverBox);

        // Creazione e configurazione dell'ImageView che mostra lo sfondo del livello
        imageView = new ImageView();
        imageView.setFitWidth(larghezzaSchermo);   // Larghezza sfondo pari alla finestra
        imageView.setFitHeight(altezzaSchermo);    // Altezza sfondo pari alla finestra
        root.getChildren().add(imageView);         // Aggiunto al root (sotto tutti gli altri elementi)

        // Carica l'immagine di sfondo iniziale
        aggiornaImmagine();

        // Creazione del giocatore posizionato inizialmente al centro (400, 400)
        player = new Player(400, 400);
        root.getChildren().add(player.getNode());  // Aggiungo il nodo grafico del player al root

        // Creazione e configurazione della barra vita, visibile in alto a sinistra
        barraVita = new ProgressBar();
        barraVita.setProgress(1.0);  // Barra piena all'inizio (100%)
        barraVita.setPrefWidth(200);
        barraVita.setPrefHeight(20);
        barraVita.setStyle("-fx-accent: red;");  // Colore rosso per la vita
        barraVita.setTranslateX(10);  // Posizione X barra
        barraVita.setTranslateY(10);  // Posizione Y barra
        root.getChildren().add(barraVita);

        // Inizializzo la lista dei nemici (vuota)
        enemies = new ArrayList<>();

        // Creo e aggiungo 10 nemici con posizione casuale all'interno dello schermo
        for (int i = 0; i < 10; i++) {
            double x = Math.random() * larghezzaSchermo;  // Posizione X casuale
            double y = Math.random() * altezzaSchermo;   // Posizione Y casuale
            Enemy enemy = new Enemy();  // Creo un nuovo nemico
            enemy.setImageTerzoLivello("mara.jpeg");  // Setto l'immagine del nemico per questo livello
            root.getChildren().add(enemy.getNode());  // Aggiungo il nodo grafico al root
            System.out.println("Nemico creato 2: x=" + x + ", y=" + y);
            enemies.add(enemy);  // Lo aggiungo alla lista nemici per la gestione logica
            System.out.println("Nemico aggiunto alla scena: " + enemy);
        }

        // Creo la scena con la dimensione specificata e la setto nello stage principale
        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
        primaryStage.setScene(scene);
        // Aggiungo il file CSS per lo stile degli elementi
        scene.getStylesheets().add(getClass().getResource("PrimoLivelloStile.css").toExternalForm());

        // Setto il focus su questo StackPane per ricevere eventi da tastiera
        this.setFocusTraversable(true);
        this.requestFocus();

        // Gestisco evento pressione tasti, uso la classe Player per muovere il giocatore
        scene.setOnKeyPressed(event -> {
            player.handleKeyPress(event);  // Muove il giocatore in base al tasto
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                sparoPlayer();  // Il giocatore spara quando preme SPAZIO
            }
        });

        // Gestisco evento rilascio tasti per fermare il movimento se necessario
        scene.setOnKeyReleased(event -> {
            player.handleKeyRelease(event);
        });

        // Creo un AnimationTimer: ciclo principale di gioco, chiamato ogni frame (~60fps)
        timer = new AnimationTimer()  {
            private long lastUpdate = 0;  // Per eventuali calcoli di delta time

            @Override
            public void handle(long now) {
                // Aggiorno la posizione e lo stato del giocatore (input, animazioni, ecc)
                player.update();

                // Aggiorno ogni nemico (movimento, comportamento)
                for (Enemy enemy : enemies) {
                    enemy.update();

                    // Controllo se il nemico può sparare adesso (cooldown ecc)
                    if (enemy.canShoot(now)) {
                        ColpoNemico colpo = enemy.shoot();  // Crea proiettile nemico
                        enemyBullets.add(colpo);             // Lo aggiunge alla lista di colpi nemici
                        root.getChildren().add(colpo.getNode());  // Aggiunge al grafico
                    }
                }

                // Aggiorno proiettili nemici, controllo collisioni e rimozioni
                ArrayList<ColpoNemico> toRemove = new ArrayList<>();
                for (ColpoNemico colpo : enemyBullets) {
                    colpo.update();

                    // Se un colpo colpisce il giocatore, tolgo vita e rimuovo il colpo
                    if (player.getNode().getBoundsInParent().intersects(colpo.getNode().getBoundsInParent())) {
                        takeDamage(10);  // Il giocatore perde 10 punti vita
                        root.getChildren().remove(colpo.getNode()); // Rimuovo graficamente il colpo
                        toRemove.add(colpo);  // Lo rimuovo dalla lista logica
                        continue;  // Passo al prossimo colpo
                    }

                    // Rimuovo il colpo se esce fuori schermo (qui fuori a sinistra)
                    if (colpo.getNode().getX() < -50) {
                        root.getChildren().remove(colpo.getNode());
                        toRemove.add(colpo);
                    }
                }
                enemyBullets.removeAll(toRemove);  // Rimuovo tutti i colpi segnati

                // (Ridondante) aggiorno nuovamente i nemici (potrebbe essere ottimizzato)
                for (Enemy enemy : enemies) {
                    enemy.update();
                }

                // Liste per rimuovere colpi giocatore e nemici quando si scontrano
                ArrayList<ColpoPlayer> colpiDaRimuovere = new ArrayList<>();
                ArrayList<Enemy> nemiciDaRimuovere = new ArrayList<>();

                // Se non ci sono più nemici, il giocatore ha vinto: mostro schermata vittoria
                if (enemies.isEmpty()) {
                    showVictoryScreen();
                    return;
                }

                // Controllo collisioni tra proiettili giocatore e nemici
                for (ColpoPlayer colpo : playerBullets) {
                    for (Enemy enemy : enemies) {
                        if (colpo.getNode().getBoundsInParent().intersects(enemy.getNode().getBoundsInParent())) {
                            // Rimuovo graficamente nemico e proiettile
                            root.getChildren().remove(enemy.getNode());
                            root.getChildren().remove(colpo.getNode());

                            // Li aggiungo alle liste di rimozione logica
                            nemiciDaRimuovere.add(enemy);
                            colpiDaRimuovere.add(colpo);
                            break;  // Esco dal ciclo nemici per questo proiettile
                        }
                    }
                }

                // Rimuovo dalla lista logica i nemici uccisi e i colpi sparati
                enemies.removeAll(nemiciDaRimuovere);
                playerBullets.removeAll(colpiDaRimuovere);

                // Limito il movimento del giocatore per farlo rimanere dentro i bordi dello schermo
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

                // Aggiorno la barra della vita in base all'health attuale
                updateHealthBar();
            }
        };
        timer.start();  // Avvio il ciclo principale di gioco
    }

    // Metodo per aggiornare l'immagine di sfondo con quella corrente nell'array
    private void aggiornaImmagine() {
        if (currentImageIndex >= immaginiSfondo.length) {
            currentImageIndex = 0;  // Ciclo di nuovo all'inizio se supero l'array
        }

        // Carico l'immagine da risorse e la assegno all'ImageView
        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
        imageView.setImage(image);
        currentImageIndex++;  // Passo alla prossima immagine (se presente)
    }

    // Aggiorna la barra della vita in base al valore corrente di health
    private void updateHealthBar() {
        barraVita.setProgress(health / MAX_HEALTH);  // Normalizzo fra 0 e 1
        if (health <= 0) {
            // Se il giocatore è morto, mostro il box game over e blocco input sul gioco
            gameOverBox.setVisible(true);
            root.setDisable(true);  // Blocca tutte le interazioni col gioco
            timer.stop();           // FERMA il ciclo principale di gioco!
        }
    }

    // Metodo per far subire danno al giocatore (riduce health)
    public void takeDamage(double damage) {
        System.out.println("takeDamage chiamato con danno: " + damage + ", health prima: " + health);
        health -= damage;
        if (health < 0) {
            health = 0;  // Non scendere sotto zero
        }
        System.out.println("health dopo: " + health);
        updateHealthBar();  // Aggiorna la barra della vita a video
    }

    // Metodo per curare il giocatore (aumenta health)
    public void heal(double healAmount) {
        health += healAmount;
        if (health > MAX_HEALTH) {
            health = MAX_HEALTH;  // Non superare la vita massima
        }
        updateHealthBar();
    }

    // Metodo per far sparare il giocatore
    private void sparoPlayer() {
        System.out.println("colpo sparato");

        ImageView playerImage = player.getImageView();

        // Posizione iniziale del proiettile: un po' davanti al giocatore
        double colpoX = playerImage.getX() + playerImage.getFitWidth()/3;
        double colpoY = playerImage.getY() + playerImage.getFitHeight() / 2;

        // Creo un nuovo proiettile nella direzione destra (costante del player)
        ColpoPlayer colpo = new ColpoPlayer(colpoX, colpoY, Player.Direzione.DESTRA);

        playerBullets.add(colpo);            // Aggiungo alla lista dei colpi del giocatore
        root.getChildren().add(colpo.getNode());  // Aggiungo graficamente al root

        // Creo un AnimationTimer dedicato al movimento del proiettile sparato
        AnimationTimer colpoTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                colpo.update();  // Aggiorno posizione proiettile
                if (colpo.getX() > larghezzaSchermo) {
                    // Se il proiettile esce dallo schermo a destra, lo rimuovo
                    root.getChildren().remove(colpo.getNode());
                    playerBullets.remove(colpo);  // Rimuovo dalla lista
                    stop();  // Ferma questo timer (proiettile terminato)
                }
            }
        };
        colpoTimer.start();  // Avvio il timer di questo proiettile
    }

    // Mostra la schermata di vittoria quando il giocatore elimina tutti i nemici
    private void showVictoryScreen() {
        gameOverText.setText("HAI VINTO");        // Cambio testo in vittoria
        gameOverText.setFill(Color.LIMEGREEN);   // Colore verde per vittoria
        gameOverBox.setVisible(true);             // Mostro il box con testo e bottone
        root.setDisable(true);                     // Blocca tutte le interazioni con il gioco
        timer.stop();                             // Ferma il ciclo principale di gioco
    }

    // Metodo per pulire tutto (eventuale chiamata a fine gioco o reset)
    private void cleanup() {
        root.getChildren().clear();    // Rimuove tutti gli elementi grafici
        enemyBullets.clear();          // Pulisce lista colpi nemici
        playerBullets.clear();         // Pulisce lista colpi giocatore
        enemies.clear();               // Pulisce lista nemici
        timer.stop();                 // Ferma il ciclo di gioco
    }

}
