package domain.entities.strategy;

import domain.entities.Movable;

import java.awt.*;
import java.util.List;

/**
 * Clase que implementa el movimiento de patrullaje.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class PatrolMovement implements MovementStrategy {
    private double speed = 1.0;
    private double startX, startY;
    private double range = 100.0;
    private int direction = 1;
    private boolean horizontal = true;
    private boolean firstRun = true;

    /**
     * Método que implementa el movimiento de patrullaje.
     *
     * @param object El objeto que realiza el movimiento.
     */
    @Override
    public void move(Movable object, List<Rectangle> walls) {
        if (firstRun) {
            startX = object.getX();
            startY = object.getY();
            firstRun = false;
        }

        double newPositionX = object.getX();
        double newPositionY = object.getY();

        if (horizontal) {
            newPositionX += speed * direction;
        } else {
            newPositionY += speed * direction;
        }

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
