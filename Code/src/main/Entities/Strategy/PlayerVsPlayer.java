package main.Entities.Strategy;

import main.Entities.Player;
import java.util.List;

public class PlayerVsPlayer implements GameModeStrategy {
    private List<Player> players;
    private long[] finishTime;
    private int levelFinished = 0;

    @Override
    public void setUp(List<Player> players) {
        int amountPlayers = Math.min(2, players.size());
        this.players = players.subList(0, amountPlayers);
        this.finishTime = new long[players.size()];
    }

    @Override
    public boolean checkWinCondition(List<Player> players, int collectedCoins, int totalCoins) {
        return levelFinished >= 1;
    }

    @Override
    public Player getWinner(List<Player> players) {
        int auxiliarWin = 0;
        long playerBestTime = Long.MAX_VALUE;
        for(int i = 0; i < players.size(); i++) {
            if(finishTime[i] > 0 && finishTime[i] < playerBestTime) {
                playerBestTime = finishTime[i];
                auxiliarWin = i;
            }
        }
        return players.get(auxiliarWin);
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    public void playerFinished(int playerCount) {
        if(playerCount < finishTime.length && finishTime[playerCount] == 0) {
            finishTime[playerCount] = System.currentTimeMillis();
            levelFinished++;
        }
    }
} // Cierre de la clase
