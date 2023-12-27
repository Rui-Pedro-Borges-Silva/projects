package Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPos {
    @Test
    public void getX(){
        Position p = new Position(1,2);
        Assertions.assertEquals(1,p.getX());
    }
    @Test
    public void getY(){
        Position p = new Position(1,2);
        Assertions.assertEquals(2,p.getY());
    }
    @Test
    public void setX(){
        Position p = new Position(1,2);
        p.setX(5);
        Assertions.assertEquals(5,p.getX());
    }
    @Test
    public void setY(){
        Position p = new Position(1,2);
        p.setY(4);
        Assertions.assertEquals(4,p.getY());
    }
}
