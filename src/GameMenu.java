import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameMenu implements MenuInterface{
    Menu menu = new Menu<GameMenu>(this, new Layer[] {
            new Layer(new JLabel("Game Menu"), new JLabel[] {
                    new JLabel("Fight"),
                    new JLabel("Shop"),
                    new JLabel("Pokemon"),
                    new JLabel("Inventory")
            }),
            new Layer(new JLabel("Locations"), null),
            new Layer(new JLabel("Shop"), new JLabel[] {
                    new JLabel("Pokeball - 10 PD"),
                    new JLabel("Health Potion - 20 PD"),
                    new JLabel("Stamina Potion - 30 PD"),
            }),
            new Layer(new JLabel("Your Pokemon"), null),
            new Layer(new JLabel("Your Items"), null),
            new Layer(new JLabel("Pick A Pokemon"), null),
    });

    JLabel[] locations = new JLabel[] {
            new JLabel("Training Room"),
            new JLabel("Jungle"),
            new JLabel("Viridian City Gym"),
            new JLabel("Team Rocket")
    };

    Player player;
    Battle battle;
    int location; // Location the player chose

    public GameMenu(Player initPlayer) {
        menu.setVisible(true);
        menu.setTitle("PokeHardWare");
        player = initPlayer;
        updatePlayerInfo();
    }

    public void updatePlayerInfo() {
        JLabel[] pokemons = new JLabel[player.pokemons.size()];
        for (int i = 0; i < pokemons.length; i++) {
            pokemons[i] = new JLabel(player.pokemons.get(i).name);
        }
        JLabel[] items = new JLabel[player.items.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new JLabel(player.items.get(i).name + " x" + player.items.get(i).amount);
        }

        menu.layers[2].title.setText("<html><pre style = \"font-family:courier\">     Shop<br>Balance = " + player.money + "</pre></html>");
        menu.layers[1].buttons = Arrays.copyOfRange(locations, 0, player.level+1);
        menu.layers[3].buttons = pokemons;
        menu.layers[4].buttons = items;
        menu.layers[5].buttons = pokemons;
    }

    public Pokemon pickRandomPokemon() {
        return null;
    }

    public void startBattle(int pokemonIndex) {
        if (location == 0) {
            // training room
            battle = new Battle(
                    player,
                    player.pokemons.get(pokemonIndex),
                    new Pidgey(),
                    new AI(null, 0)
            );
        } else {
            battle = new Battle(
                    player,
                    player.pokemons.get(pokemonIndex),
                    pickRandomPokemon(),
                    new AI(null, location)
            );
        }
    }

    public void endBattle(boolean win) {
        if (!win) {
            return;
        }
        player.level ++;
        player.money += location * 10;
        updatePlayerInfo();
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
                    menu.changeLayer(5);
                    location = pointer;
                case 5:
                    startBattle(pointer);
            }
        }
    }
}