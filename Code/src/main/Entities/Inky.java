package main.Entities;

/**
 * Clase hija que representa al jugador que puede moverse mas rapido.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class Inky extends Player {
    private static final double SPEED = 1.5;
    private static final double SIZE = 1.5;

    /**
     * Constructor de la clase Inky.
     *
     * @param name El nombre del jugador.
     */
    public Inky(String name) {

        super(name,"BLUE", SPEED, SIZE);
    } // Cierre del constructor
} // Cierre de la clase
