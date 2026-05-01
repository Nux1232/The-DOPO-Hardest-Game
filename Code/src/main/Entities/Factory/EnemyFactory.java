package main.Entities.Factory;

import main.Entities.Enemy;
import main.Entities.Strategy.*;

public class EnemyFactory {
    public static Enemy createEnemy(String type, double x, double y) {
        Enemy enemy = new Enemy(x, y);
        if (type == null) type = "BASICO";
        
        switch (type.toUpperCase()) {
            case "RAPIDO":
                enemy.setMovementStrategy(new LinearMovement(3.0, true));
                break;
            case "PATRULLERO":
                enemy.setMovementStrategy(new CircularMovement(x, y));
                break;
            case "VERTICAL":
                enemy.setMovementStrategy(new VerticalMovement(2.0));
                break;
            case "BASICO":
            default:
                enemy.setMovementStrategy(new LinearMovement(1.5, true));
                break;
        }
        return enemy;
    }
}
