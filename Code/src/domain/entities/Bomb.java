package domain.entities;

/**
 * Clase hija que representa a una bomba del juego, es un elemento especial.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 18/05/2026
 */

public class Bomb extends SpecialElement {
    private double x, y;
    private boolean intersection;

    /**
     * El constructor de la clase Bomb.
     *
     * @param x La coordenada x donde se encuentra la bomba.
     * @param y La coordenada y donde se encuentra la bomba.
     */
    public Bomb(double x, double y, String color) {
        super(x, y, 20, 20, "BLACK");
        this.intersection = false;
    } // Cierre del constructor

    public void onEntitieContact(Player player, Enemy enemy) {

    } // Cierre del método

    /**
     * Método que retorna la coordenada x de la bomba.
     *
     * @return double La coordenada x de la bomba.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que retorna la coordenada y de la bomba.
     *
     * @return double La coordenada y de la bomba.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que verifica si la bomba ha intersecto con un jugador o enemigo.
     * @return boolean Si hubo interseccion o no.
     */
    public boolean isIntersected() {
        return intersection;
    } // Cierre del método

    /**
     * Método que hace que la bomba explote.
     *
     * @return boolean Si hizo contacto con un objeto.
     */
    public void explode() {
        this.intersection = true;
    } // Cierre del método
} // Cierre de la clase
