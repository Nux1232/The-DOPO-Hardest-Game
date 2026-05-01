package main.Entities.Strategy;

import main.Entities.Enemy;

public class VerticalMovement implements MovementStrategy {
    private double speed;
    private int direction = 1;

    public VerticalMovement(double speed) {
        this.speed = speed;
    }

    @Override
    public void move(Enemy enemy) {
        enemy.setY(enemy.getY() + (speed * direction));
        if (enemy.getY() > 580 || enemy.getY() < 0) {
            direction *= -1;
        }
    }
}
