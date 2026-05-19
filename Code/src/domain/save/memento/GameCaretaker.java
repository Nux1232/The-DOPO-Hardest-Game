package domain.save.memento;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Clase responsable de guardar y cargar fotografias de partida
 * usando el patron de diseno Memento.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class GameCaretaker {
    private GameMemento lastMemento;

    /**
     * Metodo que permite cargar un nivel.
     *
     * @param file El archivo que se va a cargar.
     * @return GameMemento El nivel cargado.
     * @throws IOException La excepcion a atender.
     * @throws ClassNotFoundException La excepcion a atender.
     */
    public GameMemento load(File file) throws IOException, ClassNotFoundException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".dat")) {
            return loadDat(file);
        }
        return loadText(file);
    } // Cierre del metodo

    /**
     * Metodo que permite guardar un nivel.
     *
     * @param file El archivo que se va a guardar.
     * @param memento El nivel que se va a guardar.
     * @throws IOException La excepcion a atender.
     */
    public void save(File file, GameMemento memento) throws IOException {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".dat")) {
            saveDat(file, memento);
            return;
        }
        saveText(file, memento);
    } // Cierre del metodo

    /**
     * Metodo que permite cargar un nivel a partir de un archivo .dat.
     *
     * @param file EL archivo que se va a cargar.
     * @return GameMemento El nivel cargado.
     * @throws IOException La excepcion que se va a atender
     * @throws ClassNotFoundException La excepcion que se va a atender.
     */
    private GameMemento loadDat(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (GameMemento) input.readObject();
        }
    } // Cierre del metodo

    /**
     * Metodo que permite guardar un nivel a partir de un archivo .dat.
     *
     * @param file EL archivo que se va a guardar.
     * @param memento El nivel que se va a guardar.
     * @throws IOException La excepcion que se va a atender.
     */
    private void saveDat(File file, GameMemento memento) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(memento);
        }
    } // Cierre del metodo

    /**
     * Metodo que permite cargar un nivel a partir de un archivo .txt.
     *
     * @param file El archivo que se va a cargar.
     * @return GameMemento El nivel cargado.
     * @throws IOException La excepcion que se va a atender.
     */
    private GameMemento loadText(File file) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        }

        String mode = properties.getProperty("mode", "Player");
        String skin = properties.getProperty("skin", "BLINKY");
        Color borderColor = parseColor(properties.getProperty("borderColor", "YELLOW"));
        String secondSkin = properties.getProperty("secondSkin", "BLINKY");
        Color secondBorderColor = parseColor(properties.getProperty("secondBorderColor", "BLACK"));
        File levelFile = new File(properties.getProperty("levelFile", "src/resources/configuration1.txt"));
        int remainingTime = parseInt(properties.getProperty("remainingTime"), -1);
        List<GameMemento.PlayerSnapshot> snapshots = loadPlayerSnapshots(properties);
        return new GameMemento(mode, skin, borderColor, secondSkin, secondBorderColor, levelFile,
                remainingTime, snapshots);
    } // Cierre del metodo

    /**
     * Metodo que permite guardar un nivel a partir de un archivo .txt.
     *
     * @param file El archivo que se va a guardar.
     * @param memento El nivel que se va a guardar.
     * @throws IOException La excepcion que se va a atender.
     */
    private void saveText(File file, GameMemento memento) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("mode", memento.getMode());
        properties.setProperty("skin", memento.getSkin());
        properties.setProperty("borderColor", colorName(memento.getBorderColor()));
        properties.setProperty("secondSkin", memento.getSecondSkin());
        properties.setProperty("secondBorderColor", colorName(memento.getSecondBorderColor()));
        properties.setProperty("levelFile", memento.getLevelFile().getPath());
        properties.setProperty("remainingTime", String.valueOf(memento.getRemainingTime()));
        properties.setProperty("playerCount", String.valueOf(memento.getPlayerSnapshots().size()));
        for (int i = 0; i < memento.getPlayerSnapshots().size(); i++) {
            savePlayerSnapshot(properties, "player" + i + ".", memento.getPlayerSnapshots().get(i));
        }
        try (FileOutputStream output = new FileOutputStream(file)) {
            properties.store(output, "The DOPO Hardest Game save");
        }
    } // Cierre del metodo

    private void savePlayerSnapshot(Properties properties, String prefix, GameMemento.PlayerSnapshot snapshot) {
        properties.setProperty(prefix + "skin", snapshot.getSkin());
        properties.setProperty(prefix + "borderColor", colorName(snapshot.getBorderColor()));
        properties.setProperty(prefix + "x", String.valueOf(snapshot.getX()));
        properties.setProperty(prefix + "y", String.valueOf(snapshot.getY()));
        properties.setProperty(prefix + "speed", String.valueOf(snapshot.getCurrentSpeed()));
        properties.setProperty(prefix + "size", String.valueOf(snapshot.getSizeMultiplier()));
        properties.setProperty(prefix + "respawnX", String.valueOf(snapshot.getRespawnX()));
        properties.setProperty(prefix + "respawnY", String.valueOf(snapshot.getRespawnY()));
        properties.setProperty(prefix + "deaths", String.valueOf(snapshot.getDeaths()));
        properties.setProperty(prefix + "coins", joinIntegers(snapshot.getCollectedCoins()));
        properties.setProperty(prefix + "hasShield", String.valueOf(snapshot.hasShield()));
        properties.setProperty(prefix + "invincible", String.valueOf(snapshot.isInvincible()));
        properties.setProperty(prefix + "invincibilityTimer", String.valueOf(snapshot.getInvincibilityTimer()));
    } // Cierre del metodo

    private List<GameMemento.PlayerSnapshot> loadPlayerSnapshots(Properties properties) {
        int playerCount = parseInt(properties.getProperty("playerCount"), 0);
        List<GameMemento.PlayerSnapshot> snapshots = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            String prefix = "player" + i + ".";
            snapshots.add(new GameMemento.PlayerSnapshot(
                    properties.getProperty(prefix + "skin", "BLINKY"),
                    parseColor(properties.getProperty(prefix + "borderColor", "BLACK")),
                    parseDouble(properties.getProperty(prefix + "x"), 0),
                    parseDouble(properties.getProperty(prefix + "y"), 0),
                    parseDouble(properties.getProperty(prefix + "speed"), 1.25),
                    parseDouble(properties.getProperty(prefix + "size"), 1.0),
                    parseDouble(properties.getProperty(prefix + "respawnX"), 0),
                    parseDouble(properties.getProperty(prefix + "respawnY"), 0),
                    parseInt(properties.getProperty(prefix + "deaths"), 0),
                    parseIntegerSet(properties.getProperty(prefix + "coins", "")),
                    Boolean.parseBoolean(properties.getProperty(prefix + "hasShield", "false")),
                    Boolean.parseBoolean(properties.getProperty(prefix + "invincible", "false")),
                    parseInt(properties.getProperty(prefix + "invincibilityTimer"), 0)));
        }
        return snapshots;
    } // Cierre del metodo

    private String joinIntegers(Set<Integer> values) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Integer value : values) {
            if (!first) {
                builder.append(",");
            }
            builder.append(value);
            first = false;
        }
        return builder.toString();
    } // Cierre del metodo

    private Set<Integer> parseIntegerSet(String value) {
        Set<Integer> result = new HashSet<>();
        if (value == null || value.trim().isEmpty()) {
            return result;
        }
        for (String part : value.split(",")) {
            result.add(parseInt(part.trim(), 0));
        }
        return result;
    } // Cierre del metodo

    /**
     * Metodo que permite cambiar un color.
     *
     * @param value El color que se va a cambiar.
     * @return Color El color cambiado.
     */
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
    } // Cierre del metodo

    /**
     * Metodo que permite cambiar un color a partir de su nombre.
     *
     * @param color El color que se va a cambiar.
     * @return String El nombre del color.
     */
    private String colorName(Color color) {
        if (Color.BLACK.equals(color)) return "BLACK";
        if (Color.WHITE.equals(color)) return "WHITE";
        if (Color.MAGENTA.equals(color)) return "MAGENTA";
        return "YELLOW";
    } // Cierre del metodo

    private int parseInt(String value, int defaultValue) {
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    } // Cierre del metodo

    private double parseDouble(String value, double defaultValue) {
        try {
            return value == null ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    } // Cierre del metodo

    /**
     * Metodo que permite guardar un memento.
     *
     * @param memento El memento que se va a guardar.
     */
    public void saveMemento(GameMemento memento) {
        this.lastMemento = memento;
    } // Cierre del metodo

    /**
     * Metodo que obtiene el ultimo memento guardado.
     *
     * @return GameMemento El ultimo memento guardado.
     */
    public GameMemento getLastMemento() {
        return lastMemento;
    } // Cierre del metodo
} // Cierre de la clase
