package main.Level.Builder;

import main.Level.Level;

public interface LevelBuilder {
    LevelBuilder reset(int id);
    LevelBuilder setTimeLimit(int seconds);
    LevelBuilder setStartPoint(int x, int y);
    LevelBuilder addSafeZone(int x, int y, boolean isFinal);
    LevelBuilder addEnemy(String type, int x, int y);
    LevelBuilder addCoin(int x, int y, String type);
    LevelBuilder addWall(int x, int y);
    Level build();
}
