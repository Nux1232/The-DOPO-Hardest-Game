package main.UI;

import main.Core.GameEngine;
import main.UI.Observer.ScoreBoard;
import main.UI.Observer.TimerDisplay;

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
