package presentation.ui.menu;

import domain.core.TheDopoHardestGame;

/**
 * Contrato que usan los estados del menú para cambiar de pantalla o iniciar el juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
 */
public interface MenuContext {
    MenuData getMenuData();
    void changeState(MenuScreenState state);
    void startSelectedGame();
    void startNextLevel();
    void loadSavedGame();
    void saveGame();
    void resumeGame();
    void returnToMainMenu();
    TheDopoHardestGame getGame();
    void exitGame();
} // Cierre de la interfaz
