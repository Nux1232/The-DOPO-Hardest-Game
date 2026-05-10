package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de selección de modalidad.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class ModeSelectionState implements MenuScreenState {
    public static final String NAME = "modeSelection";

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
     * @param context La interfaz de contexto del menu.
     * @return JPanel El panel de la pantalla.
     */
    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("The DOPO Hardest Game");
        JButton playerButton = MenuStyles.primaryButton("Player");
        JButton pvpButton = MenuStyles.primaryButton("Player vs Player");
        JButton pvmButton = MenuStyles.disabledButton("Player vs Machine - Proximamente");
        JButton loadButton = MenuStyles.secondaryButton("Cargar Partida");
        JButton exitButton = MenuStyles.secondaryButton("Salir del juego");

        playerButton.addActionListener(event -> {
            context.getMenuData().setSelectedMode("Player");
            context.changeState(new PlayerCustomizationState());
        });

        pvpButton.addActionListener(event -> {
            context.getMenuData().setSelectedMode("Player vs Player");
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
    } // Cierre del método
} // Cierre de la clase
