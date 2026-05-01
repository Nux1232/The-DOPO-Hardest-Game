package main.Core;

import java.util.ArrayList;
import java.util.List;
import main.Entities.Player;
import main.Entities.Enemy;
import main.Entities.Coin;
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
    private List<Player> players = new ArrayList<>();
    private List<GameObserver> observers = new ArrayList<>();
    private int remainingTime = 180;
    private long lastSecondTime;

    private GameEngine() { this.currentState = GameState.MENU; }

    public static GameEngine getInstance() {
        if (instance == null) instance = new GameEngine();
        return instance;
    }

    public void addObserver(GameObserver observer) { observers.add(observer); }

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
            if (now - lastTime >= TARGET_TIME) {
                update();
                notifyObservers();
                lastTime = now;
            }

            if (System.currentTimeMillis() - lastSecondTime >= 1000) {
                if (currentState == GameState.PLAYING && remainingTime > 0) {
                    remainingTime--;
                }
                lastSecondTime = System.currentTimeMillis();
            }

            try { Thread.sleep(1); } catch (Exception e) {}
        }
    }

    private void update() {
        if (currentState != GameState.PLAYING || currentLevel == null) return;

        for (Enemy e : currentLevel.getEnemies()) e.update();
        checkCollisions();
        checkCoinCollection();
    }

    private void checkCollisions() {
        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(), 20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (Enemy e : currentLevel.getEnemies()) {
                if (pRect.intersects(e.getX(), e.getY(), 15, 15)) {
                    p.handleHit();
                    break;
                }
            }
        }
    }

    private void checkCoinCollection() {
        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(), 20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (Coin c : currentLevel.getCoins()) {
                if (!c.isCollected() && pRect.intersects(c.getX(), c.getY(), 10, 10)) {
                    c.collect();
                    handleCoinEffect(p, c);
                }
            }
        }
    }

    private void handleCoinEffect(Player p, Coin c) {
        String type = c.getType().toUpperCase();
        if (type.equals("RED")) p.applySkin("ROJO", 1.0, 1.0);
        else if (type.equals("BLUE")) p.applySkin("AZUL", 1.5, 1.5);
        else if (type.equals("GREEN")) p.applySkin("VERDE", 1.0, 1.0);
    }

    private void notifyObservers() {
        for (GameObserver o : new ArrayList<>(observers)) o.update();
    }

    public void loadLevel(Level level) {
        this.currentLevel = level;
        this.currentState = GameState.PLAYING;
        this.remainingTime = 180;
        for (Player p : players) {
            if (level.getStartPoint() != null) {
                p.setRespawnPoint(level.getStartPoint().getX(), level.getStartPoint().getY());
                p.resetPosition(level.getStartPoint().getX(), level.getStartPoint().getY());
            }
        }
    }

    public List<Player> getPlayers() { return players; }
    public Level getCurrentLevel() { return currentLevel; }
    public int getRemainingTime() { return remainingTime; }
    public GameState getCurrentState() { return currentState; }
}
