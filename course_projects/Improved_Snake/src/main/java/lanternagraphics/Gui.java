package lanternagraphics;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Gui {
    TerminalScreen screen;
    TextGraphics graphics;
    int width;
    int height;
    String filename;

    public TerminalScreen getScreen() {
        return screen;
    }
    public void setScreen(TerminalScreen screen) {
        this.screen = screen;
    }
    public TextGraphics getGraphics() {
        return graphics;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public Gui(int height , int width){
        this.height = height;
        this.width = width;
        try{
            TerminalSize tsize = new TerminalSize(width, height +1);
            DefaultTerminalFactory tfactory = new DefaultTerminalFactory().setInitialTerminalSize(tsize);
            tfactory.setForceAWTOverSwing(true);
            tfactory.setTerminalEmulatorFontConfiguration(loadFont("src/main/resources/Fonts/square.ttf"));
            createScreen(tfactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Gui(int h, int w , String filename){
        this.height = h;
        this.width = w;
        try{
            TerminalSize terminalSize = new TerminalSize(width, height +1 );
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            terminalFactory.setForceAWTOverSwing(true);
            terminalFactory.setTerminalEmulatorFontConfiguration(loadFont(filename));
            createScreen(terminalFactory);
        }
        catch (IOException  | FontFormatException e){
            e.printStackTrace();
        }
    }
    private void createScreen(DefaultTerminalFactory tfactory) throws IOException {
        tfactory.setTerminalEmulatorTitle("SNAKE");
        Terminal terminal = tfactory.createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.graphics = screen.newTextGraphics();
        screen.setCursorPosition(null);
        screen.startScreen();
        screen.doResizeIfNecessary();
    }
    public AWTTerminalFontConfiguration loadFont(String filename) throws FontFormatException, IOException{
        File fontFile = new File(filename);
        Font font = Font.createFont(Font.TRUETYPE_FONT,fontFile);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        Font loadedFont = font.deriveFont(Font.BOLD, 23);
        return AWTTerminalFontConfiguration.newInstance(loadedFont);

    }
}
