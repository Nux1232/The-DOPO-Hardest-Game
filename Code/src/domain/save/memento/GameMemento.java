package domain.save.memento;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Clase que permite guardar una partida usando el patron de diseno Memento.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class GameMemento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String mode;
    private final String skin;
    private final Color borderColor;
    private final String secondSkin;
    private final Color secondBorderColor;
    private final File levelFile;
    private final Integer remainingTime;
    private final List<PlayerSnapshot> playerSnapshots;

    /**
     * Constructor de la clase GameMemento.
     *
     * @param mode El modo seleccionado por el usuario.
     * @param skin La skin seleccionada por el usuario.
     * @param borderColor El color del borde seleccionado por el usuario.
     * @param levelFile El archivo del nivel seleccionado por el usuario.
     */
    public GameMemento(String mode, String skin, Color borderColor, File levelFile) {
        this(mode, skin, borderColor, "BLINKY", Color.BLACK, levelFile);
    } // Cierre del constructor

    public GameMemento(String mode, String skin, Color borderColor,
                       String secondSkin, Color secondBorderColor, File levelFile) {
        this(mode, skin, borderColor, secondSkin, secondBorderColor, levelFile, -1,
                Collections.emptyList());
    } // Cierre del constructor

    public GameMemento(String mode, String skin, Color borderColor,
                       String secondSkin, Color secondBorderColor, File levelFile,
                       int remainingTime, List<PlayerSnapshot> playerSnapshots) {
        this.mode = mode;
        this.skin = skin;
        this.borderColor = borderColor;
        this.secondSkin = secondSkin;
        this.secondBorderColor = secondBorderColor;
        this.levelFile = levelFile;
        this.remainingTime = remainingTime;
        this.playerSnapshots = playerSnapshots == null
                ? Collections.emptyList()
                : new ArrayList<>(playerSnapshots);
    } // Cierre del constructor

    /**
     * Metodo que obtiene el modo de juego.
     *
     * @return String El modo de juego.
     */
    public String getMode() {
        return mode;
    } // Cierre del metodo

    /**
     * Metodo que obtiene la skin seleccionada por el usuario.
     *
     * @return La skin seleccionada.
     */
    public String getSkin() {
        return skin;
    } // Cierre del metodo

    /**
     * Metodo que obtiene el color del borde seleccionado por el usuario.
     *
     * @return El color del borde.
     */
    public Color getBorderColor() {
        return borderColor;
    } // Cierre del metodo

    /**
     * Metodo que obtiene la skin seleccionada por el segundo usuario.
     *
     * @return La skin seleccionada.
     */
    public String getSecondSkin() {
        return secondSkin == null ? "BLINKY" : secondSkin;
    } // Cierre del metodo

    public Color getSecondBorderColor() {
        return secondBorderColor == null ? Color.BLACK : secondBorderColor;
    } // Cierre del metodo

    public File getLevelFile() {
        return levelFile;
    } // Cierre del metodo

    public int getRemainingTime() {
        return remainingTime == null ? -1 : remainingTime;
    } // Cierre del metodo

    public List<PlayerSnapshot> getPlayerSnapshots() {
        return playerSnapshots == null ? Collections.emptyList() : Collections.unmodifiableList(playerSnapshots);
    } // Cierre del metodo

    public static class PlayerSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String skin;
        private final Color borderColor;
        private final double x;
        private final double y;
        private final double currentSpeed;
        private final double sizeMultiplier;
        private final double respawnX;
        private final double respawnY;
        private final int deaths;
        private final Set<Integer> collectedCoins;
        private final boolean hasShield;
        private final boolean invincible;
        private final int invincibilityTimer;

        public PlayerSnapshot(String skin, Color borderColor, double x, double y,
                              double currentSpeed, double sizeMultiplier,
                              double respawnX, double respawnY, int deaths,
                              Set<Integer> collectedCoins, boolean hasShield,
                              boolean invincible, int invincibilityTimer) {
            this.skin = skin;
            this.borderColor = borderColor;
            this.x = x;
            this.y = y;
            this.currentSpeed = currentSpeed;
            this.sizeMultiplier = sizeMultiplier;
            this.respawnX = respawnX;
            this.respawnY = respawnY;
            this.deaths = deaths;
            this.collectedCoins = collectedCoins == null
                    ? Collections.emptySet()
                    : new HashSet<>(collectedCoins);
            this.hasShield = hasShield;
            this.invincible = invincible;
            this.invincibilityTimer = invincibilityTimer;
        } // Cierre del constructor

        public String getSkin() {
            return skin;
        } // Cierre del metodo

        public Color getBorderColor() {
            return borderColor;
        } // Cierre del metodo

        public double getX() {
            return x;
        } // Cierre del metodo

        public double getY() {
            return y;
        } // Cierre del metodo

        public double getCurrentSpeed() {
            return currentSpeed;
        } // Cierre del metodo

        public double getSizeMultiplier() {
            return sizeMultiplier;
        } // Cierre del metodo

        public double getRespawnX() {
            return respawnX;
        } // Cierre del metodo

        public double getRespawnY() {
            return respawnY;
        } // Cierre del metodo

        public int getDeaths() {
            return deaths;
        } // Cierre del metodo

        public Set<Integer> getCollectedCoins() {
            return Collections.unmodifiableSet(collectedCoins);
        } // Cierre del metodo

        public boolean hasShield() {
            return hasShield;
        } // Cierre del metodo

        public boolean isInvincible() {
            return invincible;
        } // Cierre del metodo

        public int getInvincibilityTimer() {
            return invincibilityTimer;
        } // Cierre del metodo
    } // Cierre de la clase interna
} // Cierre de la clase
