package Model.components.Button;

import Model.Position;

public class Bbutton extends AbstButton{
    public Bbutton(Position position, String text) {
        super(position, text);
        setHeight(2);
        setWidth(10);
    }
}
