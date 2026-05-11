package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de personalización del primer jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class PlayerCustomizationState implements MenuScreenState {
    public static final String NAME = "playerCustomization";

    /**
     * Método que devuelve el nombre del estado.
     *
     * @return String El nombre del estado.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Método que construye el panel de la pantalla.
     *
     * @param context La interfaz de contexto del menú de personalización.
     * @return JPanel El panel de la pantalla.
     */
    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Jugador 1");
        JLabel skinLabel = MenuStyles.sectionLabel("Skin");
        JPanel skins = new JPanel(new GridLayout(1, 3, 12, 0));
        skins.setOpaque(false);

        JToggleButton blinky = skinButton("Rojo (Blinky)", Color.RED, true);
        JToggleButton inky = skinButton("Azul (Inky)", Color.BLUE, true);
        JToggleButton clyde = skinButton("Verde (Clyde)", Color.GREEN, true);
        ButtonGroup skinGroup = new ButtonGroup();
        skinGroup.add(blinky);
        skinGroup.add(inky);
        skinGroup.add(clyde);
        blinky.setSelected(true);
        blinky.addActionListener(event -> context.getMenuData().setSelectedSkin("BLINKY"));
        inky.addActionListener(event -> context.getMenuData().setSelectedSkin("INKY"));
        clyde.addActionListener(event -> context.getMenuData().setSelectedSkin("CLYDE"));

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
        addBorderOption(context, borders, borderGroup, "Morado", Color.MAGENTA, false);

        JButton nextButton = MenuStyles.primaryButton("Siguiente");
        JButton backButton = MenuStyles.secondaryButton("Volver atrás");
        nextButton.addActionListener(event -> {
                String gameMode = context.getMenuData().getSelectedMode();
                if(gameMode.equals("Player vs Player")) {
                    context.changeState(new SecondPlayerCustomizationState());
                } else {
                    context.changeState(new LevelSelectionState());
                }
        });
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
    } // Cierre del método

    /**
     * Método privado que modela los botones de las skins.
     *
     * @param text El texto a mostrar.
     * @param color El color a elegir.
     * @param enabled Si el botón está habilitado o no.
     * @return JToggleButton El botón a habilitar.
     */
    private JToggleButton skinButton(String text, Color color, boolean enabled) {
        JToggleButton button = new JToggleButton(text);
        button.setPreferredSize(new Dimension(190, 72));
        button.setBackground(color);
        button.setForeground(color == Color.YELLOW || color == Color.WHITE ? Color.BLACK : Color.WHITE);
        button.setEnabled(enabled);
        MenuStyles.configureSelectableOption(button);
        return button;
    } // Cierre del método

    /**
     * Método que añade una opción de borde al panel.
     *
     * @param context La interfaz de contexto de la personalización del borde.
     * @param panel El panel donde esta.
     * @param group El grupo de botones.
     * @param name El nombre a poner.
     * @param color El color a elegir.
     * @param selected Si fue seleccionado o no el boton.
     */
    private void addBorderOption(MenuContext context, JPanel panel, ButtonGroup group,
                                 String name, Color color, boolean selected) {
        JToggleButton button = new JToggleButton(name);
        button.setPreferredSize(new Dimension(105, 40));
        button.setBackground(color);
        button.setForeground(color == Color.YELLOW || color == Color.WHITE ? Color.BLACK : Color.WHITE);
        MenuStyles.configureSelectableOption(button);
        button.setSelected(selected);
        button.addActionListener(event -> context.getMenuData().setSelectedBorderColor(color));
        group.add(button);
        panel.add(button);
    } // Cierre del método
} // Cierre de la clase
