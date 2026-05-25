package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla que se muestra cuando se acaba el tiempo (Game Over).
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 23/05/2026
 */
public class TimeExpiredState implements MenuScreenState {
    public static final String NAME = "timeExpired";

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
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Se acabó el tiempo");
        JLabel subtitle = MenuStyles.title("¡GAME OVER!");
        subtitle.setForeground(Color.RED);

        JButton restartButton = MenuStyles.primaryButton("Reiniciar");
        JButton mainMenuButton = MenuStyles.secondaryButton("Volver al menú principal");

        restartButton.addActionListener(event -> context.restartTimeExpiredGame());
        mainMenuButton.addActionListener(event -> context.returnToMainMenu());

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitle);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(restartButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(mainMenuButton);
        panel.add(Box.createVerticalGlue());

        return panel;
    } // Cierre del método
} // Cierre de la clase
