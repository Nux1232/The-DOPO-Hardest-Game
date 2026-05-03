package main.UI.Menu;

import javax.swing.JPanel;

/**
 * Estado visual del menú principal.
 */
public interface MenuScreenState {
    String getName();
    JPanel buildPanel(MenuContext context);
}
