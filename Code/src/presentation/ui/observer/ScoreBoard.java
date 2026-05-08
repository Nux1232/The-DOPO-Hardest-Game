package presentation.ui.observer;

import domain.core.GameEngine;
import domain.entities.Player;

/**
 * Clase que implementa la puntuación del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */

public class ScoreBoard implements GameObserver {

    /**
     * Método que actualiza el estado del juego para mostrar
     * la puntuación.
     */
    @Override
    public void update() {
        GameEngine engine = GameEngine.getInstance();
        System.out.println("=== TABLERO DE PUNTUACIÓN ===");
        for (Player p : engine.getPlayers()) {
            // En una UI real, aquí se actualizarían etiquetas de texto (Labels)
            // System.out.println("Jugador: " + p.getName() + " | Muertes: " + p.getDeaths());
        }
        // Mostrar también monedas recolectadas si el nivel está cargado
    } // Cierre del método
} // Cierre de la clase
