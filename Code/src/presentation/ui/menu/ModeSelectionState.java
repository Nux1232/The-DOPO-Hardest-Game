package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de selección de modalidad.
 */
public class ModeSelectionState implements MenuScreenState {
    public static final String NAME = "modeSelection";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("The DOPO Hardest Game");
        JButton playerButton = MenuStyles.primaryButton("Player");
        JButton pvpButton = MenuStyles.disabledButton("Player vs Player");
        JButton pvmButton = MenuStyles.disabledButton("Player vs Machine - Proximamente");
        JButton loadButton = MenuStyles.secondaryButton("Cargar Partida");
        JButton exitButton = MenuStyles.secondaryButton("Salir del juego");

        playerButton.addActionListener(event -> {
            context.getMenuData().setSelectedMode("Player");
            context.changeState(new PlayerCustomizationState());
        });
        loadButton.addActionListener(event -> context.loadSavedGame());
        exitButton.addActionListener(event -> context.exitGame());

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(playerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(pvpButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(pvmButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(loadButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(exitButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
}
