package main.Core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import main.Entities.Player;
import main.Entities.Enemy;
import main.Entities.Coin;
import main.Level.Level;
import main.UI.Observer.GameObserver;
import java.awt.geom.Rectangle2D;

/**
 * Clase que representa el motor del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

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

    /**
     * El constructor privado de la clase GameEngine.
     */
    private GameEngine() {
        this.currentState = GameState.MENU;
    } // Cierre del constructor

    /**
     * El constructor que instancia la clase GameEngine.
     *
     * @return GameEngine La instancia de la clase GameEngine.
     */
    public static GameEngine getInstance() {
        if (instance == null) instance = new GameEngine();
        return instance;
    } // Cierre del constructor

    /**
     * Método que añade un observador al juego por si este murio.
     *
     * @param observer El observador a añadir.
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    } // Cierre del método

    /**
     * Método que inicia el juego de modo que solo un hilo lo ejecute a la vez.
     */
    public synchronized void startGame() {
        if (running) return;
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    } // Cierre del método

    /**
     * Método que pausa el juego donde solo un hilo lo ejecute a la vez.
     */
    public synchronized void stopGame() {
        running = false;
    } // Cierre del método

    /**
     * Método que ejecuta el juego.
     */
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
    } // Cierre del método

    /**
     * Método privado que actualiza el estado del juego.
     */
    private void update() {
        if (currentState != GameState.PLAYING || currentLevel == null) return;

        for(Player p: players) p.update();
        for (Enemy e : currentLevel.getEnemies()) e.update();
        checkCollisions();
        checkCoinCollection();
    } // Cierre del método

    /**
     * Método que permite mover al jugador a una direccion deseada.
     * @param p
     * @param direction
     */
    public void movePlayer(Player p, String direction) {
        double newX = p.getX();
        double newY = p.getY();
        double speed = p.getCurrentSpeed();
        int size = (int)(20 * p.getSizeMultiplier());

        switch (direction.toUpperCase()) {
            case "UP":    newY -= speed; break;
            case "DOWN":  newY += speed; break;
            case "LEFT":  newX -= speed; break;
            case "RIGHT": newX += speed; break;
        }
        boolean blocked = collidesWithWall(newX, newY, size);
        if (!blocked) {
            p.move(direction);
        }
    } // Cierre del método

    /**
     * Método privado que comprueba si el jugador esta colisionando con una pared.
     *
     * @param x La coordenada x del jugador.
     * @param y La coordenada y del jugador.
     * @param size El tamaño del jugador.
     * @return boolean Verifica si esta haciendo colision o no.
     */
    private boolean collidesWithWall(double x, double y, int size) {
        if(currentLevel == null) return false;
        Rectangle2D.Double playerRect = new Rectangle2D.Double(x, y, size, size);
        for(Rectangle wall : currentLevel.getWalls()) {
            if (playerRect.intersects(wall)) return true;
        }
        return false;
    } // Cierre del método

    /**
     * Método privado que comprueba si el jugador esta colisionando con un enemigo.
     */
    private void checkCollisions() {
        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(), 20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (Enemy e : currentLevel.getEnemies()) {
                if (pRect.intersects(e.getX() - 7.5, e.getY(), 15, 15)) {
                    p.handleHit();
                    break;
                }
            }
        }
    } // Cierre del método

    /**
     * Método privado que verifica si el jugador obtuvo una moneda.
     */
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
    } // Cierre del método

    /**
     * Método privado que aplica el efecto de la moneda al jugador.
     * @param p El jugador que recibe el efecto.
     * @param c La moneda que contiene el efecto.
     */
    private void handleCoinEffect(Player p, Coin c) {
        String type = c.getType().toUpperCase();
        if (type.equals("RED")) p.applySkin("ROJO", 1.0, 1.0);
        else if (type.equals("BLUE")) p.applySkin("AZUL", 1.5, 1.5);
        else if (type.equals("GREEN")) p.applySkin("VERDE", 1.0, 1.0);
    } // Cierre del método

    /**
     * Método privado que notifica a los observadores del estado del juego.
     */
    private void notifyObservers() {
        for (GameObserver o : new ArrayList<>(observers)) o.update();
    } // Cierre del método

    /**
     * Método que carga un nivel en el juego.
     *
     * @param level El nivel a cargar.
     */
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
    } // Cierre del método

    /**
     * Método que devuelve la lista de jugadores.
     *
     * @return List<Player> La lista de jugadores.
     */
    public List<Player> getPlayers() {
        return players;
    } // Cierre del método

    /**
     * Método que devuelve el nivel actual del juego.
     * @return
     */
    public Level getCurrentLevel() {
        return currentLevel;
    } // Cierre del método

    /**
     * Método que devuelve el tiempo restante del nivel.
     *
     * @return int El tiempo restante del nivel.
     */
    public int getRemainingTime() {
        return remainingTime;
    } // Cierre del método

    /**
     * Método que devuelve el estado actual del juego.
     *
     * @return GameState El estado del juego.
     */

    public GameState getCurrentState() {
        return currentState;
    } // Cierre del método
} // Cierre de la clase
