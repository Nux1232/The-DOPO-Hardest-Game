package domain.entities.state;

/**
 * Clase que representa el estado penalizado del jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class PenalizedState implements PlayerState {

    /**
     * Método que maneja la entrada del jugador en este estado.
     */
    @Override
    public void handleInput() {
        /* Movimiento lento */
    } // Cierre del método

    /**
     * Método que obtiene la velocidad del jugador en este estado.
     *
     * @return double La velocidad del jugador en este estado.
     */
    @Override
    public double getSpeedMultiplier() {
        return 0.7;
    } // Cierre del método
} // Cierre de la clase
