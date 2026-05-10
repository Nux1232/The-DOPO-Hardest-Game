package presentation.ui.menu;

import domain.core.TheDopoHardestGame;
import domain.entities.Player;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla que se muestra cuando el jugador completa un nivel.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class LevelCompletedState implements MenuScreenState {
    public static final String NAME = "levelCompleted";

    /**
     * Método que devuelve el nombre del estado.
     *
     * @return String El nombre del estado.
     */
    @Override
    public String getName() {
        return NAME;
    } // Cierre del método

    /**
     * Método que construye el panel de la pantalla.
     *
     * @param context La interfaz de contexto del menú.
     * @return JPanel El panel de la pantalla.
     */
    @Override
    public JPanel buildPanel(MenuContext context) {
        TheDopoHardestGame game = context.getGame();
        Player winner = game.getWinner();
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Nivel completado");
        JLabel winnerLabel = MenuStyles.title("Ganador: " + winner.getName());
        JButton nextLevelButton = MenuStyles.primaryButton("Siguiente nivel");
        JButton retryButton = MenuStyles.secondaryButton("Reintentar");
        JButton mainMenuButton = MenuStyles.secondaryButton("Volver al menú principal");

        nextLevelButton.addActionListener(event -> context.startNextLevel());
        retryButton.addActionListener(event -> context.startSelectedGame());
        mainMenuButton.addActionListener(event -> context.returnToMainMenu());

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(winnerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(nextLevelButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(retryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(mainMenuButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    } // Cierre del método
} // Cierre de la clase
