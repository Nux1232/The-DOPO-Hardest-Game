package Entities;

import Entities.Strategy.MovementStrategy;

public class Enemy {
    private double x, y;
    private MovementStrategy movementStrategy;

    public void update() {
        if (movementStrategy != null) {
            movementStrategy.move();
        }
    }
}
