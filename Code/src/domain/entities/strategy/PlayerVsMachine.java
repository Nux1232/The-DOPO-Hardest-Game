package domain.entities.strategy;

import domain.core.GameEngine;
import domain.entities.Player;
import domain.entities.Coin;
import domain.level.Level;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Clase que implementa el modo jugador contra máquina (bot).
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 23/05/2026
 */
public class PlayerVsMachine implements GameModeStrategy {
    private List<Player> players;
    private long[] finishTime;
    private int levelFinished = 0;

    /**
     * Nodo para el buscador de caminos BFS.
     */
    private static class PathNode {
        int x, y;
        PathNode parent;

        PathNode(int x, int y, PathNode parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PathNode)) return false;
            PathNode other = (PathNode) o;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    @Override
    public void setUp(List<Player> players) {
        int amountPlayers = Math.min(2, players.size());
        this.players = new ArrayList<>(players.subList(0, amountPlayers));
        this.finishTime = new long[amountPlayers];
        this.levelFinished = 0;
    } // Cierre del método

    @Override
    public boolean checkWinCondition(List<Player> players, int collectedCoins, int totalCoins) {
        return levelFinished >= 1;
    } // Cierre del método

    @Override
    public Player getWinner(List<Player> players) {
        int auxiliarWin = 0;
        long playerBestTime = Long.MAX_VALUE;
        for (int i = 0; i < players.size(); i++) {
            if (finishTime[i] > 0 && finishTime[i] < playerBestTime) {
                playerBestTime = finishTime[i];
                auxiliarWin = i;
            }
        }
        return players.get(auxiliarWin);
    } // Cierre del método

    @Override
    public List<Player> getPlayers() {
        return players;
    } // Cierre del método

    public void playerFinished(int playerCount) {
        if (playerCount < finishTime.length && finishTime[playerCount] == 0) {
            finishTime[playerCount] = System.currentTimeMillis();
            levelFinished++;
        }
    } // Cierre del método

    @Override
    public boolean independentGameModeCoins() {
        return true;
    } // Cierre del método

    @Override
    public void checkLevelCompletion(List<Player> players, Level level) {
        // Mover a la máquina si está en la partida
        if (players.size() > 1) {
            moveMachine(players.get(1), level);
        }

        Point finalSecureZone = level.getFinalSafeZone();
        Point startSecureZone = level.getStartPoint();
        if (finalSecureZone == null || startSecureZone == null) return;

        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);

            if (!player.hasCollectedAllCoins(level.getCoins().size())) continue;
            Point target = level.getFinalZoneForPlayer(i);

