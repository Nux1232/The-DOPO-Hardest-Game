package presentation.ui.menu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

/**
 * Pantalla de seleccion de archivo de configuracion del nivel.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
 */
public class LevelSelectionState implements MenuScreenState {
    public static final String NAME = "levelSelection";

    /**
     * Metodo que devuelve el nombre del estado.
     *
     * @return String El nombre del estado.
     */
    @Override
    public String getName() {
        return NAME;
    } // Cierre del metodo

    /**
     * Metodo que construye el panel de la pantalla.
     *
     * @param context La interfaz de contexto del menu.
     * @return JPanel El panel de la pantalla.
     */
    @Override
    public JPanel buildPanel(MenuContext context) {
        JPanel panel = MenuStyles.basePanel();
        JLabel title = MenuStyles.title("Seleccion del Nivel");
        JPanel levelButtons = new JPanel();
        levelButtons.setLayout(new BoxLayout(levelButtons, BoxLayout.Y_AXIS));
        levelButtons.setOpaque(false);
        ButtonGroup levelGroup = new ButtonGroup();
        File[] levelFiles = findLevelFiles();

        for (int i = 0; i < levelFiles.length; i++) {
            File levelFile = levelFiles[i];
            JToggleButton levelButton = new JToggleButton(formatLevelName(levelFile.getName()));
            levelButton.setPreferredSize(new Dimension(390, 42));
            levelButton.setMaximumSize(new Dimension(390, 42));
            levelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            MenuStyles.configureSelectableOption(levelButton);
            levelButton.addActionListener(event -> context.getMenuData().setSelectedLevelFile(levelFile));
            levelGroup.add(levelButton);
            levelButtons.add(levelButton);
            levelButtons.add(Box.createRigidArea(new Dimension(0, 8)));
            if (i == 0) {
                levelButton.setSelected(true);
                context.getMenuData().setSelectedLevelFile(levelFile);
            }
        }

        JScrollPane scrollPane = new JScrollPane(levelButtons);
        scrollPane.setPreferredSize(new Dimension(430, 220));
        scrollPane.setMaximumSize(new Dimension(430, 220));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton playButton = MenuStyles.primaryButton("Jugar");
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
    } // Cierre del metodo

    /**
     * Metodo privado que busca los archivos de los niveles.
     *
     * @return File[] Los archivos de los niveles.
     */
    private File[] findLevelFiles() {
        File resources = getLevelResourcesDirectory();
        FilenameFilter txtFilter = (dir, name) -> name.toLowerCase().endsWith(".txt");
        File[] files = resources.listFiles(txtFilter);
        if (files == null) {
            return new File[0];
        }
        Arrays.sort(files, java.util.Comparator.comparing(File::getName));
        return files;
    } // Cierre del metodo

    private File getLevelResourcesDirectory() {
        File sourceResources = new File("src/resources");
        if (sourceResources.isDirectory()) {
            return sourceResources;
        }
        return new File("resources");
    } // Cierre del metodo

    /**
     * Metodo privado que formatea el nombre del archivo para mostrarlo limpio en el menu.
     *
     * @param fileName El nombre del archivo con su extension.
     * @return String El nombre formateado para la interfaz.
     */
    private String formatLevelName(String fileName) {
        String nameWithoutExt = fileName.replaceFirst("[.][^.]+$", "");
        if (nameWithoutExt.toLowerCase().startsWith("configuration")) {
            return nameWithoutExt.toLowerCase().replace("configuration", "Nivel ");
        } else if (nameWithoutExt.length() > 0) {
            return nameWithoutExt.substring(0, 1).toUpperCase() + nameWithoutExt.substring(1);
        }
        return nameWithoutExt;
    } // Cierre del metodo
} // Cierre de la clase