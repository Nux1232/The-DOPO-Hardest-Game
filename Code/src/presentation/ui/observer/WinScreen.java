package presentation.ui.observer;

import java.awt.*;

/**
 * Clase que representa la pantalla de victoria.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class WinScreen {
    /**
     * Método que dibuja la pantalla de victoria.
     *
     * @param g2 Permite dibujar en la pantalla.
     * @param width La anchura de la pantalla.
     * @param height La altura de la pantalla.
     */
    public void draw(Graphics g2, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.YELLOW);
        Font victoryLetterType = new Font("Times New Roman", Font.BOLD, 48);
        g2.setFont(victoryLetterType);
        String message = "Ganaste!!";
        FontMetrics fmv = g2.getFontMetrics();
        g2.drawString(message, (width - fmv.stringWidth(message)) /2, height / 2);

        g2.setColor(Color.WHITE);
        Font exitLetterType = new Font("Times New Roman", Font.PLAIN, 20);
        g2.setFont(exitLetterType);
        String exit = "Presione ESC para salir";
        FontMetrics fms = g2.getFontMetrics();
        g2.drawString(exit, (width - fms.stringWidth(exit)) / 2, height / 2 + 40);
    } // Cierre del método
} // Cierre de la clase
