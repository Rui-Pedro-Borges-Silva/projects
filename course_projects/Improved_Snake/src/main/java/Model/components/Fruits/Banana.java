package Model.components.Fruits;

import Model.Position;

public class Banana extends Fruits{
    public Banana(Position position) {
        super(position);
        setSymbol("b");
        setColour("#FFFF00");
        setVelocity(7);
        setSize(1);
    }
}
