package Model.components;

import Model.Position;
import Model.components.Fruits.Fruits;
import Model.components.Snakes.Body;
import Model.components.Snakes.Head;
import com.googlecode.lanterna.graphics.TextGraphics;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Drawable{
    private List<Components> snake = new ArrayList<>();
    private double velocidade=1;
    private double pace;
    private int pacemovement = 10;
    private int size;
    private boolean alive;
    private int dirx;
    private int diry;

    String corcorpocobra;

    public Snake(Position position,String cor){
        alive = true;
        size=2;
        dirx=1;
        diry=0;
        corcorpocobra=cor;
        pace=0;
        velocidade=1;
        snake.add(new Head(new Position(position.getX(), position.getY())));
        snake.add(new Body(new Position(position.getX(), position.getY()),corcorpocobra));
    }

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive=alive;
    }
    public int getdirx() {
        return dirx;
    }
    public void setdirx(int directionX) {
        this.dirx = directionX;
    }
    public int getdiry() {
        return diry;
    }
    public void setdiry(int directionY) {
        this.diry = directionY;
    }
    public int getSize() {
        return size;
    }
    public double getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public int getPacemovement() {
        return pacemovement;
    }

    public void setPacemovement(int pacemovement) {
        this.pacemovement = pacemovement;
    }

    public List<Components> getSnake() {
        return snake;
    }
    public void setSnake(List<Components> snake) {
        this.snake = snake;
    }

    public void setSize(int size) {
        this.size = size;
    }
    public Components getHead(){
        return snake.get(0);
    }
    public Components getBody(){
        return snake.get(size-1);
    }
    @Override
    public void draw(TextGraphics screen){
        for(Components components:snake){
            components.draw(screen);
        }
    }

    public void changedir(int x, int y){
        setdirx(x);
        setdiry(y);
    }
    public Position screenlimit(Position position, int height, int width){
        if(position.getX()>width){
            return new Position(0, position.getY());
        }
        if(position.getX()<0){
            return new Position(width, position.getY());
        }
        if(position.getY()>height){
            return new Position(position.getX(),0);
        }
        if(position.getY()<0){
            return new Position(position.getX(),height);
        }
        return position;
    }

    public void move(int height,int width){
        pace += velocidade;
        if(pace==pacemovement) {
            pace = 0;
            snakemove(height, width);
        }
    }

    public void snakemove(int height, int width){
        Position newposition = screenlimit(new Position(getHead().getPosition().getX()+dirx,getHead().getPosition().getY()+diry),height,width);
        for(Components components : snake){
            Position passedpos = components.getPosition();
            components.setPosition(newposition);
            newposition = passedpos;
        }
    }
    public void eatfruit(Fruits fruit, int height, int width, int maxsize) {
        if (size + fruit.getSize() < 2) {
            while (size > 2) {
                snake.remove(size - 1);
                size--;
            }
        } else if (fruit.getSize() < 0) {
            for (int i = 0; i > fruit.getSize(); i--) {
                snake.remove(size - 1);
                size--;
            }
        } else if (fruit.getSize() > 0) {
            for (int i = 1; i <= fruit.getSize(); i++) {
                if (i <= maxsize) {
                    Position position = ScreenLimits(new Position(snake.get(size - 1).getPosition().getX() - dirx, snake.get(size - 1).getPosition().getY() - diry), height, width);
                    snake.add(new Body(position, corcorpocobra));
                    size++;
                }
            }
        }
        velocidade *= fruit.getVelocity();
        if (velocidade > 4) {
            velocidade = 4;
        }
        if (velocidade < 1) {
            velocidade = 1;
        }

    }

        public Position ScreenLimits(Position posicao,int height,int width){
            if(posicao.getX()>width){
                return new Position(0,posicao.getY());
            }
            if(posicao.getX()<0){
                return new Position(width,posicao.getY());
            }
            if(posicao.getY()>height){
                return new Position(posicao.getX(),0);
            }
            if(posicao.getY()<0){
                return new Position(posicao.getX(),height);
            }
            return posicao;
        }
    }

