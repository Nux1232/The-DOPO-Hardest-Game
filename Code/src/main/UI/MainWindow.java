package main.UI;

import main.Core.GameEngine;
import main.UI.Observer.ScoreBoard;
import main.UI.Observer.TimerDisplay;

import javax.swing.*;
import java.awt.BorderLayout;

/**
 * Clase que contiene la ventana principal del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */
public class MainWindow extends JFrame {
    private GamePanel gamePanel;
    private ScoreBoard scoreBoard;
    private TimerDisplay timerDisplay;

    /**
     * Constructor de la clase MainWindow.
     */
    public MainWindow() {
        setTitle("The DOPO Hardest Game");
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
    } // Cierre del constructor

    /**
     *  Método que muestra la ventana principal del juego.
     */
    public void showWindow() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    } // Cierre del método

    /**
     * Método que devuelve el panel del juego.
     * @return GamePanel El panel del juego.
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    } // Cierre del método
} // Cierre de la clase
