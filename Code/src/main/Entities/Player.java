package main.Entities;

import java.awt.Color;

/**
 * Clase abstracta que representa a un jugador.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public abstract class Player {
    private String name;
    private String originalColor;
    private String currentColor;
    private Color borderColor;
    private int deaths;
    private double x, y;
    protected double baseSpeed;
    protected double currentSpeed;
    private double sizeMultiplier;
    protected boolean hasShield = false; // Para el verde
    protected boolean isInvincible;
    protected int invincibilityTimer;
    private double respawnX, respawnY;

    /**
     * Constructor de la clase Player.
     *
     * @param name EL nombre del jugador.
     * @param color El color del jugador.
     * @param speed La velocidad del jugador.
     * @param sizeMultiplier El tamaño del jugador.
     */
    public Player(String name, String color, double speed, double sizeMultiplier) {
        this.name = name;
        this.originalColor = color;
        this.currentColor = color;
        this.borderColor = Color.BLACK;
        this.baseSpeed = speed;
        this.currentSpeed = speed;
        this.sizeMultiplier = sizeMultiplier;
        this.deaths = 0;
        this.hasShield = color.equalsIgnoreCase("VERDE");
        this.isInvincible = false;
        this.invincibilityTimer = 0;
    } // Cierre del constructor

    /**
     * Método que permite mover al jugador a una direccion dada.
     *
     * @param direction La direccion deseada por el jugador.
     */
    public void move(String direction) {
        double s = currentSpeed;

        switch (direction.toUpperCase()) {
            case "UP":
                y -= s;
                break;
            case "DOWN":
                y += s;
                break;
            case "LEFT":
                x -= s;
                break;
            case "RIGHT":
                x += s;
                break;
        }
    } // Cierre del método

    /**
     * Método que aplica la skin a un jugador.
     *
     * @param color El color de la skin.
     * @param speed La velocidad de la skin.
     * @param size EL tamaño de la skin.
     */
    public void applySkin(String color, double speed, double size) {
        this.currentColor = color;
        this.currentSpeed = speed;
        this.sizeMultiplier = size;
        this.hasShield = color.equalsIgnoreCase("VERDE");
    } // Cierre del método

    /**
     * Método que reinicia al jugador a la skin por defecto (Blinky).
     */
    public void resetToOriginal() {
        this.currentColor = originalColor;
        this.currentSpeed = baseSpeed;
        this.sizeMultiplier = (originalColor.equalsIgnoreCase("AZUL")) ? 1.5 : 1.0;
        this.hasShield = false;
        this.isInvincible = false;
        this.invincibilityTimer = 0;
    } // Cierre del método

    /**
     * Método que maneja la colision de un jugador con un enemigo.
     */
    public void handleHit() {
        if (isInvincible) return;

        if (hasShield) {
            hasShield = false;
            currentSpeed = baseSpeed * 0.7;
            isInvincible = true;
            invincibilityTimer = 60;
            System.out.println("¡Escudo roto! Velocidad reducida.");
            } else {
                die();
            }
    } // Cierre del método

    /**
     * Método que permite que un jugador muera.
     */
    protected void die() {
        deaths++;
        resetToOriginal();
        this.x = respawnX;
        this.y = respawnY;
        System.out.println("Muerte registrada. Reapareciendo...");
    } // Cierre del método

    /**
     * Método que actualiza el estado del jugador.
     */
    public void update() {
        if(isInvincible) {
            invincibilityTimer--;
            if(invincibilityTimer <= 0) {
                isInvincible = false;
            }
        }
    } // Cierre del método

    /**
     * Método que pone la reaparicion en una coordenada dada.
     *
     * @param x La coordenada x.
     * @param y La coordenada y.
     */
    public void setRespawnPoint(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
    } // Cierre del método

    /**
     * Método que reinicia la posicion del jugador (lo deja en el ultimo respawn).
     *
     * @param x La coordenada x.
     * @param y La coordenada y.
     */
    public void resetPosition(double x, double y) {
        this.x = x;
        this.y = y;
    } // Cierre del método

    /**
     * Método que devuelve el nombre del jugador.
     * @return String El nombre del jugador.
     */
    public String getName() {
        return name;
    } // Cierre del método

    /**
     * Método que devuelve el color del jugador.
     *
     * @return String El color del jugador.
     */
    public String getColor() {
        return currentColor;
    } // Cierre del método

    /**
     * Método que define el color del borde del jugador.
     *
     * @param borderColor El color del borde.
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    } // Cierre del método

    /**
     * Método que devuelve el color del borde del jugador.
     *
     * @return Color El color del borde.
     */
    public Color getBorderColor() {
        return borderColor;
    } // Cierre del método

    /**
     * Método que devuelve la cantidad de muertes del jugador.
     *
     * @return int La cantidad de muertes del jugador.
     */
    public int getDeaths() {
        return deaths;
    } // Cierre del método

    /**
     * Método que devuelve la coordenada X del jugador.
     *
     * @return double La coordenada X del jugador.
     */
    public double getX() {
        return x;
    } // Cierre del método

    /**
     * Método que devuelve la coordenada Y del jugador.
     *
     * @return double La coordenada Y del jugador.
     */
    public double getY() {
        return y;
    } // Cierre del método

    /**
     * Método que retorna el tamaño del jugador.
     *
     * @return double El tamaño del jugador.
     */
    public double getSizeMultiplier() {
        return sizeMultiplier;
    } // Cierre del método

    /**
     * Método que devuelve si el jugador es invencible por ciertos frames.
     *
     * @return boolean Si el jugador es invencible o no.
     */
    public boolean isInvincible() {
        return isInvincible;
    } // Cierre del método

    /**
     * Método que retorna la velocidad actual del jugador.
     *
     * @return double La velocidad actual del jugador.
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    } // Cierre del método
} // Cierre de la clase
