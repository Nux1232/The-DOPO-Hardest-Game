package main.Core;

import java.util.ArrayList;
import java.util.List;
import main.Entities.Player;
import main.Level.Level;
import main.UI.Observer.GameObserver;

/**
 * GameEngine - Implementación del motor principal del juego.
 * Gestiona el bucle de 60 FPS, el estado del juego, el tiempo y los observadores.
 */
public class GameEngine implements Runnable {
    private static GameEngine instance;
    
    private Thread gameThread;
    private boolean running;
    private GameState currentState;
    
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000 / FPS;

    // Estado de la partida
    private Level currentLevel;
    private List<Player> players;
    private List<GameObserver> observers;
    
    private int remainingTime; // en segundos
    private long lastSecondTime;

    private GameEngine() {
        this.currentState = GameState.MENU;
        this.players = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    // --- Gestión de Observadores ---
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.update();
        }
    }

    // --- Control del Hilo ---

    public synchronized void startGame() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stopGame() {
        running = false;
        // En una implementación real, esperaríamos a que el hilo termine.
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        lastSecondTime = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            long delta = now - lastTime;

            if (delta >= TARGET_TIME) {
                update();
                render();
                lastTime = now;
            }

            // Gestión del cronómetro (1 segundo)
            if (System.currentTimeMillis() - lastSecondTime >= 1000) {
                if (currentState == GameState.PLAYING && remainingTime > 0) {
                    remainingTime--;
                    if (remainingTime <= 0) {
                        handleTimeOut();
                    }
                }
                lastSecondTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        if (currentState == GameState.PLAYING) {
            // 1. Actualizar jugadores
            // 2. Actualizar enemigos del currentLevel
            // 3. Verificar colisiones y recolección de monedas
            // 4. Verificar condición de victoria o muerte
        }
    }

    private void render() {
        // Notificar a la UI que debe redibujarse
        notifyObservers();
    }

    private void handleTimeOut() {
        // Lógica cuando se acaba el tiempo de 3 minutos
        System.out.println("¡Tiempo agotado!");
        currentState = GameState.GAME_OVER;
    }

    // --- Métodos de Control de Juego ---

    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.remainingTime = 180; // 3 minutos por defecto
        this.currentState = GameState.PLAYING;
    }

    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
        }
    }

    public void resumeGame() {
        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
        }
    }

    // Getters
    public GameState getCurrentState() { return currentState; }
    public int getRemainingTime() { return remainingTime; }
    public List<Player> getPlayers() { return players; }
}
