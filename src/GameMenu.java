import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GameMenu implements MenuInterface {
    // Stores all the functions needed for the menus before a battle
    // Uses the Menu class to handle the UI
    Menu menu = new Menu<>(this, new Layer[] {
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
            new JLabel("BOSS BATTLE - Team Rocket")
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
        // Updates the layers in the menu to reflect new pokemon and items the player has
        JLabel[] pokemons = new JLabel[player.pokemons.size()];
        for (int i = 0; i < pokemons.length; i++) {
            pokemons[i] = new JLabel(player.pokemons.get(i).name);
        }
        JLabel[] items = new JLabel[player.items.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new JLabel(player.items[i].name + " x" + player.items[i].amount);
        }

        menu.layers[2].title.setText("<html><pre style = \"font-family:sans-serif\">     Shop<br>Balance = " + player.money + "</pre></html>");
        menu.layers[1].buttons = Arrays.copyOfRange(locations, 0, player.level+1);
        menu.layers[3].buttons = pokemons;
        menu.layers[4].buttons = items;
        menu.layers[5].buttons = pokemons;
    }

    public Pokemon pickRandomPokemon() {
        // Chooses a random pokemon to fight
        int pick = (int) (5*Math.random());
        Pokemon enemy;
        switch (pick) {
            case 0:
                enemy = new Pikachu();
                break;
            case 1:
                enemy = new Pidgey();
                break;
            case 2:
                enemy = new Bulbasaur();
                break;
            case 3:
                enemy = new Squirtle();
                break;
            default:
                enemy = new Charmander();
                break;
        }
        return enemy;
    }

    public void startBattle(int pokemonIndex) {
        // Creates the Battle
        if (location == 0) {
            // training room
            Pidgey enemy = new Pidgey();
            enemy.health = 50;
            battle = new Battle(
                    player,
                    player.pokemons.get(pokemonIndex),
                    enemy,
                    new AI(null, 0),
                    this,
                    location
            );
        } else {
            Pokemon enemy = pickRandomPokemon();
            if (location == 3) {
                enemy.health = 200;
                enemy.stamina = 200;
            }
            battle = new Battle(
                    player,
                    player.pokemons.get(pokemonIndex),
                    enemy,
                    new AI(null, location),
                    this,
                    location
            );
        }
        menu.setVisible(false);
        battle.setVisible(true);
    }

    public void endBattle(boolean win) {
        // Ends the battle and rewards the player if they win
        ActionListener end = evt -> {
            menu.changeLayer(0);
            menu.setVisible(true);
            battle.setVisible(false);
            for (Pokemon pokemon : player.pokemons) {
                pokemon.health = 100;
                pokemon.stamina = 100;
            }
            if (!win) {
                return;
            }
            if (location == player.level) {
                player.level = Math.min(player.level + 1, 3);
            }
            player.money += (location+1) * 10;
            updatePlayerInfo();
        };
        Timer timer = new Timer(1500 ,end);
        timer.setRepeats(false);
        timer.start();
    }

    public void handleShop(int index) {
        // Handles buying items from the shop
        if (player.money >= player.items[index].price) {
            player.items[index].amount ++;
            player.money -= player.items[index].price;
        }
        updatePlayerInfo();
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
                    menu.changeLayer(5);
                    location = pointer;
                    break;
                case 2:
                    handleShop(pointer);
                    break;
                case 5:
                    startBattle(pointer);
                    break;
            }
        }
    }
}
