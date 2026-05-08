package domain.entities.strategy;

import domain.entities.Player;
import java.util.List;

public class SinglePlayer implements GameModeStrategy {
    private List<Player> players;

    @Override
    public void setUp(List<Player> players) {
        int amountPlayers = Math.min(1, players.size());
        this.players = players.subList(0, amountPlayers);
    }

    @Override
    public boolean checkWinCondition(List<Player> players, int collectedCoins, int totalCoins) {
        return collectedCoins >= totalCoins;
    }

    @Override
    public Player getWinner(List<Player> players) {
        return players.isEmpty() ? null : players.get(0);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public boolean independentGameModeCoins() {
        return false;
    }
} // Cierre de la clase
