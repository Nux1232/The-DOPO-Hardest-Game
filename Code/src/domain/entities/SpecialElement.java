package domain.entities;

/**
 * Clase padre que representa a los elementos especiales del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 18/05/2026
 */

public abstract class SpecialElement {
    private double x;
    private double y;
    private int width;
    private int height;
    private String color;
    private boolean elementCollected;

    /**
     * Constructor de la clase SpecialElement.
     */
    public SpecialElement(double x, double y, int width, int height, String color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.elementCollected = false;
    } // Cierre del constructor

    public abstract void onEntitieContact(Player player, Enemy enemy); // Cierre del método

    public boolean isActive() {
        return getCollected();
    } // Cierre del método

    public void deactivate() {
        this.elementCollected = false;
    } // Cierre del método

    public boolean getCollected() {
        return elementCollected;
    } // Cierre del método

} // Cierre de la clase
