package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de pausa que permite reanudar la partida o volver al menu principal.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class PauseMenuState implements MenuScreenState {
    public static final String NAME = "pauseMenu";

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
     * @param context La interfaz de contexto del menu de pausa.
     * @return JPanel El panel de la pantalla.
     */
    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Juego pausado");
        JButton resumeButton = MenuStyles.primaryButton("Continuar");
        JButton mainMenuButton = MenuStyles.secondaryButton("Volver al menú principal");

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
    } // Cierre del método
} // Cierre de la clase
