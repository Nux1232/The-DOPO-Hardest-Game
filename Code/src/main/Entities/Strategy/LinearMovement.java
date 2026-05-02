package main.Entities.Strategy;

import main.Entities.Enemy;

/**
 * Clase que implementa el movimiento lineal de los enemigos.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class LinearMovement implements MovementStrategy {
    private double speed;
    private boolean horizontal;
    private int direction = 1;
    private double minBound;
    private double maxBound;

    /**
     * Constructor de la clase LinearMovement.
     *
     * @param speed La velocidad del movimiento.
     * @param horizontal Indica si el movimiento es horizontal.
     * @param minBound El limite minimo del movimiento.
     * @param maxBound El limite maximo del movimiento.
     */
    public LinearMovement(double speed, boolean horizontal, double minBound, double maxBound) {
        this.speed = speed;
        this.horizontal = horizontal;
        this.minBound = minBound;
        this.maxBound = maxBound;
    } // Cierre del constructor

    /**
     * Método que permite mover al enemigo de forma lineal.
     *
     * @param enemy El enemigo que aplica el movimiento.
     */
    @Override
    public void move(Enemy enemy) {
        if (horizontal) {
            enemy.setX(enemy.getX() + (speed * direction));
            if (enemy.getX() > maxBound || enemy.getX() < minBound) {
                direction *= -1;
            }
        } else {
            enemy.setY(enemy.getY() + (speed * direction));
            if (enemy.getY() > maxBound || enemy.getY() < minBound) {
                direction *= -1;
            }
        }
    } // Cierre del método
} // Cierre de la clase
