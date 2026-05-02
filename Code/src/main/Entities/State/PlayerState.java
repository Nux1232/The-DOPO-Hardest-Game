package main.Entities.State;

/**
 * Interfaz que representa el estado del jugador usando el patron de diseño State.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */
public interface PlayerState {
    void handleInput();
    double getSpeedMultiplier();
} // Cierre de la interfaz
