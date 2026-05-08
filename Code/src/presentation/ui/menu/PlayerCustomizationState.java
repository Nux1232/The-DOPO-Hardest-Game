package presentation.ui.menu;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Pantalla de personalización del Jugador 1.
 */
public class PlayerCustomizationState implements MenuScreenState {
    public static final String NAME = "playerCustomization";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Jugador 1");
        JLabel skinLabel = MenuStyles.sectionLabel("Skin");
        JPanel skins = new JPanel(new GridLayout(1, 3, 12, 0));
        skins.setOpaque(false);

        JToggleButton blinky = skinButton("Rojo (Blinky)", Color.RED, true);
        JToggleButton inky = skinButton("Azul (Inky) - Bloqueado", Color.BLUE, false);
        JToggleButton clyde = skinButton("Verde (Clyde) - Bloqueado", Color.GREEN, false);
        ButtonGroup skinGroup = new ButtonGroup();
        skinGroup.add(blinky);
        skinGroup.add(inky);
        skinGroup.add(clyde);
        blinky.setSelected(true);
        blinky.addActionListener(event -> context.getMenuData().setSelectedSkin("BLINKY"));

        skins.add(blinky);
        skins.add(inky);
        skins.add(clyde);

        JLabel borderLabel = MenuStyles.sectionLabel("Color del borde");
        JPanel borders = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        borders.setOpaque(false);
        ButtonGroup borderGroup = new ButtonGroup();
        addBorderOption(context, borders, borderGroup, "Amarillo", Color.YELLOW, true);
        addBorderOption(context, borders, borderGroup, "Negro", Color.BLACK, false);
        addBorderOption(context, borders, borderGroup, "Blanco", Color.WHITE, false);
        addBorderOption(context, borders, borderGroup, "Magenta", Color.MAGENTA, false);

        JButton nextButton = MenuStyles.primaryButton("Siguiente");
        JButton backButton = MenuStyles.secondaryButton("Volver atrás");
        nextButton.addActionListener(event -> context.changeState(new LevelSelectionState()));
        backButton.addActionListener(event -> context.changeState(new ModeSelectionState()));

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(skinLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(skins);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(borderLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(borders);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(nextButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JToggleButton skinButton(String text, Color color, boolean enabled) {
        JToggleButton button = new JToggleButton(text);
        button.setPreferredSize(new Dimension(190, 72));
        button.setBackground(color);
        button.setForeground(color == Color.YELLOW || color == Color.WHITE ? Color.BLACK : Color.WHITE);
        button.setFocusPainted(false);
        button.setEnabled(enabled);
        return button;
    }

    private void addBorderOption(MenuContext context, JPanel panel, ButtonGroup group, String name, Color color, boolean selected) {
        JToggleButton button = new JToggleButton(name);
        button.setPreferredSize(new Dimension(105, 40));
        button.setBackground(color);
        button.setForeground(color == Color.YELLOW || color == Color.WHITE ? Color.BLACK : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.DARK_GRAY, 2));
        button.setSelected(selected);
        button.addActionListener(event -> context.getMenuData().setSelectedBorderColor(color));
        group.add(button);
        panel.add(button);
    }
}
