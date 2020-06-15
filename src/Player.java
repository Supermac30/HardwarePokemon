import java.util.ArrayList;

public class Player {
    ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();
    ArrayList<Items> items = new ArrayList<Items>();
    int level;
    int money = 0;

    public Player(Pokemon starter) {
        pokemons.add(starter);
        level = 0;
    }
}
