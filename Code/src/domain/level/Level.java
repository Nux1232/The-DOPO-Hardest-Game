package domain.level;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import domain.entities.Enemy;
import domain.entities.Coin;

/**
 * Clase que representa un nivel del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class Level {
    private int id;
    private int timeLimit;
    private List<Enemy> enemies;
    private List<Coin> coins;
    private List<Rectangle> walls;
    private Point startPoint;
    private Point intermediateSafeZone;
    private Point finalSafeZone;

    /**
     * Constructor de la clase Level.
     *
     * @param id Un entero que identifica al nivel.
     */
    public Level(int id) {
        this.id = id;
        this.enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.timeLimit = 180; // 3 minutos
    } // Cierre del constructor

    /**
     * Método setter para el tiempo del juego.
     *
     * @param timeLimit El tiempo limite en segundos.
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    } // Cierre del método

    /**
     * Método setter para el punto de inicio de un nivel.
     *
     * @param p El punto de inicio.
     */
    public void setStartPoint(Point p) {
        this.startPoint = p;
    } // Cierre del método

    /**
     * Método setter para la zona intermedia segura de un nivel.
     *
     * @param p El punto de la zona intermedia segura.
     */
    public void setIntermediateSafeZone(Point p) {
        this.intermediateSafeZone = p;
    } // Cierre del método

    /**
     * Método setter para la zona final segura de un nivel.
     *
     * @param p El punto de la zona final.
     */
    public void setFinalSafeZone(Point p) {
        this.finalSafeZone = p;
    } // Cierre del método

    /**
     * Método que añade un enemigo al nivel.
     *
     * @param e El enemigo a añadir.
     */
    public void addEnemy(Enemy e) {
        this.enemies.add(e);
    } // Cierre del método

    /**
     * Método que añade una moneda al nivel.
     *
     * @param c La moneda que se añade al nivel.
     */
    public void addCoin(Coin c) {
        this.coins.add(c);
    } // Cierre del método

    /**
     * Método que añade una pared al nivel.
     *
     * @param r Un rectángulo que representa la pared.
     */
    public void addWall(Rectangle r) {
        this.walls.add(r);
    } // Cierre del método

    /**
     * Método que obtiene el identificador del nivel.
     *
     * @return int El identificador del nivel.
     */
    public int getId() {
        return id;
    } // Cierre del método

    /**
     * Método que obtiene el tiempo limite del nivel.
     *
     * @return int El tiempo limite del nivel.
     */
    public int getTimeLimit() {
        return timeLimit;
    } // Cierre del método

    /**
     * Método que obtiene la cantidad de enemigos de un nivel.
     *
     * @return List<Enemy> La lista de enemigos del nivel.
     */
    public List<Enemy> getEnemies() {
        return enemies;
    } // Cierre del método

    /**
     * Método que obtiene la cantidad de monedas de un nivel.
     *
     * @return List<Coin> La lista de monedas del nivel.
     */
    public List<Coin> getCoins() {
        return coins;
    } // Cierre del método

    /**
     * Método que obtiene la lista de paredes del nivel.
     *
     * @return List<Rectangle> La lista de paredes del nivel.
     */
    public List<Rectangle> getWalls() {
        return walls;
    } // Cierre del método

    /**
     * Método que obtiene el punto de inicio del nivel.
     *
     * @return Point El punto de inicio del nivel.
     */
    public Point getStartPoint() {
        return startPoint;
    } // Cierre del método

    /**
     * Método que obtiene las zonas intermedias seguras del nivel.
     *
     * @return Point La zona intermedia segura del nivel.
     */
    public Point getIntermediateSafeZone() {
        return intermediateSafeZone;
    } // Cierre del método

    /**
     * Método que obtiene la zona final segura del nivel.
     *
     * @return Point La zona final segura del nivel.
     */
    public Point getFinalSafeZone() {
        return finalSafeZone;
    } // Cierre del método

    /**
     * Método que obtiene la zona final segura de cada jugador (modo pvp).
     *
     * @param playerIndex Las posiciones de los jugadores.
     * @return Point La zona final segura de cada jugador.
     */
    public Point getFinalZoneForPlayer(int playerIndex) {
        if (playerIndex == 1) return startPoint;
        return finalSafeZone;
    } // Cierre del método
} // Cierre de la clase
