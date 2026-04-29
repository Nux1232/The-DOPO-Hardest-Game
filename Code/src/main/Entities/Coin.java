package Entities;

public class Coin {
    private double x, y;
    private boolean collected;

    public boolean isCollected() { return collected; }
    public void collect() { this.collected = true; }
}
