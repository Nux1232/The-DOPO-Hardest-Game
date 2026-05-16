package domain.core;

import domain.entities.Coin;
import domain.entities.Enemy;
import domain.entities.Player;
import domain.entities.factory.PlayerFactory;
import domain.entities.strategy.VerticalMovement;
import domain.exceptions.TheDopoHardestGameException;
import domain.level.GameConfiguration;
import domain.level.Level;
import domain.save.memento.GameMemento;
import presentation.ui.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Clase que permite realizar pruebas de unidad del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 10/05/2026
 */

public class GameEngineTest {
    private static int testsRun = 0;

    public static void main(String[] args) throws Exception {
        run("single player collects a coin once and receives its effect",
                GameEngineTest::singlePlayerCollectsCoinOnceAndReceivesEffect);
        run("player vs player collects coins independently",
                GameEngineTest::playerVsPlayerCollectsCoinsIndependently);
        run("clock reaches zero and changes the game to game over",
                GameEngineTest::clockReachesZeroAndChangesToGameOver);
        run("player cannot move through walls",
                GameEngineTest::playerCannotMoveThroughWalls);
        run("intermediate safe zone updates respawn point",
                GameEngineTest::intermediateSafeZoneUpdatesRespawnPoint);
        run("single player victory resets when a new level is loaded",
                GameEngineTest::singlePlayerVictoryResetsWhenNewLevelIsLoaded);
        run("player factory creates expected skins",
                GameEngineTest::playerFactoryCreatesExpectedSkins);
        run("game configuration builds a level from a file",
                GameEngineTest::gameConfigurationBuildsLevelFromFile);
        run("vertical movement uses configured speed and bounces on walls",
                GameEngineTest::verticalMovementUsesConfiguredSpeedAndBouncesOnWalls);
        run("HUD is painted inside the current panel size",
                GameEngineTest::hudIsPaintedInsideCurrentPanelSize);
        run("save game writes a memento file and load restores menu data",
                GameEngineTest::saveGameWritesMementoFileAndLoadRestoresData);
        run("save game restores player state respawn and remaining time",
                GameEngineTest::saveGameRestoresPlayerStateRespawnAndRemainingTime);

        System.out.println("OK - " + testsRun + " tests passed.");
    }

    private static void singlePlayerCollectsCoinOnceAndReceivesEffect() throws Exception {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Jugador 1", "ROJO");
        engine.getPlayers().add(player);

        Level level = basicLevel(20, 20, 30);
        Coin coin = new Coin(20, 20, "BLUE");
        level.addCoin(coin);

        engine.loadLevel(level);
        invokeUpdate(engine);
        invokeUpdate(engine);

        assertTrue(coin.isCollected(), "the coin should be marked as collected");
        assertEquals(1, player.getCollectedCoins(), "the player should collect the coin only once");
        assertEquals(1, engine.getCollectedCoinsCount(), "the engine should count the collected coin");
        assertEquals("AZUL", player.getColor(), "a blue coin should apply the blue skin");
        assertDoubleEquals(1.75, player.getCurrentSpeed(), "the blue skin should update speed");
    }

    private static void playerVsPlayerCollectsCoinsIndependently() throws Exception {
        GameEngine engine = resetEngine();
        engine.setGameMode("Player vs Player");
        Player firstPlayer = PlayerFactory.createPlayer("Jugador 1", "ROJO");
        Player secondPlayer = PlayerFactory.createPlayer("Jugador 2", "ROJO");
        engine.getPlayers().add(firstPlayer);
        engine.getPlayers().add(secondPlayer);

        Level level = basicLevel(20, 20, 60);
        level.addCoin(new Coin(20, 20, "GREEN"));
        engine.loadLevel(level);

        secondPlayer.resetPosition(200, 200);
        invokeUpdate(engine);
        firstPlayer.resetPosition(200, 200);
        secondPlayer.resetPosition(20, 20);
        invokeUpdate(engine);

        assertTrue(firstPlayer.hasCollectedCoin(0), "the first player should collect the coin");
        assertTrue(secondPlayer.hasCollectedCoin(0), "the second player should collect the same coin independently");
        assertEquals(1, firstPlayer.getCollectedCoins(), "first player coin count");
        assertEquals(1, secondPlayer.getCollectedCoins(), "second player coin count");
    }

    private static void clockReachesZeroAndChangesToGameOver() {
        GameEngine engine = resetEngine();
        engine.getPlayers().add(PlayerFactory.createPlayer("Jugador 1", "ROJO"));

        Level level = basicLevel(20, 20, 1);
        engine.loadLevel(level);
        engine.advanceGameClockOneSecond();

        assertEquals(0, engine.getRemainingTime(), "remaining time should not go below zero");
        assertEquals(GameState.GAME_OVER, engine.getCurrentState(), "the game should end when time expires");
    }

    private static void playerCannotMoveThroughWalls() {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Jugador 1", "ROJO");
        engine.getPlayers().add(player);

        Level level = basicLevel(20, 20, 30);
        level.addWall(new Rectangle(21, 20, 20, 20));
        engine.loadLevel(level);
        engine.movePlayer(player, "RIGHT");

        assertDoubleEquals(20, player.getX(), "wall collision should keep the player x position");
        assertDoubleEquals(20, player.getY(), "wall collision should keep the player y position");
    }

