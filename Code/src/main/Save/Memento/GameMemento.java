package main.Save.Memento;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;

/**
 * Fotografía simple de una partida guardada.
 */
public class GameMemento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String mode;
    private final String skin;
    private final Color borderColor;
    private final File levelFile;

    public GameMemento(String mode, String skin, Color borderColor, File levelFile) {
        this.mode = mode;
        this.skin = skin;
        this.borderColor = borderColor;
        this.levelFile = levelFile;
    }

    public String getMode() {
        return mode;
    }

    public String getSkin() {
        return skin;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public File getLevelFile() {
        return levelFile;
    }
}
