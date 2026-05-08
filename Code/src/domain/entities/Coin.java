package domain.entities;

/**
 * Método que representa a una moneda del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class Coin {
    private double x, y;
    private String type;
    private boolean collected;

    /**
     * Constructor de la clase Coin.
     *
     * @param x La coordenada x donde se encuentra la moneda.
     * @param y La coordenada y donde se encuentra la moneda.
     * @param type El tipo de moneda.
     */
    public Coin(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.collected = false;
    } // Cierre del constructor

    /**
     * Método que retorna la coordenada x de la moneda.
     *
     * @return double La coordenada x de la moneda.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que devuelve la coordenada y de la moneda.
     *
     * @return double La coordenada y de la moneda.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que obtiene el tipo de moneda.
     *
     * @return String El tipo de moneda: amarillo, rojo, verde, azul.
     */
    public String getType() {
        return type;
    } // Cierre del método

    /**
     * Método que verifica si la moneda ha sido obtenida por el jugador.
     *
     * @return boolean Mira si fue obtenida o no.
     */
    public boolean isCollected() {
        return collected;
    } // Cierre del método

    /**
     * Método que marca la moneda como obtenida por el jugador.
     */
    public void collect() {
        this.collected = true;
    } // Cierre del método
} // Cierre de la clase
