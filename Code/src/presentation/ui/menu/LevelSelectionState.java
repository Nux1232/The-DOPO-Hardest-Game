package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Pantalla de selección de archivo de configuración del nivel.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class LevelSelectionState implements MenuScreenState {
    public static final String NAME = "levelSelection";

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
        JLabel title = MenuStyles.title("Selección del Nivel");
        DefaultListModel<File> model = new DefaultListModel<>();
        for (File file : findLevelFiles()) {
            model.addElement(file);
        }

        JList<File> levelList = new JList<>(model);
        levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        levelList.setCellRenderer((list, value, index, selected, hasFocus) -> {
            JLabel label = new JLabel(value.getName());
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            label.setBackground(selected ? new Color(40, 90, 180) : Color.WHITE);
            label.setForeground(selected ? Color.WHITE : Color.BLACK);
            return label;
        });
        if (!model.isEmpty()) {
            levelList.setSelectedIndex(0);
            context.getMenuData().setSelectedLevelFile(model.firstElement());
        }
        levelList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                context.getMenuData().setSelectedLevelFile(levelList.getSelectedValue());
            }
        });

        JScrollPane scrollPane = new JScrollPane(levelList);
        scrollPane.setPreferredSize(new Dimension(430, 220));
        scrollPane.setMaximumSize(new Dimension(430, 220));

        JButton playButton = MenuStyles.primaryButton("Jugar");
        JButton backButton = MenuStyles.secondaryButton("Volver atrás");
        playButton.addActionListener(event -> context.startSelectedGame());
        backButton.addActionListener(event -> context.changeState(new PlayerCustomizationState()));

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 28)));
        panel.add(playButton);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));
        panel.add(backButton);
        panel.add(Box.createVerticalGlue());
        return panel;
    } // Cierre del método

    /**
     * Método privado que busca los archivos de los niveles
     * .
     * @return File[] Los archivos de los niveles.
     */
    private File[] findLevelFiles() {
        File resources = new File("src/resources");
        FilenameFilter txtFilter = (dir, name) -> name.toLowerCase().endsWith(".txt");
        File[] files = resources.listFiles(txtFilter);
        return files == null ? new File[0] : files;
    } // Cierre del método
} // Cierre de la clase
