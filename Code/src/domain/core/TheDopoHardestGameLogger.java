package domain.core;

import domain.exceptions.TheDopoHardestGameException;
import java.io.IOException;
import java.util.logging.*;
import java.io.File;

/**
 * Clase que registra las excepciones generadas en un log.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 16/05/2026
 */
public class TheDopoHardestGameLogger {
    private static TheDopoHardestGameLogger instance;
    private final Logger logger;

    /**
     * El constructor de la clase TheDopoHardestGameLogger.
     *
     */
    private TheDopoHardestGameLogger() {
        File logs = new File("logs");
        if(!logs.exists()) {
            logs.mkdirs();
        }

        logger = Logger.getLogger(TheDopoHardestGameLogger.class.getName());
        try {
            FileHandler handler = new FileHandler("logs/game.log", true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
        } catch(IOException exception) {
            logger.warning("Archivo de registro no creado");
        }
    } // Cierre del constructor

    /**
     * Método que obtiene la instancia de la clase logger.
     *
     * @return TheDopoHardestGameLogger La instancia de la clase logger.
     */
    public static TheDopoHardestGameLogger getInstance() {
        if (instance == null) {
            instance = new TheDopoHardestGameLogger();
        }
        return instance;
    } // Cierre del método

    /**
     * Método que obtiene la excepción y la registra en el log.
     *
     * @param exception La excepción obtenida y a registrar.
     */
    public void logException(TheDopoHardestGameException exception) {
        logger.log(Level.SEVERE, exception.getMessage(), exception);
    } // Cierre del método
} // Cierre de la clase
