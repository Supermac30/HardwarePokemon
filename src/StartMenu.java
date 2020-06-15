import javax.swing.*;
import java.awt.*;

public class StartMenu implements MenuInterface{
    Menu menu = new Menu<StartMenu>(this, new Layer[]{
            new Layer(new JLabel("PokeHardware"), new JLabel[]{
                    new JLabel("Start"),
                    new JLabel("How To Play"),
            }),
            new Layer(new JLabel("Choose a Starting Pokemon"), new JLabel[]{
                    new JLabel("Squirtle"),
                    new JLabel("Charmander"),
                    new JLabel("Bulbasaur")
            }),
            new Layer(new JLabel("How to Play"), null, new JLabel(
                    "<html><p>Welcome to PokeHardware. Use the arrow keys, enter key, and backspace key to navigate the menus." +
                            "To win you must defeat Team Rocket at the end of the game. Whenever you defeat a PokeHardwareMon " +
                            "in a location you can proceed to the next. \n To be able to beat Team Rocket you will need stronger " +
                            "PokeHardwareMon which you can capture in the jungle, and items which you can buy in the shop. \n" +
                            "Good Luck!" +
                            "</p></html>"
            ))
    });

    public StartMenu() {
        menu.setVisible(true);
        menu.setSize(new Dimension(800, 500));
        menu.setPreferredSize(new Dimension(800, 500));
    }

    @Override
    public void handleMenu(boolean pressedEnter, int layerNumber, int pointer) {
        if (!pressedEnter) {
            if (layerNumber != 0) {
                menu.changeLayer(0);
            }
        } else {
            switch (layerNumber) {
                case 0:
                    menu.changeLayer(pointer+1);
                    break;
                case 1:
                    Pokemon starter;
                    switch(pointer) {
                        case 0:
                            starter = new Squirtle();
                            break;
                        case 1:
                            starter = new Charmander();
                            break;
                        default:
                            starter = new Bulbasaur();
                            break;
                    }
                    Main.startGame(starter);
                    menu.setVisible(false);
                    break;
            }
        }
    }
}
