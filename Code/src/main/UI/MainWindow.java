package UI;

import Core.GameEngine;
import UI.Observer.ScoreBoard;
import UI.Observer.TimerDisplay;

public class MainWindow {
    private GamePanel gamePanel;
    private ScoreBoard scoreBoard;
    private TimerDisplay timerDisplay;

    public MainWindow() {
        this.gamePanel = new GamePanel();
        this.scoreBoard = new ScoreBoard();
        this.timerDisplay = new TimerDisplay();

        // Registrar observadores en el motor
        GameEngine engine = GameEngine.getInstance();
        engine.addObserver(scoreBoard);
        engine.addObserver(timerDisplay);
    }

    public void show() {
        System.out.println("Ventana principal iniciada.");
        // Aquí iría la lógica para mostrar el JFrame o Stage de JavaFX
    }
}
