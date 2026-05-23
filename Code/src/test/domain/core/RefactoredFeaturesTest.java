package domain.core;

import domain.entities.Enemy;
import domain.entities.Movable;
import domain.entities.strategy.LinearMovement;
import domain.level.Level;
import presentation.ui.GameObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase de pruebas de unidad para verificar los cambios de refactorización realizados en el juego.
 * Cubre la interfaz Movable, la clase abstracta GameObserver, la delegación en la Fachada
 * y la lógica de reinicio por tiempo agotado.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 23/05/2026
 */
public class RefactoredFeaturesTest {

    // ========== Clase Dummy que implementa Movable para pruebas de extensibilidad ==========
    private static class DummyMovable implements Movable {
        private double x;
        private double y;

        public DummyMovable(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override public double getX() { return x; }
        @Override public double getY() { return y; }
        @Override public void setX(double x) { this.x = x; }
        @Override public void setY(double y) { this.y = y; }
    }

    @Test
    @DisplayName("las estrategias de movimiento funcionan con cualquier objeto Movable")
    void movementStrategiesWorkWithAnyMovable() {
        Movable dummy = new DummyMovable(10.0, 10.0);
        LinearMovement linearMovement = new LinearMovement(2.0, true);

        // Mover horizontalmente sin colisión
        linearMovement.move(dummy, Collections.emptyList());
        assertEquals(12.0, dummy.getX(), 0.0001, "el objeto dummy debe moverse en el eje X");
        assertEquals(10.0, dummy.getY(), 0.0001, "el eje Y del dummy debe permanecer inmóvil");
    }

    @Test
    @DisplayName("Enemy implementa la interfaz Movable y responde a sus métodos")
    void enemyImplementsMovable() {
        Enemy enemy = new Enemy(50.0, 60.0);
        assertTrue(enemy instanceof Movable, "Enemy debe ser una instancia de Movable");
        assertEquals(50.0, enemy.getX(), 0.0001);
        assertEquals(60.0, enemy.getY(), 0.0001);

        enemy.setX(100.0);
        enemy.setY(200.0);
        assertEquals(100.0, enemy.getX(), 0.0001);
        assertEquals(200.0, enemy.getY(), 0.0001);
    }

    @Test
    @DisplayName("GameObserver como clase abstracta puede ser extendida y recibe notificaciones")
    void gameObserverAbstractClassReceivesNotifications() {
        GameEngine engine = GameEngine.getInstance();
        engine.returnToMenu();

        final boolean[] notified = {false};
        GameObserver mockObserver = new GameObserver() {
            @Override
            public void update() {
                notified[0] = true;
            }
        };

        engine.addObserver(mockObserver);
        engine.advanceGameClockOneSecond(); // Cuando no está jugando no cambia y no notifica, pero...
        
        // Simular notificación manual
        Level level = new Level(1);
        level.setTimeLimit(1);
        engine.loadLevel(level);
        engine.advanceGameClockOneSecond(); // Ahora sí avanza y llega a 0 -> Game Over -> Notifica

        assertTrue(notified[0], "el observador anónimo debe haber sido notificado por el motor");
    }

    @Test
    @DisplayName("la Fachada getEnemies() delega la lista de enemigos correctamente")
    void facadeDelegatesGetEnemiesCorrectly() {
        TheDopoHardestGame game = new TheDopoHardestGame();
        GameEngine engine = GameEngine.getInstance();
        engine.returnToMenu();

        // Caso 1: Nivel actual es null
        List<Enemy> enemiesWhenNull = game.getEnemies();
        assertNotNull(enemiesWhenNull, "no debe retornar null si el nivel no está cargado");
        assertTrue(enemiesWhenNull.isEmpty(), "debe ser una lista vacía");

        // Caso 2: Nivel cargado con enemigos
        Level level = new Level(2);
        Enemy enemy1 = new Enemy(10, 10);
        Enemy enemy2 = new Enemy(20, 20);
        level.addEnemy(enemy1);
        level.addEnemy(enemy2);

        engine.loadLevel(level);
        List<Enemy> enemiesFromFacade = game.getEnemies();
        assertEquals(2, enemiesFromFacade.size(), "debe retornar los 2 enemigos del nivel");
        assertSame(enemy1, enemiesFromFacade.get(0));
        assertSame(enemy2, enemiesFromFacade.get(1));
    }
}
