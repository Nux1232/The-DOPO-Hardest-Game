package Entities.Strategy;

public class LinearMovement implements MovementStrategy {
    private double speed;
    private boolean horizontal;

    public LinearMovement(double speed, boolean horizontal) {
        this.speed = speed;
        this.horizontal = horizontal;
    }

    @Override
    public void move() {
        // Lógica de rebote en paredes se implementará con acceso al mapa
    }
}
