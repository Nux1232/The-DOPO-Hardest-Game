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

        // 2. Crear un jugador (Rojo por defecto)
        GameEngine engine = GameEngine.getInstance();
        engine.getPlayers().add(PlayerFactory.createPlayer("Jugador 1", "ROJO"));

        // 3. Construir un nivel de prueba con monedas de skin y enemigos variados
        LevelBuilder builder = new ConcreteLevelBuilder();
        Level level1 = builder.reset(1)
            .setStartPoint(50, 50)
            // Monedas de skin
            .addCoin(150, 50, "BLUE")
            .addCoin(250, 50, "GREEN")
            .addCoin(350, 50, "RED")
            .addCoin(450, 50, "YELLOW")
            // Enemigos
            .addEnemy("RAPIDO", 100, 200)
            .addEnemy("BASICO", 200, 300)
            .addEnemy("PATRULLERO", 300, 400)
            .addSafeZone(700, 500, true)
            .build();

        // 4. Cargar el nivel y arrancar el motor
        System.out.println("Cargando Nivel de Prueba...");
        engine.loadLevel(level1);
        engine.startGame();
    }
}
