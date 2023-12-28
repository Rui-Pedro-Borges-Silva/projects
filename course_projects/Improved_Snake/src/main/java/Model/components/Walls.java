package Model.components;

import Model.Position;

public class Walls extends Components{
        public Walls(Position position) {
            super(position);
            this.setSymbol("#");
            this.setColour("#FFFFFF");
        }
}