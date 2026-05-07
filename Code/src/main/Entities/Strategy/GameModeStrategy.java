package main.Entities.Strategy;

import main.Entities.Player;
import java.util.List;

public interface GameModeStrategy {
    void setUp(List<Player> players);
    boolean checkWinCondition(List<Player> players, int CollectedCoins, int totalCoins);
    Player getWinner(List<Player> players);
    List<Player> getPlayers();
} // Cierre de la interfaz
