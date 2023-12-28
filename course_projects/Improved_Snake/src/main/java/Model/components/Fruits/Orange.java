package Model.components.Fruits;

import Model.Position;

public class Orange extends Fruits{
    public Orange(Position position) {
        super(position);
        setSymbol("O");
        setColour("#FFA500");
        setSize(1);
    }
}
