package main.Entities.Factory;

import main.Entities.Strategy.GameModeStrategy;
import main.Entities.Strategy.PlayerVsPlayer;
import main.Entities.Strategy.SinglePlayer;

public class GameModeFactory {
    public static GameModeStrategy createGameMode(String mode) {
        if(mode == null) return new SinglePlayer();
        switch(mode.toUpperCase()) {
            case "PVP":
            case "PLAYER VS PLAYER":
                return new PlayerVsPlayer();
            case "PLAYER":
            default:
                return new SinglePlayer();
        }
    } // Cierre del método
} // Cierre de la clase
