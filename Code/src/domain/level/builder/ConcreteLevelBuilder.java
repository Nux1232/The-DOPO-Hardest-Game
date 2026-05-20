package domain.level.builder;

import domain.level.Level;
import domain.entities.factory.EnemyFactory;
import domain.entities.Coin;
import domain.entities.LifeSource;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * Clase que construye un nivel del juego siguiendo el patron de diseño Builder.
 * Asi mismo, implementa la interfaz LevelBuilder.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 02/05/2026
 */

public class ConcreteLevelBuilder implements LevelBuilder {
    private Level level;

    /**
     * Método que reinicia un nivel del juego.
     *
     * @param id El identificador del nivel.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder reset(int id) {
        this.level = new Level(id);
        return this;
    } // Cierre del método

    /**
     * Método que establece el tiempo limite del nivel.
     *
     * @param seconds El tiempo en segundos.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder setTimeLimit(int seconds) {
        level.setTimeLimit(seconds);
        return this;
    } // Cierre del método

    /**
     * Método que establece su propio comportamiento del punto de inicio de un nivel.
     *
     * @param x La coordenada x del punto de inicio.
     * @param y La coordenada y del punto de inicio.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder setStartPoint(int x, int y) {
        level.setStartPoint(new Point(x, y));
        return this;
    } // Cierre del método

    /**
     * Método que añade zonas seguras en un nivel con su propio comportamiento.
     *
     * @param x La coordenada x de la zona segura.
     * @param y La coordenada y de la zona segura.
     * @param isFinal Indica si la zona es final o intermedia.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder addSafeZone(int x, int y, boolean isFinal) {
        if (isFinal) {
            level.setFinalSafeZone(new Point(x, y));
        } else {
            level.setIntermediateSafeZone(new Point(x, y));
        }
        return this;
    } // Cierre del método

    /**
     * Método que añade un enemigo al nivel con su propio comportamiento.
     * @param type El tipo de enemigo.
     * @param x La coordenada x del enemigo.
     * @param y La coordenada y del enemigo.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder addEnemy(String type, int x, int y) {
        level.addEnemy(EnemyFactory.createEnemy(type, x, y));
        return this;
    } // Cierre del método

    /**
     * Método que añade una moneda al nivel con su propio comportamiento.
     *
     * @param x La coordenada x de la moneda.
     * @param y La coordenada y de la moneda.
     * @param type El tipo de moneda.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder addCoin(int x, int y, String type) {
        level.addCoin(new Coin(x, y, type)); 
        return this;
    } // Cierre del método

    /**
     * Método que añade una fuente de vida al nivel con su propio comportamiento.
     *
     * @param x La coordenada x de la fuente de vida.
     * @param y La coordenada y de la fuente de vida.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder addLifeSource(int x, int y) {
        level.addLifeSource(new LifeSource(x, y));
        return this;
    } // Cierre del método

    /**
     * Método que añade una pared al nivel con su propio comportamiento.
     *
     * @param x La coordenada x de la pared.
     * @param y La coordenada y de la pared.
     * @param width La anchura de la pared.
     * @param height La altura de la pared.
     * @return LevelBuilder El nivel construido.
     */
    @Override
    public LevelBuilder addWall(int x, int y, int width, int height) {
        level.addWall(new Rectangle(x, y, width, height));
        return this;
    } // Cierre del método

    /**
     * Método que construye un nivel del juego.
     *
     * @return Level El nivel a construir.
     */
    @Override
    public Level build() {
        return this.level;
    } // Cierre del método
} // Cierre de la clase
