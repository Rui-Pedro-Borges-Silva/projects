package components;

import Model.Position;
import Model.components.Fruits.Apple;
import Model.components.Snake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSnake {
    Snake snake;
    @BeforeEach
    public void setup(){
        snake = new Snake(new Position(15,15));
    }
    @Test
    public void inPos(){
        Assertions.assertEquals(new Position(16,15),snake.getHead().getPosition());
        //Assertions.assertEquals(new Position(15,15),snake.getBody().getPosition());
    }

    @Test
    public void switchdir(){
        snake.changedir(1,0);
        Assertions.assertEquals(1,snake.getdirx());
        Assertions.assertEquals(0,snake.getdiry());
    }
     @Test
    public void apple_eaten(){
        snake.eatfruit(new Apple(new Position(10,10)),60,60,10);
        Assertions.assertEquals(1,snake.getSize());
     }


}
