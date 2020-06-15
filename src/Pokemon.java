
public class Pokemon {
    double health = 100;
    double stamina = 80;
    String name;
    String[] attacks;
    double[][] attackStats; // Stores the strength, stamina, and opponent stun chance of each attack,
    // [strength, stamina, opponent stun chance]
    int defendIncrease;
    double criticalHitChance;

    // Stores the name of the text file with the sprites inside inside
    String sprite;

    public boolean attack(int attack) {
        if (attackStats[attack][1] > stamina) {
            return false;
        }
        stamina -= attackStats[attack][1];
        return true;
    }

    public boolean isCritical() {
        return Math.random() < criticalHitChance;
    }

    public boolean isStunned(double stunChance) {
        return Math.random() < stunChance;
    }

    public boolean attacked(double amount) {
        // Changes health and returns whether or not the pokemon is dead
        health = Math.max(health-amount, 0);
        return health == 0;
    }

    public void defend() {
        stamina = Math.min(stamina + defendIncrease, 100);
        health = Math.min(health + defendIncrease, 100);
    }

    public Pokemon(Pokemon copy) {
        name = copy.name;
        attacks = copy.attacks;
        attackStats = copy.attackStats;
        defendIncrease = copy.defendIncrease;
        criticalHitChance = copy.criticalHitChance;
        sprite = copy.sprite;
    }

    public Pokemon() {}
}

class Pikachu extends Pokemon {
    public Pikachu() {
        name = "Pikachu";

        defendIncrease = 10;

        attacks = new String[] {
                "THUNDER SHOCK",
                "TAIL WHIP",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 10, 0.3},
                {10, 5, 0.1},
                {5, 1, 0}
        };

        criticalHitChance = 0.2;

        sprite = "Assets/pikachu.png";
    }
}

class Pidgey extends Pokemon {
    public Pidgey() {
        name = "Pidgey";

        defendIncrease = 5;

        attacks = new String[] {
                "TACKLE",
                "SAND ATTACK",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 20, 0.1},
                {1, 10, 0.5},
                {5, 1, 0}
        };

        criticalHitChance = 0.05;

        sprite = "Assets/pidgey.png";
    }
}

class Charmander extends Pokemon {
    public Charmander() {
        name = "Charmander";

        defendIncrease = 10;

        attacks = new String[] {
                "GROWL",
                "DRAGON BREATH",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {1, 10, 0.9},
                {15, 15, 0},
                {5, 1, 0}
        };

        criticalHitChance = 0.05;

        sprite = "Assets/charmander.png";
    }
}

class Squirtle extends Pokemon {
    public Squirtle() {
        name = "Squirtle";

        defendIncrease = 5;

        attacks = new String[] {
                "TAIL WHIP",
                "WATER PULSE",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 10, 0.1},
                {25, 40, 0},
                {5, 1, 0}
        };

        criticalHitChance = 0.05;

        sprite = "Assets/squirtle.png";
    }
}

class Bulbasaur extends Pokemon {
    public Bulbasaur() {
        name = "Bulbasaur";

        defendIncrease = 5;

        attacks = new String[] {
                "VINE WHIP",
                "RAZOR LEAF",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 30, 0.5},
                {10, 8, 0},
                {5, 1, 0}
        };

        criticalHitChance = 0.05;

        sprite = "Assets/bulbasaur.png";
    }
}