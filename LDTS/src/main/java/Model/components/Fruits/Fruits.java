package Model.components.Fruits;

import Model.Position;
import Model.components.Components;

public abstract class Fruits extends Components {
        private int size = 1;

    private double velocity = 2;

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

        public Fruits(Position position){
            super(position);
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

