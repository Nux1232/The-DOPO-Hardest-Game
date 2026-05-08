package presentation.ui.menu;

import javax.swing.JPanel;

/**
 * Estado visual del menú principal.
 */
public interface MenuScreenState {
    String getName();
    JPanel buildPanel(MenuContext context);
}
