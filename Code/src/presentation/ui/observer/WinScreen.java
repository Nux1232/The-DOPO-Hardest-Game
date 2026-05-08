package presentation.ui.observer;

import java.awt.*;

public class WinScreen {
    public void draw(Graphics g2, int width, int height) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.YELLOW);
        Font tipoLetraVictoria = new Font("Times New Roman", Font.BOLD, 48);
        g2.setFont(tipoLetraVictoria);
        String mensaje = "Ganaste!!";
        FontMetrics fmv = g2.getFontMetrics();
        g2.drawString(mensaje, (width - fmv.stringWidth(mensaje)) /2, height / 2);

        g2.setColor(Color.WHITE);
        Font tipoLetraSalir = new Font("Times New Roman", Font.PLAIN, 20);
        g2.setFont(tipoLetraSalir);
        String salir = "Presione ESC para salir";
        FontMetrics fms = g2.getFontMetrics();
        g2.drawString(salir, (width - fms.stringWidth(salir)) / 2, height / 2 + 40);
    }
} // Cierre del método
