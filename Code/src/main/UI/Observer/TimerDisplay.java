package main.UI.Observer;

import main.Core.GameEngine;

public class TimerDisplay implements GameObserver {
    @Override
    public void update() {
        GameEngine engine = GameEngine.getInstance();
        int time = engine.getRemainingTime();
        int minutes = time / 60;
        int seconds = time % 60;
        
        // En una UI real, actualizaría un componente visual del cronómetro
        // System.out.printf("Tiempo restante: %02d:%02d\n", minutes, seconds);
    }
}
