package main.UI;

import main.Core.GameEngine;
import main.Core.GameState;
import main.Entities.Player;
import main.Entities.Factory.PlayerFactory;
import main.Level.GameConfiguration;
import main.Level.Level;
import main.Save.Memento.GameCaretaker;
import main.Save.Memento.GameMemento;
import main.UI.Menu.MenuContext;
import main.UI.Menu.MenuData;
import main.UI.Menu.MenuScreenState;
import main.UI.Menu.ModeSelectionState;
import main.UI.Menu.LevelCompletedState;
import main.UI.Menu.PauseMenuState;
import main.UI.Observer.ScoreBoard;
import main.UI.Observer.TimerDisplay;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Clase que contiene la ventana principal del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */
public class MainWindow extends JFrame implements MenuContext {
    private GamePanel gamePanel;
    private ScoreBoard scoreBoard;
    private TimerDisplay timerDisplay;
    private JPanel rootPanel;
    private JPanel menuPanel;
    private CardLayout rootLayout;
    private CardLayout menuLayout;
    private MenuData menuData;
    private boolean levelCompletedScreenVisible;

    /**
     * Constructor de la clase MainWindow.
     */
    public MainWindow() {
        setTitle("The DOPO Hardest Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.rootLayout = new CardLayout();
        this.menuLayout = new CardLayout();
        this.rootPanel = new JPanel(rootLayout);
        this.menuPanel = new JPanel(menuLayout);
        this.menuData = new MenuData();

        this.gamePanel = new GamePanel();
        this.gamePanel.setPauseAction(this::showPauseMenu);
        this.scoreBoard = new ScoreBoard();
        this.timerDisplay = new TimerDisplay();

        rootPanel.add(menuPanel, "menu");
        rootPanel.add(gamePanel, "game");
        add(rootPanel, BorderLayout.CENTER);
        changeState(new ModeSelectionState());

        // Registrar observadores
        GameEngine engine = GameEngine.getInstance();
        engine.addObserver(scoreBoard);
        engine.addObserver(timerDisplay);
        engine.addObserver(this::showLevelCompletedIfNeeded);
    } // Cierre del constructor

    /**
     *  Método que muestra la ventana principal del juego.
     */
    public void showWindow() {
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    } // Cierre del método

    /**
     * Método que devuelve el panel del juego.
     * @return GamePanel El panel del juego.
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    } // Cierre del método

    @Override
    public MenuData getMenuData() {
        return menuData;
    }

    @Override
    public void changeState(MenuScreenState state) {
        menuPanel.add(state.buildPanel(this), state.getName());
        menuLayout.show(menuPanel, state.getName());
        rootLayout.show(rootPanel, "menu");
        revalidate();
        repaint();
    }

    @Override
    public void startSelectedGame() {
        File levelFile = menuData.getSelectedLevelFile();
        if (levelFile == null) {
            JOptionPane.showMessageDialog(this, "No hay un archivo de nivel seleccionado.", "Nivel requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GameEngine engine = GameEngine.getInstance();
        engine.getPlayers().clear();
        Player player = PlayerFactory.createPlayer("Jugador 1", menuData.getSelectedSkin());
        player.setBorderColor(menuData.getSelectedBorderColor());
        engine.getPlayers().add(player);

        GameConfiguration configuration = new GameConfiguration(levelFile.getPath());
        Level level = configuration.buildLevel(1);
        levelCompletedScreenVisible = false;
        engine.loadLevel(level);
        engine.startGame();

        rootLayout.show(rootPanel, "game");
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }

    @Override
    public void startNextLevel() {
        File nextLevelFile = findNextLevelFile();
        if (nextLevelFile == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay un siguiente nivel disponible.",
                    "Nivel no disponible",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        menuData.setSelectedLevelFile(nextLevelFile);
        startSelectedGame();
    }

    private File findNextLevelFile() {
        File currentFile = menuData.getSelectedLevelFile();
        File resources = new File("src/resources");
        File[] files = resources.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (currentFile == null || files == null || files.length == 0) {
            return null;
        }

        java.util.Arrays.sort(files, java.util.Comparator.comparing(File::getName));
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().equals(currentFile.getName()) && i + 1 < files.length) {
                return files[i + 1];
            }
        }
        return null;
    }

    private void showLevelCompletedIfNeeded() {
        if (GameEngine.getInstance().getCurrentState() != GameState.VICTORY || levelCompletedScreenVisible) {
            return;
        }

        levelCompletedScreenVisible = true;
        SwingUtilities.invokeLater(() -> {
            gamePanel.clearPressedKeys();
            changeState(new LevelCompletedState());
        });
    }

    private void showPauseMenu() {
        GameEngine engine = GameEngine.getInstance();
        if (engine.getCurrentState() != GameState.PLAYING) {
            return;
        }

        engine.pauseGame();
        gamePanel.clearPressedKeys();
        changeState(new PauseMenuState());
    }

    @Override
    public void resumeGame() {
        GameEngine.getInstance().resumeGame();
        rootLayout.show(rootPanel, "game");
        revalidate();
        repaint();
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }

    @Override
    public void returnToMainMenu() {
        GameEngine.getInstance().returnToMenu();
        gamePanel.clearPressedKeys();
        changeState(new ModeSelectionState());
    }

    @Override
    public void exitGame() {
        GameEngine.getInstance().stopGame();
        dispose();
        System.exit(0);
    }

    @Override
    public void loadSavedGame() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Cargar partida");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            File selectedFile = chooser.getSelectedFile();
            GameMemento memento = new GameCaretaker().load(selectedFile);
            menuData.setSelectedSaveFile(selectedFile);
            menuData.setSelectedMode(memento.getMode());
            menuData.setSelectedSkin(memento.getSkin());
            menuData.setSelectedBorderColor(memento.getBorderColor());
            menuData.setSelectedLevelFile(memento.getLevelFile());
            startSelectedGame();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la partida: " + exception.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} // Cierre de la clase
