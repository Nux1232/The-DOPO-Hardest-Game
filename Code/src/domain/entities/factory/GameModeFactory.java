package domain.entities.factory;

import domain.entities.strategy.GameModeStrategy;
import domain.entities.strategy.PlayerVsPlayer;
import domain.entities.strategy.PlayerVsMachine;
import domain.entities.strategy.SinglePlayer;

public class GameModeFactory {
    public static GameModeStrategy createGameMode(String mode) {
        if(mode == null) return new SinglePlayer();
        switch(mode.toUpperCase()) {
            case "PVP":
            case "PLAYER VS PLAYER":
            case "PLAYERVSPLAYER":
                return new PlayerVsPlayer();
            case "PVM":
            case "PLAYER VS MACHINE":
            case "PLAYERVSMACHINE":
                return new PlayerVsMachine();
            case "PLAYER":
            case "SINGLEPLAYER":
            default:
                return new SinglePlayer();
        }
    } // Cierre del método
} // Cierre de la clase
