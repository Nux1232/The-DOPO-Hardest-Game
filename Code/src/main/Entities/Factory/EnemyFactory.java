package main.Entities.Factory;

import main.Entities.Enemy;
import main.Entities.Strategy.*;

/**
 * Clase que crea los enemigos usando el patron de diseño Factory.
 */
public class EnemyFactory {
    private static final double RANGE_MOVEMENT = 80.0;

    /**
     * Método que crea un enemigo.
     *
     * @param type El tipo de enemigo.
     * @param x La coordenada x del enemigo.
     * @param y La coordenada y del enemigo.
     * @return Enemy El enemigo creado.
     */
    public static Enemy createEnemy(String type, double x, double y) {
        Enemy enemy = new Enemy(x, y);
        if (type == null) type = "BASICO";
        
        switch (type.toUpperCase()) {
            case "RAPIDO":
                enemy.setMovementStrategy(new LinearMovement(3.0, true, x - RANGE_MOVEMENT, x + RANGE_MOVEMENT));
                break;
            case "PATRULLERO":
                enemy.setMovementStrategy(new CircularMovement(x, y));
                break;
            case "VERTICAL":
                enemy.setMovementStrategy(new VerticalMovement(2.0));
                break;
            case "BASICO":
            default:
                enemy.setMovementStrategy(new LinearMovement(1.5, true, x - RANGE_MOVEMENT, x + RANGE_MOVEMENT));
                break;
        }
        return enemy;
    } // Cierre del método
} // Cierre de la clase
