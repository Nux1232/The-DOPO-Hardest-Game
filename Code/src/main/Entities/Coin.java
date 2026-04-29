package main.Entities;

public class Coin {
    private double x, y;
    private boolean collected;

    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
        this.collected = false;
    }

    public boolean isCollected() { return collected; }
    public void collect() { this.collected = true; }
    public double getX() { return x; }
    public double getY() { return y; }
}
