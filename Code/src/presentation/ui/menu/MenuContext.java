package presentation.ui.menu;

/**
 * Contrato que usan los estados del menú para cambiar de pantalla o iniciar el juego.
 */
public interface MenuContext {
    MenuData getMenuData();
    void changeState(MenuScreenState state);
    void startSelectedGame();
    void startNextLevel();
    void loadSavedGame();
    void resumeGame();
    void returnToMainMenu();
    void exitGame();
}
