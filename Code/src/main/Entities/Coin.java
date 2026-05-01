package main.Entities;

public class Coin {
    private double x, y;
    private String type; // YELLOW, RED, BLUE, GREEN
    private boolean collected;

    public Coin(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.collected = false;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public String getType() { return type; }
    public boolean isCollected() { return collected; }
    public void collect() { this.collected = true; }
}
