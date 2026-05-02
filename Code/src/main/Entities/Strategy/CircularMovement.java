package main.Entities.Strategy;

import main.Entities.Enemy;

/**
 * Clase que implementa el movimiento circular de un enemigo.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */
public class CircularMovement implements MovementStrategy {
    private double angle = 0;
    private double centerX, centerY;
    private double radius = 50;
    private double speed = 0.05;

    /**
     * Constructor de la clase CircularMovement.
     *
     * @param x La coordenada x donde sera el movimiento.
     * @param y La coordenada y donde sera el movimiento.
     */
    public CircularMovement(double x, double y) {
        this.centerX = x;
        this.centerY = y;
    } // Cierre del constructor

    /**
     * Método que permite mover al enemigo de forma circular.
     *
     * @param enemy El enemigo que aplica el movimiento.
     */
    @Override
    public void move(Enemy enemy) {
        angle += speed;
        enemy.setX(centerX + Math.cos(angle) * radius);
        enemy.setY(centerY + Math.sin(angle) * radius);
    } // Cierre del método
} // Cierre de la clase
