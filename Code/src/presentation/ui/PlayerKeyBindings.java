package presentation.ui;

import java.util.HashMap;
import java.util.Map;
import java.awt.event.KeyEvent;

/**
 * Clase que implementa las combinaciones de teclas del jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
 */

public class PlayerKeyBindings {
    private final Map<Integer, String> keyBinds;

    /**
     * Constructor de la clase PlayerKeyBindings.
     *
     * @param up La direccion de movimiento del jugador hacia arriba.
     * @param down La direccion de movimiento del jugador hacia abajo.
     * @param left La direccion de movimiento del jugador hacia la izquierda.
     * @param right La direccion de movimiento del jugador hacia la derecha.
     */
    public PlayerKeyBindings(int up, int down, int left, int right) {
        this.keyBinds = new HashMap<>();
        keyBinds.put(up, "UP");
        keyBinds.put(down, "DOWN");
        keyBinds.put(left, "LEFT");
        keyBinds.put(right, "RIGHT");
    } // Cierre del constructor

    /**
     * Método que instancia la clase con las teclas de flechas.
     *
     * @return PlayerKeyBindings La instancia de la clase PlayerKeyBindings.
     */
    public static PlayerKeyBindings forArrowKeys() {
        return new PlayerKeyBindings(KeyEvent.VK_UP, KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
    } // Cierre del método

    /**
     * Método que instancia la clase con las teclas WASD.
     *
     * @return PlayerKeyBindings La instancia de la clase PlayerKeyBindings.
     */
    public static PlayerKeyBindings forWASD() {
        return new PlayerKeyBindings(KeyEvent.VK_W, KeyEvent.VK_S,
                KeyEvent.VK_A, KeyEvent.VK_D);
    } // Cierre del método

    /**
     * Método que obtiene la dirección a donde el jugador va a ir.
     *
     * @param keyCode La tecla presionada.
     * @return String La direccion a donde ira.
     */
    public String getDirection(int keyCode) {
        return keyBinds.get(keyCode);
    } // Cierre del método

} // Cierre de la clase
