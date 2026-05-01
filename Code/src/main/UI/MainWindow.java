package main.UI;

import main.Core.GameEngine;
import main.UI.Observer.ScoreBoard;
import main.UI.Observer.TimerDisplay;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class MainWindow extends JFrame {
    private GamePanel gamePanel;
    private ScoreBoard scoreBoard;
    private TimerDisplay timerDisplay;

    public MainWindow() {
        setTitle("The World's Hardest Game - DOPO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.gamePanel = new GamePanel();
        this.scoreBoard = new ScoreBoard();
        this.timerDisplay = new TimerDisplay();

        add(gamePanel, BorderLayout.CENTER);

        // Registrar observadores
        GameEngine engine = GameEngine.getInstance();
        engine.addObserver(scoreBoard);
        engine.addObserver(timerDisplay);
        engine.addObserver(gamePanel);
    }

    public void showWindow() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        System.out.println("Ventana principal visible.");
    }
}
