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
	
	// Dimensioni dello schermo di gioco
	public int larghezzaSchermo = 1350;
	public int altezzaSchermo = 750;

	// Lista che contiene tutti i nemici attivi nel livello
	private ArrayList<Enemy> enemies;

	// Liste per gestire i proiettili: quelli nemici e quelli del giocatore
	private ArrayList<ColpoNemico> enemyBullets = new ArrayList<>();
	private ArrayList<ColpoPlayer> playerBullets = new ArrayList<>();

	// ImageView per lo sfondo
	private ImageView imageView;

	// Array con i nomi delle immagini di sfondo (puoi aggiungerne di più)
	private String[] immaginiSfondo = {"terrace.png"};

	// Indice dell'immagine di sfondo corrente
	private int currentImageIndex = 0;

	// Riferimento al giocatore
	private Player player;

	// Pane principale dove vengono aggiunti tutti i nodi di gioco
	private Pane root;

	// Barra di progresso per la vita del giocatore
	private ProgressBar barraVita;

	// Costante per la salute massima del giocatore
	private static final double MAX_HEALTH = 100.0;

	// Variabile per la salute corrente del giocatore
	private double health = MAX_HEALTH;

	// Testo e bottone per la schermata di game over
	private Text gameOverText;
	private Button backToMenuButton;

	// Riferimento allo stage principale
	private Stage primaryStage;

	// Contenitore verticale per mostrare game over e bottoni (overlay)
	private VBox gameOverBox;

	// Timer per gestire il ciclo di gioco (aggiornamento frame)
	private AnimationTimer timer;


	// Costruttore della classe, prende come parametro lo stage principale
	public SecondoLivello(Stage primaryStage) {
		this.primaryStage = primaryStage;  // Salva riferimento allo stage per poi cambiare scena

		// Crea il root Pane dove aggiungere gli elementi del gioco
		root = new Pane();
		this.getChildren().add(root);  // Aggiunge root a questo StackPane (il livello stesso)
		System.out.println("Root children count: " + root.getChildren().size());

		// Crea un overlay (StackPane) per mettere sopra la schermata di game over
		StackPane overlay = new StackPane();
		overlay.setPickOnBounds(false); // Non bloccare eventi su aree trasparenti
		this.getChildren().add(overlay);  // Aggiunge overlay sopra root

		// Crea il box verticale per il game over (inizialmente nascosto)
		gameOverBox = new VBox(20);
		gameOverBox.setAlignment(Pos.CENTER);
		gameOverBox.setVisible(false); // Nascondilo all'inizio

		// Testo grande e rosso che dice "HAI PERSO"
		gameOverText = new Text("HAI PERSO");
		gameOverText.getStyleClass().add("game-over-text");
		gameOverText.setFont(Font.font("Arial", 80));
		gameOverText.setFill(Color.RED);

		// Bottone "Esci" che chiude il gioco
		backToMenuButton = new Button("Esci");
		backToMenuButton.getStyleClass().add("game-over-button");
		backToMenuButton.setOnAction(e -> System.exit(0));

		// Aggiunge testo e bottone al box del game over e aggiunge il box all'overlay
		gameOverBox.getChildren().addAll(gameOverText, backToMenuButton);
		overlay.getChildren().add(gameOverBox);

		// Crea l'immagine di sfondo e la imposta per occupare tutta la finestra
		imageView = new ImageView();
		imageView.setFitWidth(larghezzaSchermo);
		imageView.setFitHeight(altezzaSchermo);
		root.getChildren().add(imageView);

		// Imposta l'immagine iniziale dello sfondo
		aggiornaImmagine();

		// Crea il giocatore e lo aggiunge al root, posizione iniziale (400,400)
		player = new Player(400, 400);
		root.getChildren().add(player.getNode());

		// Crea e configura la barra della vita (progress bar rossa in alto a sinistra)
		barraVita = new ProgressBar();
		barraVita.setProgress(1.0);  // 100% salute iniziale
		barraVita.setPrefWidth(200);
		barraVita.setPrefHeight(20);
		barraVita.setStyle("-fx-accent: red;");
		barraVita.setTranslateX(10);
		barraVita.setTranslateY(10);
		root.getChildren().add(barraVita);

		// Crea la lista dei nemici
		enemies = new ArrayList<>();

		// Genera 8 nemici in posizioni casuali e li aggiunge alla scena e lista
		for (int i = 0; i < 8; i++) {
			double x = Math.random() * larghezzaSchermo;  // Posizione casuale X
			double y = Math.random() * altezzaSchermo;   // Posizione casuale Y
			Enemy enemy = new Enemy();  // Crea un nemico
			enemy.setImageSecondoLivello("stocchi.jpg");  // Imposta immagine nemico
			root.getChildren().add(enemy.getNode());      // Aggiunge il nodo del nemico al root
			System.out.println("Nemico creato 2: x=" + x + ", y=" + y);
			enemies.add(enemy);                            // Aggiunge alla lista dei nemici
			System.out.println("Nemico aggiunto alla scena: " + enemy);
		}

		// Crea la scena con questo StackPane come root e dimensioni definite
		Scene scene = new Scene(this, larghezzaSchermo, altezzaSchermo);

		// Imposta la scena sullo stage
		primaryStage.setScene(scene);

		// Aggiunge il file css per lo stile
		scene.getStylesheets().add(getClass().getResource("PrimoLivelloStile.css").toExternalForm());

		// Permette al nodo di ricevere eventi da tastiera
		this.setFocusTraversable(true);
		this.requestFocus();

		// Gestione evento pressione tasti: muove player e spara con SPAZIO
		scene.setOnKeyPressed(event -> {
			player.handleKeyPress(event);
			if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
				sparoPlayer();  // Spara un colpo quando premi SPAZIO
			}
		});

		// Gestione rilascio tasti per fermare movimenti
		scene.setOnKeyReleased(event -> {
			player.handleKeyRelease(event);
		});

		// Timer per gestire aggiornamenti costanti ogni frame
		timer = new AnimationTimer()  {
			private long lastUpdate = 0;

			@Override
			public void handle(long now) {
				// Aggiorna la posizione e stato del giocatore
				player.update();

				// Aggiorna ogni nemico (movimento, logica, sparo)
				for (Enemy enemy : enemies) {
					enemy.update();

					// Se il nemico può sparare in questo frame
					if (enemy.canShoot(now)) {
						ColpoNemico colpo = enemy.shoot();
						enemyBullets.add(colpo);          // Aggiungi colpo alla lista
						root.getChildren().add(colpo.getNode()); // Aggiungi il nodo visivo al root
					}
				}

				// Lista temporanea per colpi nemici da rimuovere (colpiti o fuori schermo)
				ArrayList<ColpoNemico> toRemove = new ArrayList<>();

				// Aggiorna posizione e controlla collisioni col player
				for (ColpoNemico colpo : enemyBullets) {
					colpo.update();

					// Se colpo nemico colpisce il player, togli vita e rimuovi il colpo
					if (player.getNode().getBoundsInParent().intersects(colpo.getNode().getBoundsInParent())) {
						takeDamage(10);  // Togli 10 punti di vita al player
						root.getChildren().remove(colpo.getNode());
						toRemove.add(colpo);
						continue;  // Passa al prossimo colpo
					}

					// Rimuove colpo se è uscito fuori dallo schermo (esempio, se X < -50)
					if (colpo.getNode().getX() < -50) {
						root.getChildren().remove(colpo.getNode());
						toRemove.add(colpo);
					}
				}

				// Rimuove i colpi nemici segnati per la rimozione dalla lista principale
				enemyBullets.removeAll(toRemove);

				// Aggiorna di nuovo i nemici (puoi rimuovere questo secondo update se ridondante)
				for (Enemy enemy : enemies) {
					enemy.update();
				}

				// Liste temporanee per rimuovere colpi player e nemici colpiti
				ArrayList<ColpoPlayer> colpiDaRimuovere = new ArrayList<>();
				ArrayList<Enemy> nemiciDaRimuovere = new ArrayList<>();

				// Se non ci sono nemici rimasti, passa al terzo livello
				if (enemies.isEmpty()) {
					passaggioAlTerzoLivello();
					return;
				}

				// Controllo collisioni tra colpi del player e nemici
				for (ColpoPlayer colpo : playerBullets) {
					for (Enemy enemy : enemies) {
						if (colpo.getNode().getBoundsInParent().intersects(enemy.getNode().getBoundsInParent())) {
							// Rimuove visivamente nemico e colpo
							root.getChildren().remove(enemy.getNode());
							root.getChildren().remove(colpo.getNode());

							// Segna per la rimozione logica dalle liste
							nemiciDaRimuovere.add(enemy);
							colpiDaRimuovere.add(colpo);

							break;  // Passa al prossimo colpo player
						}
					}
				}

				// Rimuove dalla lista principale nemici e colpi che sono stati colpiti
				enemies.removeAll(nemiciDaRimuovere);
				playerBullets.removeAll(colpiDaRimuovere);

				// Limita il movimento del giocatore all’interno dello schermo
				double playerX = player.getNode().getLayoutX();
				double playerY = player.getNode().getLayoutY();

				double playerWidth = player.getNode().getBoundsInParent().getWidth();
				double playerHeight = player.getNode().getBoundsInParent().getHeight();

				// Controlla i bordi e corregge la posizione del player se esce dallo schermo
				if (playerX < 0) playerX = 0;
				if (playerX + playerWidth > larghezzaSchermo) playerX = larghezzaSchermo - playerWidth;
				if (playerY < 0) playerY = 0;
				if (playerY + playerHeight > altezzaSchermo) playerY = altezzaSchermo - playerHeight;

				// Applica la posizione corretta al nodo del player
				player.getNode().setLayoutX(playerX);
				player.getNode().setLayoutY(playerY);

				// Aggiorna la barra della vita in base alla salute corrente
				updateHealthBar();
			}
		};
		timer.start();  // Avvia il ciclo di gioco
	}

	// Metodo che aggiorna l’immagine di sfondo ciclando tra quelle presenti nell’array
	private void aggiornaImmagine() {
		if (currentImageIndex >= immaginiSfondo.length) {
			currentImageIndex = 0;  // Ricomincia da capo se finisce la lista
		}

		// Carica l’immagine corrente come risorsa
		Image image = new Image(getClass().getResourceAsStream(immaginiSfondo[currentImageIndex]));
		imageView.setImage(image);  // Imposta l’immagine sul ImageView dello sfondo
		currentImageIndex++;
	}

	// Metodo per aggiornare la barra della vita in base alla salute attuale
	private void updateHealthBar() {
		barraVita.setProgress(health / MAX_HEALTH);  // Imposta progressione percentuale
		if (health <= 0) {
			gameOverBox.setVisible(true);  // Mostra il box di game over
			root.setDisable(true);          // Disabilita interazioni con il gioco
			 timer.stop();           // FERMA il ciclo principale di gioco!
		}
	}

	// Metodo che toglie vita al giocatore quando subisce danno
	public void takeDamage(double damage) {
		System.out.println("takeDamage chiamato con danno: " + damage + ", health prima: " + health);
		health -= damage;      // Riduce la vita
		if (health < 0) {
			health = 0;       // Non può scendere sotto zero
		}
		System.out.println("health dopo: " + health);
		updateHealthBar();     // Aggiorna la barra della vita visiva
	}

	// Metodo per curare il giocatore (aumentare la vita)
	public void heal(double healAmount) {
		health += healAmount;    // Aumenta la salute
		if (health > MAX_HEALTH) {
			health = MAX_HEALTH;  // Non può superare il massimo
		}
		updateHealthBar();       // Aggiorna la barra della vita
	}

	// Metodo che crea un colpo sparato dal giocatore
	private void sparoPlayer() {
		System.out.println("colpo sparato");

		ImageView playerImage = player.getImageView();

		// Calcola la posizione iniziale del colpo in base all’immagine del player
		double colpoX = playerImage.getX() + playerImage.getFitWidth()/3;
		double colpoY = playerImage.getY() + playerImage.getFitHeight() / 2;

		// Crea un nuovo colpo verso destra
		ColpoPlayer colpo = new ColpoPlayer(colpoX, colpoY, Player.Direzione.DESTRA);

		playerBullets.add(colpo);            // Aggiunge alla lista dei colpi player
		root.getChildren().add(colpo.getNode());  // Aggiunge il nodo visivo

		// Timer dedicato per animare il colpo (muoverlo ogni frame)
		AnimationTimer colpoTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				colpo.update();  // Aggiorna la posizione del colpo
				if (colpo.getX() > larghezzaSchermo) {  // Se esce fuori dallo schermo a destra
					root.getChildren().remove(colpo.getNode()); // Rimuovi dal root
					playerBullets.remove(colpo);                 // Rimuovi dalla lista colpi player
					stop();                                      // Ferma questo timer
				}
			}
		};
		colpoTimer.start();  // Avvia il timer del colpo
	}

	// Metodo per passare al livello successivo quando tutti i nemici sono stati eliminati
	private void passaggioAlTerzoLivello() {
		heal(50);          // Cura il giocatore di 50 punti vita
		cleanup();         // Pulisce la scena e le liste di gioco
		timer.stop();      // Ferma il timer principale del livello

		// Crea una nuova scena del terzo livello e la imposta come root dello stage
		TerzoLivello terzoLivello = new TerzoLivello(primaryStage);
		primaryStage.getScene().setRoot(terzoLivello);
	}

	// Metodo per pulire tutti i nodi e liste quando si cambia livello o si resetta
	private void cleanup() {
		root.getChildren().clear();  // Rimuove tutti i nodi dal root
		enemyBullets.clear();        // Svuota lista colpi nemici
		playerBullets.clear();       // Svuota lista colpi player
		enemies.clear();             // Svuota lista nemici
		if (timer != null) {
			timer.stop();            // Ferma il timer del gioco
		}
	}

}
