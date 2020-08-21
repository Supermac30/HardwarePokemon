import java.util.ArrayList;

public class Player {
    // Stores the related data for the player, including how far they
    // progressed and their current inventory
    ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
    Items[] items = {
            new PokeBall(),
            new HealthPotion(),
            new StaminaPotion()
    };
    int level;
    int money = 50;

    public Player(Pokemon starter) {
        pokemons.add(starter);
        level = 0;
    }
}
