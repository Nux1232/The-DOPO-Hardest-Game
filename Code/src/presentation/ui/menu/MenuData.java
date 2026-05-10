package presentation.ui.menu;

import java.awt.Color;
import java.io.File;

/**
 * Datos temporales recolectados por la interfaz antes de iniciar una partida.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class MenuData {
    private String selectedMode = "Player";
    private String selectedSkin = "BLINKY";
    private String secondSelectedSkin = "BLINKY";
    private Color selectedBorderColor = Color.YELLOW;
    private Color selectedSecondBorderColor = Color.BLACK;
    private File selectedLevelFile;
    private File selectedSaveFile;

    /**
     * Método que devuelve el modo seleccionado por el usuario.
     *
     * @return String El modo seleccionado por el usuario.
     */
    public String getSelectedMode() {
        return selectedMode;
    } // Cierre del método

    /**
     * Método que establece el modo seleccionado por el usuario.
     *
     * @param selectedMode El modo de juego seleccionado por el usuario.
     */
    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    } // Cierre del método

    /**
     * Método que obtiene la skin seleccionada por el usuario.
     *
     * @return String La skin seleccionada por el usuario.
     */
    public String getSelectedSkin() {
        return selectedSkin;
    } // Cierre del método

    /**
     * Método que establece la skin seleccionada por el usuario.
     *
     * @param selectedSkin La skin seleccionada por el usuario.
     */
    public void setSelectedSkin(String selectedSkin) {
        this.selectedSkin = selectedSkin;
    } // Cierre del método

    /**
     * Método que obtiene la skin seleccionada por el segundo usuario.
     *
     * @param selectedSkin La segunda skin seleccionada por el segundo usuario.
     */
    public void setSecondSelectedSkin(String selectedSkin) {
        this.secondSelectedSkin = selectedSkin;
    } // Cierre del método

    /**
     * Método que obtiene el borde seleccionado por el usuario.
     *
     * @return Color El color del borde seleccionado por el usuario.
     */
    public Color getSelectedBorderColor() {
        return selectedBorderColor;
    } // Cierre del método

    /**
     * Método que obtiene el borde seleccionado por el segundo usuario.
     *
     * @return Color El color del borde seleccionado por el segundo usuario.
     */
    public Color getSelectedSecondBorderColor() {
        return selectedSecondBorderColor;
    } // Cierre del método

    /**
     * Método que establece el borde seleccionado por el usuario.
     *
     * @param selectedBorderColor El color del borde seleccionado por el usuario.
     */
    public void setSelectedBorderColor(Color selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
    } // Cierre del método

    /**
     * Método que establece el borde seleccionado por el segundo usuario.
     *
     * @param selectedBorderColor El color del borde seleccionado por el segundo usuario.
     */
    public void setSelectedSecondBorderColor(Color selectedBorderColor) {
        this.selectedSecondBorderColor = selectedBorderColor;
    } // Cierre del método

    /**
     * Método que obtiene el archivo seleccionado por el usuario.
     *
     * @return File El archivo seleccionado por el usuario.
     */
    public File getSelectedLevelFile() {
        return selectedLevelFile;
    } // Cierre del método

    /**
     * Método que establece el archivo seleccionado por el usuario.
     *
     * @param selectedLevelFile El archivo seleccionado por el usuario.
     */
    public void setSelectedLevelFile(File selectedLevelFile) {
        this.selectedLevelFile = selectedLevelFile;
    } // Cierre del método

    /**
     * Método que obtiene el archivo seleccionado por el usuario.
     *
     * @return File El archivo seleccionado por el usuario.
     */
    public File getSelectedSaveFile() {
        return selectedSaveFile;
    } // Cierre del método

    /**
     * Método que establece el archivo seleccionado por el usuario.
     *
     * @param selectedSaveFile El archivo seleccionado por el usuario.
     */
    public void setSelectedSaveFile(File selectedSaveFile) {
        this.selectedSaveFile = selectedSaveFile;
    } // Cierre del método
} // Cierre de la clase
