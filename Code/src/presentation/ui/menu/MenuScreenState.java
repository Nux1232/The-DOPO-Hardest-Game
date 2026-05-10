package presentation.ui.menu;

import javax.swing.JPanel;

/**
 * Estado visual del menú principal.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public interface MenuScreenState {
    String getName();
    JPanel buildPanel(MenuContext context);
} // Cierre de la interfaz
