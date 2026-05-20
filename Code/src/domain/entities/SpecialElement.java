package domain.entities;

/**
 * Clase padre que representa a los elementos especiales del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 19/05/2026
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

    /**
     * Método abstracto que verifica si el jugador tuvo contacto con un elemento.
     *
     * @param player El jugador que toco el elemento.
     */
    public abstract void onPlayerContact(Player player); // Cierre del método

    /**
     * Método abstracto que verifica si el enemigo tuvo contacto con un elemento.
     *
     * @param enemy El enemigo que toco el elemento.
     */
    public abstract void onEnemyContact(Enemy enemy); // Cierre del método

    /**
     * Método que verifica si el elemento esta activo
     * (no ha sido tocado por algun objeto del juego).
     *
     * @return boolean Determina si el elemento esta activo o no.
     */
    public boolean isActive() {
        return !elementCollected;
    } // Cierre del método

    /**
     * Método que permite desactivar el elemento.
     */
    public void deactivate() {
        this.elementCollected = true;
    } // Cierre del método

    /**
     * Método que devuelve la posición en x del elemento.
     *
     * @return double El valor en la coordenada x del elemento.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que devuelve la posición en y del elemento.
     *
     * @return double El valor en la coordenada y del elemento.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que devuelve la anchura del elemento.
     *
     * @return int El valor de la anchura del elemento.
     */
    public int getWidth() {
        return width;
    } // Cierre del método

    /**
     * Método que devuelve la altura del elemento.
     *
     * @return int La altura del elemento.
     */
    public int getHeight() {
        return height;
    } // Cierre del método

} // Cierre de la clase
