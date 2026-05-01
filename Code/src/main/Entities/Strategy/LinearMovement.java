package main.Entities.Strategy;

import main.Entities.Enemy;

public class LinearMovement implements MovementStrategy {
    private double speed;
    private boolean horizontal;
    private int direction = 1; // 1 o -1

    public LinearMovement(double speed, boolean horizontal) {
        this.speed = speed;
        this.horizontal = horizontal;
    }

    @Override
    public void move(Enemy enemy) {
        if (horizontal) {
            enemy.setX(enemy.getX() + (speed * direction));
            // Rebote simple en bordes de pantalla (800x600) por ahora
            if (enemy.getX() > 780 || enemy.getX() < 0) {
                direction *= -1;
            }
        } else {
            enemy.setY(enemy.getY() + (speed * direction));
            if (enemy.getY() > 580 || enemy.getY() < 0) {
                direction *= -1;
            }
        }
    }
}
