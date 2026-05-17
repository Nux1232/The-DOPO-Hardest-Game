package domain.entities.factory;

import domain.entities.Player;
import domain.entities.Blinky;
import domain.entities.Inky;
import domain.entities.Clyde;

/**
 * Clase que crea un jugador utilizando el patron de diseño Factory.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */
public class PlayerFactory {
    /**
     * Método que permite crear un jugador.
     *
     * @param name El nombre del jugador.
     * @param type El tipo de jugador.
     * @return Player El jugador creado.
     */
    public static Player createPlayer(String name, String type) {
        if (type == null) type = "BLINKY";
        switch (type.toUpperCase()) {
            case "INKY":
            case "BLUE":
            case "AZUL":
            case "BLUE":
                return new Inky(name);
            case "CLYDE":
            case "GREEN":
            case "VERDE":
            case "GREEN":
                return new Clyde(name);
            case "BLINKY":
            case "RED":
            case "ROJO":
            case "RED":
            default:
                return new Blinky(name);
        }
    } // Cierre del método
} // Cierre de la clase
