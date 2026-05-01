package main.Level.Builder;

import main.Level.Level;
import main.Entities.Factory.EnemyFactory;
import main.Entities.Coin;
import java.awt.Point;

public class ConcreteLevelBuilder implements LevelBuilder {
    private Level level;

    @Override
    public LevelBuilder reset(int id) {
        this.level = new Level(id);
        return this;
    }

    @Override
    public LevelBuilder setTimeLimit(int seconds) {
        level.setTimeLimit(seconds);
        return this;
    }

    @Override
    public LevelBuilder setStartPoint(int x, int y) {
        level.setStartPoint(new Point(x, y));
        return this;
    }

    @Override
    public LevelBuilder addSafeZone(int x, int y, boolean isFinal) {
        if (isFinal) {
            level.setFinalSafeZone(new Point(x, y));
        } else {
            level.setIntermediateSafeZone(new Point(x, y));
        }
        return this;
    }

    @Override
    public LevelBuilder addEnemy(String type, int x, int y) {
        level.addEnemy(EnemyFactory.createEnemy(type, x, y));
        return this;
    }

    @Override
    public LevelBuilder addCoin(int x, int y, String type) {
        level.addCoin(new Coin(x, y, type)); 
        return this;
    }

    @Override
    public LevelBuilder addWall(int x, int y) {
        level.addWall(new Point(x, y));
        return this;
    }

    @Override
    public Level build() {
        return this.level;
    }
}
