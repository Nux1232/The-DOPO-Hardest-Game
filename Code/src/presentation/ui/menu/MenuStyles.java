package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Estilos compartidos para las pantallas del menú.
 */
class MenuStyles {
    private static final Dimension BUTTON_SIZE = new Dimension(300, 46);

    private MenuStyles() {
    }

    static JPanel basePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(238, 241, 246));
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));
        return panel;
    }

    static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    static JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    static JButton primaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(30, 90, 190));
        button.setForeground(Color.WHITE);
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(45, 45, 45));
        button.setForeground(Color.WHITE);
        return button;
    }

    static JButton disabledButton(String text) {
        JButton button = baseButton(text);
        button.setEnabled(false);
        button.setBackground(new Color(190, 194, 204));
        button.setForeground(new Color(80, 80, 80));
        return button;
    }

    private static JButton baseButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(BUTTON_SIZE);
        button.setMaximumSize(BUTTON_SIZE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        return button;
    }
}
