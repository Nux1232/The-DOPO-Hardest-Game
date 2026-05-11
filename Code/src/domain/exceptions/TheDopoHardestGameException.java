package domain.exceptions;

/**
 * Clase que representa excepciones que pueden ocurrir durante el juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 10/05/2026
 */

public class TheDopoHardestGameException extends Exception {
    public static final String LEVEL_LOAD_ERROR = "El nivel no se pudo cargar";
    public static final String INVALID_GAMEMODE = "Este modo de juego no existe";
    public static final String LOAD_GAME_ERROR = "El juego no se pudo cargar";
    public static final String SAVE_GAME_ERROR = "El juego no se pudo guardar";
    public static final String NO_PLAYERS = "No hay jugadores registrados en el juego";
    public static final String NO_LEVEL_SELECTED = "No hay un nivel seleccionado";

    /**
     * Constructor de la clase excepcion.
     *
     * @param message El mensaje a mostrar de lo ocurrido.
     */
    public TheDopoHardestGameException(String message) {
        super(message);
    } // Cierre del constructor
} // Cierre de la clase
