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


public class SecondoLivello extends StackPane {
	
	 public int larghezzaSchermo = 1350;
	    public int altezzaSchermo = 750;
	    private ArrayList<Enemy> enemies;  // Lista per gestire i nemici
	    private ArrayList<ColpoNemico> enemyBullets = new ArrayList<>();
	    private ArrayList<ColpoPlayer> playerBullets = new ArrayList<>();


	    private ImageView imageView;
	    private String[] immaginiSfondo = {"terrace.png"};
	    private int currentImageIndex = 0;

	    private Player player;
	    private Pane root;  // Definisci la variabile root

	    private ProgressBar barraVita;
	    private static final double MAX_HEALTH = 100.0;
	    private double health = MAX_HEALTH;
	    
	    private Text gameOverText;
	    private Button backToMenuButton;
	    private Stage primaryStage;
	    private VBox gameOverBox;
	    private AnimationTimer timer;


	    public SecondoLivello(Stage primaryStage) {
	    	this.primaryStage = primaryStage;  // SALVA il riferimento allo stage

	        
	        // Crea il root Pane
	        root = new Pane();
	        this.getChildren().add(root);
	        System.out.println("Root children count: " + root.getChildren().size());
	        
	        StackPane overlay = new StackPane();
	        overlay.setPickOnBounds(false);
	        this.getChildren().add(overlay);

	        gameOverBox = new VBox(20);       // ✅ diventa campo della classe
	        gameOverBox.setAlignment(Pos.CENTER);
	        gameOverBox.setVisible(false); // Nascondilo all'inizio

	        gameOverText = new Text("HAI PERSO");
	        gameOverText.getStyleClass().add("game-over-text"); 
	        gameOverText.setFont(Font.font("Arial", 80));
	        gameOverText.setFill(Color.RED);

	        backToMenuButton = new Button("Esci");
	        backToMenuButton.getStyleClass().add("game-over-button");
	        backToMenuButton.setOnAction(e -> System.exit(0));

	        gameOverBox.getChildren().addAll(gameOverText, backToMenuButton);  // ✅ OK
	        overlay.getChildren().add(gameOverBox);                             // ✅ OK


	        // Crea e configura l'ImageView per lo sfondo
	        imageView = new ImageView();
	        imageView.setFitWidth(larghezzaSchermo);
	        imageView.setFitHeight(altezzaSchermo);
	        root.getChildren().add(imageView);

	        // Aggiorna l'immagine iniziale dello sfondo
	        aggiornaImmagine();

	        // Crea il giocatore e aggiungilo al root
	        player = new Player(400, 400);
	        root.getChildren().add(player.getNode());

	        // Crea e configura la barra della vita
	        barraVita = new ProgressBar();
	        barraVita.setProgress(1.0);
	        barraVita.setPrefWidth(200);
	        barraVita.setPrefHeight(20);
	        barraVita.setStyle("-fx-accent: red;");
	        barraVita.setTranslateX(10);
	        barraVita.setTranslateY(10);
	        root.getChildren().add(barraVita);
	        
	     
	     // Crea la lista di nemici
	        enemies = new ArrayList<>();

	        // Aggiungi nemici al gioco (puoi aggiungerne quanti vuoi)
	        for (int i = 0; i < 8; i++) {  // Per esempio, 5 nemici
	        	double x = Math.random() * larghezzaSchermo;  // Posizione casuale sull'asse X (0 - 1350)
	            double y = Math.random() * altezzaSchermo;   // Posizione casuale sull'asse Y (0 - 750)
	            Enemy enemy = new Enemy();  // Crea un nemico
	            enemy.setImage("stocchi.jpg");
	            root.getChildren().add(enemy.getNode());
	            System.out.println("Nemico creato 2: x=" + x + ", y=" + y);
	            enemies.add(enemy);  // Aggiungi il nemico alla lista
	            System.out.println("Nemico aggiunto alla scena: " + enemy);
	        }

	        // Imposta la scena e aggiungi il root
	        Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);
	        primaryStage.setScene(scene);
	        scene.getStylesheets().add(getClass().getResource("PrimoLivelloStile.css").toExternalForm());


	        // Aggiungi il focus per ricevere gli eventi da tastiera
	        this.setFocusTraversable(true);
	        this.requestFocus();

