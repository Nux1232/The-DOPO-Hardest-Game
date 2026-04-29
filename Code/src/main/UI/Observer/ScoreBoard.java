package UI.Observer;

import Core.GameEngine;
import Entities.Player;

public class ScoreBoard implements GameObserver {
    @Override
    public void update() {
        GameEngine engine = GameEngine.getInstance();
        System.out.println("=== TABLERO DE PUNTUACIÓN ===");
        for (Player p : engine.getPlayers()) {
            // En una UI real, aquí se actualizarían etiquetas de texto (Labels)
            // System.out.println("Jugador: " + p.getName() + " | Muertes: " + p.getDeaths());
        }
        // Mostrar también monedas recolectadas si el nivel está cargado
    }
}