    private static void intermediateSafeZoneUpdatesRespawnPoint() throws Exception {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Jugador 1", "ROJO");
        engine.getPlayers().add(player);

        Level level = basicLevel(20, 20, 30);
        level.setIntermediateSafeZone(new Point(120, 120));
        engine.loadLevel(level);

        player.resetPosition(120, 120);
        invokeUpdate(engine);
        player.resetPosition(300, 300);
        player.handleHit();

        assertDoubleEquals(120, player.getX(), "player should respawn at the intermediate safe zone x");
        assertDoubleEquals(120, player.getY(), "player should respawn at the intermediate safe zone y");
        assertEquals(1, player.getDeaths(), "hitting the player should count one death");
    }

    private static void singlePlayerVictoryResetsWhenNewLevelIsLoaded() throws Exception {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Jugador 1", "ROJO");
        engine.getPlayers().add(player);

        Level completedLevel = basicLevel(20, 20, 30);
        completedLevel.addCoin(new Coin(20, 20, "YELLOW"));
        engine.loadLevel(completedLevel);
        invokeUpdate(engine);
        player.resetPosition(700, 500);
        invokeUpdate(engine);

        assertEquals(GameState.VICTORY, engine.getCurrentState(), "first level should be completed");

        Level nextLevel = basicLevel(40, 40, 30);
        engine.loadLevel(nextLevel);

        assertEquals(GameState.PLAYING, engine.getCurrentState(), "loading a new level should reset victory state");
    }

    private static void playerFactoryCreatesExpectedSkins() {
        Player red = PlayerFactory.createPlayer("Rojo", "BLINKY");
        Player blue = PlayerFactory.createPlayer("Azul", "INKY");
        Player green = PlayerFactory.createPlayer("Verde", "CLYDE");

        assertEquals("RED", red.getColor(), "Blinky should start red");
        assertEquals("BLUE", blue.getColor(), "Inky should start blue");
        assertEquals("VERDE", green.getColor(), "Clyde should start green");
        assertDoubleEquals(1.25, red.getCurrentSpeed(), "Blinky speed");
        assertDoubleEquals(1.75, blue.getCurrentSpeed(), "Inky speed");
    }

    private static void gameConfigurationBuildsLevelFromFile() throws Exception {
        File file = File.createTempFile("level-test", ".txt");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("TIME 45\n");
            writer.write("START 10 20\n");
            writer.write("INTERMEDIATE_ZONE 100 110\n");
            writer.write("FINAL_ZONE 700 500\n");
            writer.write("WALL 1 2 3 4\n");
            writer.write("COIN 30 40 RED\n");
            writer.write("ENEMY BASICO 50 60\n");
        }

        Level level = new GameConfiguration(file.getAbsolutePath()).buildLevel(7);

