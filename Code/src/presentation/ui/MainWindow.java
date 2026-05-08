package presentation.ui;

import domain.core.GameEngine;
import domain.core.GameState;
import domain.core.TheDopoHardestGame;
import domain.entities.Player;
import domain.entities.factory.PlayerFactory;
import domain.level.GameConfiguration;
import domain.level.Level;
import domain.save.memento.GameCaretaker;
import domain.save.memento.GameMemento;
import presentation.ui.menu.MenuContext;
import presentation.ui.menu.MenuData;
import presentation.ui.menu.MenuScreenState;
import presentation.ui.menu.ModeSelectionState;
import presentation.ui.menu.LevelCompletedState;
import presentation.ui.menu.PauseMenuState;
import presentation.ui.observer.ScoreBoard;
import presentation.ui.observer.TimerDisplay;

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
    private final TheDopoHardestGame game = new TheDopoHardestGame();

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

        this.gamePanel = new GamePanel(game);
        this.gamePanel.setPauseAction(this::showPauseMenu);
        this.scoreBoard = new ScoreBoard();
        this.timerDisplay = new TimerDisplay();

        rootPanel.add(menuPanel, "menu");
        rootPanel.add(gamePanel, "game");
        add(rootPanel, BorderLayout.CENTER);
        changeState(new ModeSelectionState());

        // Registrar observadores
        game.addObserver(scoreBoard);
        game.addObserver(timerDisplay);
        game.addObserver(this::showLevelCompletedIfNeeded);
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
        try{
            File levelFile = menuData.getSelectedLevelFile();
            if (levelFile == null) {
                JOptionPane.showMessageDialog(this, "No hay un archivo de nivel seleccionado.", "Nivel requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            game.clearPlayers();
            game.getPlayers().clear();
            game.setGameMode(menuData.getSelectedMode());
            Player player = PlayerFactory.createPlayer("Jugador 1", menuData.getSelectedSkin());
            player.setBorderColor(menuData.getSelectedBorderColor());
            game.addPlayer(player);

            GameConfiguration configuration = new GameConfiguration(levelFile.getPath());
            Level level = configuration.buildLevel(1);
            levelCompletedScreenVisible = false;
            game.loadLevel(levelFile);
            game.startGame(configuration);

            rootLayout.show(rootPanel, "game");
            SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al iniciar el juego: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        if (game.getGameState() != GameState.VICTORY || levelCompletedScreenVisible) {
            return;
        }

        levelCompletedScreenVisible = true;
        SwingUtilities.invokeLater(() -> {
            gamePanel.clearPressedKeys();
            changeState(new LevelCompletedState());
        });
    }

    private void showPauseMenu() {
        if (game.getGameState() != GameState.PLAYING) {
            return;
        }

        game.pauseGame();
        gamePanel.clearPressedKeys();
        changeState(new PauseMenuState());
    }

    @Override
    public void resumeGame() {
        game.resumeGame();
        rootLayout.show(rootPanel, "game");
        revalidate();
        repaint();
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }

    @Override
    public void returnToMainMenu() {
        game.endGame();
        gamePanel.clearPressedKeys();
        changeState(new ModeSelectionState());
    }

    @Override
    public void exitGame() {
        game.endGame();
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
            game.loadLevel(selectedFile);
            startSelectedGame();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la partida: " + exception.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
} // Cierre de la clase
