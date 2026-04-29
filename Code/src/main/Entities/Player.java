package main.Entities;

import main.Entities.State.NormalState;
import main.Entities.State.PlayerState;

public class Player {
    private String name;
    private String color;
    private int deaths;
    private double x, y;
    private double speed;
    private double sizeMultiplier;
    private PlayerState currentState;

    public Player(String name, String color, double speed, double sizeMultiplier) {
        this.name = name;
        this.color = color;
        this.speed = speed;
        this.sizeMultiplier = sizeMultiplier;
        this.deaths = 0;
        this.currentState = new NormalState();
    }

    public void move(String direction) {
        // Lógica de movimiento
    }

    public void resetPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void incrementDeaths() {
        this.deaths++;
    }

    public String getName() { return name; }
    public String getColor() { return color; }
    public int getDeaths() { return deaths; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSpeed() { return speed; }
    public double getSizeMultiplier() { return sizeMultiplier; }
    public PlayerState getCurrentState() { return currentState; }
    public void setCurrentState(PlayerState currentState) { this.currentState = currentState; }
}
