import Controller.MenudeEstados;
import View.Game;
import lanternagraphics.Gui;

public class Application {
    public static void main(String[] args) {
            Game game = new Game(new MenudeEstados(new Gui(30,50)), 15);
            game.start();

    }
}
