
public class Pokemon {
    double health = 100;
    double stamina = 100;
    int type; // Types for now: normal - 0, water - 1, fire - 2, electric - 3
    String[] attacks;
    double[][] attackStats; // Stores the strength, stamina, and opponent stun chance of each attack, [strength, stamina, opponent stun chance]
    int defendIncrease;
    double criticalHitChance;
    String icon; // Stores the name of the text file with the icon inside

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

    public boolean attacked(double amount) {
        // Changes health and returns whether or not the pokemon is dead
        health = Math.max(health-amount, 0);
        return health == 0;
    }

    public void defend() {
        stamina = Math.max(stamina + defendIncrease, 100);
        health = Math.max(health + defendIncrease, 100);
    }
}

class Pikachu extends Pokemon {
    public Pikachu() {
        type = 3;

        attacks = new String[] {
                "THUNDER SHOCK",
                "TAIL WHIP",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 10, 0.3},
                {10, 5, 0.1},
                {5, 0, 0}
        };

        criticalHitChance = 0.2;
    }
}

class Pidgey extends Pokemon {
    public Pidgey() {
        type = 0;

        attacks = new String[] {
                "TACKLE",
                "SAND ATTACK",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 5, 0.1},
                {0, 5, 0.5},
                {5, 0, 0}
        };

        criticalHitChance = 0.05;
    }
}

class Charmander extends Pokemon {
    public Charmander() {
        type = 2;

        attacks = new String[] {
                "GROWL",
                "DRAGON BREATH",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {0, 10, 0.9},
                {15, 15, 0},
                {5, 0, 0}
        };

        criticalHitChance = 0.05;
    }
}

class Squirtle extends Pokemon {
    public Squirtle() {
        type = 1;

        attacks = new String[] {
                "TAIL WHIP",
                "WATER PULSE",
                "QUICK ATTACK"
        };

        attackStats = new double[][] {
                {10, 10, 0.1},
                {50, 80, 0},
                {5, 0, 0}
        };

        criticalHitChance = 0.05;
    }
}