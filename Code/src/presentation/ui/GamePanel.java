package presentation.ui;

import domain.core.GameEngine;
import domain.core.GameState;
import domain.core.TheDopoHardestGame;
import domain.entities.*;
import domain.level.Level;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase que representa el panel del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 09/05/2026
 */

public class GamePanel extends JPanel {
    private static final int WORLD_WIDTH = 800;
    private static final int WORLD_HEIGHT = 600;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final PlayerKeyBindings firstPlayerBindings = PlayerKeyBindings.forArrowKeys();
    private final PlayerKeyBindings secondPlayerBindings = PlayerKeyBindings.forWASD();
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
        game.addObserver(new GameObserver() {
            @Override
            public void update() {
                GamePanel.this.update();
            }
        });

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

    /**
     * Método que establece la pausa del juego.
     *
     * @param pauseAction La accion para pausar el juego.
     */
    public void setPauseAction(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    } // Cierre del método

    /**
     * Método que limpia las teclas presionadas.
     */
    public void clearPressedKeys() {
        pressedKeys.clear();
    } // Cierre del método

    /**
     * Método privado que maneja el movimiento del jugador.
     */
    private void handleInput() {
        if (game.getPlayers().isEmpty()) return;
        keysLogic();
    } // Cierre del método

    /**
     * Método privado que maneja el movimiento del jugador.
     */
    private void keysLogic() {
        Player firstPlayer = game.getPlayers().get(0);
        for(int keyCode : pressedKeys) {
            String direction = firstPlayerBindings.getDirection(keyCode);
            if(direction != null) {
                game.movePlayer(firstPlayer, direction);
            }

            if(game.getPlayers().size() > 1 && !game.getGameMode().equals("Player vs Machine")) {
                Player secondPlayer = game.getPlayers().get(1);
                String secondDirection = secondPlayerBindings.getDirection(keyCode);
                if(secondDirection != null) {
                    game.movePlayer(secondPlayer, secondDirection);
                }
            }
        }
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

        // Punto intermedio
        Point intermediateZone = game.getCurrentLevel().getIntermediateSafeZone();
        if(intermediateZone != null) {
            g2.setColor(new Color(180, 230, 120));
            g2.fillRect(intermediateZone.x, intermediateZone.y, 60, 60);
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
            for (Coin c : level.getCoins()) {
                boolean coinCollected;
                if(game.getPlayers().size() > 1) {
                    int coinIndex = level.getCoins().indexOf(c);
                    coinCollected = game.getPlayers().stream()
                            .allMatch(p -> p.hasCollectedCoin(coinIndex));
                } else {
                    coinCollected = c.isCollected();
                }

                if (!coinCollected) {
                    g2.setColor(parseCoinColor(c.getType()));
                    g2.fillOval((int)c.getX() - radius/2, (int)c.getY() - radius/2, radius, radius);
                    g2.setColor(Color.BLACK);
                    g2.drawOval((int)c.getX() - radius/2, (int)c.getY() - radius/2, radius, radius);
                }
            }
        }
        // Fuente de vida (corazón)
        if (game.getCurrentLevel() != null) {
            for (LifeSource ls : level.getLifeSources()) {
                if (!ls.isCollected()) {
                    g2.setColor(Color.MAGENTA);
                    int hx = (int)ls.getX();
                    int hy = (int)ls.getY();
                    int size = 20;
                    g2.fillArc(hx, hy, size/2, size/2, 0, 180);
                    g2.fillArc(hx + size/2, hy, size/2, size/2, 0, 180);
                    int[] xPts = {hx, hx + size, hx + size/2};
                    int[] yPts = {hy + size/4, hy + size/4, hy + size};
                    g2.fillPolygon(xPts, yPts, 3);
                }
            }
        }

        //Bomba
        if(game.getCurrentLevel() != null) {
            for(Bomb bomb : game.getCurrentLevel().getBombs()) {
                if(!bomb.isActive()) continue;
                g2.setColor(Color.BLACK);
                g2.fillOval((int)bomb.getX() - radius/2, (int)bomb.getY() - radius/2, radius, radius);
                g2.drawOval((int)bomb.getX() - radius/2, (int)bomb.getY() - radius/2, radius, radius);
            }
        }

        // Enemigo
        g2.setColor(Color.BLUE);
        for (Enemy e : game.getEnemies()) {
            if(!e.isAlive()) continue;
            g2.fillOval((int)e.getX() - diameter/2, (int)e.getY() - diameter/2, diameter, diameter);
        }

        // Jugador
        for (Player p : game.getPlayers()) {
            // Parpadeo durante invencibilidad
            if (p.isInvincible() && (p.getInvincibilityTimer() / 5) % 2 == 0) {
                continue; // No dibujar en frames alternos para efecto de parpadeo
            }
            g2.setColor(parsePlayerColor(p.getColor()));
            int size = (int)(20 * p.getSizeMultiplier());
            g2.fillRect((int)p.getX(), (int)p.getY(), size, size);
            g2.setColor(p.getBorderColor());
            g2.setStroke(new BasicStroke(3));
            g2.drawRect((int)p.getX(), (int)p.getY(), size, size);
            g2.setStroke(new BasicStroke(1));
        }

        g2.dispose();

        Graphics2D hudGraphics = (Graphics2D) g.create();
        hudGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawHud(hudGraphics);
        hudGraphics.dispose();
    } // Cierre del método