	        // Gestione degli eventi da tastiera
	        scene.setOnKeyPressed(event -> {
	            player.handleKeyPress(event);
	            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
	                sparoPlayer();  // Aggiungi il colpo quando si preme SPACE
	            }
	        });

	        scene.setOnKeyReleased(event -> {
	            player.handleKeyRelease(event);
	        });

	        // Crea un timer per animazioni e aggiornamenti
	        timer = new AnimationTimer()  {
	            private long lastUpdate = 0;

	            @Override
	            public void handle(long now) {
	                // Aggiorna la posizione del giocatore
	                player.update();
	                
	                // Aggiorna la posizione e il comportamento dei nemici
	                for (Enemy enemy : enemies) {
	                    enemy.update();

	                    // Controlla se il nemico può sparare adesso
	                    if (enemy.canShoot(now)) {
	                        ColpoNemico colpo = enemy.shoot();
	                        enemyBullets.add(colpo);
	                        root.getChildren().add(colpo.getNode());
	                        
	                    }
	                    
	                   
	                }

	                // Aggiorna i colpi nemici e rimuovi quelli fuori schermo
	                ArrayList<ColpoNemico> toRemove = new ArrayList<>();
	                for (ColpoNemico colpo : enemyBullets) {
	                    colpo.update();

	                    // Controlla collisione col player
	                    if (player.getNode().getBoundsInParent().intersects(colpo.getNode().getBoundsInParent())) {
	                        takeDamage(10);  // togli 10 punti di vita (puoi cambiare il valore)
	                        root.getChildren().remove(colpo.getNode());
	                        toRemove.add(colpo);
	                        continue;  // passa al prossimo colpo, questo è stato rimosso
	                    }

	                    // Rimuovi colpo se è fuori schermo
	                    if (colpo.getNode().getX() < -50) {
	                        root.getChildren().remove(colpo.getNode());
	                        toRemove.add(colpo);
	                    }
	                }
	                enemyBullets.removeAll(toRemove);
	                // Aggiorna la posizione dei nemici
	                for (Enemy enemy : enemies) {
	                    enemy.update();  // Aggiorna il movimento e il comportamento del nemico
	                }

	                
	                // Lista di colpi da rimuovere e nemici da rimuovere
	                ArrayList<ColpoPlayer> colpiDaRimuovere = new ArrayList<>();
	                ArrayList<Enemy> nemiciDaRimuovere = new ArrayList<>();

	                for (ColpoPlayer colpo : playerBullets) {
	                    for (Enemy enemy : enemies) {
	                        if (colpo.getNode().getBoundsInParent().intersects(enemy.getNode().getBoundsInParent())) {
	                        	// 👇 Qui rimuovi il nodo visivo
	                            root.getChildren().remove(enemy.getNode());
	                            root.getChildren().remove(colpo.getNode());

	                            // 👇 Poi segni per rimuovere dalla logica
	                            nemiciDaRimuovere.add(enemy);
	                            colpiDaRimuovere.add(colpo);
	                            
	                            break;  // Esci dal ciclo nemici per questo colpo
	                        }
	                    }
	                }

	                // Rimuovi i nemici morti e i colpi sparati
	                enemies.removeAll(nemiciDaRimuovere);
	                playerBullets.removeAll(colpiDaRimuovere);
	                
	               
	                // Ottieni la posizione del giocatore (usando getLayoutX() e getLayoutY())
	                double playerX = player.getNode().getLayoutX();
	                double playerY = player.getNode().getLayoutY();

	                // Ottieni la larghezza e l'altezza del giocatore usando getBoundsInParent()
	                double playerWidth = player.getNode().getBoundsInParent().getWidth();
	                double playerHeight = player.getNode().getBoundsInParent().getHeight();

	                // Limiti di movimento per il giocatore
	                if (playerX < 0) playerX = 0;
	                if (playerX + playerWidth > larghezzaSchermo) playerX = larghezzaSchermo - playerWidth;
	                if (playerY < 0) playerY = 0;
	                if (playerY + playerHeight > altezzaSchermo) playerY = altezzaSchermo - playerHeight;

	                // Aggiorna la posizione del giocatore (usando setLayoutX() e setLayoutY())
	                player.getNode().setLayoutX(playerX);
	                player.getNode().setLayoutY(playerY);

	                // Aggiorna la barra della vita
	                updateHealthBar();
	            }
	        };
	        timer.start();
	    }

	    // Metodo per aggiornare l'immagine di sfondo
	    private void aggiornaImmagine() {
	        if (currentImageIndex >= immaginiSfondo.length) {
	            currentImageIndex = 0;
	        }

	        // Carica l'immagine da un file
	        Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
	        imageView.setImage(image);
	        currentImageIndex++;
	    }

	    // Metodo per aggiornare la barra della vita
	    private void updateHealthBar() {
	        barraVita.setProgress(health / MAX_HEALTH);
	        if (health <= 0) {
	        	gameOverBox.setVisible(true);
	        	root.setDisable(true);// blocca interazioni col gioco
	        }
	    }
	    // Metodo per ridurre la vita del giocatore (prendere danno)
	    public void takeDamage(double damage) {
	        System.out.println("takeDamage chiamato con danno: " + damage + ", health prima: " + health);
	        health -= damage;
	        if (health < 0) {
	            health = 0;
	        }
	        System.out.println("health dopo: " + health);
	        updateHealthBar();
	    }
	    

	    // Metodo per guarire il giocatore
	    public void heal(double healAmount) {
	        health += healAmount;
	        if (health > MAX_HEALTH) {
	            health = MAX_HEALTH;
	        }
	        updateHealthBar();
	    }

	    // Metodo per sparare il colpo del giocatore
	    private void sparoPlayer() {
	        System.out.println("colpo sparato");

	        ImageView playerImage = player.getImageView();

	        double colpoX = playerImage.getX() + playerImage.getFitWidth()/3;
	        double colpoY = playerImage.getY() + playerImage.getFitHeight() / 2;

	        ColpoPlayer colpo = new ColpoPlayer(colpoX, colpoY, Player.Direzione.DESTRA);

	        playerBullets.add(colpo);            // Aggiunto alla lista
	        root.getChildren().add(colpo.getNode());

	        AnimationTimer colpoTimer = new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	                colpo.update();
	                if (colpo.getX() > larghezzaSchermo) {
	                    root.getChildren().remove(colpo.getNode());
	                    playerBullets.remove(colpo);  // Rimuovi dalla lista quando esce
	                    stop();
	                }
	            }
	        };
	        colpoTimer.start();
	    }
	    
	    private void cleanup() {
	        root.getChildren().clear();
	        enemyBullets.clear();
	        playerBullets.clear();
	        enemies.clear();
	        timer.stop();
	    }

}
