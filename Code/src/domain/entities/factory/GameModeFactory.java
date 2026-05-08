package domain.entities.factory;

import domain.entities.strategy.GameModeStrategy;
import domain.entities.strategy.PlayerVsPlayer;
import domain.entities.strategy.SinglePlayer;

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
