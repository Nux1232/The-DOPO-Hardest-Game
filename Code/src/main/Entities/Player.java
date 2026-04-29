package Entities;

import Entities.State.PlayerState;

public class Player {
    private String name;
    private String color;
    private int deaths;
    private double x, y;
    private double speed;
    private PlayerState currentState;

    public Player(String name, String color) {
        this.name = name;
        this.color = color;
        this.deaths = 0;
        // Valores iniciales según el color/tipo se manejarán en la Factory
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

    // Getters y setters...
}
