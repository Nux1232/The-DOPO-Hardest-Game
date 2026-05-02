package main.Entities.Strategy;

import main.Entities.Enemy;

/**
 * La interfaz que representa una estrategia de movimiento usando el patron de diseño Strategy.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public interface MovementStrategy {
    void move(Enemy enemy);
} // Cierre de la interfaz
