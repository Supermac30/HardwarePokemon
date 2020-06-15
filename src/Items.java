interface ItemInterface {
    void use(Player trainer, Pokemon player, Pokemon enemy);
}

public class Items implements ItemInterface{
    String name;
    int amount = 0;
    String description;
    int price;

    @Override
    public void use(Player trainer, Pokemon player, Pokemon enemy) {}
}

class PokeBall extends Items implements ItemInterface{
    public PokeBall () {
        name = "PokeBall";
        description = "Captures a PokeHardWareMon with less than 30 HP in a Jungle Area";
        price = 10;
    }

    public void use(Player trainer, Pokemon player, Pokemon enemy) {
        trainer.pokemons.add(new Pokemon(enemy));
        amount --;
    }
}

class HealthPotion extends Items implements ItemInterface{
    public HealthPotion () {
        name = "Health Potion";
        description = "Adds 20 Health Without Using Up a Turn";
        price = 20;
    }

    public void use(Player trainer, Pokemon player, Pokemon enemy) {
        player.health = Math.min(player.health + 20, 100);
        amount --;
    }
}

class StaminaPotion extends Items implements ItemInterface{
    public StaminaPotion () {
        name = "Stamina Potion";
        description = "Adds 20 Stamina Without Using Up a Turn";
        price = 30;
    }

    public void use(Player trainer, Pokemon player, Pokemon enemy) {
        player.stamina = Math.min(player.stamina + 20, 100);
        amount --;
    }
}