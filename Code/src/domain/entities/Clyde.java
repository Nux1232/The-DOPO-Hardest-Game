package domain.entities;

/**
 * Clase hija que representa al jugador que puede tener un escudo.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class Clyde extends Player {
    private static final double SPEED = 1.25;
    private static final double SIZE = 1.0;
    private static final int INVINCIBILITY_FRAMES = 60;

    /**
     * Constructor de la clase Clyde.
     *
     * @param name El nombre del jugador.
     */
    public Clyde(String name) {
        super(name, "VERDE", SPEED, SIZE);
        this.hasShield = true;
    } // Cierre del constructor

    /**
     * Método que maneja el impacto del enemigo, definiendo su propio
     * comportamiento.
     */
    @Override
    public void handleHit() {
        if(isInvincible) {
            return;
        }

        if (hasShield) {
            hasShield = false;
            currentSpeed = baseSpeed * 0.7;
            isInvincible = true;
            invincibilityTimer = INVINCIBILITY_FRAMES;
        } else {
            super.handleHit();
        }
    } // Cierre del método

    /**
     * Método que aplica la skin al jugador (en este caso, a Clyde).
     *
     * @param color El color de la skin.
     * @param speed La velocidad de la skin.
     * @param size EL tamaño de la skin.
     */
    @Override
    public void applySkin(String color, double speed, double size) {
        super.applySkin(color, speed, size);
        if(color.equalsIgnoreCase("VERDE")) {
            this.hasShield = true;
        }
    } // Cierre del método

    /**
     * Método que restaura la skin al jugador a la original (Blinky)
     * por si llega a chocar con un enemigo.
     */
    @Override
    public void resetToOriginal() {
        super.resetToOriginal();
        this.hasShield = true;
    } // Cierre del método
} // Cierre de la clase
