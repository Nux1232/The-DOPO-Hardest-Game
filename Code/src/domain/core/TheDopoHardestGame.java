package domain.core;

import domain.entities.Player;
import domain.entities.factory.GameModeFactory;
import domain.entities.strategy.GameModeStrategy;
import domain.level.GameConfiguration;
import domain.level.Level;
import domain.save.memento.GameCaretaker;
import domain.save.memento.GameMemento;
import presentation.ui.observer.GameObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase principal del juego: The DOPO Hardest Game.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 07/05/2026
 */

public class TheDopoHardestGame {
    private final GameEngine engine;
    private final GameCaretaker caretaker;
    private final List<GameConfiguration> configurations;
    private int currentConfigurationIndex;
    private File currentLevelFile;

    /**
     * El constructor de la fachada.
     */
    public TheDopoHardestGame() {
        this.engine = GameEngine.getInstance();
        this.caretaker = new GameCaretaker();
        this.configurations = new ArrayList<>();
        this.currentConfigurationIndex = 0;
    } // Cierre del constructor

    public void startGame(GameConfiguration configuration) {
        Level level = configuration.buildLevel(currentConfigurationIndex + 1);
        engine.loadLevel(level);
        engine.startGame();
    } // Cierre del método

    public void pauseGame() {
        engine.pauseGame();
    } // Cierre del método

    public void resumeGame() {
        engine.resumeGame();
    } // Cierre del método

    public void endGame() {
        engine.stopGame();
        engine.returnToMenu();
    } // Cierre del método

    public void nextLevel(GameConfiguration nextConfiguration) {
        currentConfigurationIndex++;
        engine.getPlayers().forEach(Player::resetCoins);
        startGame(nextConfiguration);
    } // Cierre del método

    public void addPlayer(Player player) {
        engine.getPlayers().add(player);
    } // Cierre del método

    public void movePlayer(Player player, String direction) {
        engine.movePlayer(player, direction);
    } // Cierre del método

    public void setGameMode(String mode) {
        engine.setGameMode(mode);
    } // Cierre del método

    public void saveGame(File currentLevel) {
        engine.setCurrentLevelFile(currentLevel);
        GameMemento memento = engine.createMemento();
        caretaker.saveMemento(memento);
    } // Cierre del método

    public void loadLevel(File configuration) throws Exception {
        GameMemento memento = caretaker.load(configuration);
        if(memento != null) {
            engine.restoreMemento(memento);
        }
    } // Cierre del método

    public void addObserver(GameObserver observer) {
        engine.addObserver(observer);
    } // Cierre del método

    public List<Player> getPlayers() {
        return engine.getPlayers();
    } // Cierre del método

    public Level getCurrentLevel() {
        return engine.getCurrentLevel();
    } // Cierre del método

    public int getRemainingTime() {
        return engine.getRemainingTime();
    } // Cierre del método

    public GameState getGameState() {
        return engine.getCurrentState();
    } // Cierre del método

    public boolean isGamePaused() {
        return engine.getCurrentState() == GameState.PAUSED;
    } // Cierre del método

    public boolean isGameWon() {
        return engine.getCurrentState() == GameState.VICTORY;
    } // Cierre del método

    public int getCollectedCoins() {
        return engine.getCollectedCoinsCount();
    } // Cierre del método

    public int getTotalCoins() {
        return engine.getTotalCoinsCount();
    } // Cierre del método

    public Player getWinner() {
        return engine.getWinner();
    } // Cierre del método

    public void clearPlayers() {
        engine.getPlayers().clear();
    }
} // Cierre de la clase
