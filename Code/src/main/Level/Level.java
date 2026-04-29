package Level;

import java.util.ArrayList;
import java.util.List;
import Entities.Enemy;
import Entities.Coin;
import java.awt.Point;

public class Level {
    private int id;
    private int timeLimit;
    private List<Enemy> enemies;
    private List<Coin> coins;
    private List<Point> walls; // Representación simple de obstáculos
    private Point startPoint;
    private Point intermediateSafeZone;
    private Point finalSafeZone;

    public Level(int id) {
        this.id = id;
        this.enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.timeLimit = 180; // 3 minutos
    }

    // Setters para el Builder
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
    public void setStartPoint(Point p) { this.startPoint = p; }
    public void setIntermediateSafeZone(Point p) { this.intermediateSafeZone = p; }
    public void setFinalSafeZone(Point p) { this.finalSafeZone = p; }
    public void addEnemy(Enemy e) { this.enemies.add(e); }
    public void addCoin(Coin c) { this.coins.add(c); }
    public void addWall(Point p) { this.walls.add(p); }

    // Getters...
    public int getId() { return id; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Coin> getCoins() { return coins; }
}
