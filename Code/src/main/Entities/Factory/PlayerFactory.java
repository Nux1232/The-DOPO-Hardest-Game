package main.Entities.Factory;

import main.Entities.Player;

public class PlayerFactory {
    public static Player createPlayer(String name, String type) {
        if (type == null) type = "ROJO";
        switch (type.toUpperCase()) {
            case "AZUL":
                return new Player(name, "AZUL", 1.5, 0.8);
            case "VERDE":
                return new Player(name, "VERDE", 1.0, 1.0);
            case "ROJO":
            default:
                return new Player(name, "ROJO", 1.0, 1.0);
        }
    }
}
