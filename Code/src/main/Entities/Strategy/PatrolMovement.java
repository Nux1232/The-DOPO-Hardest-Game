package main.Entities.Strategy;

import main.Entities.Enemy;

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
    private boolean firstRun = true;

    /**
     * Método que implementa el movimiento de patrullaje.
     *
     * @param enemy El enemigo que realiza el movimiento.
     */
    @Override
    public void move(Enemy enemy) {
        if (firstRun) {
            startX = enemy.getX();
            startY = enemy.getY();
            firstRun = false;
        }

        enemy.setX(enemy.getX() + (speed * direction));
        if (Math.abs(enemy.getX() - startX) > range) {
            direction *= -1;
        }
    } // Cierre del método
} // Cierre de la clase
