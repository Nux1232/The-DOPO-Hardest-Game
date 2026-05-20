package domain.level.builder;

import domain.level.Level;

/**
 * Interfaz que construye un nivel del juego usando el patron de diseño Builder.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 19/05/2026
 */

public interface LevelBuilder {
    LevelBuilder reset(int id);
    LevelBuilder setTimeLimit(int seconds);
    LevelBuilder setStartPoint(int x, int y);
    LevelBuilder addSafeZone(int x, int y, boolean isFinal);
    LevelBuilder addEnemy(String type, int x, int y);
    LevelBuilder addCoin(int x, int y, String type);
    LevelBuilder addWall(int x, int y, int width, int height);
    LevelBuilder addBomb(int x, int y);
    Level build();
} // Cierre de la interfaz
