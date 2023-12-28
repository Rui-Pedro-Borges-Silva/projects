package Controller;

import Model.Position;
import Model.components.Button.Bbutton;
import View.Game;
import com.googlecode.lanterna.TerminalPosition;
import lanternagraphics.Gui;

import java.io.IOException;

public class MenudeEstados extends Estados {

    public MenudeEstados(Gui screen) {
        super(screen);
        buttonList.add(new Bbutton(new Position((screen.getWidth()/2)-7, 6),"  ORIGINAL   "));
        buttonList.add(new Bbutton(new Position((screen.getWidth()/2)-7, 26),"    RULES    "));
        actualbutton=buttonList.get(0);
    }

    @Override
    public void step(Game game) throws IOException {
        screen.getScreen().clear();
        drawBackground("#69B8F9");
        drawAllText("#000097");
        drawButtons();
        screen.getScreen().refresh();
        checkInputButtons(game);
    }

    @Override
    public void drawAllText(String color){
        drawText("SNAKE",color,new TerminalPosition((screen.getWidth()/2)-7, 1));
        drawText("Q to exit",color,new TerminalPosition(screen.getWidth()-9, screen.getHeight()));
    }

    @Override
    public void exit(Game game) throws IOException{
        screen.getScreen().stopScreen();
        screen.getScreen().close();
        changeState(game, null);
    }
    @Override
    public void enterState(Game game){
        switch(actualbutton.getText()){
            case "  ORIGINAL   ": changeState(game,new EstadoInicial(new Gui(screen.getHeight(), screen.getWidth())));break;
            // case "    RULES    ": changeState(game,new RulesState(new LanternaGUI(screen.getHeight(), screen.getWidth())));break;
        }
    }
}

