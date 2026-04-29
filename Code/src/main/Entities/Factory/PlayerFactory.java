package Entities.Factory;

import Entities.Player;

public class PlayerFactory {
    public static Player createPlayer(String name, String type) {
        switch (type.toUpperCase()) {
            case "AZUL":
                // Rápido, tamaño 0.8x (según una de las descripciones)
                return new Player(name, "AZUL", 1.5, 0.8);
            case "VERDE":
                // Resistente, velocidad normal
                return new Player(name, "VERDE", 1.0, 1.0);
            case "ROJO":
            default:
                // Estándar
                return new Player(name, "ROJO", 1.0, 1.0);
        }
    }
}
