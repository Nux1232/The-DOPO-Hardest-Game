package main.UI.Observer;

import main.Core.GameEngine;

/**
 * Clase que muestra el tiempo del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */

public class TimerDisplay implements GameObserver {

    /**
     * Método que actualiza el estado del juego para mostrar
     * el tiempo restante.
     */
    @Override
    public void update() {
        GameEngine engine = GameEngine.getInstance();
        int time = engine.getRemainingTime();
        int minutes = time / 60;
        int seconds = time % 60;
        
        // En una UI real, actualizaría un componente visual del cronómetro
        // System.out.printf("Tiempo restante: %02d:%02d\n", minutes, seconds);
    } // Cierre del método
} // Cierre de la clase