            if (target == null) continue;
            Rectangle2D.Double pRect = new Rectangle2D.Double(player.getX(), player.getY(),
                    20 * player.getSizeMultiplier(), 20 * player.getSizeMultiplier());
            if (pRect.intersects(target.x, target.y, 60, 60)) {
                playerFinished(i);
            }
        }
    } // Cierre del método

    /**
     * Método que calcula y realiza el movimiento del bot (máquina) en cada tick.
     */
    private void moveMachine(Player bot, Level level) {
        // Si el bot ya cruzó la meta, no se mueve
        if (finishTime.length > 1 && finishTime[1] > 0) {
            return;
        }

        Point target = getBotTarget(bot, level);
        if (target == null) return;

        String direction = getNextMove(bot, target, level.getWalls());
        if (direction != null) {
            GameEngine.getInstance().movePlayer(bot, direction);
        }
    } // Cierre del método

    /**
     * Determina el objetivo actual del bot (recolectar monedas o ir a la meta).
     */
    private Point getBotTarget(Player bot, Level level) {
        List<Coin> coins = level.getCoins();
        Coin closestCoin = null;
        double minDistance = Double.MAX_VALUE;

        // Buscar la moneda no recolectada más cercana
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (!bot.hasCollectedCoin(i)) {
                double dist = Point.distance(bot.getX(), bot.getY(), coin.getX(), coin.getY());
                if (dist < minDistance) {
                    minDistance = dist;
                    closestCoin = coin;
                }
            }
        }

        if (closestCoin != null) {
            return new Point((int) closestCoin.getX(), (int) closestCoin.getY());
        }

        // Si ya recolectó todas, su meta es el punto de inicio (según getFinalZoneForPlayer(1))
        return level.getStartPoint();
    } // Cierre del método

    /**
     * Determina el siguiente movimiento usando BFS o codicioso de respaldo.
     */
    private String getNextMove(Player bot, Point target, List<Rectangle> walls) {
        int startX = (int) Math.round(bot.getX());
        int startY = (int) Math.round(bot.getY());
        int targetX = target.x;
        int targetY = target.y;
        int size = (int) (20 * bot.getSizeMultiplier());
        int step = 10;

        Queue<PathNode> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        PathNode startNode = new PathNode(startX, startY, null);
        queue.add(startNode);
        visited.add(startX + "," + startY);

        PathNode bestNode = null;
        double bestDist = Double.MAX_VALUE;

        int minX = 0, maxX = 1600;
        int minY = 0, maxY = 1200;

        // Direcciones de paso
        int[][] dirs = {
                {0, -step}, // UP
                {0, step},  // DOWN
                {-step, 0}, // LEFT
                {step, 0}   // RIGHT
        };

        int maxIterations = 10000; // Límite incrementado para garantizar la resolución en pasillos grandes
        int iterations = 500;

        while (!queue.isEmpty() && iterations < maxIterations) {
            iterations++;
            PathNode curr = queue.poll();

            double dist = Point.distance(curr.x, curr.y, targetX, targetY);
            if (dist < bestDist) {
                bestDist = dist;
                bestNode = curr;
            }

            // Si llegamos lo suficientemente cerca del objetivo (dentro del tamaño del paso del grid)
            if (dist < 11) {
                bestNode = curr;
                break;
            }

            for (int[] dir : dirs) {
                int nextX = curr.x + dir[0];
                int nextY = curr.y + dir[1];

                if (nextX < minX || nextX > maxX || nextY < minY || nextY > maxY) {
                    continue;
                }

                String key = nextX + "," + nextY;
                if (visited.contains(key)) {
                    continue;
                }

                if (collidesWithWall(nextX, nextY, size, walls)) {
                    continue;
                }

                visited.add(key);
                queue.add(new PathNode(nextX, nextY, curr));
            }
        }

        if (bestNode != null && bestNode.parent != null) {
            // Reconstruir camino para obtener el primer paso después del inicio
            PathNode stepNode = bestNode;
            while (stepNode.parent != null && stepNode.parent.parent != null) {
                stepNode = stepNode.parent;
            }

            int dx = stepNode.x - startX;
            int dy = stepNode.y - startY;

            String direction;
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 1 ? "RIGHT" : "LEFT";
            } else {
                direction = dy > 1 ? "DOWN" : "UP";
            }

            if (canMove(bot, direction, walls)) {
                return direction;
            }
        }

        return getBestLegalMove(bot, target, walls);
    } // Cierre del método

    /**
     * Verifica si la caja de colisión intersecta alguna pared.
     */
    /**
     * Busca un movimiento legal simple que acerque al bot al objetivo.
     */
    private String getBestLegalMove(Player bot, Point target, List<Rectangle> walls) {
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
        String bestDirection = null;
        double bestDistance = Double.MAX_VALUE;

        for (String direction : directions) {
            if (!canMove(bot, direction, walls)) {
                continue;
            }

            double nextX = bot.getX();
            double nextY = bot.getY();
            double speed = bot.getCurrentSpeed();
            switch (direction.toUpperCase()) {
                case "UP":    nextY -= speed; break;
                case "DOWN":  nextY += speed; break;
                case "LEFT":  nextX -= speed; break;
                case "RIGHT": nextX += speed; break;
            }

            double distance = Point.distance(nextX, nextY, target.x, target.y);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestDirection = direction;
            }
        }

        return bestDirection;
    } // Cierre del método

    /**
     * Valida que el primer paso calculado por la IA no sea bloqueado por una pared.
     */
    private boolean canMove(Player bot, String direction, List<Rectangle> walls) {
        double nextX = bot.getX();
        double nextY = bot.getY();
        double speed = bot.getCurrentSpeed();

        switch (direction.toUpperCase()) {
            case "UP":
                nextY -= speed;
                break;
            case "DOWN":
                nextY += speed;
                break;
            case "LEFT":
                nextX -= speed;
                break;
            case "RIGHT":
                nextX += speed;
                break;
        }
        int size = (int) (20 * bot.getSizeMultiplier());
        return !collidesWithWall(nextX, nextY, size, walls);
    } // Cierre del método

    private boolean collidesWithWall(double x, double y, double size, List<Rectangle> walls) {
        Rectangle2D.Double playerRect = new Rectangle2D.Double(x, y, size, size);
        for (Rectangle wall : walls) {
            if (playerRect.intersects(wall)) {
                return true;
            }
        }
        return false;
    } // Cierre del método
}
