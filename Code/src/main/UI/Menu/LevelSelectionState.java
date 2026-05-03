package main.UI.Menu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Pantalla de selección de archivo de configuración del nivel.
 */
public class LevelSelectionState implements MenuScreenState {
    public static final String NAME = "levelSelection";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Selecciona Nivel");
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

        JButton playButton = MenuStyles.primaryButton("¡JUGAR!");
        JButton backButton = MenuStyles.secondaryButton("Volver atras");
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
    }

    private File[] findLevelFiles() {
        File resources = new File("src/resources");
        FilenameFilter txtFilter = (dir, name) -> name.toLowerCase().endsWith(".txt");
        File[] files = resources.listFiles(txtFilter);
        return files == null ? new File[0] : files;
    }
}
