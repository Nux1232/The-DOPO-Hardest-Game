package test.domain.core;

import domain.core.GameEngine;
import domain.entities.LifeSource;
import domain.entities.Player;
import domain.entities.factory.PlayerFactory;
import domain.level.GameConfiguration;
import domain.level.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;

/**
 * Clase que permite realizar pruebas de unidad para las vidas extras usando JUnit 5.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 19/05/2026
 */

public class ExtraLivesTest {

    @Test
    @DisplayName("un jugador nuevo empieza con 0 vidas extras")
    void newPlayerStartsWithZeroExtraLives() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        assertEquals(0, player.getExtraLives(), "un jugador nuevo debe tener 0 vidas extras");
    }

    @Test
    @DisplayName("addExtraLife incrementa el contador de vidas extras")
    void addExtraLifeIncrementsCounter() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.addExtraLife();
        assertEquals(1, player.getExtraLives(), "debería tener 1 vida extra");
        player.addExtraLife();
        assertEquals(2, player.getExtraLives(), "debería tener 2 vidas extras");
    }

    // ========== Tests de handleHit() con vidas extras ==========

    @Test
    @DisplayName("handleHit consume una vida extra en vez de morir")
    void handleHitConsumesExtraLifeInsteadOfDying() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.setRespawnPoint(100, 100);
        player.resetPosition(50, 50);
        player.addExtraLife();

        player.handleHit();

        assertEquals(0, player.getExtraLives(), "debería consumir la vida extra");
        assertEquals(0, player.getDeaths(), "no debería sumar muerte al usar vida extra");
        assertTrue(player.isInvincible(), "debería ser invencible tras usar vida extra");
    }

    @Test
    @DisplayName("handleHit causa muerte cuando no hay vidas extras")
    void handleHitCausesDeathWithNoExtraLives() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.setRespawnPoint(100, 100);
        player.resetPosition(50, 50);

        player.handleHit();

        assertEquals(0, player.getExtraLives(), "no debería tener vidas extras");
        assertEquals(1, player.getDeaths(), "debería sumar 1 muerte");
        assertEquals(100.0, player.getX(), 0.0001, "debería reaparecer en respawn X");
        assertEquals(100.0, player.getY(), 0.0001, "debería reaparecer en respawn Y");
    }

    @Test
    @DisplayName("tras morir el jugador recibe invencibilidad temporal")
    void afterDeathPlayerReceivesTemporaryInvincibility() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.setRespawnPoint(100, 100);
        player.resetPosition(50, 50);

        player.handleHit();

        assertTrue(player.isInvincible(), "debería ser invencible tras morir");
        assertEquals(1, player.getDeaths(), "debería tener 1 muerte");

        // Un segundo hit no debería contar
        player.handleHit();
        assertEquals(1, player.getDeaths(), "no debería sumar otra muerte mientras es invencible");
    }

    @Test
    @DisplayName("las vidas extras se acumulan correctamente")
    void extraLivesAccumulateCorrectly() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.setRespawnPoint(100, 100);
        player.resetPosition(50, 50);
        player.addExtraLife();
        player.addExtraLife();
        player.addExtraLife();

        assertEquals(3, player.getExtraLives(), "debería tener 3 vidas extras");

        player.handleHit();
        assertEquals(2, player.getExtraLives(), "debería tener 2 vidas extras tras primer golpe");
        assertEquals(0, player.getDeaths(), "no debería tener muertes");

        // Esperar a que termine la invencibilidad
        for (int i = 0; i < 90; i++) player.update();

        player.handleHit();
        assertEquals(1, player.getExtraLives(), "debería tener 1 vida extra tras segundo golpe");
        assertEquals(0, player.getDeaths(), "todavía no debería tener muertes");

        for (int i = 0; i < 90; i++) player.update();

        player.handleHit();
        assertEquals(0, player.getExtraLives(), "debería tener 0 vidas extras tras tercer golpe");
        assertEquals(0, player.getDeaths(), "todavía no debería tener muertes");

        for (int i = 0; i < 90; i++) player.update();

        player.handleHit();
        assertEquals(1, player.getDeaths(), "ahora sí debería morir");
    }

    // ========== Tests de Clyde (skin verde) con vidas extras ==========

    @Test
    @DisplayName("Clyde usa escudo primero, luego vida extra, luego muere")
    void clydeUsesShieldThenExtraLifeThenDies() {
        Player clyde = PlayerFactory.createPlayer("Test", "CLYDE");
        clyde.setRespawnPoint(100, 100);
        clyde.resetPosition(50, 50);
        clyde.addExtraLife();

        // Primer golpe: rompe escudo
        clyde.handleHit();
        assertEquals(0, clyde.getDeaths(), "no debería morir, tiene escudo");
        assertEquals(1, clyde.getExtraLives(), "vida extra intacta");
        assertTrue(clyde.isInvincible(), "invencible tras romper escudo");

        // Esperar invencibilidad
        for (int i = 0; i < 90; i++) clyde.update();

        // Segundo golpe: usa vida extra
        clyde.handleHit();
        assertEquals(0, clyde.getDeaths(), "no debería morir, tiene vida extra");
        assertEquals(0, clyde.getExtraLives(), "vida extra consumida");

        // Esperar invencibilidad
        for (int i = 0; i < 90; i++) clyde.update();

        // Tercer golpe: muere
        clyde.handleHit();
        assertEquals(1, clyde.getDeaths(), "ahora sí debería morir");
    }

    // ========== Tests de LifeSource ==========

    @Test
    @DisplayName("LifeSource empieza sin recoger")
    void lifeSourceStartsUncollected() {
        LifeSource ls = new LifeSource(10, 20);
        assertFalse(ls.isCollected(), "debería empezar sin recoger");
    }

    @Test
    @DisplayName("LifeSource se marca como recogida al llamar collect()")
    void lifeSourceCollectMarksAsCollected() {
        LifeSource ls = new LifeSource(10, 20);
        ls.collect();
        assertTrue(ls.isCollected(), "debería estar recogida");
    }

    @Test
    @DisplayName("LifeSource almacena correctamente sus coordenadas")
    void lifeSourceStoresCoordinates() {
        LifeSource ls = new LifeSource(150, 275);
        assertEquals(150, ls.getX(), 0.0001, "coordenada X");
        assertEquals(275, ls.getY(), 0.0001, "coordenada Y");
    }

    // ========== Tests de integración con GameEngine ==========

    @Test
    @DisplayName("el motor del juego otorga vida extra al tocar LifeSource")
    void engineGrantsExtraLifeOnLifeSourceCollision() throws Exception {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        engine.getPlayers().add(player);

        Level level = basicLevel(20, 20, 30);
        LifeSource ls = new LifeSource(20, 20);
        level.addLifeSource(ls);
        engine.loadLevel(level);
        invokeUpdate(engine);

        assertTrue(ls.isCollected(), "la fuente de vida debería estar recogida");
        assertEquals(1, player.getExtraLives(), "el jugador debería tener 1 vida extra");
    }

    @Test
    @DisplayName("LifeSource recogida no otorga vidas duplicadas")
    void collectedLifeSourceDoesNotGrantDuplicateLives() throws Exception {
        GameEngine engine = resetEngine();
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        engine.getPlayers().add(player);

        Level level = basicLevel(20, 20, 30);
        LifeSource ls = new LifeSource(20, 20);
        level.addLifeSource(ls);
        engine.loadLevel(level);
        invokeUpdate(engine);
        invokeUpdate(engine);

        assertEquals(1, player.getExtraLives(), "no debería otorgar vidas duplicadas");
    }

    @Test
    @DisplayName("resetForNextLevel preserva muertes y vidas extras")
    void resetForNextLevelPreservesDeathsAndLives() {
        Player player = PlayerFactory.createPlayer("Test", "BLINKY");
        player.setRespawnPoint(100, 100);
        player.resetPosition(50, 50);
        player.addExtraLife();
        player.addExtraLife();
        player.handleHit(); // usa 1 vida extra, queda 1

        // Esperar invencibilidad
        for (int i = 0; i < 90; i++) player.update();
        player.handleHit(); // usa la última vida extra, queda 0

        for (int i = 0; i < 90; i++) player.update();
        player.handleHit(); // muere, deaths = 1

        player.addExtraLife(); // nueva vida extra
        player.resetForNextLevel();

        assertEquals(1, player.getDeaths(), "las muertes se deben preservar");
        assertEquals(1, player.getExtraLives(), "las vidas extras se deben preservar");
        assertEquals(0, player.getCollectedCoins(), "las monedas se deben reiniciar");
    }

    @Test
    @DisplayName("la configuración de nivel parsea LIFESOURCE correctamente")
    void gameConfigurationParsesLifeSource() throws Exception {
        File file = File.createTempFile("level-lifesource-test", ".txt");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("TIME 30\n");
            writer.write("START 10 20\n");
            writer.write("FINAL_ZONE 700 500\n");
            writer.write("LIFESOURCE 200 300\n");
            writer.write("LIFESOURCE 400 150\n");
        }

        Level level = new GameConfiguration(file.getAbsolutePath()).buildLevel(1);

        assertEquals(2, level.getLifeSources().size(), "debería tener 2 fuentes de vida");
        assertEquals(200, level.getLifeSources().get(0).getX(), 0.0001, "X de primera fuente");
        assertEquals(300, level.getLifeSources().get(0).getY(), 0.0001, "Y de primera fuente");
        assertEquals(400, level.getLifeSources().get(1).getX(), 0.0001, "X de segunda fuente");
        assertEquals(150, level.getLifeSources().get(1).getY(), 0.0001, "Y de segunda fuente");
    }

    // ========== Utilidades ==========

    private Level basicLevel(int startX, int startY, int timeLimit) {
        Level level = new Level(1);
        level.setStartPoint(new Point(startX, startY));
        level.setFinalSafeZone(new Point(700, 500));
        level.setTimeLimit(timeLimit);
        return level;
    }

    private GameEngine resetEngine() {
        GameEngine engine = GameEngine.getInstance();
        engine.returnToMenu();
        engine.getPlayers().clear();
        engine.setGameMode("Player");
        return engine;
    }

    private void invokeUpdate(GameEngine engine) throws Exception {
        Method update = GameEngine.class.getDeclaredMethod("update");
        update.setAccessible(true);
        update.invoke(engine);
    }
} // Cierre de la clase
