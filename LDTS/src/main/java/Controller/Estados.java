package Controller;

import Model.components.Button.AbstButton;
import Model.components.Snake;
import Model.inputs.arrowset;
import View.Game;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import lanternagraphics.Gui;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Estados {

    Gui screen;
    arrowset observer;
    long startTime;
    long pauseTime;
    List<AbstButton> buttonList = new ArrayList<AbstButton>();
    AbstButton actualbutton;
    Snake snake;
    String name;

    public Estados(Gui screen) {
        this.screen = screen;
        observer = new arrowset(screen);
        startTime = System.currentTimeMillis();
        pauseTime = 0;
    }

    abstract public void step(Game game) throws IOException;

    public void changeState(Game game, Estados newState) {
        game.setGameState(newState);
    }

    public void drawText(String text, String color, TerminalPosition position){
        screen.getGraphics().setForegroundColor(TextColor.Factory.fromString(color));
        screen.getGraphics().putString(position, text);
    }

    public abstract void drawAllText(String color);

    public void drawBackground(String color){
        screen.getGraphics().setBackgroundColor(TextColor.Factory.fromString(color));
        for (int i = 0;i<screen.getWidth();i++){
            for (int j = 0;j<=screen.getHeight();j++)
                screen.getGraphics().putString(new TerminalPosition(i,j), " ");
        }
    }

    public void drawButtons(){
        for(AbstButton b : buttonList){
            if (b.equals(actualbutton)){
                b.sethighlight(true);
            }
            b.draw(screen.getGraphics());
            b.sethighlight(false);
        }
    }

    public void returnMenu(Game game) throws IOException{
        screen.getScreen().stopScreen();
        screen.getScreen().close();
        changeState(game, new MenudeEstados(new Gui(screen.getHeight(), screen.getWidth())));
    }


    public void pause() throws  IOException{
        long initialTime = System.currentTimeMillis();
        while(true){
            drawText("PAUSE","#FF0000",new TerminalPosition((screen.getWidth()/2)-2, screen.getHeight()/2));
            drawText("PRESS ANY KEY TO CONTINUE","#FFFFFF",new TerminalPosition((screen.getWidth()/2)-12, (screen.getHeight()/2)+3));
            screen.getScreen().refresh();
            if(observer.readinput()){
                KeyStroke key = observer.getKeys().get(0);
                if(key.getKeyType()!= KeyType.EOF){
                    pauseTime += System.currentTimeMillis()-initialTime;
                    break;
                }
            }
        }
    }

    public void checkInputPlay(Game game) throws IOException{
        if(observer.readinput()){
            for(KeyStroke key : observer.getKeys()){
                checkMovement(key);
                checkAction(game,key);
            }
        }
    }

    public void checkInputButtons(Game game) throws IOException {
        if(observer.readinput()){
            KeyStroke key = observer.getKeys().get(0);
            if(key.getKeyType()== KeyType.Enter){
                screen.getScreen().stopScreen();
                screen.getScreen().close();
                enterState(game);
            }
            if(key.getKeyType()== KeyType.Character && key.getCharacter().toString().equalsIgnoreCase("q")){
                exit(game);
            }
            if(key.getKeyType()== KeyType.ArrowDown){
                int index = (buttonList.indexOf(actualbutton)+1);
                if(index==buttonList.size()){
                    index = 0;
                }
                actualbutton = buttonList.get(index);
            }
            if(key.getKeyType()== KeyType.ArrowUp){
                int index = (buttonList.indexOf(actualbutton)-1) ;
                if(index==-1){
                    index = buttonList.size()-1;
                }
                actualbutton = buttonList.get(index);
            }
        }
    }

    public void checkInputEndGame(Game game) throws IOException{
        if(observer.readinput()){
            KeyStroke key = observer.getKeys().get(0);
            if(key.getKeyType()== KeyType.Character && name.length() <= 10 ){
                name += key.getCharacter().toString().toLowerCase();
            }
            else if(key.getKeyType()== KeyType.Enter){
                returnMenu(game);
            }
            else if(key.getKeyType()== KeyType.Backspace && name.length() >=1){
                name = name.substring(0,name.length()-1);
            }
        }
    }

    public void checkMovement(KeyStroke key){
        moveArrows(key);
    }

    public void moveArrows(KeyStroke key){
        switch (key.getKeyType()) {
            case ArrowUp: {
                if (!(snake.getdirx() == 0 && snake.getdiry() == 1)) {
                    snake.changedir(0, -1);}break;}
            case ArrowDown: {
                if (!(snake.getdirx() == 0 && snake.getdiry() == -1)) {
                    snake.changedir(0, 1);}break;}
            case ArrowLeft: {
                if (!(snake.getdirx() == 1 && snake.getdiry() == 0)) {
                    snake.changedir(-1, 0);}break;}
            case ArrowRight: {
                if (!(snake.getdirx() == -1 && snake.getdiry() == 0)) {
                    snake.changedir(1, 0);}break;}
            default :break;
        }

    }

    public void checkAction(Game game, KeyStroke key) throws IOException{}

    public void enterState(Game game) throws FileNotFoundException {}

    public void exit(Game game) throws IOException{}

}

