package main.Entities.Strategy;

import main.Entities.Enemy;

public class PatrolMovement implements MovementStrategy {
    private double speed = 1.0;
    private double startX, startY;
    private double range = 100.0;
    private int direction = 1;
    private boolean firstRun = true;

    @Override
    public void move(Enemy enemy) {
        if (firstRun) {
            startX = enemy.getX();
            startY = enemy.getY();
            firstRun = false;
        }

        enemy.setX(enemy.getX() + (speed * direction));
        if (Math.abs(enemy.getX() - startX) > range) {
            direction *= -1;
        }
    }
}
