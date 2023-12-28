package Model.components.Fruits;

import Model.Position;

public class Apple extends Fruits{
    public Apple(Position position) {
        super(position);
        setSymbol("a");
        setColour("#FF0000");
        setSize(1);
    }
}
