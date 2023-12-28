package Model;

import Model.components.Fruits.*;
import Model.components.Snake;
import Model.components.Walls;
import lanternagraphics.Gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConstrucaodaArena {
    Arena arena;

    public ConstrucaodaArena(List<Snake> two_snakes, Gui screen) {
        arena = new Arena();
        arena.height = screen.getHeight()-1;
        arena.width = screen.getWidth()-1;
        for(Snake snake : two_snakes){
            arena.getElements().add(snake);
            arena.getSnakes().add(snake);
        }
        arena.getPossibleFruits().add(new Apple(new Position(0,0)));
        arena.getPossibleFruits().add(new Orange(new Position(0,0)));
        arena.getPossibleFruits().add(new kiwi(new Position(0,0)));
        arena.getPossibleFruits().add(new Banana(new Position(0,0)));
        arena.getPossibleFruits().add(new grapes(new Position(0,0)));

    }

    public Arena getArena() {
        return arena;
    }
}

