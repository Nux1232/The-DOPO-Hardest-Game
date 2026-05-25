package domain.entities.strategy;

import domain.entities.Player;
import domain.level.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Clase que implementa el modo solitario.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class SinglePlayer implements GameModeStrategy {
    private List<Player> players;
    private boolean levelFinished = false;

    /**
     * Método que sobreescribe el cómo se prepara el jugador.
     *
     * @param players El jugador que va a jugar.
     */
    @Override
    public void setUp(List<Player> players) {
        int amountPlayers = Math.min(1, players.size());
        this.players = players.subList(0, amountPlayers);
        this.levelFinished = false;
    } // Cierre del método

    /**
     * Método que sobreescribe el comportamiento de cómo se gana una partida.
     *
     * @param players El jugador que va a jugar.
     * @param collectedCoins Las monedas obtenidas por el jugador.
     * @param totalCoins Las monedas totales que hay en el nivel.
     * @return boolean Indica si el juego ha terminado.
     */
    @Override
    public boolean checkWinCondition(List<Player> players, int collectedCoins, int totalCoins) {
        return levelFinished;
    } // Cierre del método

    /**
     * Método que sobreescribe el cómo se obtiene al ganador de la partida.
     *
     * @param players El jugador que va a jugar.
     * @return Player El ganador de la partida.
     */
    @Override
    public Player getWinner(List<Player> players) {
        return players.isEmpty() ? null : players.get(0);
    } // Cierre del método

    /**
     * Método que establece el jugador que quiere jugar.
     *
     * @return List<Player> El jugador que va a jugar.
     */
    @Override
    public List<Player> getPlayers() {
        return players;
    } // Cierre del método

    /**
     * Método que sobreescribe el cómo se obtienen las monedas.
     *
     * @return boolean Verifica el cómo se obtienen las monedas.
     */
    @Override
    public boolean independentGameModeCoins() {
        return false;
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

        if(finalSecureZone == null) return;
        Player player = players.get(0);

        if (!player.hasCollectedAllCoins(level.getCoins().size())) return;
        Rectangle2D.Double pRect = new Rectangle2D.Double(player.getX(), player.getY(),
                20 * player.getSizeMultiplier(), 20 * player.getSizeMultiplier());

        if (pRect.intersects(finalSecureZone.getX(), finalSecureZone.getY(),
                60, 60)) {
            this.levelFinished = true;
        }
    } // Cierre del método

    /**
     * Método que retorna el nombre del modo de juego.
     * @return String El nombre del modo de juego.
     */
    public String getName() {
        return "SinglePlayer";
    } // Cierre del método
} // Cierre de la clase
