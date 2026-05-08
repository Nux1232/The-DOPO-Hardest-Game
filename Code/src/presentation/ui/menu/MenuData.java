package presentation.ui.menu;

import java.awt.Color;
import java.io.File;

/**
 * Datos temporales recolectados por la interfaz antes de iniciar una partida.
 */
public class MenuData {
    private String selectedMode = "Player";
    private String selectedSkin = "BLINKY";
    private Color selectedBorderColor = Color.YELLOW;
    private File selectedLevelFile;
    private File selectedSaveFile;

    public String getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    }

    public String getSelectedSkin() {
        return selectedSkin;
    }

    public void setSelectedSkin(String selectedSkin) {
        this.selectedSkin = selectedSkin;
    }

    public Color getSelectedBorderColor() {
        return selectedBorderColor;
    }

    public void setSelectedBorderColor(Color selectedBorderColor) {
        this.selectedBorderColor = selectedBorderColor;
    }

    public File getSelectedLevelFile() {
        return selectedLevelFile;
    }

    public void setSelectedLevelFile(File selectedLevelFile) {
        this.selectedLevelFile = selectedLevelFile;
    }

    public File getSelectedSaveFile() {
        return selectedSaveFile;
    }

    public void setSelectedSaveFile(File selectedSaveFile) {
        this.selectedSaveFile = selectedSaveFile;
    }
}
