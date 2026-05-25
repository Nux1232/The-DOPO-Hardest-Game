package domain.entities.strategy;

import domain.entities.Movable;
import java.awt.Rectangle;
import java.util.List;

/**
 * Clase que implementa el movimiento lineal de los objetos.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class LinearMovement implements MovementStrategy {
    private double speed;
    private boolean horizontal;
    private int direction = 1;

    /**
     * Constructor de la clase LinearMovement.
     *
     * @param speed La velocidad del movimiento.
     * @param horizontal Indica si el movimiento es horizontal.
     */
    public LinearMovement(double speed, boolean horizontal) {
        this.speed = speed;
        this.horizontal = horizontal;
    } // Cierre del constructor

    /**
     * Método que permite mover al objeto de forma lineal.
     *
     * @param object El objeto que aplica el movimiento.
     */
    @Override
    public void move(Movable object, List<Rectangle> walls) {
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
