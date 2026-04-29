package main;

import Core.GameEngine;
import Level.Builder.ConcreteLevelBuilder;
import Level.Builder.LevelBuilder;
import Level.Level;
import Entities.Factory.PlayerFactory;
import UI.MainWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando The DOPO Hardest Game ---");

        // 1. Inicializar la UI (y sus observadores)
        MainWindow window = new MainWindow();
        window.show();

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

        // Simulación: Esperar 3 segundos y mostrar que el motor está corriendo
        try {
            Thread.sleep(3000);
            System.out.println("Simulación terminada. Estado actual: " + engine.getCurrentState());
            System.out.println("Tiempo restante en el motor: " + engine.getRemainingTime() + "s");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.exit(0);
    }
}
