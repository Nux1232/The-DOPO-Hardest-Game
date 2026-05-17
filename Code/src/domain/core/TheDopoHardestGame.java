package domain.core;

import domain.entities.Player;
import domain.entities.factory.GameModeFactory;
import domain.entities.strategy.GameModeStrategy;
import domain.exceptions.TheDopoHardestGameException;
import domain.level.GameConfiguration;
import domain.level.Level;
import domain.save.memento.GameCaretaker;
import domain.save.memento.GameMemento;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase principal del juego en la capa de dominio.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
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

    /**
     * Método que permite iniciar el juego.
     * @param configuration El nivel que se va a jugar.
     * @throws TheDopoHardestGameException La excepción a lanzar.
     */
    public void startGame(GameConfiguration configuration) throws TheDopoHardestGameException {
        startGame(configuration, currentLevelFile);
    } // Cierre del método

    /**
     * Método que permite iniciar el juego.
     * @param configuration El nivel que se va a jugar.
     * @param levelFile El archivo del nivel que se va a jugar.
     * @throws TheDopoHardestGameException La excepción a lanzar.
     */
    public void startGame(GameConfiguration configuration, File levelFile)
            throws TheDopoHardestGameException {
        startGame(configuration, levelFile, null);
    } // Cierre del método

    /**
     * Método que permite iniciar el juego.
     *
     * @param configuration El nivel que se va a jugar.
     * @param levelFile El archivo del nivel que se va a jugar.
     * @param memento El estado del juego.
     * @throws TheDopoHardestGameException La excepción a lanzar si no hay jugadores.
     */
    public void startGame(GameConfiguration configuration, File levelFile, GameMemento memento)
            throws TheDopoHardestGameException {

        if(engine.getPlayers().isEmpty()) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.NO_PLAYERS);
        }

        engine.stopGame();

        this.currentLevelFile = levelFile;
        engine.setCurrentLevelFile(levelFile);

        if(memento != null) {
            engine.setGameMode(memento.getMode());
        }

        Level level = configuration.buildLevel(currentConfigurationIndex + 1);

        if(level == null) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.LEVEL_LOAD_ERROR);
        }

        engine.loadLevel(level);

        if(memento != null) {
            engine.restoreMemento(memento);
        }

        engine.startGame();
    } // Cierre del método

    /**
     * Método que permite pausar el juego.
     */
    public void pauseGame() {
        engine.pauseGame();
    } // Cierre del método

    /**
     * Método que permite reanudar el juego.
     */
    public void resumeGame() {
        engine.resumeGame();
    } // Cierre del método

    /**
     * Método que permite guardar el estado del juego.
     *
     * @param saveFile El nivel a ser guardado.
     */
    public void saveGame(File saveFile) throws TheDopoHardestGameException {
        if(saveFile == null || currentLevelFile == null) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.SAVE_GAME_ERROR);
        }

        engine.setCurrentLevelFile(currentLevelFile);

        GameMemento memento = engine.createMemento();

        try {
            caretaker.save(saveFile, memento);
            caretaker.saveMemento(memento);
        } catch (IOException exception) {
            throw new TheDopoHardestGameException(
                    TheDopoHardestGameException.SAVE_GAME_ERROR
                            + ": "
                            + exception.getMessage());
        }
    }// Cierre del método

    /**
     * Método que permite finalizar el juego.
     */
    public void endGame() {
        engine.stopGame();
        engine.returnToMenu();
    } // Cierre del método

    /**
     * Método que permite añadir un jugador al juego.
     *
     * @param player El jugador a añadir.
     */
    public void addPlayer(Player player) throws TheDopoHardestGameException {
        if(player == null) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.NO_PLAYERS);
        }
        engine.getPlayers().add(player);
    } // Cierre del método

    /**
     * Método que permite mover un jugador a cualquier dirección.
     *
     * @param player El jugador que se va a mover.
     * @param direction La dirección donde quiere ir.
     */
    public void movePlayer(Player player, String direction) {
        engine.movePlayer(player, direction);
    } // Cierre del método

    /**
     * Método que permite eliminar todos los jugadores del juego.
     */
    public void clearPlayers() {
        engine.getPlayers().clear();
    } // Cierre del método

    /**
     * Método que permite cambiar el modo del juego.
     *
     * @param mode El modo que se va a cambiar: SinglePlayer y PlayerVsPlayer por el momento.
     */
    public void setGameMode(String mode) throws TheDopoHardestGameException {
        if(mode == null) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.INVALID_GAMEMODE);
        }
        engine.setGameMode(mode);
    } // Cierre del método

    /**
     * Método que permite cargar un nivel guardado.
     *
     * @param saveFile El nivel guardado que se va a jugar.
     * @throws Exception La excepción que se lanza por si no se encuentra el archivo.
     */
    public GameMemento loadLevel(File saveFile)
            throws TheDopoHardestGameException, IOException, ClassNotFoundException {

        if(saveFile == null) {
            throw new TheDopoHardestGameException(
                    TheDopoHardestGameException.LOAD_GAME_ERROR);
        }

        try {
            GameMemento memento = caretaker.load(saveFile);
            caretaker.saveMemento(memento);
            return memento;

        } catch (IOException | ClassNotFoundException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new TheDopoHardestGameException(
                    TheDopoHardestGameException.LOAD_GAME_ERROR
                            + ": "
                            + exception.getMessage());
        }
    } // Cierre del método

    /**
     * Método que permite añadir un observador al juego.
     *
     * @param observer El observador que sólo verá el juego.
     */
    public void addObserver(GameObserver observer) {
        engine.addObserver(observer);
    } // Cierre del método

    /**
     * Método que obtiene la cantidad de jugadores.
     *
     * @return List<Player> La lista de jugadores en el juego.
     */
    public List<Player> getPlayers() {
        return engine.getPlayers();
    } // Cierre del método

    /**
     * Método que obtiene el nivel actual.
     *
     * @return Level El nivel actual.
     */
    public Level getCurrentLevel() {
        return engine.getCurrentLevel();
    } // Cierre del método

    /**
     * Método que obtiene el tiempo restante del nivel.
     *
     * @return int El tiempo restante del nivel (180s).
     */
    public int getRemainingTime() {
        return engine.getRemainingTime();
    } // Cierre del método

    /**
     * Método que obtiene el estado del juego.
     *
     * @return GameState El estado del juego.
     */
    public GameState getGameState() {
        return engine.getCurrentState();
    } // Cierre del método

    /**
     * Método que obtiene la cantidad de monedas
     * recogidas por el jugador.
     *
     * @return int La cantidad de monedas obtenidas por el jugador.
     */
    public int getCollectedCoins() {
        return engine.getCollectedCoinsCount();
    } // Cierre del método

    /**
     * Método que obtiene la cantidad total de monedas.
     *
     * @return int La cantidad total de monedas.
     */
    public int getTotalCoins() {
        return engine.getTotalCoinsCount();
    } // Cierre del método

    /**
     * Método que obtiene el ganador del juego.
     *
     * @return Player El ganador del juego.
     */
    public Player getWinner() {
        return engine.getWinner();
    } // Cierre del método

} // Cierre de la clase
