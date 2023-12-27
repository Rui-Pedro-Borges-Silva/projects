package components;

import Model.Position;
import Model.components.Components;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import lanternagraphics.Gui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TestComponent {
    Gui screen;
    Components components;
    TextGraphics graphics;

    @BeforeEach
    public void setup(){
        graphics = Mockito.mock(TextGraphics.class);
        screen = Mockito.mock(Gui.class);
        Mockito.when(screen.getGraphics()).thenReturn(graphics);
        components = new Components(new Position(15,15));
        components.setSymbol("#");
    }

    @Test
    public void test_draw(){
        components.draw(screen.getGraphics());
        Mockito.verify(graphics,Mockito.atLeastOnce()).putString(Mockito.any(TerminalPosition.class),Mockito.anyString());
    }
}
