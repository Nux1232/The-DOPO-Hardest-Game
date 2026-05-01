package main.Core;

import java.util.ArrayList;
import java.util.List;
import main.Entities.Player;
import main.Entities.Enemy;
import main.Level.Level;
import main.UI.Observer.GameObserver;
import java.awt.geom.Rectangle2D;

public class GameEngine implements Runnable {
    private static GameEngine instance;
    
    private Thread gameThread;
    private boolean running;
    private GameState currentState;
    
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000 / FPS;

    private Level currentLevel;
    private List<Player> players;
    private List<GameObserver> observers;
    
    private int remainingTime;
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

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        List<GameObserver> copy = new ArrayList<>(observers);
        for (GameObserver observer : copy) {
            observer.update();
        }
    }

    public synchronized void startGame() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stopGame() {
        running = false;
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

            if (System.currentTimeMillis() - lastSecondTime >= 1000) {
                if (currentState == GameState.PLAYING && remainingTime > 0) {
                    remainingTime--;
                }
                lastSecondTime = System.currentTimeMillis();
            }

            try { Thread.sleep(1); } catch (InterruptedException e) {}
        }
    }

    private void update() {
        if (currentState == GameState.PLAYING && currentLevel != null) {
            // 1. Actualizar enemigos
            for (Enemy e : currentLevel.getEnemies()) {
                e.update();
            }

            // 2. Verificar colisiones Jugador vs Enemigos
            checkCollisions();
        }
    }

    private void checkCollisions() {
        for (Player p : players) {
            double pSize = 20 * p.getSizeMultiplier();
            Rectangle2D.Double playerRect = new Rectangle2D.Double(p.getX(), p.getY(), pSize, pSize);

            for (Enemy e : currentLevel.getEnemies()) {
                // Los enemigos se dibujan de 15x15
                Rectangle2D.Double enemyRect = new Rectangle2D.Double(e.getX(), e.getY(), 15, 15);
                
                if (playerRect.intersects(enemyRect)) {
                    handlePlayerDeath(p);
                    break; 
                }
            }
        }
    }

    private void handlePlayerDeath(Player p) {
        p.incrementDeaths();
        System.out.println("¡Colisión! Muertes totales de " + p.getName() + ": " + p.getDeaths());
        if (currentLevel.getStartPoint() != null) {
            p.resetPosition(currentLevel.getStartPoint().getX(), currentLevel.getStartPoint().getY());
        }
    }

    private void render() {
        notifyObservers();
    }

    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.remainingTime = 180;
        this.currentState = GameState.PLAYING;
        
        if (level.getStartPoint() != null) {
            for (Player p : players) {
                p.resetPosition(level.getStartPoint().getX(), level.getStartPoint().getY());
            }
        }
    }

    public GameState getCurrentState() { return currentState; }
    public int getRemainingTime() { return remainingTime; }
    public List<Player> getPlayers() { return players; }
    public Level getCurrentLevel() { return currentLevel; }
}
