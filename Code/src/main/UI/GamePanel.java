package UI;

import Core.GameEngine;
import UI.Observer.GameObserver;

/**
 * GamePanel - El área donde se dibuja el juego.
 * También actúa como observador para redibujar el escenario.
 */
public class GamePanel implements GameObserver {
    
    public GamePanel() {
        // Registrarse en el motor
        GameEngine.getInstance().addObserver(this);
    }

    @Override
    public void update() {
        // Lógica de redibujado (ej. repaint() en Swing)
        // 1. Dibujar paredes
        // 2. Dibujar monedas
        // 3. Dibujar enemigos
        // 4. Dibujar jugadores
    }
}
