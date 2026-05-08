package domain.entities.strategy;

import domain.entities.Player;
import java.util.List;

public interface GameModeStrategy {
    void setUp(List<Player> players);
    boolean checkWinCondition(List<Player> players, int CollectedCoins, int totalCoins);
    Player getWinner(List<Player> players);
    List<Player> getPlayers();
    boolean independentGameModeCoins();
} // Cierre de la interfaz
