package domain.entities.state;

/**
 * Clase que representa el estado normal del jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class NormalState implements PlayerState {

    /**
     * Método que maneja el estado normal del jugador.
     */
    @Override
    public void handleInput() {
        /* Movimiento normal */
    } // Cierre del método

    /**
     * Método que obtiene la velocidad del jugador.
     *
     * @return double La velocidad del jugador.
     */
    @Override
    public double getSpeedMultiplier() {
        return 1.0;
    } // Cierre del método
} // Cierre de la clase
