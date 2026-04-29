package Entities.Factory;

import Entities.Enemy;
import Entities.Strategy.MovementStrategy;
// Importar estrategias concretas cuando existan

public class EnemyFactory {
    public static Enemy createEnemy(String type, double x, double y) {
        Enemy enemy = new Enemy();
        // Aquí se le asignará la posición y la estrategia de movimiento
        switch (type.toUpperCase()) {
            case "RAPIDO":
                // Lógica para configurar velocidad 2x
                break;
            case "PATRULLERO":
                // Lógica para asignar PatrolMovement
                break;
            case "BASICO":
            default:
                // Lógica para asignar LinearMovement
                break;
        }
        return enemy;
    }
}
