package domain.entities;

/**
 * Clase hija que representa a una bomba del juego, es un elemento especial.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 19/05/2026
 */

public class Bomb extends SpecialElement {
    /**
     * El constructor de la clase Bomb.
     *
     * @param x La coordenada x donde se encuentra la bomba.
     * @param y La coordenada y donde se encuentra la bomba.
     */
    public Bomb(double x, double y) {
        super(x, y, 20, 20, "BLACK");
    } // Cierre del constructor

    @Override
    public void onPlayerContact(Player player) {
        player.handleHit();
        deactivate();
    } // Cierre del método

    public void onEnemyContact(Enemy enemy) {
        enemy.die();
        deactivate();
    } // Cierre del método
} // Cierre de la clase