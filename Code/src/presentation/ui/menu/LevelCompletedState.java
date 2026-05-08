package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla que se muestra cuando el jugador completa un nivel.
 */
public class LevelCompletedState implements MenuScreenState {
    public static final String NAME = "levelCompleted";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Nivel completado");
        JButton nextLevelButton = MenuStyles.primaryButton("Siguiente nivel");
        JButton retryButton = MenuStyles.secondaryButton("Reintentar");
        JButton mainMenuButton = MenuStyles.secondaryButton("Volver al menu principal");

        nextLevelButton.addActionListener(event -> context.startNextLevel());
        retryButton.addActionListener(event -> context.startSelectedGame());
        mainMenuButton.addActionListener(event -> context.returnToMainMenu());

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(nextLevelButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(retryButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(mainMenuButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
}
