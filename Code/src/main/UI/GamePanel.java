package main.UI;

import main.Core.GameEngine;
import main.Entities.*;
import main.UI.Observer.GameObserver;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements GameObserver {
    private final Set<Integer> pressedKeys = new HashSet<>();
    
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(230, 230, 255));
        setFocusable(true);
        GameEngine.getInstance().addObserver(this);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { pressedKeys.add(e.getKeyCode()); }
            public void keyReleased(KeyEvent e) { pressedKeys.remove(e.getKeyCode()); }
        });
    }

    private void handleInput() {
        if (GameEngine.getInstance().getPlayers().isEmpty()) return;
        Player p = GameEngine.getInstance().getPlayers().get(0);
        if (pressedKeys.contains(KeyEvent.VK_UP)) p.move("UP");
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) p.move("DOWN");
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) p.move("LEFT");
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) p.move("RIGHT");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Dibujar Monedas
        if (GameEngine.getInstance().getCurrentLevel() != null) {
            for (Coin c : GameEngine.getInstance().getCurrentLevel().getCoins()) {
                if (!c.isCollected()) {
                    g2.setColor(parseCoinColor(c.getType()));
                    g2.fillOval((int)c.getX(), (int)c.getY(), 10, 10);
                    g2.setColor(Color.BLACK);
                    g2.drawOval((int)c.getX(), (int)c.getY(), 10, 10);
                }
            }
        }

        // 2. Dibujar Enemigos
        g2.setColor(Color.BLUE);
        if (GameEngine.getInstance().getCurrentLevel() != null) {
            for (Enemy e : GameEngine.getInstance().getCurrentLevel().getEnemies()) {
                g2.fillOval((int)e.getX(), (int)e.getY(), 15, 15);
            }
        }

        // 3. Dibujar Jugadores
        for (Player p : GameEngine.getInstance().getPlayers()) {
            g2.setColor(parsePlayerColor(p.getColor()));
            int size = (int)(20 * p.getSizeMultiplier());
            g2.fillRect((int)p.getX(), (int)p.getY(), size, size);
            g2.setColor(Color.BLACK);
            g2.drawRect((int)p.getX(), (int)p.getY(), size, size);
        }
    }

    private Color parseCoinColor(String type) {
        switch(type.toUpperCase()){
            case "YELLOW": return Color.YELLOW;
            case "RED": return Color.RED;
            case "BLUE": return Color.BLUE;
            case "GREEN": return Color.GREEN;
            default: return Color.YELLOW;
        }
    }

    private Color parsePlayerColor(String color) {
        if (color.equalsIgnoreCase("VERDE")) return Color.GREEN;
        if (color.equalsIgnoreCase("AZUL")) return Color.BLUE;
        return Color.RED;
    }

    @Override
    public void update() {
        handleInput();
        repaint();
    }
}