        assertEquals(7, level.getId(), "level id");
        assertEquals(45, level.getTimeLimit(), "time limit");
        assertEquals(new Point(10, 20), level.getStartPoint(), "start point");
        assertEquals(new Point(100, 110), level.getIntermediateSafeZone(), "intermediate zone");
        assertEquals(new Point(700, 500), level.getFinalSafeZone(), "final zone");
        assertEquals(1, level.getWalls().size(), "wall count");
        assertEquals(1, level.getCoins().size(), "coin count");
        assertEquals(1, level.getEnemies().size(), "enemy count");
    }

    private static void verticalMovementUsesConfiguredSpeedAndBouncesOnWalls() {
        Enemy enemy = new Enemy(10, 10);
        VerticalMovement movement = new VerticalMovement(2.0);

        movement.move(enemy, Collections.emptyList());
        assertDoubleEquals(12, enemy.getY(), "enemy should move by its configured speed");

        movement.move(enemy, Collections.singletonList(new Rectangle(10, 14, 20, 20)));
        assertDoubleEquals(12, enemy.getY(), "enemy should not enter a wall");

        movement.move(enemy, Collections.emptyList());
        assertDoubleEquals(10, enemy.getY(), "enemy should reverse direction after touching a wall");
    }

    private static void hudIsPaintedInsideCurrentPanelSize() throws TheDopoHardestGameException {
        GameEngine engine = resetEngine();
        TheDopoHardestGame game = new TheDopoHardestGame();
        game.clearPlayers();
        game.addPlayer(PlayerFactory.createPlayer("Jugador 1", "ROJO"));

        Level level = basicLevel(20, 20, 30);
        engine.loadLevel(level);

        GamePanel panel = new GamePanel(game);
        panel.setSize(300, 200);

        BufferedImage image = new BufferedImage(300, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        panel.paint(graphics);
        graphics.dispose();

        int background = new Color(173, 216, 230).getRGB();
        int hudPixel = image.getRGB(10, 10);
        assertTrue(hudPixel != background, "the HUD should be visible near the top-left margin");
    }

    private static void saveGameWritesMementoFileAndLoadRestoresData() throws Exception {
        resetEngine();
        TheDopoHardestGame game = new TheDopoHardestGame();
        game.clearPlayers();
        game.setGameMode("Player vs Player");

        Player firstPlayer = PlayerFactory.createPlayer("Jugador 1", "INKY");
        firstPlayer.setBorderColor(Color.MAGENTA);
        Player secondPlayer = PlayerFactory.createPlayer("Jugador 2", "CLYDE");
        secondPlayer.setBorderColor(Color.WHITE);
        game.addPlayer(firstPlayer);
        game.addPlayer(secondPlayer);

        File levelFile = File.createTempFile("level-save-test", ".txt");
        levelFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(levelFile)) {
            writer.write("TIME 45\n");
            writer.write("START 10 20\n");
            writer.write("FINAL_ZONE 700 500\n");
        }

        File saveFile = File.createTempFile("game-save-test", ".txt");
        saveFile.deleteOnExit();

        game.startGame(new GameConfiguration(levelFile.getAbsolutePath()), levelFile);
        game.saveGame(saveFile);
        game.endGame();

        GameMemento memento = game.loadGame(saveFile);

        assertTrue(saveFile.length() > 0, "save file should be written to disk");
        assertEquals("Player vs Player", memento.getMode(), "saved mode");
        assertEquals("BLUE", memento.getSkin(), "first player skin");
        assertEquals(Color.MAGENTA, memento.getBorderColor(), "first player border");
        assertEquals("VERDE", memento.getSecondSkin(), "second player skin");
        assertEquals(Color.WHITE, memento.getSecondBorderColor(), "second player border");
        assertEquals(levelFile.getAbsolutePath(), memento.getLevelFile().getAbsolutePath(), "saved level file");
    }

    private static void saveGameRestoresPlayerStateRespawnAndRemainingTime() throws Exception {
        GameEngine engine = resetEngine();
        TheDopoHardestGame game = new TheDopoHardestGame();
        game.clearPlayers();
        game.setGameMode("Player");

        Player player = PlayerFactory.createPlayer("Jugador 1", "BLINKY");
        player.setBorderColor(Color.YELLOW);
        game.addPlayer(player);

        File levelFile = File.createTempFile("level-state-save-test", ".txt");
        levelFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(levelFile)) {
            writer.write("TIME 45\n");
            writer.write("START 10 20\n");
            writer.write("INTERMEDIATE_ZONE 120 120\n");
            writer.write("FINAL_ZONE 700 500\n");
            writer.write("COIN 120 120 BLUE\n");
        }

        File saveFile = File.createTempFile("game-state-save-test", ".txt");
        saveFile.deleteOnExit();

        game.startGame(new GameConfiguration(levelFile.getAbsolutePath()), levelFile);
        engine.advanceGameClockOneSecond();
        player.resetPosition(120, 120);
        invokeUpdate(engine);
        player.resetPosition(250, 260);
        game.saveGame(saveFile);
        game.endGame();

        GameMemento memento = game.loadGame(saveFile);
        game.clearPlayers();
        Player restoredPlayer = PlayerFactory.createPlayer("Jugador 1", memento.getSkin());
        game.addPlayer(restoredPlayer);
        game.startGame(new GameConfiguration(memento.getLevelFile().getAbsolutePath()),
                memento.getLevelFile(), memento);

        assertEquals(44, game.getRemainingTime(), "remaining time should be restored");
        assertDoubleEquals(250, restoredPlayer.getX(), "player x position should be restored");
        assertDoubleEquals(260, restoredPlayer.getY(), "player y position should be restored");

        restoredPlayer.handleHit();

        assertDoubleEquals(120, restoredPlayer.getX(), "respawn x should be restored from the safe zone");
        assertDoubleEquals(120, restoredPlayer.getY(), "respawn y should be restored from the safe zone");
        assertEquals(1, restoredPlayer.getCollectedCoins(), "collected coins should be restored");
    }

    private static Level basicLevel(int startX, int startY, int timeLimit) {
        Level level = new Level(1);
        level.setStartPoint(new Point(startX, startY));
        level.setFinalSafeZone(new Point(700, 500));
        level.setTimeLimit(timeLimit);
        return level;
    }

    private static GameEngine resetEngine() {
        GameEngine engine = GameEngine.getInstance();
        engine.returnToMenu();
        engine.getPlayers().clear();
        engine.setGameMode("Player");
        return engine;
    }

    private static void invokeUpdate(GameEngine engine) throws Exception {
        Method update = GameEngine.class.getDeclaredMethod("update");
        update.setAccessible(true);
        update.invoke(engine);
    }

    private static void run(String name, TestCase testCase) throws Exception {
        testCase.run();
        testsRun++;
        System.out.println("PASS - " + name);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " expected:<" + expected + "> but was:<" + actual + ">");
        }
    }

    private static void assertDoubleEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.0001) {
            throw new AssertionError(message + " expected:<" + expected + "> but was:<" + actual + ">");
        }
    }

    @FunctionalInterface
    private interface TestCase {
        void run() throws Exception;
    }
} // Cierre de la clase
