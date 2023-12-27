package Model.components.Snakes;

import Model.Position;
import Model.components.Components;

import javax.lang.model.element.Element;

public class Body extends Components {
    public Body(Position position,String cor) {
        super(position);
        setSymbol("x");
        setColour(cor);
    }
}
