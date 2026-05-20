package domain.core;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domain.entities.*;
import domain.level.Level;
import domain.entities.factory.GameModeFactory;
import domain.entities.strategy.GameModeStrategy;
import domain.save.memento.GameMemento;
import java.awt.geom.Rectangle2D;

/**
 * Clase que representa el motor del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
 */

public class GameEngine implements Runnable {
    private static GameEngine instance;
    private Thread gameThread;
    private volatile boolean running;
    private volatile domain.core.GameState currentState;
    private final int FPS = 60;
    private final long TARGET_TIME = 1000000000 / FPS;

    private Level currentLevel;
    private List<Player> players = new ArrayList<>();
    private List<GameObserver> observers = new ArrayList<>();
    private GameModeStrategy gameMode = GameModeFactory.createGameMode("Player");
    private int remainingTime = 180;
    private File currentLevelFile;

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

    public void setGameMode(String mode) {
        this.gameMode = GameModeFactory.createGameMode(mode);
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
     * Método que permite pausar el juego.
     */
    public void pauseGame() {
        if (currentState == GameState.PLAYING) {
            currentState = GameState.PAUSED;
        }
    } // Cierre del método

    /**
     * Método que permite reanudar el juego.
     */
    public void resumeGame() {
        if (currentState == GameState.PAUSED) {
            currentState = GameState.PLAYING;
        }
    } // Cierre del método

    /**
     * Método que pausa el juego donde solo un hilo lo ejecute a la vez.
     */
    public synchronized void stopGame() {
        running = false;
    } // Cierre del método

    /**
     * Método que permite regresar al menu del juego.
     */
    public synchronized void returnToMenu() {
        running = false;
        currentState = GameState.MENU;
        currentLevel = null;
        players.clear();

        if (gameThread != null && gameThread != Thread.currentThread()) {
            try {
                gameThread.join(500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
        gameThread = null;
    } // Cierre del método

    /**
     * Método que ejecuta el juego.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long lastSecondTime = System.nanoTime();
        long delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime);
            lastTime = now;

            boolean updated = false;
            while (delta >= TARGET_TIME) {
                update();
                delta -= TARGET_TIME;
                updated = true;
            }

            if (updated) {
                notifyObservers();
            }

            if (now - lastSecondTime >= 1_000_000_000L) {
                advanceGameClockOneSecond();
                lastSecondTime += 1_000_000_000L;
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
        List<Rectangle> walls = currentLevel.getWalls();
        for (Enemy e : currentLevel.getEnemies()) {
            if (e.isAlive()) {
                e.update(walls);
            }
        }
        checkCollisions();
        checkPlayersCollisions();
        checkCoinCollection();
        checkLifeSourceCollection();
        checkIntermediateZone();
        checkLevelCompletion();
    } // Cierre del método

    /**
     * Método que avanza el reloj del juego un segundo.
     */
    public void advanceGameClockOneSecond() {
        if (currentState != GameState.PLAYING) return;
        if (remainingTime > 0) {
            remainingTime--;
        }
        if (remainingTime <= 0) {
            remainingTime = 0;
            currentState = GameState.GAME_OVER;
            running = false;
        }
    } // Cierre del método

    /**
     * Método que permite mover al jugador a una direccion deseada.
     * @param p El jugador.
     * @param direction La direccion donde quiere ir.
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
            case "W": newY -= speed; break;
            case "S": newY += speed; break;
            case "A": newX -= speed; break;
            case "D": newX += speed; break;
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
                if (!e.isAlive()) continue;
                if (pRect.intersects(e.getX() - 7.5, e.getY(), 15, 15)) {
                    p.handleHit();
                    break;
                }
            }
        }

        for(Bomb bomb : currentLevel.getBombs()) {
            if(!bomb.isActive()) continue;

            for(Player p : players) {
                Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(), 20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
                if(pRect.intersects(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight())) {
                    bomb.onPlayerContact(p);
                }
            }
            Iterator<Enemy> enemyIterator = currentLevel.getEnemies().iterator();
            while(enemyIterator.hasNext()) {
                Enemy e = enemyIterator.next();
                if(!e.isAlive()) continue;
                Rectangle2D.Double eRect = new Rectangle2D.Double(e.getX(), e.getY(), 20, 20);
                if(eRect.intersects(bomb.getX(), bomb.getY(), bomb.getWidth(), bomb.getHeight())) {
                    bomb.onEnemyContact(e);
                    enemyIterator.remove();
                    break;
                }
            }
        }
    } // Cierre del método

    private void checkPlayersCollisions() {
        for(int i = 0; i < players.size(); i++) {
            for(int j = i + 1; j < players.size(); j++) {
                Player firstPlayer = players.get(i);
                Player secondPlayer = players.get(j);
                Rectangle2D.Double firstRectangle = new Rectangle2D.Double(firstPlayer.getX(), firstPlayer.getY(),
                        20 * firstPlayer.getSizeMultiplier(), 20 * firstPlayer.getSizeMultiplier());
                Rectangle2D.Double secondRectangle = new Rectangle2D.Double(secondPlayer.getX(), secondPlayer.getY(),
                        20 * secondPlayer.getSizeMultiplier(), 20 * secondPlayer.getSizeMultiplier());
                if(firstRectangle.intersects(secondRectangle)) {
                    firstPlayer.handleHit();
                    secondPlayer.handleHit();
                }
            }
        }
    } // Cierre del método

    /**
     * Método privado que verifica si el jugador obtuvo una moneda.
     */
    private void checkCoinCollection() {
        if(gameMode.independentGameModeCoins()) {
            checkCoinCollectionPvPGameMode();
        } else {
            checkCoinCollectionSinglePlayerGameMode();
        }
    } // Cierre del método

    /**
     * Método privado que verifica si el jugador obtuvo una fuente de vida.
     */
    private void checkLifeSourceCollection() {
        List<LifeSource> lifeSources = currentLevel.getLifeSources();
        if (lifeSources == null) return;

        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(),
                    20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (LifeSource ls : lifeSources) {
                if (!ls.isCollected() && pRect.intersects(ls.getX(), ls.getY(), 20, 20)) {
                    ls.collect();
                    p.addExtraLife();
                }
            }
        }
    } // Cierre del método

    /**
     * Método privado que verifica si el jugador obtuvo una moneda
     * en el modo solitario.
     */
    private void checkCoinCollectionSinglePlayerGameMode() {
        List<Coin> bagOfCoins = currentLevel.getCoins();

        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(),
                    20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (int i = 0; i < bagOfCoins.size(); i++) {
                Coin coin = bagOfCoins.get(i);
                if (!coin.isCollected() && pRect.intersects(coin.getX(), coin.getY(), 10, 10)) {
                    p.collectCoin(i);
                    coin.collect();
                    handleCoinEffect(p, coin);
                }
            }
        }
    } // Cierre del método

    /**
     * Método privado que verifica si algún jugador obtuvo una moneda
     * en el modo pvp.
     */
    private void checkCoinCollectionPvPGameMode() {
        List<Coin> bagOfCoins = currentLevel.getCoins();

        for (Player p : players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(p.getX(), p.getY(),
                    20 * p.getSizeMultiplier(), 20 * p.getSizeMultiplier());
            for (int i = 0; i < bagOfCoins.size(); i++) {
                Coin coin = bagOfCoins.get(i);
                if (!p.hasCollectedCoin(i) && pRect.intersects(coin.getX(), coin.getY(), 10, 10)) {
                    p.collectCoin(i);
                    handleCoinEffect(p, coin);
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
        if (type.equals("RED")) p.applySkin("ROJO", 1.25, 1.0);
        else if (type.equals("BLUE")) p.applySkin("AZUL", 1.75, 1.5);
        else if (type.equals("GREEN")) p.applySkin("VERDE", 1.25, 1.0);
    } // Cierre del método

    private void checkLevelCompletion() {
        gameMode.checkLevelCompletion(players, currentLevel);

        if(gameMode.checkWinCondition(players, getCollectedCoinsCount(), getTotalCoinsCount())) {
            currentState = GameState.VICTORY;
        }
    } // Cierre del método

    /**
     * Método privado que verifica que el jugador reaparezca
     * en la zona intermedia.
     */
    private void checkIntermediateZone() {
        Point intermediateZone = currentLevel.getIntermediateSafeZone();
        if(intermediateZone == null) return;
        for(Player player: players) {
            Rectangle2D.Double pRect = new Rectangle2D.Double(player.getX(), player.getY(),
                    20 * player.getSizeMultiplier(), 20 * player.getSizeMultiplier());
            if(pRect.intersects(intermediateZone.x, intermediateZone.y, 60, 60)) {
                player.setRespawnPoint(intermediateZone.x, intermediateZone.y);
            }
        }
    } // Cierre del método

    /**
     * Método que obtiene el ganador del juego.
     *
     * @return Player El ganador del juego.
     */
    public Player getWinner() {
        return gameMode.getWinner(players);
    } // Cierre del método

    // Método pendiente por revisar
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
        this.remainingTime = level.getTimeLimit();
        gameMode.setUp(players);

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            Point spawnPoint = (i == 1 && level.getFinalSafeZone() != null)
                    ? level.getFinalSafeZone() : level.getStartPoint();
            if(spawnPoint != null) {
                p.setRespawnPoint(spawnPoint.getX(), spawnPoint.getY());
                p.resetPosition(spawnPoint.getX(), spawnPoint.getY());
            }
        }
    } // Cierre del método

    public GameMemento createMemento() {
        Player player = players.isEmpty() ? null : players.get(0);
        Player secondPlayer = players.size() > 1 ? players.get(1) : null;

        List<GameMemento.PlayerSnapshot> playerSnapshots = new ArrayList<>();

        for (Player currentPlayer : players) {
            playerSnapshots.add(new GameMemento.PlayerSnapshot(
                    currentPlayer.getColor(),
                    currentPlayer.getBorderColor(),
                    currentPlayer.getX(),
                    currentPlayer.getY(),
                    currentPlayer.getCurrentSpeed(),
                    currentPlayer.getSizeMultiplier(),
                    currentPlayer.getRespawnX(),
                    currentPlayer.getRespawnY(),
                    currentPlayer.getDeaths(),
                    currentPlayer.getCollectedCoinIndexes(),
                    currentPlayer.hasShield(),
                    currentPlayer.isInvincible(),
                    currentPlayer.getInvincibilityTimer()));
            }

        return new GameMemento(
                getCurrentModeName(),
                player != null ? player.getColor() : "RED",
                player != null ? player.getBorderColor() : Color.BLACK,
                secondPlayer != null ? secondPlayer.getColor() : "RED",
                secondPlayer != null ? secondPlayer.getBorderColor() : Color.BLACK,
                currentLevelFile,
                remainingTime,
                playerSnapshots);
    } // Cierre del método

    public void restoreMemento(GameMemento memento) {
        setGameMode(memento.getMode());
        this.currentLevelFile = memento.getLevelFile();

        if (memento.getRemainingTime() >= 0) {
            this.remainingTime = memento.getRemainingTime();
        }

        List<GameMemento.PlayerSnapshot> snapshots = memento.getPlayerSnapshots();

        if(!snapshots.isEmpty()) {
            int amountPlayers = Math.min(players.size(), snapshots.size());

            for (int i = 0; i < amountPlayers; i++) {
                Player player = players.get(i);
                GameMemento.PlayerSnapshot snapshot = snapshots.get(i);

                player.restoreSavedState(
                        snapshot.getSkin(),
                        snapshot.getBorderColor(),
                        snapshot.getX(),
                        snapshot.getY(),
                        snapshot.getCurrentSpeed(),
                        snapshot.getSizeMultiplier(),
                        snapshot.getRespawnX(),
                        snapshot.getRespawnY(),
                        snapshot.getDeaths(),
                        snapshot.getCollectedCoins(),
                        snapshot.hasShield(),
                        snapshot.isInvincible(),
                        snapshot.getInvincibilityTimer());
            }

            restoreCollectedCoinsOnLevel();
            gameMode.setUp(players);
            return;
        }

        if(!players.isEmpty()) {
            Player player = players.get(0);

            player.applySkin(
                    memento.getSkin(),
                    player.getCurrentSpeed(),
                    player.getSizeMultiplier());

            player.setBorderColor(memento.getBorderColor());
        }

        if(players.size() > 1) {
            Player secondPlayer = players.get(1);

            secondPlayer.applySkin(
                    memento.getSecondSkin(),
                    secondPlayer.getCurrentSpeed(),
                    secondPlayer.getSizeMultiplier());

            secondPlayer.setBorderColor(memento.getSecondBorderColor());
        }

        gameMode.setUp(players);
    }

    private void restoreCollectedCoinsOnLevel() {
        if(currentLevel == null || players.isEmpty()) {
            return;
        }

        if(gameMode.independentGameModeCoins()) {
            return;
        }

        Player player = players.get(0);

        for(Integer coinIndex : player.getCollectedCoinIndexes()) {
            if(coinIndex >= 0 && coinIndex < currentLevel.getCoins().size()) {
                currentLevel.getCoins().get(coinIndex).collect();
            }
        }
    }

    private String getCurrentModeName() {
        return gameMode.getClass().getSimpleName().equals("PlayerVsPlayer")
                ? "Player vs Player"
                : "Player";
    }

    public void setCurrentLevelFile(File currentLevel) {
        this.currentLevelFile = currentLevel;
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

    public int getCollectedCoinsCount() {
        if (currentLevel == null) return 0;
        int collectedCoins = 0;
        for (Coin c : currentLevel.getCoins()) {
            if (c.isCollected()) collectedCoins++;
        }
        return collectedCoins;
    } // Cierre del método

    public int getTotalCoinsCount() {
        if (currentLevel == null) return 0;
        return currentLevel.getCoins().size();
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
