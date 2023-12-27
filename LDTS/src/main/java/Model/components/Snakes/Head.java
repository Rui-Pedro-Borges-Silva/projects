package Model.components.Snakes;

import Model.Position;
import Model.components.Components;

public class Head extends Components {
    public Head(Position position) {
        super(position);
        setSymbol("o");
        setColour("#FFFFFF");
    }
}
