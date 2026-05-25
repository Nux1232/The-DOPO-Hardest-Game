package domain.entities.strategy;

import domain.entities.Player;
import domain.level.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa el modo jugador contra jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class PlayerVsPlayer implements GameModeStrategy {
    private List<Player> players;
    private long[] finishTime;
    private int levelFinished = 0;

    /**
     * Método que sobreescribe el cómo se preparan los jugadores.
     *
     * @param players Los jugadores que van a competir.
     */
    @Override
    public void setUp(List<Player> players) {
        int amountPlayers = Math.min(2, players.size());
        this.players = new ArrayList<>(players.subList(0, amountPlayers));
        this.finishTime = new long[amountPlayers];
        this.levelFinished = 0;
    } // Cierre del método

    /**
     * Método que sobreescribe el comportamiento de cómo se gana una partida.
     *
     * @param players Los jugadores que van a competir.
     * @param collectedCoins Las monedas obtenidas por cada jugador.
     * @param totalCoins Las monedas totales que hay en el nivel.
     * @return boolean Indica si el juego ha terminado.
     */
    @Override
    public boolean checkWinCondition(List<Player> players, int collectedCoins, int totalCoins) {
        return levelFinished >= 1;
    } // Cierre del método

    /**
     * Método que sobreescribe el cómo se obtiene al ganador de la partida.
     *
     * @param players Los jugadores que van a competir.
     * @return Player El ganador de la partida.
     */
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
    } // Cierre del método

    /**
     * Método que establece los jugadores que van a competir.
     *
     * @return List<Player> Los jugadores que van a competir.
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    } // Cierre del método

    /**
     * Método que indica que un jugador ha terminado la partida.
     *
     * @param playerCount La cantidad de jugadores que están en el nivel.
     */
    public void playerFinished(int playerCount) {
        if(playerCount < finishTime.length && finishTime[playerCount] == 0) {
            finishTime[playerCount] = System.currentTimeMillis();
            levelFinished++;
        }
    } // Cierre del método

    /**
     * Método que sobreescribe el cómo se obtienen las monedas.
     *
     * @return boolean Verifica el cómo se obtienen las monedas.
     */
    @Override
    public boolean independentGameModeCoins() {
        return true;
    } // Cierre del método

    /**
     * Método que sobreescribe el comportamiento de que se complete
     * un nivel.
     *
     * @param players Los jugadores que van a competir.
     * @param level El nivel que se va a completar.
     */
    @Override
    public void checkLevelCompletion(List<Player> players, Level level) {
        Point finalSecureZone = level.getFinalSafeZone();
        Point startSecureZone = level.getStartPoint();
        if (finalSecureZone == null || startSecureZone == null) return;

        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);

            if (!player.hasCollectedAllCoins(level.getCoins().size())) continue;
            Point target = level.getFinalZoneForPlayer(i);

            if (target == null) continue;
            Rectangle2D.Double pRect = new Rectangle2D.Double(player.getX(), player.getY(),
                    20 * player.getSizeMultiplier(), 20 * player.getSizeMultiplier());
            if (pRect.intersects(target.x, target.y, 60, 60)) {
                playerFinished(i);
            }
        }
    } // Cierre del método
} // Cierre de la clase
