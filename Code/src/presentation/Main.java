package presentation;

import presentation.ui.MainWindow;

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
    }
} // Cierre de la clase
