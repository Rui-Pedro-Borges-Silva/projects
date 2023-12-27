package Model;

import Model.components.Drawable;
import Model.components.Fruits.Fruits;
import Model.components.Snake;
import Model.components.Walls;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.floor;
import static java.lang.Math.random;

public class Arena implements Drawable {

    int height;
    int width;
    Fruits fruit1;
    Fruits fruit2;

    private List<Drawable> elements = new ArrayList<>();
    private List<Snake> snakes = new ArrayList<>();
    private List<Walls> walls = new ArrayList<>();
    private  List<Fruits> possibleFruits = new ArrayList<>();

    public List<Drawable> getElements() {
        return elements;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public List<Fruits> getPossibleFruits() {
        return possibleFruits;
    }

    public List<Walls> getWalls() {
        return walls;
    }

    public void setSnakes(List<Snake> snakes) {
        this.snakes = snakes;
    }

    public void setWalls(List<Walls> walls) {
        this.walls = walls;
    }

    @Override
    public void draw(TextGraphics screen) {
        for(Drawable drawable:elements){
            drawable.draw(screen);
        }
    }

    public Boolean execute(){
        for(Snake snake:snakes){
            snake.move(height,width);
            if(check_snake_head_collisions()){
                snakes.get(0).setAlive(false);
                snakes.get(1).setAlive(false);
                return true;
            }
            if(check_snake_collisions(snake.getHead().getPosition()) || check_wall_collisions(snake.getHead().getPosition())){
                snake.setAlive(false);
                return true;
            }
            checkEatFruits(snake);
        }
        return false;
    }

    public void checkEatFruits(Snake snake){
        if(snake.getHead().getPosition().equals(fruit1.getPosition())){
            snake.eatfruit(fruit1,height,width,maximumGrowingSize(snake));
            addFruits();
        }
        if(snake.getHead().getPosition().equals(fruit2.getPosition())){
            snake.eatfruit(fruit2,height,width,maximumGrowingSize(snake));
            addFruits();
        }
    }

    public int maximumGrowingSize(Snake s){
        int maxSize = 0;
        Position pos = new Position(s.getBody().getPosition().getX(),s.getBody().getPosition().getY());
        for (int i = 1;i<=5;i++){
            pos.setX(pos.getX()-s.getdirx());
            pos.setY(pos.getY()-s.getdiry());
            for(Walls w:walls){
                if(w.getPosition().equals(pos)){
                    return maxSize;
                }
            }
            maxSize++;
        }
        return maxSize;
    }

    public Boolean check_snake_collisions(Position pos){
        for(Snake snake1 : snakes)
            for(int i = 1; i <snake1.getSnake().size(); i++)
                if(snake1.getSnake().get(i).getPosition().equals(pos))
                    return true;
        return false;
    }

    public boolean check_snake_head_collisions(){
        if(snakes.size()==2 && snakes.get(0).getHead().getPosition().equals(snakes.get(1).getHead().getPosition())) {
            return true;
        }
        return false;
    }

    public void addFruits(){
        elements.remove(fruit1);
        elements.remove(fruit2);
        double number1 = floor(random() * possibleFruits.size());
        double number2 = number1;
        while(number2 == number1){
            number2 = floor(random() * possibleFruits.size());
        }
        Fruits f1 = possibleFruits.get(((int) number1));
        Fruits f2 = possibleFruits.get(((int) number2));
        if(f1.getSymbol().equals("?") || f2.getSymbol().equals("?")){
            possibleFruits.remove(possibleFruits.size()-1);
        }

        f1.setPosition(new Position((int)floor(random()*width),(int)floor(random()*height)));
        while(check_snake_collisions(f1.getPosition()) || check_wall_collisions(f1.getPosition()))
            f1.setPosition(new Position((int)floor(random()*width),(int)floor(random()*height)));

        f2.setPosition(new Position((int)floor(random()*width),(int)floor(random()*height)));
        while(check_snake_collisions(f2.getPosition()) || f2.getPosition().equals(f1.getPosition()) || check_wall_collisions(f2.getPosition()))
            f2.setPosition(new Position((int)floor(random()*width),(int)floor(random()*height)));

        fruit1 = f1;
        fruit2 = f2;
        elements.add(fruit1);
        elements.add(fruit2);
    }

    public Boolean check_wall_collisions(Position pos){
        Position SnakeHeadPosition = pos;
        for(Walls w : walls)
            if(w.getPosition().equals(SnakeHeadPosition))
                return true;
        return false;
    }

    public void addWall(Walls w){
        walls.add(w);
    }

}


