package Model.inputs;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import lanternagraphics.Gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class arrowset {
    Gui screen;
    List<KeyStroke> keys = new ArrayList();

    public List<KeyStroke> getKeys() {
        return keys;
    }

    public arrowset(Gui screen) {
        this.screen = screen;
    }
    public boolean readinput() throws IOException {
        keys.clear();
        KeyStroke key = screen.getScreen().pollInput();
        if(key != null) {
            if (isPauseorQuit(key)) {
                return quitPause(key);
            } else if (isArrow(key)) {
                return arrows(key);
            } else {
                return quitPause(key);
            }
        }
        return false;
    }
    public boolean arrows(KeyStroke key) throws IOException{
        keys.add(key);
        while (isArrow(key)) {
            key = screen.getScreen().pollInput();
        }
        if (isPauseorQuit(key)) {
            deleteBuffer(key);
            return true;
        }
        return  true;
    }
    public void deleteBuffer(KeyStroke key) throws IOException{
        keys.add(key);
        while (key != null) {
            key = screen.getScreen().pollInput();
        }
    }

    public boolean isArrow(KeyStroke key){
        return key != null && (key.getKeyType() == KeyType.ArrowUp || key.getKeyType() == KeyType.ArrowDown || key.getKeyType() == KeyType.ArrowLeft || key.getKeyType() == KeyType.ArrowRight);
    }

    public boolean isPauseorQuit(KeyStroke key){
        return key!=null && key.getKeyType() == KeyType.Character && (key.getCharacter().toString().equalsIgnoreCase("q") || key.getCharacter().toString().equalsIgnoreCase("p"));
    }
    public boolean quitPause(KeyStroke key) throws IOException{
        deleteBuffer(key);
        return true;
    }
    
}
