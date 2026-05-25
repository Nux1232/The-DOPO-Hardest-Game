package domain.entities;

/**
 * Interfaz que representa a cualquier objeto móvil en el dominio del juego.
 * Permite abstraer estrategias de movimiento para cualquier entidad que tenga coordenadas.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 23/05/2026
 */
public interface Movable {
    /**
     * Obtiene la coordenada X del objeto.
     * @return double Coordenada X.
     */
    double getX();

    /**
     * Obtiene la coordenada Y del objeto.
     * @return double Coordenada Y.
     */
    double getY();

    /**
     * Establece la coordenada X del objeto.
     * @param x Nueva coordenada X.
     */
    void setX(double x);

    /**
     * Establece la coordenada Y del objeto.
     * @param y Nueva coordenada Y.
     */
    void setY(double y);
}
