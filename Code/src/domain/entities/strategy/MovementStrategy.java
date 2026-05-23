package domain.entities.strategy;

import domain.entities.Movable;
import java.awt.Rectangle;
import java.util.List;

/**
 * La interfaz que representa una estrategia de movimiento usando el patron de diseño Strategy.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public interface MovementStrategy {
    void move(Movable object, List<Rectangle> walls);
} // Cierre de la interfaz
