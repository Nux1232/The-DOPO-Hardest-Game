package domain.entities;

import domain.entities.strategy.MovementStrategy;

import java.awt.*;
import java.util.List;

/**
 * Clase que representa a un enemigo del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class Enemy {
    private double x, y;
    private MovementStrategy movementStrategy;
    private boolean isAlive = true;

    /**
     * Constructor de la clase Enemy.
     *
     * @param x La coordenada x donde estara el enemigo.
     * @param y La coordenada y donde estara el enemigo.
     */
    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    } // Cierre del constructor

    /**
     * Método que actualiza el movimiento del enemigo.
     */
    public void update(List<Rectangle> walls) {
        if (movementStrategy != null) {
            movementStrategy.move(this, walls);
        }
    } // Cierre del método

    /**
     * Método que establece la estrategia de movimiento del enemigo.
     *
     * @param strategy La forma en como se mueve el enemigo.
     */
    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    } // Cierre del método

    /**
     * Método que hace que el enemigo muera.
     */
    public void die() {
        this.isAlive = false;
    } // Cierre del método

    /**
     * Método que verifica si el enemigo esta vivo.
     *
     * @return boolean Determina si el enemigo esta vivo o no.
     */
    public boolean isAlive() {
        return isAlive;
    } // Cierre del método

    /**
     * Método que obtiene la coordenada x del enemigo.
     *
     * @return double La coordenada x del enemigo.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que obtiene la coordenada y del enemigo.
     *
     * @return double La coordenada y del enemigo.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que establece la coordenada x del enemigo.
     *
     * @param x La nueva coordenada x del enemigo.
     */
    public void setX(double x) {
        this.x = x;
    } // Cierre del método

    /**
     * Método que establece la coordenada y del enemigo.
     *
     * @param y La nueva coordenada y del enemigo.
     */
    public void setY(double y) {
        this.y = y;
    } // Cierre del método
} // Cierre de la clase
