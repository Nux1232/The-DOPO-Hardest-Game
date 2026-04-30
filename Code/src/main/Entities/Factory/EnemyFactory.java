package main.Entities.Factory;

import main.Entities.Enemy;
import Entities.Strategy.LinearMovement;
import Entities.Strategy.PatrolMovement;

public class EnemyFactory {
    public static Enemy createEnemy(String type, double x, double y) {
        Enemy enemy = new Enemy(x, y);
        if (type == null) type = "BASICO";
        
        switch (type.toUpperCase()) {
            case "RAPIDO":
                // Velocidad 2.0 para el tipo rápido
                enemy.setMovementStrategy(new LinearMovement(2.0, true));
                break;
            case "PATRULLERO":
                enemy.setMovementStrategy(new PatrolMovement());
                break;
            case "BASICO":
            default:
                // Velocidad 1.0 estándar
                enemy.setMovementStrategy(new LinearMovement(1.0, true));
                break;
        }
        return enemy;
    }
}
