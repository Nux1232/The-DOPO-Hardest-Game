package Level.Builder;

import Level.Level;
import Entities.Factory.EnemyFactory;
import Entities.Coin;
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
    public LevelBuilder addCoin(int x, int y) {
        // En una implementación real, Coin también podría tener una posición
        level.addCoin(new Coin()); 
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
