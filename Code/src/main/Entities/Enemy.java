package main.Entities;

import main.Entities.Strategy.MovementStrategy;

public class Enemy {
    private double x, y;
    private MovementStrategy movementStrategy;

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (movementStrategy != null) {
            movementStrategy.move(this);
        }
    }

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
