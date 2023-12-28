package Model.components.Button;

import Model.Position;
import Model.components.Drawable;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import javax.swing.plaf.nimbus.State;

public abstract class AbstButton implements Drawable {

    Position position;
    int height;
    int width;
    String text;
    boolean highlight = false;
    String color = "#000000";
    State state;

    public AbstButton(Position position, String text){
        this.position = position;
        this.text = text;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean ishighlight() {
        return highlight;
    }

    public void sethighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public State getState(){
        return state;
    }
    @Override
    public void draw(TextGraphics screen) {
        if(highlight){
            color = "#FF00FF";
        }
        else{
            color = "#000000";
        }
        screen.setForegroundColor(TextColor.Factory.fromString(color));
        screen.putString(new TerminalPosition(position.getX()+1, position.getY() + 1), text);
        screen.drawRectangle(new TerminalPosition(position.getX(), position.getY()),new TerminalSize(width,height),'+');
    }
}
