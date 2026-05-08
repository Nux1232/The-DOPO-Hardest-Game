package domain.save.memento;

import java.awt.Color;
import java.io.*;
import java.util.Properties;

/**
 * Responsable de guardar y cargar fotografías de partida.
 */
public class GameCaretaker {
    private GameMemento lastMemento;

    public GameMemento load(File file) throws IOException, ClassNotFoundException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".dat")) {
            return loadDat(file);
        }
        return loadText(file);
    }

    public void save(File file, GameMemento memento) throws IOException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".dat")) {
            saveDat(file, memento);
            return;
        }
        saveText(file, memento);
    }

    private GameMemento loadDat(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (GameMemento) input.readObject();
        }
    }

    private void saveDat(File file, GameMemento memento) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(memento);
        }
    }

    private GameMemento loadText(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        }

        String mode = properties.getProperty("mode", "Player");
        String skin = properties.getProperty("skin", "BLINKY");
        Color borderColor = parseColor(properties.getProperty("borderColor", "YELLOW"));
        File levelFile = new File(properties.getProperty("levelFile", "src/resources/configuration1.txt"));
        return new GameMemento(mode, skin, borderColor, levelFile);
    }

    private void saveText(File file, GameMemento memento) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("mode", memento.getMode());
        properties.setProperty("skin", memento.getSkin());
        properties.setProperty("borderColor", colorName(memento.getBorderColor()));
        properties.setProperty("levelFile", memento.getLevelFile().getPath());
        try (FileOutputStream output = new FileOutputStream(file)) {
            properties.store(output, "The DOPO Hardest Game save");
        }
    }

    private Color parseColor(String value) {
        switch (value.toUpperCase()) {
            case "BLACK":
            case "NEGRO":
                return Color.BLACK;
            case "WHITE":
            case "BLANCO":
                return Color.WHITE;
            case "MAGENTA":
                return Color.MAGENTA;
            case "YELLOW":
            case "AMARILLO":
            default:
                return Color.YELLOW;
        }
    }

    private String colorName(Color color) {
        if (Color.BLACK.equals(color)) return "BLACK";
        if (Color.WHITE.equals(color)) return "WHITE";
        if (Color.MAGENTA.equals(color)) return "MAGENTA";
        return "YELLOW";
    }

    public void saveMemento(GameMemento memento) {
        this.lastMemento = memento;
    }

    public GameMemento getLastMemento() {
        return lastMemento;
    }
}
