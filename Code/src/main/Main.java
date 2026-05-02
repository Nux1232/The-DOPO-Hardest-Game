package main;

import main.Core.GameEngine;
import main.Level.Builder.ConcreteLevelBuilder;
import main.Level.Builder.LevelBuilder;
import main.Level.GameConfiguration;
import main.Level.Level;
import main.Entities.Factory.PlayerFactory;
import main.UI.MainWindow;

/**
 * La clase principal del juego.
 *
 * @author Juan Pablo Cuervo Contreras
 * @author David Felipe Ortiz Salcedo
 * @version 01/05/2026
 */

public class Main {
    public static void main(String[] args) {
        System.out.println("Inicializando el juego..");

        MainWindow window = new MainWindow();
        window.showWindow();

        GameEngine engine = GameEngine.getInstance();
        engine.getPlayers().add(PlayerFactory.createPlayer("Jugador 1", "BLINKY"));

        GameConfiguration configuration = new GameConfiguration("Resources/configuration1.txt");
        Level level1 = configuration.buildLevel(1);

        System.out.println("Cargando Nivel 1..");
        engine.loadLevel(level1);
        engine.startGame();
    }
} // Cierre de la clase
