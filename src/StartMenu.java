import javax.swing.*;
import java.awt.*;

public class StartMenu implements MenuInterface {
    // Creates start screen with the tutorial and the start button
    // Uses the Menu class to handle the UI
    Menu menu = new Menu<>(this, new Layer[]{
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
                    "<html><p>Welcome to the world of pokemon. Use the arrow keys, enter key, and backspace key to navigate the menus." +
                    "To win you must defeat Team Rocket at the end of the game. Whenever you defeat a pokemon " +
                    "in a location you unlock the next one. <br><br> To be able to beat Team Rocket you will need stronger " +
                    "pokemon which you can capture in the jungle with the pokeball item, and items which you can buy in the shop with money you gain from winning battles. <br><br>" +
                    "Once you capture a pokemon in the jungle you can use it in other battles. To capture a pokemon " +
                    "you need to get the opponent to 30 HP or under. <br><br> Try to catch them all! <br><br>" +
                    "Good Luck!" +
                    "</p></html>"
            ))
    });

    public StartMenu() {
        menu.setTitle("Start Menu");
        menu.setVisible(true);
        menu.setSize(new Dimension(800, 725));
        menu.setPreferredSize(new Dimension(800, 725));
    }

    @Override
    public void handleMenu(boolean pressedEnter, int layerNumber, int pointer) {
        // Handles the user pressing buttons in the menu
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
