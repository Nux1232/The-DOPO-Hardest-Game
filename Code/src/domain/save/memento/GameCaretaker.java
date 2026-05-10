package domain.save.memento;

import java.awt.Color;
import java.io.*;
import java.util.Properties;

/**
 * Clase responsable de guardar y cargar fotografías de partida
 * usando el patrón de diseño Memento.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */
public class GameCaretaker {
    private GameMemento lastMemento;

    /**
     * Método que permite cargar un nivel.
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
    } // Cierre del método

    /**
     * Método que permite guardar un nivel.
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
    } // Cierre del método

    /**
     * Método que permite cargar un nivel a partir de un archivo .dat.
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
    } // Cierre del método

    /**
     * Método que permite guardar un nivel a partir de un archivo .dat.
     *
     * @param file EL archivo que se va a guardar.
     * @param memento El nivel que se va a guardar.
     * @throws IOException La excepcion que se va a atender.
     */
    private void saveDat(File file, GameMemento memento) throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(memento);
        }
    } // Cierre del método

    /**
     * Método que permite cargar un nivel a partir de un archivo .txt.
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
        File levelFile = new File(properties.getProperty("levelFile", "src/resources/configuration1.txt"));
        return new GameMemento(mode, skin, borderColor, levelFile);
    } // Cierre del método

    /**
     * Método que permite guardar un nivel a partir de un archivo .txt.
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
        properties.setProperty("levelFile", memento.getLevelFile().getPath());
        try (FileOutputStream output = new FileOutputStream(file)) {
            properties.store(output, "The DOPO Hardest Game save");
        }
    } // Cierre del método

    /**
     * Método que permite cambiar un color.
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
    } // Cierre del método

    /**
     * Método que permite cambiar un color a partir de su nombre.
     *
     * @param color El color que se va a cambiar.
     * @return String El nombre del color.
     */
    private String colorName(Color color) {
        if (Color.BLACK.equals(color)) return "BLACK";
        if (Color.WHITE.equals(color)) return "WHITE";
        if (Color.MAGENTA.equals(color)) return "MAGENTA";
        return "YELLOW";
    } // Cierre del método

    /**
     * Método que permite guardar un memento.
     *
     * @param memento El memento que se va a guardar.
     */
    public void saveMemento(GameMemento memento) {
        this.lastMemento = memento;
    } // Cierre del método

    /**
     * Método que obtiene el último memento guardado.
     *
     * @return GameMemento El último memento guardado.
     */
    public GameMemento getLastMemento() {
        return lastMemento;
    } // Cierre del método
} // Cierre de la clase
