package domain.entities;

/**
 * Clase hija que representa al jugador que viene por defecto.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class Blinky extends Player {
    private static final double SPEED = 1.25;
    private static final double SIZE = 1.0;

    /**
     * Constructor de la clase Blinky.
     *
     * @param name El nombre del jugador.
     */
    public Blinky(String name) {
        super(name, "RED", SPEED, SIZE);
    } // Cierre del constructor
} // Cierre de la clase
