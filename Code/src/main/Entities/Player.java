package main.Entities;

import main.Entities.State.NormalState;
import main.Entities.State.PlayerState;

public class Player {
    private String name;
    private String originalColor;
    private String currentColor;
    private int deaths;
    private double x, y;
    private double baseSpeed;
    private double currentSpeed;
    private double sizeMultiplier;
    private boolean hasShield; // Para el verde
    private double respawnX, respawnY;

    public Player(String name, String color, double speed, double sizeMultiplier) {
        this.name = name;
        this.originalColor = color;
        this.currentColor = color;
        this.baseSpeed = speed;
        this.currentSpeed = speed;
        this.sizeMultiplier = sizeMultiplier;
        this.deaths = 0;
        this.hasShield = color.equalsIgnoreCase("VERDE");
    }

    public void move(String direction) {
        double s = currentSpeed;
        switch (direction.toUpperCase()) {
            case "UP": y -= s; break;
            case "DOWN": y += s; break;
            case "LEFT": x -= s; break;
            case "RIGHT": x += s; break;
        }
    }

    public void applySkin(String color, double speed, double size) {
        this.currentColor = color;
        this.currentSpeed = speed;
        this.sizeMultiplier = size;
    }

    public void resetToOriginal() {
        this.currentColor = originalColor;
        this.currentSpeed = baseSpeed;
        this.sizeMultiplier = (originalColor.equalsIgnoreCase("AZUL")) ? 1.5 : 1.0;
        this.hasShield = originalColor.equalsIgnoreCase("VERDE");
    }

    public void handleHit() {
        if (currentColor.equalsIgnoreCase("VERDE") && hasShield) {
            hasShield = false;
            currentSpeed = 0.7; // Baja velocidad a 0.7x
            System.out.println("¡Escudo roto! Velocidad reducida.");
        } else {
            die();
        }
    }

    private void die() {
        deaths++;
        resetToOriginal();
        this.x = respawnX;
        this.y = respawnY;
        System.out.println("Muerte registrada. Reapareciendo...");
    }

    public void setRespawnPoint(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void resetPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public String getName() { return name; }
    public String getColor() { return currentColor; }
    public int getDeaths() { return deaths; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getSizeMultiplier() { return sizeMultiplier; }
}
