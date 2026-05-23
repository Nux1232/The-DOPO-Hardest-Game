package domain.entities.strategy;

import domain.entities.Movable;

import java.awt.*;
import java.util.List;

/**
 * Clase que implementa el movimiento circular de un objeto.
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
     * Método que permite mover al objeto de forma circular.
     *
     * @param object El objeto que aplica el movimiento.
     */
    @Override
    public void move(Movable object, List<Rectangle> walls) {
        angle += speed;
        object.setX(centerX + Math.cos(angle) * radius);
        object.setY(centerY + Math.sin(angle) * radius);
    } // Cierre del método
} // Cierre de la clase
