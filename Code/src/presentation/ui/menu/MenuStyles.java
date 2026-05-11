package presentation.ui.menu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Clase que modela los estilos compartidos para las pantallas del menú.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
class MenuStyles {
    private static final Dimension BUTTON_SIZE = new Dimension(300, 46);
    private static final Color SELECTED_OUTLINE = new Color(30, 90, 190);
    private static final Color UNSELECTED_OUTLINE = new Color(80, 86, 96);

    /**
     * El constructor privado de la clase MenuStyles (no se instancia).
     */
    private MenuStyles() {
    } // Cierre del constructor privado

    /**
     * Método que modela la base del panel principal.
     *
     * @return JPanel El panel del menu principal.
     */
    static JPanel basePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(238, 241, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        return panel;
    } // Cierre del método

    /**
     * Método que modela el titulo del juego.
     *
     * @param text El texto a usar.
     * @return JLabel El titulo del juego.
     */
    static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    } // Cierre del método

    /**
     * Método que secciona las etiquetas del menu principal.
     *
     * @param text El texto a usar.
     * @return JLabel La etiqueta de cada seccion.
     */
    static JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    } // Cierre del método

    /**
     * Método que modela los botones del menu principal.
     *
     * @param text El texto a usar.
     * @return JButton El boton del menu principal.
     */
    static JButton primaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(30, 90, 190));
        button.setForeground(Color.WHITE);
        return button;
    } // Cierre del método

    /**
     * Método que modela los botones secundarios del menu principal.
     *
     * @param text El texto a usar.
     * @return JButton El boton del menu principal.
     */
    static JButton secondaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(Color.WHITE);
        return button;
    } // Cierre del método

    /**
     * Método que modela que un boton esta deshabilitado.
     * @param text El texto a usar.
     * @return JButton El boton del menu principal.
     */
    static JButton disabledButton(String text) {
        JButton button = baseButton(text);
        button.setEnabled(false);
        button.setBackground(new Color(190, 194, 204));
        button.setForeground(new Color(80, 80, 80));
        return button;
    } // Cierre del método

    /**
     * Método que actualiza el borde visual de una opción seleccionable.
     *
     * @param button El botón a actualizar.
     */
    static void updateSelectableBorder(AbstractButton button) {
        Border border;
        if (button.isSelected()) {
            border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SELECTED_OUTLINE, 4),
                    BorderFactory.createLineBorder(Color.WHITE, 2));
        } else {
            border = BorderFactory.createLineBorder(UNSELECTED_OUTLINE, 2);
        }
        button.setBorder(border);
    } // Cierre del método

    /**
     * Método que aplica el estilo base a una opción seleccionable.
     *
     * @param button El botón a configurar.
     */
    static void configureSelectableOption(AbstractButton button) {
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.addItemListener(event -> updateSelectableBorder(button));
        updateSelectableBorder(button);
    } // Cierre del método

    /**
     * Método que modela el boton principal.
     * @param text El texto a usar.
     * @return JButton El boton del menu principal.
     */
    private static JButton baseButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        return button;
    } // Cierre del método
} // Cierre de la clase
