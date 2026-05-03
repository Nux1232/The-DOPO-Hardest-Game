package main.UI.Menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de pausa que permite reanudar la partida o volver al menu principal.
 */
public class PauseMenuState implements MenuScreenState {
    public static final String NAME = "pauseMenu";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Juego en pausa");
        JButton resumeButton = MenuStyles.primaryButton("Continuar");
        JButton mainMenuButton = MenuStyles.secondaryButton("Volver al menu principal");

        resumeButton.addActionListener(event -> context.resumeGame());
        mainMenuButton.addActionListener(event -> context.returnToMainMenu());

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(resumeButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(mainMenuButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
}
