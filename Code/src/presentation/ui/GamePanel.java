package presentation.ui;

import domain.core.GameEngine;
import domain.core.TheDopoHardestGame;
import domain.entities.*;
import domain.level.Level;
import presentation.ui.observer.GameObserver;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa el panel del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */

public class GamePanel extends JPanel implements GameObserver {
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private Runnable pauseAction;
    private final TheDopoHardestGame game;

    /**
     * Contructor de la clase GamePanel.
     */
    public GamePanel(TheDopoHardestGame game) {
        this.game = game;
        setPreferredSize(new Dimension(WORLD_WIDTH, WORLD_HEIGHT));
        setBackground(new Color(173, 216, 230));
        setFocusable(true);
        GameEngine.getInstance().addObserver(this);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pressedKeys.clear();
                    if (pauseAction != null) pauseAction.run();
                    return;
                }
                pressedKeys.add(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) { pressedKeys.remove(e.getKeyCode()); }
        });

        addHierarchyListener(e -> {
            if (isShowing()) requestFocusInWindow();
        });
    } // Cierre del constructor

    public void setPauseAction(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    }

    public void clearPressedKeys() {
        pressedKeys.clear();
    }

    /**
     * Método privado que maneja el movimiento del jugador.
     */
    private void handleInput() {
        if (game.getPlayers().isEmpty()) return;
        Player p = game.getPlayers().get(0);
        GameEngine engine = GameEngine.getInstance();
        if (pressedKeys.contains(KeyEvent.VK_UP)) game.movePlayer(p, "UP");
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) game.movePlayer(p, "DOWN");
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) game.movePlayer(p, "LEFT");
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) game.movePlayer(p, "RIGHT");
    } // Cierre del método

    /**
     * Método que dibuja los objetos del juego.
     * @param g La clase Graphics para dibujar los objetos.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(game.getCurrentLevel() == null) return;
        int radius = 10;
        int diameter = 15;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        applyWorldTransform(g2);
        Level level = game.getCurrentLevel();

        // Pared
        g2.setColor(Color.BLACK);
        for(Rectangle wall : game.getCurrentLevel().getWalls()) {
            g2.fillRect(wall.x, wall.y, wall.width, wall.height);
        }

        // Punto de inicio
        Point startPoint = game.getCurrentLevel().getStartPoint();
        if(startPoint != null) {
            g2.setColor(new Color(150, 230, 150));
            g2.fillRect(startPoint.x, startPoint.y, 60, 60);
        }

        // Punto final
        Point finalZone = game.getCurrentLevel().getFinalSafeZone();
        if(finalZone != null) {
            g2.setColor(new Color(100, 200, 100));
            g2.fillRect(finalZone.x, finalZone.y, 60, 60);
            g2.setColor(new Color(60, 160, 60));
        }

        // Moneda
        if (game.getCurrentLevel() != null) {
            for (Coin c :game.getCurrentLevel().getCoins()) {
                if (!c.isCollected()) {
                    g2.setColor(parseCoinColor(c.getType()));
                    g2.fillOval((int)c.getX() - radius/2, (int)c.getY() - radius/2, radius, radius);
                    g2.setColor(Color.BLACK);
                    g2.drawOval((int)c.getX() - radius/2, (int)c.getY() - radius/2, radius, radius);
                }
            }
        }

        // Enemigo
        g2.setColor(Color.BLUE);
        if (game.getCurrentLevel() != null) {
            for (Enemy e : game.getCurrentLevel().getEnemies()) {
                g2.fillOval((int)e.getX() - diameter/2, (int)e.getY() - diameter/2, diameter, diameter);
            }
        }

        // Jugador
        for (Player p : game.getPlayers()) {
            g2.setColor(parsePlayerColor(p.getColor()));
            int size = (int)(20 * p.getSizeMultiplier());
            g2.fillRect((int)p.getX(), (int)p.getY(), size, size);
            g2.setColor(p.getBorderColor());
            g2.setStroke(new BasicStroke(3));
            g2.drawRect((int)p.getX(), (int)p.getY(), size, size);
            g2.setStroke(new BasicStroke(1));
        }

        drawHud(g2);
        g2.dispose();
    } // Cierre del método

    private void applyWorldTransform(Graphics2D g2) {
        double scaleX = getWidth() / (double) WORLD_WIDTH;
        double scaleY = getHeight() / (double) WORLD_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        double offsetX = (getWidth() - WORLD_WIDTH * scale) / 2.0;
        double offsetY = (getHeight() - WORLD_HEIGHT * scale) / 2.0;

        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);
    } // Cierre del método

    private void drawHud(Graphics2D g2) {
        int time = game.getRemainingTime();
        int minutes = time / 60;
        int seconds = time % 60;
        int deaths = game.getPlayers().isEmpty() ? 0 : game.getPlayers().get(0).getDeaths();

        String coinsText = "Monedas: " + game.getCollectedCoins() + "/" + game.getTotalCoins();
        String timeText = String.format("Tiempo: %02d:%02d", minutes, seconds);
        String deathsText = "Muertes: " + deaths;
        if(minutes == 0 && seconds == 0) {
            g2.drawString("Fin", 12, 12);
        }

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2.getFontMetrics();
        int padding = 10;
        int lineHeight = metrics.getHeight();
        int boxWidth = Math.max(metrics.stringWidth(coinsText),
                Math.max(metrics.stringWidth(timeText), metrics.stringWidth(deathsText))) + padding * 2;
        int boxHeight = lineHeight * 3 + padding * 2;

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(12, 12, boxWidth, boxHeight, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(12, 12, boxWidth, boxHeight, 8, 8);

        int textX = 12 + padding;
        int textY = 12 + padding + metrics.getAscent();
        g2.drawString(coinsText, textX, textY);
        g2.drawString(timeText, textX, textY + lineHeight);
        g2.drawString(deathsText, textX, textY + lineHeight * 2);
    } // Cierre del método

    /**
     * Método privado que retorna el color de la moneda de acuerdo a su skin.
     *
     * @param type El tipo de moneda.
     * @return Color El color de la moneda.
     */
    private Color parseCoinColor(String type) {
        switch(type.toUpperCase()){
            case "RED": return Color.RED;
            case "BLUE": return Color.BLUE;
            case "GREEN": return Color.GREEN;
            default: return Color.YELLOW;
        }
    } // Cierre del método

    /**
     * Método privado que retorna el color del jugador de acuerdo a su skin.
     *
     * @param color El color que se le dió al jugador.
     * @return Color El color del jugador.
     */
    private Color parsePlayerColor(String color) {
        switch(color.toUpperCase()) {
            case "VERDE":
                return Color.GREEN;
            case "AZUL":
                return Color.BLUE;
            case "ROJO":
            default:
                return Color.RED;
        }
    } // Cierre del método

    /**
     * Método que actualiza el estado del juego (Su visualización).
     */
    @Override
    public void update() {
        handleInput();
        repaint();
    } // Cierre del método
} // Cierre de la clase
