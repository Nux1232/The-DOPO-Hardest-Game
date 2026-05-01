package main;

import main.Core.GameEngine;
import main.Level.Builder.ConcreteLevelBuilder;
import main.Level.Builder.LevelBuilder;
import main.Level.Level;
import main.Entities.Factory.PlayerFactory;
import main.UI.MainWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando The DOPO Hardest Game ---");

        // 1. Inicializar la UI
        MainWindow window = new MainWindow();
        window.showWindow();

        // 2. Crear un jugador usando la Factory
        GameEngine engine = GameEngine.getInstance();
        engine.getPlayers().add(PlayerFactory.createPlayer("Jugador 1", "VERDE"));

        // 3. Construir un nivel usando el Builder
        LevelBuilder builder = new ConcreteLevelBuilder();
        Level level1 = builder.reset(1)
            .setStartPoint(10, 10)
            .addCoin(50, 50)
            .addEnemy("RAPIDO", 100, 100)
            .addSafeZone(200, 200, true)
            .build();

        // 4. Cargar el nivel y arrancar el motor
        System.out.println("Cargando Nivel 1...");
        engine.loadLevel(level1);
        engine.startGame();
    }
}
