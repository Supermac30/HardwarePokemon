public class Items {
    String name;
    int amount = 0;
}

class PokeBall extends Items {
    public PokeBall () {
        name = "PokeBall";
    }
}

class HealthPotion extends Items {
    public HealthPotion () {
        name = "Health Potion";
    }
}

class StaminaPotion extends Items {
    public StaminaPotion () {
        name = "Stamina Potion";
    }
}