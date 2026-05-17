package presentation.ui;

import domain.core.GameState;
import domain.core.TheDopoHardestGame;
import domain.core.TheDopoHardestGameLogger;
import domain.entities.Player;
import domain.entities.factory.PlayerFactory;
import domain.exceptions.TheDopoHardestGameException;
import domain.level.GameConfiguration;
import domain.level.Level;
import presentation.ui.menu.MenuContext;
import presentation.ui.menu.MenuData;
import presentation.ui.menu.MenuScreenState;
import presentation.ui.menu.ModeSelectionState;
import presentation.ui.menu.LevelCompletedState;
import presentation.ui.menu.PauseMenuState;
import presentation.ui.observer.ScoreBoard;
import presentation.ui.observer.TimerDisplay;
import domain.save.memento.GameCaretaker;
import domain.save.memento.GameMemento;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Clase que contiene la ventana principal del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
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
    private boolean timeExpiredScreenVisible;
    private GameMemento pendingSavedGame;
    private final TheDopoHardestGame game = new TheDopoHardestGame();
    private GameMemento pendingSavedGame;

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
        game.addObserver(this::showTimeExpiredIfNeeded);
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
     * Método que sobreescribe la obtencion de los datos del menu.
     *
     * @return MenuData Los datos del menu.
     */
    @Override
    public MenuData getMenuData() {
        return menuData;
    } // Cierre del método

    /**
     * Método que cambia el estado del menu.
     *
     * @param state El nuevo estado del menu.
     */
    @Override
    public void changeState(MenuScreenState state) {
        menuPanel.add(state.buildPanel(this), state.getName());
        menuLayout.show(menuPanel, state.getName());
        rootLayout.show(rootPanel, "menu");
        revalidate();
        repaint();
    } // Cierre del método

    /**
     * Método que inicia el juego.
     */
    @Override
    public void startSelectedGame() {
        try{
            File levelFile = menuData.getSelectedLevelFile();
            if (levelFile == null) {
                JOptionPane.showMessageDialog(this, "No hay un archivo de nivel seleccionado.",
                        "Nivel requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            game.clearPlayers();
            game.getPlayers().clear();
            game.setGameMode(menuData.getSelectedMode());
            Player player = PlayerFactory.createPlayer("Jugador 1", menuData.getSelectedSkin());
            player.setBorderColor(menuData.getSelectedBorderColor());
            game.addPlayer(player);

            if(menuData.getSelectedMode().equals("Player vs Player")) {
                Player secondPlayer = PlayerFactory.createPlayer("Jugador 2", menuData.getSecondSelectedSkin());
                secondPlayer.setBorderColor(menuData.getSelectedSecondBorderColor());
                game.addPlayer(secondPlayer);
            }

            GameConfiguration configuration = new GameConfiguration(levelFile.getPath());
            levelCompletedScreenVisible = false;
            timeExpiredScreenVisible = false;
            game.startGame(configuration, levelFile, pendingSavedGame);
            pendingSavedGame = null;

            rootLayout.show(rootPanel, "game");
            SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
        } catch (TheDopoHardestGameException e) {
            pendingSavedGame = null;
            JOptionPane.showMessageDialog(this, "Error al iniciar el juego: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            TheDopoHardestGameLogger.getInstance().logException(e);
        }
    } // Cierre del método

    /**
     * Método que permite ir al siguiente nivel.
     */
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
    } // Cierre del método

    /**
     * Método privado que encuentra el siguiente nivel.
     *
     * @return File el siguiente nivel.
     */
    private File findNextLevelFile() {
        File currentFile = menuData.getSelectedLevelFile();
        File resources = getLevelResourcesDirectory();
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
    } // Cierre del método

    /**
     * Método privado que encuentra el primer nivel disponible.
     *
     * @return File El primer nivel.
     */
    private File findFirstLevelFile() {
        File resources = getLevelResourcesDirectory();
        File[] files = resources.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null || files.length == 0) {
            return null;
        }

        java.util.Arrays.sort(files, java.util.Comparator.comparing(File::getName));
        return files[0];
    } // Cierre del método

    /**
     * Método privado que muestra el pantalla de nivel completado si es necesario.
     */
    private void showLevelCompletedIfNeeded() {
        if (game.getGameState() != GameState.VICTORY || levelCompletedScreenVisible) {
            return;
        }

        levelCompletedScreenVisible = true;
        SwingUtilities.invokeLater(() -> {
            changeState(new LevelCompletedState());
        });
    } // Cierre del método

    /**
     * Método privado que muestra la pantalla de derrota cuando se acaba el tiempo.
     */
    private void showTimeExpiredIfNeeded() {
        if (game.getGameState() != GameState.GAME_OVER || timeExpiredScreenVisible) {
            return;
        }

        timeExpiredScreenVisible = true;
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Se acabó el tiempo, has perdido :(",
                    "Tiempo agotado",
                    JOptionPane.INFORMATION_MESSAGE);

            File firstLevelFile = findFirstLevelFile();
            if (firstLevelFile == null) {
                returnToMainMenu();
                return;
            }

            menuData.setSelectedLevelFile(firstLevelFile);
            startSelectedGame();
        });
    } // Cierre del método

    /**
     * Método que enseña el menu de pausa.
     */
    private void showPauseMenu() {
        if (game.getGameState() != GameState.PLAYING) {
            return;
        }

        game.pauseGame();
        changeState(new PauseMenuState());
    } // Cierre del método

    @Override
    public void saveGame() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Guardar partida");
        chooser.setSelectedFile(new File("partida.txt"));
        int result = chooser.showSaveDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            game.saveGame(chooser.getSelectedFile());

            JOptionPane.showMessageDialog(this,
                    "Partida guardada correctamente.",
                    "Guardado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (TheDopoHardestGameException exception) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la partida: " + exception.getMessage(),
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE);
        }
    } // Cierre del método

    /**
     * Método que permite reanudar el juego.
     */
    @Override
    public void resumeGame() {
        game.resumeGame();
        rootLayout.show(rootPanel, "game");
        revalidate();
        repaint();
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    } // Cierre del método

    /**
     * Método que permite regresar al menu principal.
     */
    @Override
    public void saveGame() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Guardar partida");
        chooser.setSelectedFile(new File("partida.txt"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            game.saveGame(chooser.getSelectedFile());
            JOptionPane.showMessageDialog(this,
                    "Partida guardada correctamente.",
                    "Guardado",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (TheDopoHardestGameException exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la partida: " + exception.getMessage(),
                    "Error de guardado",
                    JOptionPane.ERROR_MESSAGE);
        }
    } // Cierre del metodo

    @Override
    public void returnToMainMenu() {
        game.endGame();
        changeState(new ModeSelectionState());
    } // Cierre del método

    /**
     * Método que permite salir del juego.
     */
    @Override
    public void exitGame() {
        game.endGame();
        dispose();
        System.exit(0);
    } // Cierre del método

    /**
     * Método que permite cargar el estado de un juego guardado.
     */
    @Override
    public void loadSavedGame() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Cargar partida");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        System.out.println("INICIO loadSavedGame");

        try {
            File selectedFile = chooser.getSelectedFile();
<<<<<<< HEAD
            GameMemento memento = game.loadLevel(selectedFile);
=======
            GameMemento memento = game.loadGame(selectedFile);
            if (memento == null || memento.getLevelFile() == null) {
                throw new TheDopoHardestGameException(TheDopoHardestGameException.LOAD_GAME_ERROR);
            }
>>>>>>> 866e39b8af658a9ef8959226695cffba8989a796
            menuData.setSelectedMode(memento.getMode());
            menuData.setSelectedSkin(memento.getSkin());
            menuData.setSelectedBorderColor(memento.getBorderColor());
            menuData.setSecondSelectedSkin(memento.getSecondSkin());
            menuData.setSelectedSecondBorderColor(memento.getSecondBorderColor());
            menuData.setSelectedLevelFile(memento.getLevelFile());
            pendingSavedGame = memento;
<<<<<<< HEAD
        } catch (TheDopoHardestGameException exception) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la partida: " + exception.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE);
            TheDopoHardestGameLogger.getInstance().logException(exception);
        } catch (IOException | ClassNotFoundException exception) {
=======
            startSelectedGame();
        } catch (TheDopoHardestGameException | IOException | ClassNotFoundException exception) {
            pendingSavedGame = null;
>>>>>>> 866e39b8af658a9ef8959226695cffba8989a796
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la partida: " + exception.getMessage(),
                    "Error de carga",
                    JOptionPane.ERROR_MESSAGE);
        }
        startSelectedGame();
    } // Cierre del método

    /**
     * Método que obtiene un juego.
     *
     * @return TheDopoHardestGame El juego.
     */
    @Override
    public TheDopoHardestGame getGame() {
        return game;
    } // Cierre del método
<<<<<<< HEAD

    public GamePanel getGamePanel() {
        return gamePanel;
    } // Cierre del método

    private File getLevelResourcesDirectory() {
        File sourceResources = new File("src/resources");

        if (sourceResources.isDirectory()) {
            return sourceResources;
        }

        return new File("resources");
    }
=======
    private File getLevelResourcesDirectory() {
        File sourceResources = new File("src/resources");
        if (sourceResources.isDirectory()) {
            return sourceResources;
        }
        return new File("resources");
    } // Cierre del metodo
>>>>>>> 866e39b8af658a9ef8959226695cffba8989a796
} // Cierre de la clase
