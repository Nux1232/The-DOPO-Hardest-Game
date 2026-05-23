package presentation.ui;

/**
 * Clase abstracta que representa a un observador del estado del juego en la capa de presentación.
 * Permite que los componentes de la interfaz de usuario se registren para recibir actualizaciones.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 23/05/2026
 */
public abstract class GameObserver {
    /**
     * Método que se ejecuta cuando el estado del juego se actualiza.
     */
    public abstract void update();
}
