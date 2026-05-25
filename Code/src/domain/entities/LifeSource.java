package domain.entities;

/**
 * Clase hija que representa una fuente de vida, es un elemento especial.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 18/05/2026
 */

public class LifeSource extends SpecialElement {
    private double x;
    private double y;
    private boolean collected;

    /**
     * Constructor de la clase LifeSource.
     *
     * @param x La coordenada x donde se encuentra la fuente de vida.
     * @param y La coordenada y donde se encuentra la fuente de vida.
     */
    public LifeSource(double x, double y) {
        super(x, y, 20, 20, "MAGENTA");
        this.x = x;
        this.y = y;
        this.collected = false;
    } // Cierre del constructor

    public void onPlayerContact(Player player) {

    } // Cierre del método

    // No tener en cuenta pq esto no se usa
    @Override
    public void onEnemyContact(Enemy enemy) {

    } // Cierre del método

    /**
     * Método que retorna la coordenada x de la fuente de vida.
     *
     * @return double La coordenada x de la fuente de vida.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que retorna la coordenada y de la fuente de vida.
     *
     * @return double La coordenada y de la fuente de vida.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que retorna si la fuente de vida ha sido recogida.
     *
     * @return double Si la fuente de vida ha sido recogida.
     */
    public boolean isCollected() {
        return collected;
    } // Cierre del método

    /**
     * Método que marca la fuente de vida como recogida.
     */
    public void collect() {
        this.collected = true;
    } // Cierre del método
} // Cierre de la clase