    /**
     * Método privado que transforma el mundo para que se ajuste al panel.
     *
     * @param g2 Dibuja el mundo.
     */
    private void applyWorldTransform(Graphics2D g2) {
        double scaleX = getWidth() / (double) WORLD_WIDTH;
        double scaleY = getHeight() / (double) WORLD_HEIGHT;
        double scale = Math.min(scaleX, scaleY);
        double offsetX = (getWidth() - WORLD_WIDTH * scale) / 2.0;
        double offsetY = (getHeight() - WORLD_HEIGHT * scale) / 2.0;

        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);
    } // Cierre del método

    /**
     * Método privado que dibuja la información de un jugador.
     *
     * @param g2 Dibuja la información del jugador.
     */
    private void drawHud(Graphics2D g2) {
        int time = game.getRemainingTime();
        int minutes = time / 60;
        int seconds = time % 60;
        int margin = Math.max(8, Math.min(getWidth(), getHeight()) / 40);

        String timeText = String.format("Tiempo: %02d:%02d", minutes, seconds);
        Dimension timeSize = getHudBoxSize(g2, new String[]{timeText});
        int timeX = Math.max(margin, (getWidth() - timeSize.width) / 2);
        int timeY = Math.max(margin, getHeight() - timeSize.height - margin);
        drawHudBox(g2, new String[]{timeText}, timeX, timeY);

        if(!game.getPlayers().isEmpty()) {
            Player firstPlayer = game.getPlayers().get(0);
            String playerName = "Jugador 1";
            String firstPlayerCoinText = "Monedas: " + firstPlayer.getCollectedCoins() + "/" + game.getTotalCoins();
            String deathsFirstPlayerText = "Muertes: " + firstPlayer.getDeaths();
            String livesFirstPlayerText = "Vidas: " + firstPlayer.getExtraLives();
            drawHudBox(g2, new String[]{playerName, firstPlayerCoinText, deathsFirstPlayerText, livesFirstPlayerText},
                    margin, margin);
        }

        if(game.getPlayers().size() > 1) {
            Player secondPlayer = game.getPlayers().get(1);
            String playerName = "Jugador 2";
            String coinsSecondPlayerText = "Monedas: " + secondPlayer.getCollectedCoins() + "/" + game.getTotalCoins();
            String deathsSecondPlayerText = "Muertes: " + secondPlayer.getDeaths();
            String livesSecondPlayerText = "Vidas: " + secondPlayer.getExtraLives();
            String[] secondPlayerLines = new String[]{playerName, coinsSecondPlayerText, deathsSecondPlayerText, livesSecondPlayerText};
            Dimension secondPlayerSize = getHudBoxSize(g2, secondPlayerLines);
            int secondPlayerX = Math.max(margin, getWidth() - secondPlayerSize.width - margin);
            drawHudBox(g2, secondPlayerLines, secondPlayerX, margin);
        }
    } // Cierre del método

    /**
     * Método privado que calcula el tamaño de un cuadro del HUD.
     *
     * @param g2 Dibuja el cuadro.
     * @param lines Las líneas del cuadro.
     * @return Dimension El tamaño del cuadro.
     */
    private Dimension getHudBoxSize(Graphics2D g2, String[] lines) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2.getFontMetrics();
        int padding = 10;
        int lineHeight = metrics.getHeight();
        int boxWidth = 0;
        for (String line : lines) {
            boxWidth = Math.max(boxWidth, metrics.stringWidth(line));
        }
        return new Dimension(boxWidth + padding * 2, lineHeight * lines.length + padding * 2);
    } // Cierre del método

    /**
     * Método privado que dibuja un cuadro con texto.
     *
     * @param g2 Dibuja el cuadro.
     * @param lines El texto que se quiere escribir.
     * @param positionX La posición en x del cuadro.
     * @param positionY La posición en y del cuadro.
     */
    private void drawHudBox(Graphics2D g2, String[] lines, int positionX, int positionY) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics metrics = g2.getFontMetrics();
        int padding = 10;
        int lineHeight = metrics.getHeight();
        int boxWidth = 0;
        for (String line : lines) {
            boxWidth = Math.max(boxWidth, metrics.stringWidth(line));
        }
        boxWidth += padding * 2;
        int boxHeight = lineHeight * lines.length + padding * 2;

        int safeX = Math.max(0, Math.min(positionX, Math.max(0, getWidth() - boxWidth)));
        int safeY = Math.max(0, Math.min(positionY, Math.max(0, getHeight() - boxHeight)));

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(safeX, safeY, boxWidth, boxHeight, 8, 8);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(safeX, safeY, boxWidth, boxHeight, 8, 8);

        int textX = safeX + padding;
        int textY = safeY + padding + metrics.getAscent();
        for(String line : lines) {
            g2.drawString(line, textX, textY);
            textY += lineHeight;
        }
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
            case "WHITE": return Color.WHITE;
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
            case "GREEN":
            case "VERDE":
                return Color.GREEN;
            case "BLUE":
            case "AZUL":
                return Color.BLUE;
            case "RED":
            case "ROJO":
            default:
                return Color.RED;
        }
    } // Cierre del método

    /**
     * Método que actualiza el estado del juego (Su visualización).
     */
    public void update() {
        if(game.getGameState() != GameState.PLAYING) {
            clearPressedKeys();
            return;
        }

        handleInput();
        repaint();
    } // Cierre del método
} // Cierre de la clase
