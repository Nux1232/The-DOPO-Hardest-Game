package domain.level;

import domain.level.builder.ConcreteLevelBuilder;
import domain.level.builder.LevelBuilder;
import java.io.BufferedReader;
import java.io.FileReader;
import domain.exceptions.TheDopoHardestGameException;

/**
 * Clase que contiene la configuracion del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class GameConfiguration {
    private final String filePath;

    /**
     * Constructor de la clase GameConfiguration.
     *
     * @param filePath La ruta del archivo donde se encuentran las configuraciones de algun nivel.
     */
    public GameConfiguration(String filePath) {
        this.filePath = filePath;
    } // Cierre del constructor

    /**
     * Método que construye un nivel a partir de su id.
     *
     * @param id El identificador de un nivel.
     * @return Level El nivel construido.
     */
    public Level buildLevel(int id) throws TheDopoHardestGameException {
        LevelBuilder builder = new ConcreteLevelBuilder();
        builder.reset(id);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                switch(parts[0].toUpperCase()) {
                    case "TIME":
                        builder.setTimeLimit(Integer.parseInt(parts[1]));
                        break;
                    case "START":
                        builder.setStartPoint(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;
                    case "INTERMEDIATE_ZONE":
                        builder.addSafeZone(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), false);
                        break;
                    case "FINAL_ZONE":
                        builder.addSafeZone(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), true);
                        break;
                    case "WALL":
                        builder.addWall(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
                                Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                        break;
                    case "COIN":
                        builder.addCoin(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
                        break;
                    case "ENEMY":
                        builder.addEnemy(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                        break;
                    case "LIFESOURCE":
                        builder.addLifeSource(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;

                    case "BOMB":
                        builder.addBomb(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        break;

                    default:
                        throw new IllegalArgumentException("Configuración desconocida: " + parts[0]);
                }
            }
        } catch (Exception exception) {
            throw new TheDopoHardestGameException(TheDopoHardestGameException.LEVEL_LOAD_ERROR + ": " + exception.getMessage());
        }
        return builder.build();
    } // Cierre del método
} // Cierre de la clase
