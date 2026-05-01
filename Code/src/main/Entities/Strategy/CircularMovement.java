package main.Entities.Strategy;

import main.Entities.Enemy;

public class CircularMovement implements MovementStrategy {
    private double angle = 0;
    private double centerX, centerY;
    private double radius = 50;
    private double speed = 0.05;

    public CircularMovement(double x, double y) {
        this.centerX = x;
        this.centerY = y;
    }

    @Override
    public void move(Enemy enemy) {
        angle += speed;
        enemy.setX(centerX + Math.cos(angle) * radius);
        enemy.setY(centerY + Math.sin(angle) * radius);
    }
}
