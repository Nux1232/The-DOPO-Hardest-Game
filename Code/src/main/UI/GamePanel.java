package main.UI;

import main.Core.GameEngine;
import main.Entities.Player;
import main.Entities.Enemy;
import main.UI.Observer.GameObserver;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel implements GameObserver {
    private final Set<Integer> pressedKeys = new HashSet<>();
    
    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);
        
        GameEngine.getInstance().addObserver(this);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

    private void handleInput() {
        if (GameEngine.getInstance().getPlayers().isEmpty()) return;
        Player player = GameEngine.getInstance().getPlayers().get(0);
        if (pressedKeys.contains(KeyEvent.VK_UP) || pressedKeys.contains(KeyEvent.VK_W)) player.move("UP");
        if (pressedKeys.contains(KeyEvent.VK_DOWN) || pressedKeys.contains(KeyEvent.VK_S)) player.move("DOWN");
        if (pressedKeys.contains(KeyEvent.VK_LEFT) || pressedKeys.contains(KeyEvent.VK_A)) player.move("LEFT");
        if (pressedKeys.contains(KeyEvent.VK_RIGHT) || pressedKeys.contains(KeyEvent.VK_D)) player.move("RIGHT");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Dibujar Jugadores
        for (Player p : GameEngine.getInstance().getPlayers()) {
            g.setColor(parseColor(p.getColor()));
            int size = (int) (20 * p.getSizeMultiplier());
            g.fillRect((int) p.getX(), (int) p.getY(), size, size);
            g.setColor(Color.BLACK);
            g.drawRect((int) p.getX(), (int) p.getY(), size, size);
        }

        // Dibujar Enemigos
        if (GameEngine.getInstance().getCurrentLevel() != null) {
            g.setColor(Color.BLUE);
            for (Enemy e : GameEngine.getInstance().getCurrentLevel().getEnemies()) {
                g.fillOval((int) e.getX(), (int) e.getY(), 15, 15);
                g.setColor(Color.BLACK);
                g.drawOval((int) e.getX(), (int) e.getY(), 15, 15);
                g.setColor(Color.BLUE);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("Mueve al jugador con las flechas o WASD. ¡Evita los círculos azules!", 20, 20);
    }

    private Color parseColor(String colorName) {
        switch (colorName.toUpperCase()) {
            case "VERDE": return Color.GREEN;
            case "ROJO": return Color.RED;
            case "AZUL": return Color.BLUE;
            default: return Color.GRAY;
        }
    }

    @Override
    public void update() {
        handleInput();
        repaint();
    }
}
