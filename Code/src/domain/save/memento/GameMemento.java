package domain.save.memento;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;

/**
 * Clase que permite guardar una partida usando el patron de diseño Memento.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class GameMemento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String mode;
    private final String skin;
    private final Color borderColor;
    private final File levelFile;

    /**
     * Constructor de la clase GameMemento.
     *
     * @param mode El modo seleccionado por el usuario.
     * @param skin La skin seleccionada por el usuario.
     * @param borderColor El color del borde seleccionado por el usuario.
     * @param levelFile El archivo del nivel seleccionado por el usuario.
     */
    public GameMemento(String mode, String skin, Color borderColor, File levelFile) {
        this.mode = mode;
        this.skin = skin;
        this.borderColor = borderColor;
        this.levelFile = levelFile;
    } // Cierre del constructor

    /**
     * Método que obtiene el modeo de juego.
     *
     * @return String El modo de juego.
     */
    public String getMode() {
        return mode;
    } // Cierre del método

    /**
     * Método que obtiene la skin seleccionada por el usuario.
     *
     * @return La skin seleccioanda.
     */
    public String getSkin() {
        return skin;
    } // Cierre del método

    /**
     * Método que obtiene el color del borde seleccionado por el usuario.
     *
     * @return El color del borde.
     */
    public Color getBorderColor() {
        return borderColor;
    } // Cierre del método

    /**
     * Método que obtiene el archivo del nivel seleccionado por el usuario.
     *
     * @return El archivo del nivel.
     */
    public File getLevelFile() {
        return levelFile;
    } // Cierre del método
} // Cierre de la clase
