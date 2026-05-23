package domain.entities.strategy;

import domain.entities.Movable;

import java.awt.*;
import java.util.List;

/**
 * Clase que implementa el movimiento de forma vertical.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class VerticalMovement implements MovementStrategy {
    private double speed;
    private int direction = 1;

    /**
     * Constructor de la clase VerticalMovement.
     *
     * @param speed La velocidad del movimiento.
     */
    public VerticalMovement(double speed) {
        this.speed = speed;
    } // Cierre del constructor

    /**
     * Método que permite mover al objeto de forma vertical usando su propio
     * comportamiento.
     *
     * @param object El objeto que aplica el movimiento.
     */
    @Override
    public void move(Movable object, List<Rectangle> walls) {
        double newPositionX = object.getX();
        double newPositionY = object.getY() + (speed * direction);

        Rectangle nextPosition = new Rectangle((int)newPositionX, (int)newPositionY, 15, 15);
        boolean wallColide = walls.stream().anyMatch(wall -> wall.intersects(nextPosition));

        if(wallColide) {
            direction *= -1;
        } else {
            object.setX(newPositionX);
            object.setY(newPositionY);
        }
    } // Cierre del método
} // Cierre de la clase
