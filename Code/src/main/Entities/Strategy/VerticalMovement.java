package main.Entities.Strategy;

import main.Entities.Enemy;

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
     * Método que permite mover al enemigo de forma vertical usando su propio
     * comportamiento.
     *
     * @param enemy El enemigo que aplica el movimiento.
     */
    @Override
    public void move(Enemy enemy) {
        enemy.setY(enemy.getY() + (speed * direction));
        if (enemy.getY() > 580 || enemy.getY() < 0) {
            direction *= -1;
        }
    } // Cierre del método
} // Cierre de la clase
